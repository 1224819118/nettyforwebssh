package com.caohao.nettyforwebssh.handler;

import com.caohao.nettyforwebssh.util.SSHConnectConfig;
import com.caohao.nettyforwebssh.util.SSHconfig;
import com.caohao.nettyforwebssh.util.WebSSH;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("swconnection connecting..");
        SSHconfig ssHconfig = WebSSH.exSSHconfig(msg.text());
        HashMap<String, String> map = ssHconfig.map;
        Channel channel = null;
        System.out.println(map);
        if (map.get("operate").equals("connect")){
            JSch jSch = new JSch();
            Session session = jSch.getSession(map.get("username"), map.get("host"), Integer.valueOf(map.get("port")));
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(map.get("password"));
            System.out.println("connect to session");
            session.connect(30000);
            channel = session.openChannel("shell");
            System.out.println("connect to shell");
            channel.connect(5000);
            SSHConnectConfig.jSch=jSch;
            SSHConnectConfig.channel=channel;
            InputStream inputStream = channel.getInputStream();
            System.out.println(channel.isConnected());
            System.out.println(inputStream.available());
            byte[] bytes = new byte[1024];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            if (inputStream.read(bytes)!=-1) {
                                System.out.println("send one message:"+new String(bytes));
                                ctx.channel().writeAndFlush(new TextWebSocketFrame(new String(bytes)));
                            }
                            else
                                System.out.println("wating commend...");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }else if (map.get("operate").equals("command")){
            String c = map.get("command");
            channel=SSHConnectConfig.channel;
            if (c.contains("\\r")){
                if (channel.isConnected())
                {
                    System.out.println("send one command is "+SSHConnectConfig.command);
                    OutputStream outputStream = channel.getOutputStream();
                    outputStream.write((SSHConnectConfig.command+"\r").getBytes());
                    outputStream.flush();
                    SSHConnectConfig.command="";
                }else {
                    System.out.println("channel is closed");
                }
            }else {
                SSHConnectConfig.command+=c;
                ctx.writeAndFlush(new TextWebSocketFrame(c));
                System.out.println("command +="+c+" and command now is "+SSHConnectConfig.command);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
