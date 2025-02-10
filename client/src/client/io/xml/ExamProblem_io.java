/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.io.xml;

import client.model.ExamProblem;
import client.util.Control;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author ytxlo
 */
public class ExamProblem_io {
    private String path = "./xml/examproblems.dat";
    public ExamProblem_io(){
        super();
    }
    public ExamProblem_io(String examId){
        super();
        path = "./xml/"+Control.getPath()+"/examproblems_"+examId+".dat";
    }
    public List<ExamProblem> getProblemList() throws Exception{
            List<ExamProblem> eplist = new ArrayList();
             StringBuffer src = new StringBuffer(path);
            src.delete(src.length()-3, src.length());
            src.append("dat");
            File f = new File(src.toString());
            if(!f.exists()){
                return eplist;
            }
            Document document = XmlUtils.getDocument(path,Control.getKey());
            
            NodeList list = document.getElementsByTagName("problem");
            for(int i=0;i<list.getLength();i++){
                Element ele = (Element)list.item(i);
                ExamProblem ep = new ExamProblem();
                ep.setProblemId(ele.getElementsByTagName("problemId").item(0).getTextContent());
                ep.setScore(ele.getElementsByTagName("score").item(0).getTextContent());
                ep.setDisplaySequence(ele.getElementsByTagName("displaySequence").item(0).getTextContent());
                ep.setTitle(ele.getElementsByTagName("title").item(0).getTextContent());
                ep.setDifficulty(ele.getElementsByTagName("difficulty").item(0).getTextContent());
                ep.setBestBefore(ele.getElementsByTagName("bestBefore").item(0).getTextContent());
                ep.setBestBeforeForClass(ele.getElementsByTagName("bestBeforeForClass").item(0).getTextContent());
                ep.setScoreCoefForClass(ele.getElementsByTagName("scoreCoefForClass").item(0).getTextContent());
                ep.setDeadLineForClass(ele.getElementsByTagName("deadlineForClass").item(0).getTextContent()); // by san_san

                if ("".equals(ele.getElementsByTagName("updateTime").item(0).getTextContent())) {
                    ep.setUpdateTime("2000-01-01 00:00:00");
                }else{
                    ep.setUpdateTime(ele.getElementsByTagName("updateTime").item(0).getTextContent());
                }
                
                eplist.add(ep);                
            }
            return eplist;
    }
    
}
