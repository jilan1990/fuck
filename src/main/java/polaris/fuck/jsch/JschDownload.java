package polaris.fuck.jsch;

import java.io.File;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class JschDownload {

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
    public JschDownload(final String ip, final String username, final String password) {

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
    public void download(final String local, String remote) {

        try {
            SftpATTRS attrs = ftp.lstat(remote);
            if (attrs.isDir()) {
                File file = new File(local);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Vector<ChannelSftp.LsEntry> files = ftp.ls(remote);
                for (ChannelSftp.LsEntry entry : files) {
                    String filename = entry.getFilename();
                    if (".".equals(filename) || "..".equals(filename)) {
                        continue;
                    }
                    download(local + '/' + filename, remote + '/' + filename);
                }
            } else {
                ftp.get(remote, local);
                System.out.println(local);
            }
        } catch (SftpException e2) {
            System.out.println("ftp.lstat failed, remote=" + remote);
        }
    }

    public static void main(final String[] args) {
        JschDownload shell = new JschDownload("192.168.3.44", "eric", "123456");
        String local = "E:/ailulu/sheldon";
        String remote = "/home/eric/os/sheldon";
        shell.download(local, remote);
    }
}
