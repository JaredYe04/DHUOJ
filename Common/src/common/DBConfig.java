/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Administrator
 */
public class DBConfig {

    private static Properties dbConfig = new Properties();
private final static String FileName="DBConfig.properties";
    static {
        try {
            InputStream in = new FileInputStream(FileFinder.findFile(FileName));
            dbConfig.load(in);
            Iterator it = dbConfig.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                System.out.println(key + ":" + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getIp() {
        String url = null;
        String IP = null;
        url = dbConfig.getProperty("hibernate.connection.url");
        int index1 = url.indexOf("//");
        index1 += "//".length();
        int index2 = url.indexOf("/gdoj");
        char[] chars = url.toCharArray();
        IP = String.valueOf(chars, index1, index2 - index1);
        return IP;
    }

    public static void setIp(String ip) {
        String url = "jdbc:mysql://" + ip + "/gdoj?useUnicode=true&amp;characterEncoding=UTF-8";
        dbConfig.setProperty("hibernate.connection.url", url);

    }

    public static String getUserName() {
        return dbConfig.getProperty("hibernate.connection.username");
    }

    public static void setUserName(String UserName) {
        dbConfig.setProperty("hibernate.connection.username", UserName);
    }

    public static String getPassword() {
        return dbConfig.getProperty("hibernate.connection.password");
    }

    public static void setPassword(String Password) {
        dbConfig.setProperty("hibernate.connection.password", Password);
    }
//
//    public static void main(String[] args) {
//        System.out.println(DBConfig.getUserName());
//        System.out.println(DBConfig.getPassword());
//        System.out.println(DBConfig.getIp());
//    }
    
    public static void writeToFile(){
         try {
            // 文件输出流 
            FileOutputStream fos = new FileOutputStream(FileFinder.findFile(FileName));
            // 将Properties集合保存到流中 
            dbConfig.store(fos, "update dbConfig.properties");
            fos.close();// 关闭流 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Properties getDBConfig(){
        return dbConfig;
    }
    
    public static boolean connectTest() {
        boolean flag = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url=dbConfig.getProperty("hibernate.connection.url");
            String username=getUserName();
            String password=getPassword();
            java.sql.Connection con=DriverManager.getConnection(url, username, password);
            flag=true;
        } catch (Exception e) {
            e.printStackTrace();
            flag=false;
        }
        return flag;
    }
}
