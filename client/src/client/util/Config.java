/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.util;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ytxlo
 */
public class Config {
    public static String IPaddress;
    private static String URLip;
    private static String URLport;
    public static String Username;
    public static final Properties prop= new Properties();
    static{
        try {
            InputStream in = new FileInputStream(common.FileFinder.findFile("client/config/config.properties"));

            prop.load(in);
            in.close();
            IPaddress = prop.getProperty("IPaddress");
            URLip = prop.getProperty("URLip");
            URLport = prop.getProperty("URLport");
            Username = prop.getProperty("Username");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String getIPaddress(){
        return IPaddress;
    }
    public static String getUsername(){
        return Username;
    }
    public static String readValue(String key) throws IOException {
        return  prop.getProperty(key);
    }
    public static void setUsername(String str){
        prop.setProperty("Username", str);
    }
    public static void setIPaddress(String str){
        prop.setProperty("IPaddress", str);
    }
    public static void setURLip(String str){
        prop.setProperty("URLip", str);
    }
    public static void setURLport(String str){
        prop.setProperty("URLport", str);
    }
    public static void save(){
        try {
            FileOutputStream fos = new FileOutputStream(common.FileFinder.findFile("config/config.properties"));
            prop.store(fos, "update config/config.properties");
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the URLip
     */
    public static String getURLip() {
        return URLip;
    }

    /**
     * @return the URLport
     */
    public static String getURLport() {
        return URLport;
    }
}
