package common;

public class Const {

    public static final short WAIT = 0;
    public static final short QUEUE = 1;
    public static final short AC = 2;
    public static final short PE = 3;//
    public static final short WA = 4;
    public static final short TLE = 5;
    public static final short MLE = 6;
   
    public static final short RE = 7;
    public static final short CE = 8;
    public static final short OLE = 9;
    public static final short SKIP = 10;
    public static final short NF = 11;
    public static final short SE = 12;

    public static final String[] STATUS = {"WAIT", "QUEUE", "AC","PE", "WA",
        "TLE", "MLE",  "RE",
        "CE", "OLE", "SKIP","NF","SE"};

  
    //public static String lattersuffix[] = {".c", ".cpp", ".java", ".exe", ".exe", ".class"};

    /**
     * Output File Size
     */
    public static final long FILEMAXSIZE = 7000;

    public static String getLatterSuffix(String language) {
        language=language.toLowerCase();
        if (language.equals("c")) {
            return ".c";
        } else if (language.equals("cpp")||language.equals("c++")) {
            return ".cpp";
        } else if (language.equals("java")) {
            return ".java";
        }else if(language.equals("python")){
            return ".py";
        }
        return null;
    }

    public static final String cCompilerDirIdentify = "MinGWDir";//"cCompilerDir";
    public static final String cppCompilerDirIdentify = "MinGWDir";//"cppCompilerDir"
    public static final String javaCompilerDirIdentify = "JavaCompileDir";
    public static final String JavaCompilerDir = "JavaCompileDir";
    public static final String RelativeJavaCompileDir = "\\Java\\bin";
    public static final String JavaRelative = "JavaRelative";
    public static final String MinGWDir = "MinGWDir";
    public static final String RelativeMinGWCompileDir = "\\MinGW\\bin";
    public static final String MinGWRelative = "MinGWRelative";
    public static final String srcDirIdentify = "srcDir";
    public static final String exeDirInentify = "exeDir";
}
