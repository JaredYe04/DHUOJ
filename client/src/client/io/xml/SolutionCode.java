/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.io.xml;

import client.util.Control;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import client.util.similarity.Similarity;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author 毛泉
 */
public class SolutionCode {

    private Document document;
    String codes;

    public boolean init() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.document = db.newDocument();
            return true;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return false;
    }

    //创建xml
    public void createXml(String fileName) {
        Element root = this.document.createElement("code");
        this.document.appendChild(root);
        Element code = this.document.createElement("finished");
        //code.setAttribute("quality","saved");
        code.appendChild(this.document.createTextNode("false"));
        root.appendChild(code);

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    //得到提交次数
    public String getSubmitTimes(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String code;
//            NodeList submits = root.getElementsByTagName("submit");
//            return "" + submits.getLength();

            NodeList submits = root.getElementsByTagName("submit");
            if (submits.getLength() == 0) {
                return "0";
            }
            code = submits.item(submits.getLength() - 1).getChildNodes().item(19).getTextContent();

            return code;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String();
    }

    public String getTime(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String code;
            NodeList submits = root.getElementsByTagName("submit");
            code = submits.item(submits.getLength() - 1).getChildNodes().item(5).getTextContent();
            return code;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String();
    }

    public String getId(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String code;
            NodeList submits = root.getElementsByTagName("submit");
            code = submits.item(submits.getLength() - 1).getChildNodes().item(3).getTextContent();

            return code;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String();
    }

    //得到代码
    public String getCode(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String code = "";
            NodeList submits = root.getElementsByTagName("submit");
            if (submits.getLength() != 0) {
                code = submits.item(submits.getLength() - 1).getChildNodes().item(1).getTextContent();
            }

            NodeList save = root.getElementsByTagName("save");

            if (save.getLength() == 1) {
                code = save.item(0).getChildNodes().item(1).getTextContent();
            }
//               NodeList wrong = root.getElementsByTagName("case");
//          code = wrong.item(1).getChildNodes().item(3).getTextContent();
//           
            // code = wrong.item(0).getChildNodes().item(1).getTextContent();

            return code;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String();
    }

    public void delSave(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.document = db.parse(fileName);

            Element root = document.getDocumentElement();

            NodeList saved = document.getElementsByTagName("save");
            if (saved.getLength() != 0) {
                Node nodeDel = saved.item(0);
                nodeDel.getParentNode().removeChild(nodeDel);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStatus(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String code;
            NodeList submits = root.getElementsByTagName("submit");
            code = submits.item(submits.getLength() - 1).getChildNodes().item(7).getTextContent();

            return code;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String();
    }

    public String[] getCorrectIds(String fileName, String Type) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String code;
            NodeList submits = root.getElementsByTagName("submit");
            code = submits.item(submits.getLength() - 1).getChildNodes().item(9).getTextContent();
            if (Type.equals("list")) {
                List<String> tp = new ArrayList<String>();
                String tmp = "";
                code += ",";
                for (int j = 0; j < code.length(); j++) {
                    if (code.charAt(j) >= '0' && code.charAt(j) <= '9') {
                        tmp += code.charAt(j);
                    } else {
                        tp.add(new String(tmp));
                        tmp = "";
                    }
                }

                int size = tp.size();

                String[] ans = new String[size];

                for (int i = 0; i < size; i++) {
                    ans[i] = tp.get(i);
                }
                return ans;
            } else {
                String[] ans = new String[1];
                ans[0] = code;
                return ans;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[1];
    }

    public double getScore(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String code;
            NodeList submits = root.getElementsByTagName("submit");
            code = submits.item(submits.getLength() - 1).getChildNodes().item(13).getTextContent();

            return Double.parseDouble(code);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1.0;
    }

    public String getRemark(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String code;
            NodeList submits = root.getElementsByTagName("submit");
            code = submits.item(submits.getLength() - 1).getChildNodes().item(15).getTextContent();

            return code;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<WrongCase> getWrongCases(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            List<WrongCase> ans = new ArrayList<WrongCase>();
            NodeList submit = root.getElementsByTagName("submit");
            Element last = (Element) submit.item(submit.getLength() - 1);
            NodeList wrong = last.getElementsByTagName("case");

            int size = wrong.getLength();
            for (int j = 0; j < size; j++) {
                String ID = wrong.item(j).getChildNodes().item(1).getTextContent();
                String OUTPUT = wrong.item(j).getChildNodes().item(3).getTextContent();
                String STATUS = wrong.item(j).getChildNodes().item(5).getTextContent();
                WrongCase tp = new WrongCase(ID, OUTPUT, STATUS);
                ans.add(tp);
            }
            return ans;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<WrongCase>();
    }

    public Boolean isSaved(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();
            NodeList save = root.getElementsByTagName("save");
            if (save.getLength() == 1 && !(save.item(0).getChildNodes().item(1).getTextContent().isEmpty())) {
                return true;
            }
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setFinished(String fileName, String finish) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();
            NodeList finished = root.getElementsByTagName("finished");
            finished.item(0).setTextContent(finish);

            //System.out.println(finished.item(0).getTextContent());
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            //System.out.println("成功修改xml");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean isEmpty(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();
            String code = "";
            NodeList submits = root.getElementsByTagName("submit");
            NodeList saves = root.getElementsByTagName("submit");
            if (submits.getLength() == 0 && saves.getLength() == 0) {
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isFinished(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();
            String status = root.getChildNodes().item(1).getTextContent();
            //System.out.println(status);
            if (status.equals("true")) {
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //保存代码之后升级xml
    public boolean updateXml(String fileName, String sourceCodes) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.document = db.parse(fileName);

            Element root = document.getDocumentElement();

            NodeList saved = document.getElementsByTagName("save");
            if (saved.getLength() != 0) {
                Node nodeDel = saved.item(0);
                nodeDel.getParentNode().removeChild(nodeDel);
            }

            Element save = document.createElement("save");

            Element sourceCode = document.createElement("sourceCode");
            sourceCode.appendChild(this.document.createTextNode(sourceCodes));
            save.appendChild(sourceCode);

            Element time = document.createElement("time");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());
            time.appendChild(this.document.createTextNode(date));
            save.appendChild(time);

            root.appendChild(save);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            //System.out.println("成功修改xml");
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

//     提交代码后升级xml
    public void updateXml(String fileName, String submitTime, String Id, Answer answer, String score, String sourceCodes, int times) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.document = db.parse(fileName);

            Element root = document.getDocumentElement();

            NodeList saved = document.getElementsByTagName("save");
            if (saved.getLength() != 0) {
                Node nodeDel = saved.item(0);
                nodeDel.getParentNode().removeChild(nodeDel);
            }

            Element submit = document.createElement("submit");

            Element sourceCode = document.createElement("sourceCode");
            sourceCode.appendChild(this.document.createTextNode(sourceCodes));
            submit.appendChild(sourceCode);

            Element id = document.createElement("id");
            id.appendChild(this.document.createTextNode(Id));
            submit.appendChild(id);

            Element time = document.createElement("time");
            //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            //String date = df.format(new Date());
            String date = submitTime;
            time.appendChild(this.document.createTextNode(date));
            submit.appendChild(time);

            Element status = document.createElement("status");
            status.appendChild(this.document.createTextNode(answer.getStatus()));
            submit.appendChild(status);

            Element correct = document.createElement("correctCaseIds");
            correct.appendChild(this.document.createTextNode(answer.getCorrectCaseIds()));
            submit.appendChild(correct);

            Element wrong = document.createElement("wrongCaseIds");
            int casesNum = answer.getStatusOfTestCase().length;
            System.out.println(casesNum);
            for (int j = 0; j < casesNum; j++) {
                if (!answer.getStatusOfTestCase()[j].equals("AC")) {
                    Element Case = document.createElement("case");

                    Element idOfWrong = document.createElement("id");
                    idOfWrong.appendChild(this.document.createTextNode(answer.getTestCaseId()[j]));
                    Case.appendChild(idOfWrong);

                    Element userOutput = document.createElement("output");
                    String output = answer.getUsersOutput()[j];
                    userOutput.appendChild(this.document.createTextNode(output));
                    Case.appendChild(userOutput);

                    Element wrongstatus = document.createElement("status");
                    wrongstatus.appendChild(this.document.createTextNode(answer.getStatusOfTestCase()[j]));
                    Case.appendChild(wrongstatus);

                    wrong.appendChild(Case);
                }
            }
            submit.appendChild(wrong);
             
            Element scores = document.createElement("score");
            scores.appendChild(this.document.createTextNode(score));
            submit.appendChild(scores);

            Element remarks = document.createElement("remark");
            remarks.appendChild(this.document.createTextNode(answer.getRemark()));
            submit.appendChild(remarks);

            Element similarity = document.createElement("similarity");
            similarity.appendChild(this.document.createTextNode(""));
            submit.appendChild(similarity);

            Element submitTimes = document.createElement("submits");
            submitTimes.appendChild(this.document.createTextNode(String.valueOf(times)));
            submit.appendChild(submitTimes);

            root.appendChild(submit);
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);        
            transformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); 
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            transformer.transform(source, new StreamResult(bos)); 
	    String s = bos.toString();
            s=s.replace("&#", "&amp;#");
            document=XmlUtils.StringToDocument(s);
             source = new DOMSource(document);
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            //System.out.println("成功修改xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSimi(String fileName, String isCopied) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.document = db.parse(fileName);

            Element root = document.getDocumentElement();

            NodeList submits = root.getElementsByTagName("submit");
            submits.item(submits.getLength() - 1).getChildNodes().item(17).setTextContent(isCopied);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            //System.out.println("成功修改xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //得到提交次数
    public void setTime(String fileName, String time) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.document = db.parse(fileName);

            Element root = document.getDocumentElement();

            NodeList submits = root.getElementsByTagName("submit");
            submits.item(submits.getLength() - 1).getChildNodes().item(19).setTextContent(time);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            //System.out.println("成功修改xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Similarity getSimi(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String copied;
            NodeList submits = root.getElementsByTagName("submit");
            copied = submits.item(submits.getLength() - 1).getChildNodes().item(17).getTextContent();
            Similarity ans;
            if (copied.equals("true")) {
                ans = new Similarity("true", submits.item(submits.getLength() - 1).getChildNodes().item(19).getTextContent(), submits.item(submits.getLength() - 1).getChildNodes().item(21).getTextContent());

            } else {
                ans = new Similarity("false", submits.item(submits.getLength() - 1).getChildNodes().item(19).getTextContent(), submits.item(submits.getLength() - 1).getChildNodes().item(21).getTextContent());

            }
            return ans;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Similarity("", "", "");
    }

    public String isCopied(String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileName);
            Element root = document.getDocumentElement();

            String copied;
            NodeList submits = root.getElementsByTagName("submit");
            if (submits.getLength() == 0) {
                return "needless";
            }
            copied = submits.item(submits.getLength() - 1).getChildNodes().item(17).getTextContent();

            if (copied.equals("true")) {
                return "true";
            } else if (copied.equals("false")) {
                return "false";
            } else {
                return "needless";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }

    //test
    public static void main(String args[]) {
        SolutionCode a = new SolutionCode();
        a.init();
        a.createXml("./xml/" + Control.getPath() + "/00.xml");
        String[] testCaseId = {"1", "2", "3", "4"};
        String[] userOutput = {"2", "3", "4", "45"};
        String[] statusOfTestCase = {"AC", "WA", "AC", "RE"};
        String status = "WA";
        String remark = "aaaaa";
        String correctCaseIds = "1,3";
        //
        System.out.println("3\n" + remark);

        Answer answer = new Answer(testCaseId, userOutput, statusOfTestCase, status, remark, correctCaseIds);

        //a.updateXml("./xml/00.xml", "02", answer, "100", "aaaaaaaaaaaaa","true");
        a.updateXml("./xml/00.xml", "ccccccc");

        // a.updateXml("./xml/00.xml", "02", answer, "96", "bbbbbbbbbb","false");
        a.setFinished("./xml/00.xml", "true");

        //System.out.println(a.getSubmitTimes("./xml/00.xml"));
        //System.out.println(a.getCode("./xml/00.xml"));
        //System.out.println(a.isCopied("./xml/00.xml"));
    }

    public void saveSolution(String fileName, int Id, String sourceCodes, String remark, double score, String correctCaseIds, List<WrongCase> wrongCases, String status) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.document = db.parse(fileName);

            Element root = document.getDocumentElement();

//            NodeList saved = document.getElementsByTagName("save");
//            if (saved.getLength() != 0) {
//                Node nodeDel = saved.item(0);
//                nodeDel.getParentNode().removeChild(nodeDel);
//            }
            Element submit = document.createElement("submit");

            Element sourceCode = document.createElement("sourceCode");
            sourceCode.appendChild(this.document.createTextNode(sourceCodes));
            submit.appendChild(sourceCode);

            Element id = document.createElement("id");
            id.appendChild(this.document.createTextNode(String.valueOf(Id)));
            submit.appendChild(id);

            Element time = document.createElement("time");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String date = df.format(new Date());
            time.appendChild(this.document.createTextNode(date));
            submit.appendChild(time);

            Element statue = document.createElement("statue");
            statue.appendChild(this.document.createTextNode(status));
            submit.appendChild(statue);

            Element correct = document.createElement("correctCaseIds");
            correct.appendChild(this.document.createTextNode(correctCaseIds));
            submit.appendChild(correct);

            Element wrong = document.createElement("wrongCaseIds");
            int size = wrongCases.size();
            for (int j = 0; j < size; j++) {
                Element Case = document.createElement("case");

                Element idOfWrong = document.createElement("id");
                idOfWrong.appendChild(this.document.createTextNode(wrongCases.get(j).getId()));
                Case.appendChild(idOfWrong);

                Element userOutput = document.createElement("output");
                userOutput.appendChild(this.document.createTextNode(wrongCases.get(j).getOutput()));
                Case.appendChild(userOutput);

                wrong.appendChild(Case);
            }
            submit.appendChild(wrong);

            Element scores = document.createElement("score");
            scores.appendChild(this.document.createTextNode(String.valueOf(score)));
            submit.appendChild(scores);

            Element remarks = document.createElement("remark");
            remarks.appendChild(this.document.createTextNode(remark));
            submit.appendChild(remarks);

            root.appendChild(submit);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            StreamResult result = new StreamResult(pw);
            transformer.transform(source, result);
            //System.out.println("成功修改xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
