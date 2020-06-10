package com.caohao.nettyforwebssh.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;

public class SSHConnectConfig {
    public static Channel channel;
    public static JSch jSch;
    public static String command="";
}
