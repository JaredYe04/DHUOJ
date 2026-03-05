/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.service;

import client.util.Control;
import client.model.Information;
import client.model.ProblemTestCase;
import client.model.Problemlist;
import client.model.StudentExamDetail;
import client.view.frame.LoginFrame;
import client.view.panel.AnswerTablePanel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import main.Answer;
import org.apache.cxf.endpoint.Client;
import persistence.oj_beans.ProblemTestCaseBean;
import client.util.similarity.Similarity;
import client.util.similarity.check;
import client.io.xml.ProblemTestCase_io;
import client.io.xml.RefreshSolution;
import client.io.xml.SolutionCode;
import client.io.xml.StudentExamDetail_io;
import client.io.xml.SubmitProblem;
import client.io.xml.WrongCase;
import java.util.Calendar;

/**
 *
 * @author ytxlo
 */
public class CompareStatus {
    private  List<String> changeStatus;
    private  List<String> submitP;
    private  List<StudentExamDetail> Status;
    private  String examId;
    public  void compare(List<StudentExamDetail> oldStatusList, List<StudentExamDetail> newStatusList,List<Problemlist> pl,String examId){
        this.examId = examId;
        changeStatus = new ArrayList();
        submitP = new ArrayList();
        Status = new ArrayList();
        HashMap<String,String> oldpl = new HashMap<String,String>();
        for(int i=0;i<pl.size();i++){
            oldpl.put(pl.get(i).getProblemId(),String.valueOf(i+1));
        }
        HashMap<String, StudentExamDetail> oldhash = new HashMap<String, StudentExamDetail>();
        for(int i = 0; i < oldStatusList.size() ; i++){
            oldhash.put(oldStatusList.get(i).getProblemId(), oldStatusList.get(i));
        }
       
        for(int i = 0; i < newStatusList.size() ; i++){
            StudentExamDetail newStatus = newStatusList.get(i);
            if (oldhash.containsKey(newStatus.getProblemId())){
                StudentExamDetail oldStatus = oldhash.get(newStatus.getProblemId());
                if (!newStatus.getStatus().equals(oldStatus.getStatus())){
                    String no = null;
                    if (oldpl.containsKey(newStatus.getProblemId())){
                        no = oldpl.get(newStatus.getProblemId());
                    }
                    StringBuffer sb = new StringBuffer("第");
                    sb.append(no).append("题状态已经由").append(oldStatus.getStatus()).append("变为").append(newStatus.getStatus());
                    changeStatus.add(sb.toString());
                    clearOldSolution(newStatus.getProblemId());
//                    getnew(newStatus.getProblemId(),examId);
                }
                else if(!newStatus.getSolutionId().equals(oldStatus.getSolutionId())){
                    String no = null;
                    if (oldpl.containsKey(newStatus.getProblemId())){
                        no = oldpl.get(newStatus.getProblemId());
                    }
                    StringBuffer sb = new StringBuffer("第");
                    sb.append(no).append("题状态已经由").append(oldStatus.getStatus()).append("变为").append(newStatus.getStatus());
                    changeStatus.add(sb.toString());
                    clearOldSolution(newStatus.getProblemId());
//                    getnew(newStatus.getProblemId(),examId);
                }
            }
            else{
                String no = null;
                if (oldpl.containsKey(newStatus.getProblemId())){
                    no = oldpl.get(newStatus.getProblemId());
                }
                StringBuffer sb = new StringBuffer("第");
                sb.append(no).append("题状态已经由无变为").append(newStatus.getStatus());
                changeStatus.add(sb.toString());
                clearOldSolution(newStatus.getProblemId());
//                getnew(newStatus.getProblemId(),examId);
            }
            if(newStatus.getFinished().equals("false")&&newStatus.getStatus().equals("AC")){
                String no = null;
                if (oldpl.containsKey(newStatus.getProblemId())){
                    no = oldpl.get(newStatus.getProblemId());
                }
                submitP.add(no);
                Status.add(newStatus);
            }
        }
        
    }
    public void doPrompt(){
        if (changeStatus.size()==0){
            if (Control.getPrompt()==true) JOptionPane.showMessageDialog(null,"题目状态无变化。");
        }
        else if (changeStatus.size()<=5){
            StringBuffer sb = new StringBuffer("一共有"+changeStatus.size()+"题状态发生变化，情况如下："+"\n");
            for(int i = 0;i<changeStatus.size();i++){
                sb.append(changeStatus.get(i)).append("\n");
            }
            JOptionPane.showMessageDialog(null,sb.toString());
        }
        else{
            JOptionPane.showMessageDialog(null,"一共有"+changeStatus.size()+"题状态发生变化。");
        }
        for(int i=0;i<submitP.size();i++){
            String No = submitP.get(i);
            StudentExamDetail status = Status.get(i);
            int r =JOptionPane .showConfirmDialog(null,"第"+No+"题已经AC，是否提交本题？", "提示", JOptionPane.YES_NO_OPTION);
                if (r==JOptionPane.YES_OPTION){
//                    clearOldSolution(status.getProblemId());
                    
                    Boolean check =Boolean.parseBoolean(Control.getMainFrame().getInformation(status.getProblemId()).getCheckSimilarity());
                    Float similarity = Float.parseFloat(Control.getMainFrame().getInformation(status.getProblemId()).getSimilarityThreshold());
                    submitProblem(status.getProblemId(),examId,check,similarity);
                }
        }
    }
    
    //TODO 可以被删除了
    //todo
    public void getnew(String proId,String examId){
             String username = Control.getUser().getUserName();
             String passwd =Control.getUser().getPassword();
//	     Client client = LoginFrame.client;
             String toWrite = new String();
             try {
                toWrite = Control.getWebsService().getExamProblemStatus(username,passwd,Integer.parseInt(examId),Integer.parseInt(proId));
                 System.out.println("------------------------------------------------------------\n"+toWrite);
                toWrite=toWrite.replace("&#", "&amp;#");
            } catch (Exception e) {
                 e.printStackTrace();
            }
            String backFile = "./xml/"+Control.getPath()+"/afterRefresh.xml";
            File tmpFile = new File(backFile);
            if(tmpFile.exists()) tmpFile.delete();
            TextToFile(backFile,toWrite);
            RefreshSolution rf = new RefreshSolution();
            rf.init();
            SolutionCode sc = new SolutionCode();
            sc.init();
            String msg = rf.hasSubmitted(backFile);
            if(msg.equals("true")){
                if(rf.isNew(backFile, proId) || !rf.getStatus(backFile).equals(sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"))){
                     fresh(backFile,proId);
                }
            if(tmpFile.exists()) tmpFile.delete();
        }
    }
    
    public void fresh(String routing,String proId){
         List<ProblemTestCase> testCaseBeansTmp = new ArrayList<ProblemTestCase>();
                    List<ProblemTestCaseBean> testCaseBeans = new  ArrayList<ProblemTestCaseBean>();
                    testCaseBeansTmp = new ProblemTestCase_io(proId).getProblemTestCaselist();
                    String[] testCaseIn = new String[testCaseBeansTmp.size()];
                    String[] testCaseOut = new String[testCaseBeansTmp.size()];
                    for(int j = 0 ; j<testCaseBeansTmp.size();j++){
                        testCaseBeans.add(new ProblemTestCaseBean());
                        testCaseBeans.get(j).setId(Integer.parseInt(testCaseBeansTmp.get(j).getId()));
                        testCaseBeans.get(j).setInput(testCaseBeansTmp.get(j).getInput());
                        testCaseBeans.get(j).setOutput(testCaseBeansTmp.get(j).getOutput());
                        testCaseBeans.get(j).setProblemId(Integer.parseInt(testCaseBeansTmp.get(j).getProblemId()));
                        testCaseIn[j] = testCaseBeansTmp.get(j).getInput();
                        testCaseOut[j] = testCaseBeansTmp.get(j).getOutput();
                    }
                    RefreshSolution rf = new RefreshSolution();
                    rf.init();
                    List<WrongCase> wrongCases = rf.getWrongCases(routing);
                    String status = rf.getStatus(routing);
                    
                    int size = testCaseBeans.size();
                    String[] testCaseIds = new String[size];
                    String[] userOutput = new String[size];
                    String[] statusofTestCase = new String[size];
                    int k = 0;
                    
                    for(int i = 0;i<size ;i++){
                        if(k<wrongCases.size() && testCaseBeansTmp.get(i).getId().equals(wrongCases.get(k).getId())){
                            testCaseIds[i] = testCaseBeansTmp.get(i).getId();
                            userOutput[i] = wrongCases.get(k).getOutput();
                            statusofTestCase[i] = wrongCases.get(k).getStatus();
                            k++;
                        }else if(!status.equals("CE")){
                            testCaseIds[i] = testCaseBeansTmp.get(i).getId();
                            userOutput[i] = "";
                            statusofTestCase[i] = "AC";
                            
                        }else{
                            testCaseIds[i] = testCaseBeansTmp.get(i).getId();
                            userOutput[i] = "";
                            statusofTestCase[i] = "CE";
                        }
                    }
                    String code = rf.getSourceCode(routing);
                    String Id = rf.getSolutionId(routing);
                    String score = String.valueOf(rf.getScore(routing));
                    String finished = String.valueOf(rf.isFinished(routing));
                    String remark = rf.getRemark(routing);
                    String correctCaseIds = rf.getCorrectIds(routing);
                    String submittimes = rf.getSubmitTimes(routing);
                    Answer answer = new Answer(testCaseIds,userOutput,statusofTestCase,status,remark,correctCaseIds);
                    SolutionCode sc = new SolutionCode();
                    sc.init();
                    String backFile = "./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml";
                    File tmpFile = new File(backFile);
                    if(!tmpFile.exists()) sc.createXml(backFile);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    String date = df.format(new Date());
                    sc.updateXml("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml",date , Id, answer, score, code,Integer.parseInt(submittimes));
                    //if(finished.equals("true")){
                        sc.setFinished(backFile, finished);
                   // }
                    
    }
            
            private void TextToFile(String strFilename, String strBuffer) {
             try{
                    File fileText = new File(strFilename);
                    FileWriter fileWriter = new FileWriter(fileText);
                    fileWriter.write(strBuffer);
                    fileWriter.close();
                }catch (IOException e){
                   e.printStackTrace();
                }
        }
            
    public void submitProblem(String proId,String examId,Boolean checkOrNot,Float similarity){
        String problemXml;
        Boolean flag = true;
          SolutionCode sc = new SolutionCode();
          sc.init();
          Similarity simi;
          if(checkOrNot==true){
            simi = new check().check(similarity,sc.getCode("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"),String.valueOf(proId));
            if(simi.getIsCopied().equals("error")){
                     String message = "无法进行提交，请检查网络连接";
                     JOptionPane.showConfirmDialog(Control.getMainFrame(),
                            message,"提示",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE
                            ); 
                    }
            sc.setSimi("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml", simi.getIsCopied());
        }else{
            simi = new Similarity("-1","","");
            sc.setSimi("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml", "-1");
        }
               
                 SubmitProblem sub = new SubmitProblem();
                   sub.init();
               if(sc.isCopied("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml").equals("true")){
                   String message = "此代码涉嫌抄袭，是否提交？";
                   int selection = JOptionPane.showConfirmDialog(Control.getMainFrame(),
                            message,"提示",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                         );
                 
                   if(selection == JOptionPane.NO_OPTION){
                       //String SubmitProblem(String examId,String problemId,String siminum,String simiId,String iscopied,String submit)
                       problemXml = sub.SubmitProblem(examId, proId, simi.getMaxnum(), simi.getMaxId(), "true", "false");
                       flag = false;
                       Control.getMainFrame().setProblemlist(true);
                   }else{
                      problemXml = sub.SubmitProblem(examId, proId, simi.getMaxnum(), simi.getMaxId(), "true", "true"); 
                   }
               }else if(sc.isCopied("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml").equals("false")){
                    problemXml = sub.SubmitProblem(examId, proId, simi.getMaxnum(), simi.getMaxId(), "false", "true"); 
               }else{
                   problemXml = sub.SubmitProblem(examId, proId, "-1","", "false", "true"); 
               }
//                Information info = new Information();
                String username = Control.getUser().getUserName();
                String passwd =Control.getUser().getPassword();
                String toWrite = new String();
                try {
                    toWrite = Control.getWebsService().submitThisProblem(username, passwd, problemXml);
                 } catch (Exception e) {
                    e.printStackTrace();
                 }
                 String backFile = "./xml/"+Control.getPath()+"/afterSubmitProblem.xml";
                 File tmpFile = new File(backFile);
                 if(tmpFile.exists()) tmpFile.delete();
                 TextToFile(backFile,toWrite);
                 SubmitProblem sb = new SubmitProblem();
                 sb.init();
                 String msg = sb.isSubmitted(backFile);
                 if(msg.equals("true")){
                     if(flag){
                            String message = "成功提交本题";
                            JOptionPane.showConfirmDialog(Control.getMainFrame(),
                                message,"提示",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.WARNING_MESSAGE
                            ); 
                            SolutionCode c = new SolutionCode();
                            c.init();
                            c.setFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml", "true");
                            Control.getCode().setNoSubmit();
                            Control.getAns().setNoSubmit();
                            //变灰
                            Control.getMainFrame().setProblemlist(true);
                    }
                    
                 }else if(msg.equals("不能重复提交")){
                      JOptionPane.showConfirmDialog(Control.getMainFrame(),"请注意：\n您已经提交过本题，不可再提交。","注意",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
                 }else{
                    String message = msg;
                    JOptionPane.showConfirmDialog(Control.getMainFrame(),
                                message,"提示",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.WARNING_MESSAGE
                    ); 
                 } 
                 if(tmpFile.exists()) tmpFile.delete();
                
                HashMap<String, StudentExamDetail> hash = new HashMap<String, StudentExamDetail>();
                 List<StudentExamDetail> sedlist = new StudentExamDetail_io(examId).getstudentExamDetaillist();
                for(int i = 0; i < sedlist.size() ; i++){
                    hash.put(sedlist.get(i).getProblemId(), sedlist.get(i));
                }
                if(hash.containsKey(proId)){
                    StudentExamDetail_io student = new StudentExamDetail_io(examId);
                    student.changeStatus(String.valueOf(proId), sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), String.valueOf(sc.isFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml")), sc.getId("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"));
                }
                else{
                    //StudentExamDetail(String id,String examId,String problemId,String submit,String status,String hintCases,String score,String elapsedTime,String finished,String SolutionId)
                    StudentExamDetail sed = new StudentExamDetail("1",examId, proId,"" , sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), "1", "1", "1", "1", "1");
                    new StudentExamDetail_io(examId).add(sed);
                     StudentExamDetail_io student = new StudentExamDetail_io(examId);
                    student.changeStatus(String.valueOf(proId), sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), String.valueOf(sc.isFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml")), sc.getId("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"));
                }
                 
                  Control.getMainFrame().setProblemlist(true);
                 //Control.getMainFrame().collection(row);
                 File tmp = new File("./xml/"+Control.getPath()+"/tmp1.xml");
                 tmp.delete();
//                List<StudentExamDetail> sedlist = new StudentExamDetail_io(examid).getstudentExamDetaillist();
//                HashMap<String, StudentExamDetail> hash = new HashMap<String, StudentExamDetail>();
//                for(int i = 0; i < sedlist.size() ; i++){
//                    hash.put(sedlist.get(i).getProblemId(), sedlist.get(i));
//                }
//                if(hash.containsKey(problemId)){
//                    有这道题
//                }
//                else{
//                    没有这道题
//                }
    }
    private void clearOldSolution(String problemId){
        File file = new File("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+problemId+".xml");
        if(file.exists()){
            file.delete();
        }
    }
}
