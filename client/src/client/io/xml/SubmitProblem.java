/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.io.xml;

import client.util.Control;
import common.LogLevel;
import common.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import main.Answer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author 毛泉
 */
public class SubmitProblem {
    
    private Document document;
    private Logger logger;

    public void init() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.logger=Logger.getInstance();
            this.document = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    
     public String SubmitProblem(String examId,String problemId,String siminum,String simiId,String iscopied,String submit){
        
        init();
        Element root = this.document.createElement("root");
        this.document.appendChild(root);
       
        
        Element examid = this.document.createElement("examId");
        examid.appendChild(this.document.createTextNode(examId));
        root.appendChild(examid);
        
        Element problemid = this.document.createElement("problemId");
        problemid.appendChild(this.document.createTextNode(problemId));
        root.appendChild(problemid);
        
        Element num = this.document.createElement("similarity");
        num.appendChild(this.document.createTextNode(siminum));
        root.appendChild(num);
        
        if(!siminum.equals("-1")){
        Element Id = this.document.createElement("similarId");
        Id.appendChild(this.document.createTextNode(simiId));
        root.appendChild(Id);
        }
        
        Element is = this.document.createElement("isOverSimilarity");
        is.appendChild(this.document.createTextNode(iscopied));
        root.appendChild(is);
        
        Element sub = this.document.createElement("ifSubmit");
        sub.appendChild(this.document.createTextNode(submit));
        root.appendChild(sub);
        
        
        
        
        String fileName = "./xml/"+Control.getPath()+"/tmp1.xml";
        File tmpfile = new File(fileName);
        if(tmpfile.exists()) tmpfile.delete();
        
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            //System.out.println("成功生成xml");
            String ans = readToString(fileName);
            tmpfile.delete();
            return ans;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return new String();
    }
     
     public String getScore(String fileName){
          try{
             DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
             DocumentBuilder db = dbf.newDocumentBuilder();
             Document document = db.parse(fileName);
             Element root = document.getDocumentElement();
             
             NodeList rspMsg = root.getElementsByTagName("score");
             String status  = rspMsg.item(0).getTextContent();
             
             
             return status;
         }catch(FileNotFoundException e){
             e.printStackTrace();
         }catch(ParserConfigurationException e){
             e.printStackTrace();
         }catch(SAXException e){
             e.printStackTrace();
         }catch(IOException e){
             e.printStackTrace();
         }
          return "";
     }
     
     public String isSubmitted(String fileName){
          try{
             DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
             DocumentBuilder db = dbf.newDocumentBuilder();
             Document document = db.parse(fileName);
             Element root = document.getDocumentElement();
             
             NodeList rspMsg = root.getElementsByTagName("rspMsg");
             String status  = rspMsg.item(0).getTextContent();
            
             if(status.equals("Success")){
                 return "true";
             }
             logger.log("[提交代码]status获取成功："+status, LogLevel.INFO);
             return status;
         }catch(FileNotFoundException e){
             e.printStackTrace();
             logger.log("[提交代码]status失败：FileNotFoundException"+e.getMessage(), LogLevel.ERROR);
         }catch(ParserConfigurationException e){
             e.printStackTrace();
             logger.log("[提交代码]status失败：ParserConfigurationException"+e.getMessage(), LogLevel.ERROR);
         }catch(SAXException e){
             e.printStackTrace();
             logger.log("[提交代码]status失败：SAXException"+e.getMessage(), LogLevel.ERROR);
         }catch(IOException e){
             e.printStackTrace();
             logger.log("[提交代码]status失败：IOException"+e.getMessage(), LogLevel.ERROR);
         }
          return "";
     }

    public String readToString(String fileName) {
		String encoding = "ISO-8859-1";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}
    
}
