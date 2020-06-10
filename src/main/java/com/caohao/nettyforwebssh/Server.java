package com.caohao.nettyforwebssh;

import com.caohao.nettyforwebssh.handler.ServerInitlizernizer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    private EventLoopGroup parentGroup = new NioEventLoopGroup();
    private EventLoopGroup childGroup = new NioEventLoopGroup();
    private ServerBootstrap serverBootstrap;
    private ChannelFuture future;
    public void start(){
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitlizernizer());
            future = serverBootstrap.bind(9001).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }
    public void stop(){

    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
