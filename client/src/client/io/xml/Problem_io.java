/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.io.xml;

import client.model.Case;
import client.model.ExamProblem;
import client.model.Problem;
import client.service.DownSwingWorker;
import client.util.Control;
import client.view.frame.ProgressBarFrame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;


/**
 *
 * @author ytxlo
 */
public class Problem_io {

    private String path;

    public Problem_io(String problemId) {
        this.path = "./xml/"+Control.getPath()+"/problem_"+Control.getExamId()+"-"+problemId+".dat";
    }
    
    public static String documentToString(Document doc) {
    try {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    } catch (TransformerException e) {
        e.printStackTrace();
        return null;
    }
}

    public Problem getproblem() {
        try {

            Document document = XmlUtils.getDocument(path, Control.getKey());
            
//            String xmlString = documentToString(document);
//            System.out.println("path:" + path + "\n" + xmlString);

            Element ele = (Element) document.getElementsByTagName("root").item(0);
            Problem p = new Problem();
            p.setId(ele.getElementsByTagName("id").item(0).getTextContent());
            p.setTitle(ele.getElementsByTagName("title").item(0).getTextContent());
            p.setDescription(ele.getElementsByTagName("description").item(0).getTextContent());


            p.setMemory_limit(ele.getElementsByTagName("memory_limit").item(0).getTextContent());
            p.setTime_limit(ele.getElementsByTagName("time_limit").item(0).getTextContent());
            p.setInputRequirement(ele.getElementsByTagName("inputRequirement").item(0).getTextContent());

            p.setOutputRequirement(ele.getElementsByTagName("outputRequirement").item(0).getTextContent());
            p.setSample_input(ele.getElementsByTagName("sample_input").item(0).getTextContent());
            p.setSample_output(ele.getElementsByTagName("sample_output").item(0).getTextContent());

            p.setAuthor(ele.getElementsByTagName("author").item(0).getTextContent());
            p.setDifficulty(ele.getElementsByTagName("difficulty").item(0).getTextContent());
            p.setScoreGrade(ele.getElementsByTagName("scoreGrade").item(0).getTextContent());

            p.setChapterName(ele.getElementsByTagName("chapterName").item(0).getTextContent());
            p.setCheckSimilarity(ele.getElementsByTagName("cheakSimilarity").item(0).getTextContent());
            p.setSimilarityThreshold(ele.getElementsByTagName("similarityThreshold").item(0).getTextContent());

            if("".equals(ele.getElementsByTagName("updateTime").item(0).getTextContent())){
                p.setUpdateTime("2000-01-01 00:00:00");
            }else{
                p.setUpdateTime(ele.getElementsByTagName("updateTime").item(0).getTextContent());
            }
            return p;
        } catch (Exception e) {
            new File(path).delete();
            e.printStackTrace();
            return null;
        }
    }

    public void setProblemDesc(Problem pro) {
        try {
            Document document = XmlUtils.getDocument(path, Control.getKey());
            NodeList list = document.getElementsByTagName("root");
            Element ele = (Element) list.item(0);
            ele.getElementsByTagName("description").item(0).setTextContent(pro.getDescription());
            XmlUtils.write2Xml(document, path);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Problem_io.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Problem_io.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Problem_io.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Problem_io.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Problem_io.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getPath() {
        return path;
    }
}
