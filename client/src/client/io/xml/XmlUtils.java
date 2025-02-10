/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.io.xml;

import client.util.Decrypt;
import client.service.DownFileWrite;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream;
  
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerException;  
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.stream.StreamResult;  
  
import org.w3c.dom.Document;  
import org.xml.sax.SAXException;  
  
public class XmlUtils { 
    public static Document createNewDocument() throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = factory.newDocumentBuilder(); 
        return builder.newDocument();
    }
    public static Document StringToDocument(String str) throws ParserConfigurationException, SAXException, IOException{  
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = factory.newDocumentBuilder();  
        InputStream is = new ByteArrayInputStream(str.getBytes());
        Document document = builder.parse(is);  
        return document;  
    }  
    public static Document getDocument(String str,String key) throws ParserConfigurationException, SAXException, IOException{  
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = factory.newDocumentBuilder(); 
//        DES des = new DES("1413201160");
//        StringBuffer src = new StringBuffer(str);
//        src.delete(src.length()-3, src.length());
//        src.append("dat");
//        try {
//            des.decrypt(src.toString(),str);
//        } catch (Exception ex) {
//            Logger.getLogger(XmlUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        File file=new File(str);
//        if(!file.exists()||file.isDirectory())
//            throw new FileNotFoundException();
//        FileInputStream fis=new FileInputStream(file);
//        InputStreamReader reader = new InputStreamReader(fis, charset);
//        char[] buf = new char[64];
//        StringBuffer sb=new StringBuffer();
//        int count = 0; 
//        while ((count = reader.read(buf)) > 0) { 
//            sb.append(buf,0,count);
//        } 
//        reader.close();
//        fis.close();
//        String result = sb.toString();
//        InputStream is = new ByteArrayInputStream(result.getBytes(charset));
//        Document document = builder.parse(is);  
//        file.delete();

        File f = new File(str);
        if(!f.exists()||f.isDirectory())
            f.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());  
        BufferedInputStream in = null;  
        try{  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while(-1 != (len = in.read(buffer,0,buf_size))){  
                bos.write(buffer,0,len);  
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
        if(in!=null){
        in.close();
        }
        }
//        System.out.println(Decrypt.decrypt(key, bos.toByteArray()));
//        System.out.println("Ω‚√‹"+key);
        String tmp = Decrypt.decrypt(key, bos.toByteArray());
        InputStream is = new ByteArrayInputStream(tmp.getBytes());
        return builder.parse(is);  
    } 
    
    public static void DocumentToString(Document doc,String path,String bm,String key) throws Exception{
	String xmlStr = null;
	TransformerFactory tf = TransformerFactory.newInstance(); 
	Transformer t = tf.newTransformer(); 
	t.setOutputProperty("encoding",bm);
	t.setOutputProperty(OutputKeys.INDENT, "yes"); 
	t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");  
	ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	t.transform(new DOMSource(doc), new StreamResult(bos)); 
	xmlStr = bos.toString();
//        System.out.println(xmlStr);
	new DownFileWrite().write(path, xmlStr, key);
    }  
    public static void write2Xml(Document document,String str) throws FileNotFoundException, TransformerException{  
        File file=new File(str);
        TransformerFactory factory = TransformerFactory.newInstance();  
        Transformer transformer= factory.newTransformer();  
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	transformer.transform(new DOMSource(document), new StreamResult(str)); 
    }  
}