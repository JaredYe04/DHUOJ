package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import log.Log;
import java.nio.file.Paths;
public class Config {

    public static int DEBUG;
//    private static boolean isCppRelative;
//    private static boolean isJavaRelative;
    public static int MinGWRelative;
    public static int JavaRelative;
    private static final Properties prop = new Properties();

    static {
        try {
            InputStream in = new FileInputStream(FileFinder.findFile("config/config.properties"));
            prop.load(in);
            DEBUG = Integer.valueOf(prop.getProperty("Debug"));
//            isCppRelative = prop.getProperty("isCppRelative").equals("true") ? true : false;
//            isJavaRelative = prop.getProperty("isJavaRelative").equals("true")?true: false;
            String tmp = prop.getProperty(Const.MinGWRelative);
            if (tmp != null && !"".equals(tmp)) {
                MinGWRelative = Integer.valueOf(tmp);
            } else {
                MinGWRelative = -1;
            }
            tmp = prop.getProperty(Const.JavaRelative);
            if (tmp != null && !"".equals(tmp)) {
                JavaRelative = Integer.valueOf(tmp);
            } else {
                JavaRelative = -1;
            }
            
        } catch (Exception e) {
            Log.writeExceptionLog(e.getClass()+e.getMessage());
            e.printStackTrace();
        }
    }

    public static void freshConfig() {
        try {
            InputStream in = new FileInputStream(FileFinder.findFile("config/config.properties"));
            prop.load(in);
            DEBUG = Integer.valueOf(prop.getProperty("Debug"));

            
            String tmp = prop.getProperty(Const.MinGWRelative);
            
            if (tmp != null && !"".equals(tmp)) {
                MinGWRelative = Integer.valueOf(tmp);
            } else {
                MinGWRelative = -1;
            }
            tmp = prop.getProperty(Const.JavaRelative);
            if (tmp != null && !"".equals(tmp)) {
                JavaRelative = Integer.valueOf(tmp);
            } else {
                JavaRelative = -1;
            }
             //initRegistryMessage();
        } catch (Exception e) {
             Log.writeExceptionLog(e.getClass()+e.getMessage());
            e.printStackTrace();
        }
    }

    public static Properties getProp() {
        return prop;
    }

    public static String getValue(String key) {
        return prop.getProperty(key);
    }

    public static void save() {
        try {
            // 文件输出流 
            FileOutputStream fos = new FileOutputStream(FileFinder.findFile("config/config.properties"));
            // 将Properties集合保存到流中 
            prop.store(fos, "update config.properties");
            fos.close();// 关闭流 
            freshConfig();
        } catch (Exception e) {
             Log.writeExceptionLog(e.getClass()+e.getMessage());
            e.printStackTrace();
        }
    }

    //临时代码输出路径  若要改回配置文件读取 从const里获取字段
    public static String getSourcePath() {
        String curDir = Paths.get(".").toAbsolutePath().normalize().toString();
        return curDir+"/test";
    }
    public static String getLogPath(){
        String curDir = Paths.get(".").toAbsolutePath().normalize().toString();
        return curDir+"/log";
    }
    //临时代码输出路径 

    public static String getTargetPath() {
//        if (isTestRelative) {
//            return System.getProperty("user.dir") + prop.getProperty("relativeExeDir");
//        } else {
        String curDir = Paths.get(".").toAbsolutePath().normalize().toString();
        return curDir+"/test";
        //}

    }

    public static String getCompilerDir(String language,String compiler) {
        String dir = null;
         //todo
        dir=LangSelector.getCompilerPath(language,compiler);
        //if (language.equals("c") || language.equals("cpp") || language.equals("c++")) {
         //  dir=LangSelector.getCompilerPath("C++", compiler);
            //dir = prop.getProperty(Const.MinGWDir);
        //} else if (language.equals("java")) {
        //    dir=LangSelector.getCompilerPath("Java", compiler);
            //dir = prop.getProperty(Const.JavaCompilerDir);
        //}
        return dir;
    }

    //public static String getCompilerDir(String language) {
    //    String dir = null;
         //todo
    //    if (language.equals("c") || language.equals("cpp") || language.equals("c++")) {
    //       dir=LangSelector.getCompilerPath("C++", null);
     //       //dir = prop.getProperty(Const.MinGWDir);
    //    } else if (language.equals("java")) {
     //       dir=LangSelector.getCompilerPath("Java", null);
     //       //dir = prop.getProperty(Const.JavaCompilerDir);
    //    }
    //    return dir;
   // }
    public static String CompilerDir(String language,String compiler) {
        String dir = null;

        if (language.equals("c") || language.equals("cpp") || language.equals("c++")) //返回各种语言的编译器地址
        {
            //dir = System.getProperty("user.dir") + Const.MinGWDir;
            dir=LangSelector.getCompilerPath("C++",compiler);
        } else if (language.equals("java")) {
            //dir = System.getProperty("user.dir") + Const.JavaCompilerDir;
            dir=LangSelector.getCompilerPath("Java",compiler);
        }
        return dir;
    }
//    public static String CompilerDir(String language) {
 //       String dir = null;
//
  //      if (language.equals("c") || language.equals("cpp") || language.equals("c++")) //返回各种语言的编译器地址
 //       {
 //           //dir = System.getProperty("user.dir") + Const.MinGWDir;
  //          dir = LangSelector.getCompilerPath("C++", compiler);
   //     } else if (language.equals("java")) {
  //          //dir = System.getProperty("user.dir") + Const.JavaCompilerDir;
  //          dir = LangSelector.getCompilerPath("Java", null);
  //      }
  //      return dir;
   // }
}