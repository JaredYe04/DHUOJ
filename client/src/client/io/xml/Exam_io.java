/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.io.xml;

import client.model.Exam;
import client.util.Control;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList;  
import org.xml.sax.SAXException;

public class Exam_io {  
    
    String str = "./xml/"+Control.getPath()+"/examlist.dat";  
    private void save(){
        try {  
            List<Exam> l = new ArrayList();
            Document document = XmlUtils.StringToDocument(str); 
            NodeList list = document.getElementsByTagName("exam");  
            for(int i=0;i<list.getLength();i++){  
                Element ele = (Element)list.item(i);  
                Exam exam = new Exam();
 
                if (ele.getElementsByTagName("id").getLength()!=0) exam.setId(ele.getElementsByTagName("id").item(0).getTextContent());
                if (ele.getElementsByTagName("name").getLength()!=0) exam.setName(ele.getElementsByTagName("name").item(0).getTextContent());
                if (ele.getElementsByTagName("starttime").getLength()!=0) exam.setStarttime(ele.getElementsByTagName("starttime").item(0).getTextContent());
                if (ele.getElementsByTagName("endtime").getLength()!=0) exam.setEndtime(ele.getElementsByTagName("endtime").item(0).getTextContent());
                if (ele.getElementsByTagName("description").getLength()!=0) exam.setDescription(ele.getElementsByTagName("description").item(0).getTextContent());
                if (ele.getElementsByTagName("problemNum").getLength()!=0) exam.setProblemNum(ele.getElementsByTagName("problemNum").item(0).getTextContent());
                if (ele.getElementsByTagName("canGetHint").getLength()!=0) exam.setCanGetHint(ele.getElementsByTagName("canGetHint").item(0).getTextContent());
                if (ele.getElementsByTagName("partialScore").getLength()!=0) exam.setPartialScore(ele.getElementsByTagName("partialScore").item(0).getTextContent());
                if (ele.getElementsByTagName("submitOnlyAC").getLength()!=0) exam.setSubmitOnlyAC(ele.getElementsByTagName("submitOnlyAC").item(0).getTextContent());
                if (ele.getElementsByTagName("language").getLength()!=0) exam.setLanguage(ele.getElementsByTagName("language").item(0).getTextContent());
                if (ele.getElementsByTagName("teacherId").getLength()!=0) exam.setTeacherId(ele.getElementsByTagName("teacherId").item(0).getTextContent());
                if (ele.getElementsByTagName("updateTime").getLength()!=0) exam.setUpdateTime(ele.getElementsByTagName("updateTime").item(0).getTextContent());
                if (ele.getElementsByTagName("status").getLength()!=0) exam.setStatus(ele.getElementsByTagName("status").item(0).getTextContent());
                if (ele.getElementsByTagName("allowChangeSeat").getLength()!=0) exam.setAllowChangeSeat(ele.getElementsByTagName("allowChangeSeat").item(0).getTextContent());
                if (ele.getElementsByTagName("bestBefore").getLength()!=0) exam.setBestBefore(ele.getElementsByTagName("bestBefore").item(0).getTextContent());
                if (ele.getElementsByTagName("scoreCoef").getLength()!=0) exam.setScoreCoef(ele.getElementsByTagName("scoreCoef").item(0).getTextContent());
                if (ele.getElementsByTagName("limittime").getLength()!=0) exam.setLimitTime(ele.getElementsByTagName("limittime").item(0).getTextContent());
                if (ele.getElementsByTagName("type").getLength()!=0) exam.setType(ele.getElementsByTagName("type").item(0).getTextContent());

                l.add(exam);
            }  
            XmlUtils.DocumentToString(document, str,"GBK",Control.getKey());  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    public void add(Exam ex){  
        try {  
            // document  
            Document document = XmlUtils.getDocument(str,Control.getKey());  
  
            // Element  
            Element examlist = document.getDocumentElement();          
            Element exam = document.createElement("exam");
            
            Element id = document.createElement("id");         
            Element name = document.createElement("name");             
            Element starttime  = document.createElement("starttime");
            
            Element endtime = document.createElement("endtime");
            Element description = document.createElement("description");
            Element problemNum = document.createElement("problemNum");
            
            Element canGetHint = document.createElement("canGetHint");
            Element partialScore = document.createElement("partialScore");
            Element submitOnlyAC = document.createElement("submitOnlyAC");
            
            Element language = document.createElement("language");
            Element teacherId = document.createElement("teacherId");
            Element lastUpdateTime = document.createElement("lastUpdateTime");
            Element status = document.createElement("status");
            Element studentViewScore = document.createElement("studentViewScore");

            Element type = document.createElement("type");
            Element limittime = document.createElement("limittime");
  
            id.setTextContent(ex.getId());
            name.setTextContent(ex.getName());  
            starttime.setTextContent(ex.getStarttime());
            
            endtime.setTextContent(ex.getEndtime());
            description.setTextContent(ex.getDescription());
            problemNum.setTextContent(ex.getProblemNum());
            canGetHint.setTextContent(ex.getCanGetHint());
            partialScore.setTextContent(ex.getPartialScore());
            submitOnlyAC.setTextContent(ex.getSubmitOnlyAC());
            language.setTextContent(ex.getLanguage());
            teacherId.setTextContent(ex.getTeacherId());
            lastUpdateTime.setTextContent(ex.getUpdateTime());
            status.setTextContent(ex.getStatus());
            studentViewScore.setTextContent(ex.getStudentViewScore());
            type.setTextContent(ex.getType());

            limittime.setTextContent(null); 
            if (ex.getLimitTime() != null){
		   limittime.setTextContent(ex.getLimitTime());
	      }
           
            exam.appendChild(id);  
            exam.appendChild(name);
            exam.appendChild(starttime);
            exam.appendChild(endtime);
            exam.appendChild(description);
            exam.appendChild(problemNum);
            exam.appendChild(canGetHint);
            exam.appendChild(partialScore);
            exam.appendChild(submitOnlyAC);
            exam.appendChild(language);
            exam.appendChild(teacherId);
            exam.appendChild(lastUpdateTime);
            exam.appendChild(status);
            exam.appendChild(studentViewScore);
            exam.appendChild(type);
            exam.appendChild(limittime);

            examlist.appendChild(exam);  
       
            XmlUtils.write2Xml(document, str);  
 
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
          
    }  
    public List<Exam> getexamlist(){
        try {  
            List<Exam> l = new ArrayList();
            StringBuffer src = new StringBuffer(str);
            File f = new File(src.toString());
            if(!f.exists()){
                return new ArrayList();
            }
            Document document = XmlUtils.getDocument(str,Control.getKey()); 
            
            NodeList list = document.getElementsByTagName("exam");  
            for(int i=0;i<list.getLength();i++){  
                Element ele = (Element)list.item(i);  
                Exam exam = new Exam();
 
                if (ele.getElementsByTagName("id").getLength()!=0) exam.setId(ele.getElementsByTagName("id").item(0).getTextContent());
                if (ele.getElementsByTagName("name").getLength()!=0) exam.setName(ele.getElementsByTagName("name").item(0).getTextContent());
                if (ele.getElementsByTagName("starttime").getLength()!=0) exam.setStarttime(ele.getElementsByTagName("starttime").item(0).getTextContent());
                if (ele.getElementsByTagName("endtime").getLength()!=0) exam.setEndtime(ele.getElementsByTagName("endtime").item(0).getTextContent());
                if (ele.getElementsByTagName("description").getLength()!=0) exam.setDescription(ele.getElementsByTagName("description").item(0).getTextContent());
                if (ele.getElementsByTagName("problemNum").getLength()!=0) exam.setProblemNum(ele.getElementsByTagName("problemNum").item(0).getTextContent());
                if (ele.getElementsByTagName("canGetHint").getLength()!=0) exam.setCanGetHint(ele.getElementsByTagName("canGetHint").item(0).getTextContent());
                if (ele.getElementsByTagName("partialScore").getLength()!=0) exam.setPartialScore(ele.getElementsByTagName("partialScore").item(0).getTextContent());
                if (ele.getElementsByTagName("submitOnlyAC").getLength()!=0) exam.setSubmitOnlyAC(ele.getElementsByTagName("submitOnlyAC").item(0).getTextContent());
                if (ele.getElementsByTagName("language").getLength()!=0) exam.setLanguage(ele.getElementsByTagName("language").item(0).getTextContent());
                if (ele.getElementsByTagName("teacherId").getLength()!=0) exam.setTeacherId(ele.getElementsByTagName("teacherId").item(0).getTextContent());
                if (ele.getElementsByTagName("updateTime").getLength()!=0) exam.setUpdateTime(ele.getElementsByTagName("updateTime").item(0).getTextContent());
                if (ele.getElementsByTagName("status").getLength()!=0) exam.setStatus(ele.getElementsByTagName("status").item(0).getTextContent());
                if (ele.getElementsByTagName("allowChangeSeat").getLength()!=0) exam.setAllowChangeSeat(ele.getElementsByTagName("allowChangeSeat").item(0).getTextContent());
                if (ele.getElementsByTagName("studentViewScore").getLength()!=0) exam.setStudentViewScore(ele.getElementsByTagName("studentViewScore").item(0).getTextContent());
                if (ele.getElementsByTagName("type").getLength()!=0) exam.setType(ele.getElementsByTagName("type").item(0).getTextContent());

                if (ele.getElementsByTagName("limittime").getLength()!=0) exam.setLimitTime(ele.getElementsByTagName("limittime").item(0).getTextContent());
                
                l.add(exam);
            }  
            return l;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    public void changeStatus(String Id,String status){
        try {
            Document document = XmlUtils.getDocument(str,Control.getKey());
            NodeList list = document.getElementsByTagName("exam");
            for(int i=0;i<list.getLength();i++){
                Element ele = (Element)list.item(i);
                if (ele.getElementsByTagName("id").item(0).getTextContent().equals(Id)){
                    ele.getElementsByTagName("status").item(0).setTextContent(status);
                }
            }            
            XmlUtils.DocumentToString(document, str,"GBK",Control.getKey()); 
            
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } 
    }
    public void changeUuid(String Id,String uuid){
        try {
            Document document = XmlUtils.getDocument(str,Control.getKey());
            NodeList list = document.getElementsByTagName("exam");
            for(int i=0;i<list.getLength();i++){
                Element ele = (Element)list.item(i);
                if (ele.getElementsByTagName("id").item(0).getTextContent().equals(Id)){
                    ele.getElementsByTagName("uuid").item(0).setTextContent(uuid);
                }
            }            
            XmlUtils.DocumentToString(document, str,"GBK",Control.getKey()); 
            
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } 
    }
     public void updateExam(String id,Exam ex){
        try {
            Document document = XmlUtils.getDocument(str,Control.getKey());
            NodeList list = document.getElementsByTagName("exam");
            for(int i=0;i<list.getLength();i++){
                Element ele = (Element)list.item(i);
                if (ele.getElementsByTagName("id").item(0).getTextContent().equals(id)){
                    //ele.getElementsByTagName("id").item(0).setTextContent(ex.getId());
                    ele.getElementsByTagName("name").item(0).setTextContent(ex.getName());
                    ele.getElementsByTagName("starttime").item(0).setTextContent(ex.getStarttime());
                    ele.getElementsByTagName("endtime").item(0).setTextContent(ex.getEndtime());
                    ele.getElementsByTagName("description").item(0).setTextContent(ex.getDescription());
                    ele.getElementsByTagName("problemNum").item(0).setTextContent(ex.getProblemNum());
                    ele.getElementsByTagName("canGetHint").item(0).setTextContent(ex.getCanGetHint());
                    ele.getElementsByTagName("partialScore").item(0).setTextContent(ex.getPartialScore());
                    ele.getElementsByTagName("submitOnlyAC").item(0).setTextContent(ex.getSubmitOnlyAC());
                    ele.getElementsByTagName("language").item(0).setTextContent(ex.getLanguage());
                    ele.getElementsByTagName("teacherId").item(0).setTextContent(ex.getTeacherId());
                    ele.getElementsByTagName("updateTime").item(0).setTextContent(ex.getUpdateTime());
                    ele.getElementsByTagName("status").item(0).setTextContent(ex.getStatus());
                    ele.getElementsByTagName("allowChangeSeat").item(0).setTextContent(ex.getAllowChangeSeat());
                    ele.getElementsByTagName("type").item(0).setTextContent(ex.getType());
                    ele.getElementsByTagName("studentViewScore").item(0).setTextContent(ex.getStudentViewScore());

                    // by san_san
                    if (ex.getLimitTime() != null){
		          ele.getElementsByTagName("limittime").item(0).setTextContent(ex.getLimitTime());
	              }
                }
            }            
            XmlUtils.DocumentToString(document, str,"GBK",Control.getKey()); 
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } 
    }
    public List<Exam> getexamlistString(String str){
        try {  
            List<Exam> l = new ArrayList();
            Document document = XmlUtils.StringToDocument(str); 
            NodeList list = document.getElementsByTagName("exam");  
            for(int i=0;i<list.getLength();i++){  
                Element ele = (Element)list.item(i);  
                Exam exam = new Exam();
 
                if (ele.getElementsByTagName("id").getLength()!=0) exam.setId(ele.getElementsByTagName("id").item(0).getTextContent());
                if (ele.getElementsByTagName("name").getLength()!=0) exam.setName(ele.getElementsByTagName("name").item(0).getTextContent());
                if (ele.getElementsByTagName("starttime").getLength()!=0) exam.setStarttime(ele.getElementsByTagName("starttime").item(0).getTextContent());
                if (ele.getElementsByTagName("endtime").getLength()!=0) exam.setEndtime(ele.getElementsByTagName("endtime").item(0).getTextContent());
                if (ele.getElementsByTagName("description").getLength()!=0) exam.setDescription(ele.getElementsByTagName("description").item(0).getTextContent());
                if (ele.getElementsByTagName("problemNum").getLength()!=0) exam.setProblemNum(ele.getElementsByTagName("problemNum").item(0).getTextContent());
                if (ele.getElementsByTagName("canGetHint").getLength()!=0) exam.setCanGetHint(ele.getElementsByTagName("canGetHint").item(0).getTextContent());
                if (ele.getElementsByTagName("partialScore").getLength()!=0) exam.setPartialScore(ele.getElementsByTagName("partialScore").item(0).getTextContent());
                if (ele.getElementsByTagName("submitOnlyAC").getLength()!=0) exam.setSubmitOnlyAC(ele.getElementsByTagName("submitOnlyAC").item(0).getTextContent());
                if (ele.getElementsByTagName("language").getLength()!=0) exam.setLanguage(ele.getElementsByTagName("language").item(0).getTextContent());
                if (ele.getElementsByTagName("teacherId").getLength()!=0) exam.setTeacherId(ele.getElementsByTagName("teacherId").item(0).getTextContent());
                if (ele.getElementsByTagName("updateTime").getLength()!=0) exam.setUpdateTime(ele.getElementsByTagName("updateTime").item(0).getTextContent());
                if (ele.getElementsByTagName("status").getLength()!=0) exam.setStatus(ele.getElementsByTagName("status").item(0).getTextContent());
                if (ele.getElementsByTagName("allowChangeSeat").getLength()!=0) exam.setAllowChangeSeat(ele.getElementsByTagName("allowChangeSeat").item(0).getTextContent());
                if (ele.getElementsByTagName("studentViewScore").getLength()!=0) exam.setStudentViewScore(ele.getElementsByTagName("studentViewScore").item(0).getTextContent());
                if (ele.getElementsByTagName("type").getLength()!=0) exam.setType(ele.getElementsByTagName("type").item(0).getTextContent());

                if (ele.getElementsByTagName("limittime").getLength()!=0) exam.setLimitTime(ele.getElementsByTagName("limittime").item(0).getTextContent());

                l.add(exam);
            }  
            return l;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    public Exam getexamString(String str){
        try {  
//            System.out.println(str);
            Document document = XmlUtils.StringToDocument(str);
            NodeList list = document.getElementsByTagName("root");  

            Element ele = (Element)list.item(0);  
            Exam exam = new Exam();

            if (ele.getElementsByTagName("id").getLength()!=0) exam.setId(ele.getElementsByTagName("id").item(0).getTextContent());
                if (ele.getElementsByTagName("name").getLength()!=0) exam.setName(ele.getElementsByTagName("name").item(0).getTextContent());
                if (ele.getElementsByTagName("starttime").getLength()!=0) exam.setStarttime(ele.getElementsByTagName("starttime").item(0).getTextContent());
                if (ele.getElementsByTagName("endtime").getLength()!=0) exam.setEndtime(ele.getElementsByTagName("endtime").item(0).getTextContent());
                if (ele.getElementsByTagName("description").getLength()!=0) exam.setDescription(ele.getElementsByTagName("description").item(0).getTextContent());
                if (ele.getElementsByTagName("problemNum").getLength()!=0) exam.setProblemNum(ele.getElementsByTagName("problemNum").item(0).getTextContent());
                if (ele.getElementsByTagName("canGetHint").getLength()!=0) exam.setCanGetHint(ele.getElementsByTagName("canGetHint").item(0).getTextContent());
                if (ele.getElementsByTagName("partialScore").getLength()!=0) exam.setPartialScore(ele.getElementsByTagName("partialScore").item(0).getTextContent());
                if (ele.getElementsByTagName("submitOnlyAC").getLength()!=0) exam.setSubmitOnlyAC(ele.getElementsByTagName("submitOnlyAC").item(0).getTextContent());
                if (ele.getElementsByTagName("language").getLength()!=0) exam.setLanguage(ele.getElementsByTagName("language").item(0).getTextContent());
                if (ele.getElementsByTagName("teacherId").getLength()!=0) exam.setTeacherId(ele.getElementsByTagName("teacherId").item(0).getTextContent());
                if (ele.getElementsByTagName("updateTime").getLength()!=0) exam.setUpdateTime(ele.getElementsByTagName("updateTime").item(0).getTextContent());
                if (ele.getElementsByTagName("status").getLength()!=0) exam.setStatus(ele.getElementsByTagName("status").item(0).getTextContent());
                if (ele.getElementsByTagName("studentViewScore").getLength()!=0) exam.setStudentViewScore(ele.getElementsByTagName("studentViewScore").item(0).getTextContent());
                if (ele.getElementsByTagName("allowChangeSeat").getLength()!=0) exam.setAllowChangeSeat(ele.getElementsByTagName("allowChangeSeat").item(0).getTextContent());
                if (ele.getElementsByTagName("limittime").getLength()!=0) exam.setLimitTime(ele.getElementsByTagName("limittime").item(0).getTextContent());
                if (ele.getElementsByTagName("type").getLength()!=0) exam.setType(ele.getElementsByTagName("type").item(0).getTextContent());

            return exam;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
    public String getTime(){
        try { 
            Document document = XmlUtils.getDocument(str,Control.getKey());
            Element time = (Element) document.getElementsByTagName("time").item(0);
            return time.getTextContent();           
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } 
    }
}  