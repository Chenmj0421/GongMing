package com.gongming.gmcommon.ftp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

public class FtpUtils {
    private final static Log log = LogFactory.getLog(FtpUtils.class);
    private final static String UTF8 = "utf-8";
    private final static String ISO_8859_1 = "iso-8859-1";
    private String userName;
    private String password;
    private String ip;
    private int port;
    private String encodeing;

    private FTPClient ftpClient;
    public boolean connected = false;
    public FtpUtils(boolean isPrint){
        ftpClient = new FTPClient();
        initArgs("ftp.properties");
        connect();
        if (isPrint) {
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        }
    }

    /**
     * 建立连接
     * @return
     */
    private void connect() {
        try {
            ftpClient.connect(ip, port);
            ftpClient.setConnectTimeout(10);
            if(FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.login(userName, password);
                ftpClient.setControlEncoding(encodeing);
                log.info("ftp服务成功连接");
                connected = true;
            } else {
                log.info("ftp服务连接失败！");
            }

        } catch (Exception e) {
            log.error("ftp服务连接异常:" + e.getMessage());
        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            log.error("关闭失败：" + e.getMessage());
        }

    }

    /**
     * 参数初始化
     * @param configFile  配置文件名称
     */
    private void initArgs(String configFile) {
        Properties property = new Properties();
        try {
            BufferedInputStream inBuff = new BufferedInputStream(new FileInputStream(getClass().getResource("/").getPath() + configFile));
            property.load(inBuff);
            userName = property.getProperty("username");
            password = property.getProperty("password");
            ip = property.getProperty("ip");
            port = Integer.parseInt(property.getProperty("port"));
            encodeing = property.getProperty("encodeing");
        } catch (Exception e) {
            log.debug("初始化参数错误：" + e.getMessage());
        }
    }

    /**
     * 新建文件目录
     * @param dir 目录名
     * @return true or false
     */
    public boolean mkDir(String dir) {
        try {
           return ftpClient.makeDirectory(new String(dir.getBytes(UTF8),ISO_8859_1));
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 删除文件目录
     * @param dir 目录名
     * @return true or false
     */
    public boolean rmDir(String dir) {
        try {
            return ftpClient.removeDirectory(new String(dir.getBytes(UTF8),ISO_8859_1));
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 上传文件（单个）
     * @param dir
     * @param filePath
     * @return true or false
     */
    public boolean upFile(String dir, String filePath) {
        try {
            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1,filePath.length());
            ftpClient.changeWorkingDirectory(new String(dir.getBytes(UTF8),ISO_8859_1));
            InputStream local = new FileInputStream(filePath);
            return ftpClient.storeFile(new String(fileName.getBytes(UTF8),ISO_8859_1), local);
        } catch (Exception e) {
            log.error("上传文件异常：" + e.getMessage());
        }
        return false;
    }

    /**
     * 删除文件（单个）
     * @param dir  目录
     * @param fileName  文件名
     * @return true or false
     */
    public boolean delfile(String dir,String fileName) {
        try {
            ftpClient.changeWorkingDirectory(new String(dir.getBytes(UTF8),ISO_8859_1));
            return ftpClient.deleteFile(new String(fileName.getBytes(UTF8),ISO_8859_1));
        } catch (Exception e) {
            log.error("删除文件异常：" + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        FtpUtils ftpUtils = new FtpUtils(true);
        if (ftpUtils.initArgs("ftp.properties")) {
            if (ftpUtils.connect()) {
               log.info("---------" + ftpUtils.upFile("中国","D:\\肥胖症通用库.222.xml"));
            }
        }
        if (null != ftpUtils) {
            ftpUtils.close();
        }
    }
}
