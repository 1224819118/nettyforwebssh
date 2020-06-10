package com.caohao.nettyforwebssh.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSSH {
    private ExecutorService executorService = Executors.newCachedThreadPool();
    public void recvHandler(Channel shell, io.netty.channel.Channel channel){
        receiveMessage(shell,channel);

    }
    public Channel initConnection(HashMap<String,String> map){
        JSch jSch = new JSch();
        try {
            Session session = jSch.getSession(map.get("username"), map.get("host"), Integer.valueOf(map.get("port")));
            session.setPassword(map.get("password"));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(30000);
            Channel shell = session.openChannel("shell");
            shell.connect(3000);
            return shell;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return null;
    }
    public synchronized void receiveMessage(Channel channel, io.netty.channel.Channel socketChannel){
        try {
            InputStream inputStream = channel.getInputStream();
            byte[] buffer = new byte[1024];
            while (inputStream.read(buffer) != -1) {
                socketChannel.writeAndFlush(buffer);
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendCommend(String commend,Channel channel){
        OutputStream outputStream=null;
        try {
            outputStream = channel.getOutputStream();
            outputStream.write(commend.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SSHconfig exSSHconfig(String text){
        System.out.println(text);
        String[] split = text.split(",");
        SSHconfig ssHconfig = new SSHconfig();
        for (String s:split){
            String[] strings = s.split(":");
            String replace1 = strings[0].replace("\"", "");
            String replace2 = replace1.replace("{", "");
            replace1=null;
            String result1 = replace2.replace("}", "");
            replace2=null;
            String replace11 = strings[1].replace("\"", "");
            String replace21 = replace11.replace("{", "");
            replace11=null;
            String result2 = replace21.replace("}", "");
            replace21=null;
            ssHconfig.map.put(result1,result2);
            result1=null;
            result2=null;
        }
        return ssHconfig;

    }
}

