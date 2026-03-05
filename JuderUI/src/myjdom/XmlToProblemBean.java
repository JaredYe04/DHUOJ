/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjdom;

import common.Const;
import java.util.ArrayList;
import java.util.List;
import myjdom.model.ProblemBean;
import persistence.oj_beans.ProblemTestCaseBean;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import resultData.CompileInfo;
import resultData.Result;

/**
 *
 * @author ytxlo
 */
public class XmlToProblemBean extends XmlToBase implements XmlConvert<ProblemBean >{

    @Override
    public ProblemBean convertXML() throws Exception {
        ProblemBean pb =new ProblemBean();   
        List<ProblemTestCaseBean> testCaseList = new ArrayList<>();
  
       String s = doc.getDocumentElement().getElementsByTagName("time_limit").item(0).getTextContent();
        pb.setTimeOut(Float.parseFloat(doc.getDocumentElement().getElementsByTagName("time_limit").item(0).getTextContent()));
        NodeList list =doc.getDocumentElement().getElementsByTagName("Case");
        //  String ss=XmlUtil.DocumentToString(doc,"gbk");
         for(int i=0;i<list.getLength();i++){  
            Element e = (Element)list.item(i);
            ProblemTestCaseBean testCase = new ProblemTestCaseBean();
            testCase.setId(Integer.parseInt(e.getElementsByTagName("id").item(0).getTextContent()));
            if(!e.getElementsByTagName("problemId").item(0).getTextContent().isEmpty()){
                testCase.setProblemId(Integer.parseInt(e.getElementsByTagName("problemId").item(0).getTextContent()));
            }
            testCase.setInput(e.getElementsByTagName("input").item(0).getTextContent());
            testCase.setOutput(e.getElementsByTagName("output").item(0).getTextContent());
            testCaseList.add(testCase);
        }
        pb.setTestCaseBeanList(testCaseList);
        return pb;
    }
}
