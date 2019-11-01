package polaris.fuck.jsch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JschShell {
    // 远程主机的ip地址
    private String ip;
    // 远程主机登录用户名
    private String username;
    // 远程主机的登录密码
    private String password;
    // 设置ssh连接的远程端口
    public static final int DEFAULT_SSH_PORT = 22;
    // 保存输出内容的容器
    private ArrayList<String> stdout;

    /**
     * 初始化登录信息
     * 
     * @param ip
     * @param username
     * @param password
     */
    public JschShell(final String ip, final String username, final String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        stdout = new ArrayList<String>();
    }

    /**
     * 执行shell命令
     * 
     * @param command
     * @return
     */
    public int execute(final String filename) {
        int returnCode = 0;
        JSch jsch = new JSch();
        MyUserInfo userInfo = new MyUserInfo();

        try {
            // 创建session并且打开连接，因为创建session之后要主动打开连接
            Session session = jsch.getSession(username, ip, DEFAULT_SSH_PORT);
            session.setPassword(password);
            session.setUserInfo(userInfo);
            session.connect();

            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
            InputStream inputStream = channel.getInputStream();
            OutputStream outputStream = channel.getOutputStream();

            PrintStream commander = new PrintStream(outputStream);

            File file = new File(filename);
            FileInputStream fileInput = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileInput));
            String line = null;
            while ((line = reader.readLine()) != null) {
                commander.println(line);
            }
            commander.flush();
            reader.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

            String msg = null;
            while ((msg = in.readLine()) != null) {
                System.out.println(msg);
            }
            in.close();

        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnCode;
    }

    /**
     * get stdout
     * 
     * @return
     */
    public ArrayList<String> getStandardOutput() {
        return stdout;
    }

    public static void main(final String[] args) {
        JschShell shell = new JschShell("192.168.3.44", "eric", "123456");
        shell.execute("cmds.txt");

        ArrayList<String> stdout = shell.getStandardOutput();
        for (String str : stdout) {
            System.out.println(str);
        }
    }
}
