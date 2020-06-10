package com.caohao.nettyforwebssh.test;

import com.caohao.nettyforwebssh.util.SSHconfig;
import com.caohao.nettyforwebssh.util.WebSSH;

public class jsontest {
    public static void main(String[] args) {
//        SSHconfig ssHconfig = WebSSH.exSSHconfig("{\"sex\":\"男\",\"name\":\"张三\",\"age\":25}");
//        System.out.println(ssHconfig.map);
        String s = "ls\\r";
        System.out.println(s.contains("\r"));
    }
}
