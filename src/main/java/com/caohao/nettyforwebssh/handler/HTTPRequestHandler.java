package com.caohao.nettyforwebssh.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.File;
import java.io.FileInputStream;

public class HTTPRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String uri = msg.uri();
        System.out.println(uri);
        if (uri.contains("/swssh")){
            System.out.println("one websocket conection");
            ctx.fireChannelRead(msg.retain());
        }else {
            File file = new File("/Users/caohao/IdeaProjects/nettyforwebssh/src/main/resources/webssh"+uri);
            FileInputStream inputStream = new FileInputStream(file);
            ByteBuf buffer = Unpooled.buffer();
            int code ;
            while ((code=inputStream.read())!=-1){
                buffer.writeByte(code);
            }
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,buffer);
            ctx.channel().writeAndFlush(response);
            ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
