package polaris.fuck.jsch;

import java.io.File;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class JschUpload {

    private ChannelSftp ftp;
    // 设置ssh连接的远程端口
    public static final int DEFAULT_SSH_PORT = 22;

    /**
     * 初始化登录信息
     * 
     * @param ip
     * @param username
     * @param password
     */
    public JschUpload(final String ip, final String username, final String password) {

        JSch jsch = new JSch();

        try {
            // 创建session并且打开连接，因为创建session之后要主动打开连接
            Session session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            ftp = (ChannelSftp) session.openChannel("sftp");
            ftp.connect();
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行shell命令
     * 
     * @param command
     * @return
     */
    public void upload(final String source, String target) {
        System.out.println(source);
        File file = new File(source);
        if (!file.exists()) {
            System.out.println("upload !file.exists, source" + source);
            return;
        }
        if (file.isFile()) {
            try {
                ftp.put(source, target);
                System.out.println(target);
            } catch (SftpException e) {
                System.out.println("upload failed, target" + target);
            }
        } else {
            try {
                ftp.cd(target);
            } catch (SftpException e1) {
                try {
                    ftp.mkdir(target);
                } catch (SftpException e) {
                    System.out.println("upload failed, target" + target);
                    return;
                }
            }

            File[] files = file.listFiles();
            for (File f : files) {
                String fname = f.getName();
                upload(source + '/' + fname, target + '/' + fname);
            }
        }
    }

    public static void main(final String[] args) {
        JschUpload shell = new JschUpload("192.168.3.44", "eric", "123456");
        String source = "E:/ailulu/sheldon";
        String target = "/home/eric/os/sheldon";
        shell.upload(source, target);
    }
}
