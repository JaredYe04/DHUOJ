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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Jared Ye
 */
public abstract class LangSelector {
    private static String ConfigPath="";
    private static Document Data;
    public static boolean Loaded=false;

    private static String lastLanguage; // 最后使用的语言
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
    //
    private static Map<String,String>customDefaultCompiler;

    // by san_san
    public static String getDefaultCompiler(String language) {
        try {
            String xpathExpr = String.format("/languages/language[@id='%s']/lastCompiler", language);


            Node lastCompilerNode = (Node) xpath.evaluate(xpathExpr, Data, XPathConstants.NODE);

            if (lastCompilerNode != null) {
                return lastCompilerNode.getTextContent();
            }
        } catch (XPathExpressionException e) {
          e.printStackTrace();
        }
    
        return customDefaultCompiler.get(language);
    }

    public static void setDefaultCompiler(String language,String compiler) {
        customDefaultCompiler.put(language, compiler);
        try {
            // 更新 XML 中的 lastCompiler 节点
            String xpathExpr = String.format("/languages/language[@id='%s']", language);
            Node languageNode = (Node) xpath.evaluate(xpathExpr, Data, XPathConstants.NODE);

            if (languageNode != null) {
                Element languageElement = (Element) languageNode;
                Node lastCompilerNode = (Node) xpath.evaluate("lastCompiler", languageElement, XPathConstants.NODE);

                if (lastCompilerNode == null) {
                    // 如果 lastCompiler 节点不存在，创建它
                    lastCompilerNode = Data.createElement("lastCompiler");
                    languageElement.appendChild(lastCompilerNode);
                }

             lastCompilerNode.setTextContent(compiler);
           }
       } catch (XPathExpressionException e) {
         e.printStackTrace();
       }     
    }
    //

    // by san_san
    // 获取最后使用的语言
    public static String getLastLanguage() {
        try {
            String xpathExpr = "/languages/lastLanguage";
            Node lastLanguageNode = (Node) xpath.evaluate(xpathExpr, Data, XPathConstants.NODE);

            if (lastLanguageNode != null) {
                return lastLanguageNode.getTextContent();
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 设置最后使用的语言
    public static void setLastLanguage(String language) {
        try {
            // 标准化语言名称
            String standardLanguage = parseStandardLanguageName(language);

            // 查找现有的 lastLanguage 节点
            String xpathExpr = "/languages/lastLanguage";
            Node lastLanguageNode = (Node) xpath.evaluate(xpathExpr, Data, XPathConstants.NODE);

            if (lastLanguageNode != null) {
                // 如果节点存在，更新内容
                lastLanguageNode.setTextContent(standardLanguage);
            } else {
                // 如果节点不存在，创建新节点
                Element root = Data.getDocumentElement();
                Element newLastLanguage = Data.createElement("lastLanguage");
                newLastLanguage.setTextContent(standardLanguage);
                root.appendChild(newLastLanguage);
            }

            // 更新内存中的值
            lastLanguage = standardLanguage;

            // 保存到文件
            save(ConfigPath);
        } catch (Exception e) {
            e.printStackTrace();
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
    /////////////////////////////////新增的逻辑
    public static void save(String filePath) throws Exception {
        Document doc = Data;
        // 创建TransformerFactory实例
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // 将Document转换为DOMSource
        DOMSource source = new DOMSource(doc);

        // 创建StreamResult以保存到文件
        StreamResult result = new StreamResult(new File(filePath));

        // 执行转换并写入文件
        transformer.transform(source, result);

        System.out.println("XML 文件保存成功: " + filePath);
    }
    public static boolean setCompilerPath(String languageName, String compilerName, String newPath) {
        languageName = parseStandardLanguageName(languageName);
        if (compilerName == null || compilerName.trim().equals("")) {
            compilerName = getDefaultCompilerName(languageName);
        }

        try {
            // 定义XPath表达式来找到对应的编译器path节点
            String exp = "/languages/language" + "[@id='" + languageName + "']" + "//compiler[@name='" + compilerName + "']//path";

            // 使用XPath查找对应节点
            Node pathNode = (Node) xpath.evaluate(exp, Data, XPathConstants.NODE);

            // 检查path节点是否存在
            if (pathNode != null) {
                // 设置新路径
                pathNode.setTextContent(newPath);
                return true;  // 返回true表示设置成功
            } else {
                System.out.println("找不到指定的编译器路径节点");
                return false;
            }
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static void setCompileCommand(String languageName, String compilerName, String compileCommand) {
        Document doc=Data;
        languageName = parseStandardLanguageName(languageName);
        if (compilerName == null || compilerName.trim().equals("")) {
            compilerName = getDefaultCompilerName(languageName);
        }

        try {
            String exp = "/languages/language[@id='" + languageName + "']//compiler[@name='" + compilerName + "']//compileCmd";

            // 获取节点
            Node compileCmdNode = (Node) xpath.evaluate(exp, Data, XPathConstants.NODE);
            if (compileCmdNode != null) {
                compileCmdNode.setTextContent(compileCommand);
            } else {
            // 创建新节点逻辑
                // 这里的实现取决于你的 XML 结构，假设我们有 Document 对象 doc
                Element newCompilerElement = doc.createElement("compiler");
                newCompilerElement.setAttribute("name", compilerName);
                Element newCompileCmdElement = doc.createElement("compileCmd");
                newCompileCmdElement.setTextContent(compileCommand);
                newCompilerElement.appendChild(newCompileCmdElement);

                // 假设你有一个表示语言的节点
                Node languageNode = (Node) xpath.evaluate("/languages/language[@id='" + languageName + "']", Data, XPathConstants.NODE);
                if (languageNode != null) {
                    languageNode.appendChild(newCompilerElement);
                }
            }
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
        }
    }
//////////////////////
        public static void setLinkCommand(String languageName, String compilerName, String linkCommand) {
        Document doc = Data;
        languageName = parseStandardLanguageName(languageName);
        if (compilerName == null || compilerName.trim().equals("")) {
            compilerName = getDefaultCompilerName(languageName);
        }

        try {
            String exp = "/languages/language[@id='" + languageName + "']//compiler[@name='" + compilerName + "']//linkCmd";

            // 获取节点
            Node compileCmdNode = (Node) xpath.evaluate(exp, Data, XPathConstants.NODE);
            if (compileCmdNode != null) {
                compileCmdNode.setTextContent(linkCommand);
            } else {
                // 创建新节点逻辑
                // 这里的实现取决于你的 XML 结构，假设我们有 Document 对象 doc
                Element newCompilerElement = doc.createElement("compiler");
                newCompilerElement.setAttribute("name", compilerName);
                Element newCompileCmdElement = doc.createElement("linkCmd");
                newCompileCmdElement.setTextContent(linkCommand);
                newCompilerElement.appendChild(newCompileCmdElement);

                // 假设你有一个表示语言的节点
                Node languageNode = (Node) xpath.evaluate("/languages/language[@id='" + languageName + "']", Data, XPathConstants.NODE);
                if (languageNode != null) {
                    languageNode.appendChild(newCompilerElement);
                }
            }
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
        }
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
        {
           return customDefaultCompiler.get(languageName); 
        }
           
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
        
        // 加载默认编译器设置 by san_san
        NodeList languageNodes = (NodeList) xpath.evaluate("/languages/language", doc, XPathConstants.NODESET);
        for (int i = 0; i < languageNodes.getLength(); i++) {
            Element languageElement = (Element) languageNodes.item(i);
            String languageId = languageElement.getAttribute("id");

            Node lastCompilerNode = (Node) xpath.evaluate("lastCompiler", languageElement, XPathConstants.NODE);
            if (lastCompilerNode != null) {
                customDefaultCompiler.put(languageId, lastCompilerNode.getTextContent());
            } else {
                // 如果 lastCompiler 不存在，使用第一个编译器作为默认
                NodeList compilerNodes = languageElement.getElementsByTagName("compiler");
                if (compilerNodes.getLength() > 0) {
                    Element defaultCompiler = (Element) compilerNodes.item(0);
                    customDefaultCompiler.put(languageId, defaultCompiler.getAttribute("name"));
                }
            }
        }

        // 初始化 lastLanguage
        Node lastLanguageNode = (Node) xpath.evaluate("/languages/lastLanguage", doc, XPathConstants.NODE);
        if (lastLanguageNode != null) {
            lastLanguage = lastLanguageNode.getTextContent();
        } else {
            // 如果不存在，创建节点并设置默认值为第一个可用的语言
            languageNodes = (NodeList) xpath.evaluate("/languages/language", doc, XPathConstants.NODESET);
            if (languageNodes.getLength() > 0) {
                Element firstLanguage = (Element) languageNodes.item(0);
                lastLanguage = firstLanguage.getAttribute("id");

                Element root = doc.getDocumentElement();
                Element lastLanguageElement = doc.createElement("lastLanguage");
                lastLanguageElement.setTextContent(lastLanguage);
                root.appendChild(lastLanguageElement);
            }
        }
        
        return doc;
    }

    // 获取语言ID的辅助方法
    public static String getLanguageIdFromAlias(String alias) {
        return parseStandardLanguageName(alias);
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
        if(compilerName==null||compilerName.equals(""))compilerName=getDefaultCompilerName(languageName);
        try{
            String exp="/languages/language"+ "[@id='" +languageName+ "']"+"//compiler[@name='"+compilerName+"']//path/text()";
            return (String) xpath.evaluate(exp,Data);
        } catch (XPathExpressionException e) {
                        System.out.println("null!");

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
        if(compilerName==null||compilerName.equals(""))compilerName=getDefaultCompilerName(languageName);
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
        if(compilerName==null||compilerName.equals(""))compilerName=getDefaultCompilerName(languageName);
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
