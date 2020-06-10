package com.caohao.nettyforwebssh.test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class test {
    public static void main(String[] args) {
        try{
            JSch jsch=new JSch();
            Session session = jsch.getSession("root","47.95.7.231",22);
            session.setPassword("CaoHao_(1224819118)");

            UserInfo userInfo = new UserInfo() {
                @Override
                public String getPassphrase() {
                    System.out.println("getPassphrase");
                    return null;
                }
                @Override
                public String getPassword() {
                    System.out.println("getPassword");
                    return null;
                }
                @Override
                public boolean promptPassword(String s) {
                    System.out.println("promptPassword:"+s);
                    return false;
                }
                @Override
                public boolean promptPassphrase(String s) {
                    System.out.println("promptPassphrase:"+s);
                    return false;
                }
                @Override
                public boolean promptYesNo(String s) {
                    System.out.println("promptYesNo:"+s);
                    return true;//notice here!
                }
                @Override
                public void showMessage(String s) {
                    System.out.println("showMessage:"+s);
                }
            };

            session.setUserInfo(userInfo);

            // It must not be recommended, but if you want to skip host-key check,
            // invoke following,
            // session.setConfig("StrictHostKeyChecking", "no");

            //session.connect();
            session.connect(30000);   // making a connection with timeout.

            Channel channel=session.openChannel("shell");

            // Enable agent-forwarding.
            //((ChannelShell)channel).setAgentForwarding(true);

            channel.setInputStream(System.in);
      /*
      // a hack for MS-DOS prompt on Windows.
      channel.setInputStream(new FilterInputStream(System.in){
          public int read(byte[] b, int off, int len)throws IOException{
            return in.read(b, off, (len>1024?1024:len));
          }
        });
       */

            channel.setOutputStream(System.out);

      /*
      // Choose the pty-type "vt102".
      ((ChannelShell)channel).setPtyType("vt102");
      */

      /*
      // Set environment variable "LANG" as "ja_JP.eucJP".
      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
      */

            //channel.connect();
            channel.connect(3*1000);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }
}
