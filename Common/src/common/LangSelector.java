package common;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jared Ye
 */
public abstract class LangSelector {
    //
    private static Map<String,String>customDefaultCompiler;

    public static String getDefaultCompiler(String language) {
        return customDefaultCompiler.get(language);
    }

    public static void setDefaultCompiler(String language,String compiler) {
        customDefaultCompiler.put(language, compiler);
    }
    private static String ConfigPath="";
    private static Document Data;
    public static boolean Loaded=false;
    private static XPath xpath = XPathFactory.newInstance().newXPath();
    static {
        try {
            customDefaultCompiler=new HashMap<>();
            init();
            Loaded=true;
        } catch (Exception e) {
            //初始化的时候没有读到config.xml
        }
    }
    private static String findConfigFilePath() {
        String currentPath = System.getProperty("user.dir");
        String configFile = "config.xml";

        // Try to find config.xml in the current directory
        File currentDirConfigFile = new File(currentPath, configFile);
        if (currentDirConfigFile.exists()) {
            return currentDirConfigFile.getAbsolutePath();
        }

        // If not found, recursively search in parent directories
        return findConfigInParentDirectory(new File(currentPath), configFile);
    }

    private static String findConfigInParentDirectory(File directory, String configFile) {
        File configFileInParent = new File(directory, configFile);
        if (configFileInParent.exists()) {
            return configFileInParent.getAbsolutePath();
        }

        // If not found and not the root directory, recursively search in parent
        File parentDirectory = directory.getParentFile();
        if (parentDirectory != null) {
            return findConfigInParentDirectory(parentDirectory, configFile);
        }

        // If reached the root directory and still not found, return null
        return null;
    }
    //自动递归向上级获取config.xml的路径
    public static String getConfigPath(){
        return ConfigPath;
    }
    public static String parseStandardLanguageName(String languageName) {
        try {


            // 创建 XPath 对象
            XPath xpath = XPathFactory.newInstance().newXPath();

            // 编译 XPath 表达式
            XPathExpression expr = xpath.compile("//language");

            // 评估 XPath 表达式
            Object result = expr.evaluate(Data, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;

            // 遍历每个语言节点
            for (int i = 0; i < nodes.getLength(); i++) {
                String id = nodes.item(i).getAttributes().getNamedItem("id").getNodeValue();
                String aliasString = nodes.item(i).getAttributes().getNamedItem("alias").getNodeValue();
                String[] aliases = aliasString.split(",");

                // 检查 languageName 是否与 ID 匹配
                if (id.equalsIgnoreCase(languageName)) {
                    return id;
                }

                // 检查 languageName 是否与别名匹配
                for (String alias : aliases) {
                    if (alias.equalsIgnoreCase(languageName)) {
                        return id;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果没有匹配项，则返回原始 languageName
        return languageName;
    }
    public static String getDefaultCompilerName(String languageName){
        languageName=parseStandardLanguageName(languageName);
        if(customDefaultCompiler.containsKey(languageName))
           return customDefaultCompiler.get(languageName);
        //if(languageName.charAt(0)>='a')languageName
        try{
            String exp="/languages/language"+ "[@id='" +languageName+ "']"+"/compiler[1]/@name";
            return (String) xpath.evaluate(exp,Data);
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //当没有指定具体的编译器时，自动获取一个默认的编译器。



    public static Document init() throws Exception {
        // 创建Document对象
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        // 创建XPath对象
        LangSelector.ConfigPath =findConfigFilePath();
        //JOptionPane.showMessageDialog(null,"当前工作目录:"+ConfigPath);
//        LangSelector.ConfigPath=new File(LangSelector.ConfigPath).getAbsolutePath();
        Document doc=db.parse(new File(LangSelector.ConfigPath));
        Data=doc;
        return doc;
    }
    public static List<String> getCompilerNames(String languageName){
        languageName=parseStandardLanguageName(languageName);
        
        ArrayList<String>arrayListStr=new ArrayList<>();
        try {
            String exp="/languages/language"+ "[@id='" +languageName+ "']"+"//compiler";
            NodeList nodeList = (NodeList) xpath.evaluate(exp,Data,XPathConstants.NODESET);
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node childNode = nodeList.item(i);
                arrayListStr.add(childNode.getAttributes().getNamedItem("name").getTextContent());
                // 如果需要获取属性值，可以使用 childNode.getAttributes().getNamedItem("attributeName").getTextContent()
            }
            return arrayListStr;

        } catch (XPathExpressionException ex) {
            Logger.getLogger(LangSelector.class.getName()).log(Level.SEVERE, null, ex);
            return arrayListStr;
        }
    }

    public static String getCompilerPath(String languageName,String compilerName) {
        languageName=parseStandardLanguageName(languageName);
        if(compilerName==null)compilerName=getDefaultCompilerName(languageName);
        try{
            String exp="/languages/language"+ "[@id='" +languageName+ "']"+"//compiler[@name='"+compilerName+"']//path/text()";
            return (String) xpath.evaluate(exp,Data);
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String getCompileCommand(String languageName,String compilerName){
        languageName=parseStandardLanguageName(languageName);
        if(compilerName==null)compilerName=getDefaultCompilerName(languageName);
        try{
            String exp="/languages/language"+ "[@id='" +languageName+ "']"+"//compiler[@name='"+compilerName+"']//compileCmd/text()";
            return (String) xpath.evaluate(exp,Data);
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String getLinkCommand(String languageName,String compilerName){
        languageName=parseStandardLanguageName(languageName);
        if(compilerName==null)compilerName=getDefaultCompilerName(languageName);
        try{
            String exp="/languages/language"+ "[@id='" +languageName+ "']"+"//compiler[@name='"+compilerName+"']//linkCmd/text()";
            return (String) xpath.evaluate(exp,Data);
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static String getRunCommand(String languageName,String compilerName){
        languageName=parseStandardLanguageName(languageName);
        if(compilerName==null)compilerName=getDefaultCompilerName(languageName);
        try{
            String exp="/languages/language"+ "[@id='" +languageName+ "']"+"//compiler[@name='"+compilerName+"']//runCmd/text()";
            return (String) xpath.evaluate(exp,Data);
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public enum PlaceHolder{

        SourceFile("$sourceFile$"),
        ObjFile("$objFile$"),
        ExeFile("$exeFile$"),
        CompilerPath("$compilerPath$");
        private final String strPlaceHolder;
        private PlaceHolder(String str){
            strPlaceHolder=str;
        }
        public String getStr(){
            return strPlaceHolder;
        }
    }
    public static  String matchPlaceHolder(String src, HashMap<String,String>map){
        if(src==null||src.trim().isEmpty())return null;
        for (java.util.Map.Entry<String, String> Entry : map.entrySet()) {
            src = src.replace(Entry.getKey(), Entry.getValue());
            //System.out.println(Entry.getKey() + "已替换：" + Entry.getValue());
        }
        return src;
    }

}
