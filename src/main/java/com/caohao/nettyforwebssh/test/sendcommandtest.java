package com.caohao.nettyforwebssh.test;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class sendcommandtest {
    public static void main(String[] args) throws JSchException {
        JSch jSch = new JSch();
        Session root = jSch.getSession("root", "47.95.7.231", 22);
        root.setPassword("CaoHao_(1224819118)");
        root.setConfig("StrictHostKeyChecking", "no");
        root.connect(30000);
        Channel channel = root.openChannel("shell");
        channel.connect(5000);
//        channel.setInputStream(System.in);
        try {
//            ((ChannelExec)channel).setCommand("ls");
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write("pwd \r".getBytes());
            outputStream.flush();
            InputStream inputStream = channel.getInputStream();
            byte[] bytes = new byte[1024];
            System.out.println(inputStream.read(bytes));
            System.out.println(new String(bytes));
            System.out.println(inputStream.read(bytes));
            System.out.println(new String(bytes));
            outputStream.write("ls \r".getBytes());
            outputStream.flush();
            System.out.println(inputStream.read(bytes));
            System.out.println(new String(bytes));
            System.out.println(inputStream.read(bytes));
            System.out.println(new String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
