/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import cache.ProblemsCachManager;
import common.Const;
import gui.Control;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import main.Answer;
import myjdom.AnswerToXml;
import myjdom.XmlToProblemBean;
import myjdom.XmlToRequest;
import myjdom.model.ProblemBean;
import myjdom.model.Request;
import myjdom.model.ResultBean;
import myjdom.model.Solution;
import myjdom.model.Solutions;
import persistence.oj_beans.ProblemTestCaseBean;
import swingworker.MySwingWorker;
import log.Log;
import resultData.CompileInfo;
import resultData.Result;

/**
 *
 * @author 10102
 */
public class JudgeFromQueue extends Thread {

    private boolean stop = false;
    public Queue<Solution> queue = null;
    private Solutions solutions;
    private List<ProblemTestCaseBean> testCaseBeans = null;
    private Map<String, ProblemBean> problemBeanMap = null;
    private Lock lock;
    private int threadNo;

    public JudgeFromQueue(int threadNo) {
        this.threadNo = threadNo;
    }

    @Override
    public void run() {
        while (Control.threadCountsManager[threadNo] || !Control.queue.isEmpty() && Control.threadCountsManager[0] == false) {
            try {
                if (Control.queue.isEmpty()) {
                    Thread.sleep(1000);
                    continue;
                }
                Solution s = null;
                synchronized (Control.queue) {
                    if (!Control.queue.isEmpty()) {
                        System.out.println("poll队列");
                        s = Control.queue.poll();
                        System.out.println("poll完成");
                        //Swing不是线程安全的，invoke方法立即返回而run方法被异步执行
                        EventQueue.invokeLater(() -> {
                            Control.setGuiQueueSize("" + Control.queue.size());
                        });
                    }
                }
                if (s != null) {
                    String problemIdString = s.getProblemId();
                    System.out.println("获取promblem");
                    getWebServiceProblems(problemIdString);
                     System.out.println("获取promblem完成");
                    System.out.println("开始裁判");
                    Judge(s);
                     System.out.println("裁判完成");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                EventQueue.invokeLater(() -> {
                    Control.addExceptionInfo(threadNo, ex.toString());
                });
            }
        }
        Control.threadState[threadNo] = false;
        EventQueue.invokeLater(() -> {
            Control.setTabbStopTitle(threadNo);
            Control.addJudgeInfo(threadNo, "线程" + threadNo + "stop success\n");
        });
        //判断该停止的线程已经停止
        int i = 1;
        boolean runFlag = false;
        synchronized (Control.threadCountsManager) {
            for (; i <= 4; i++) {
                if (Control.threadCountsManager[i] == true) {
                    runFlag = true;
                }
                if (Control.threadCountsManager[i] != Control.threadState[i]) {
                    break;
                }
            }
            if (i > 4 && runFlag == true) {
                //判断该停止的线程已经停止并且还有线程在运行
                EventQueue.invokeLater(() -> {
                    Control.setStartThreadButtontEnable();
                });
            }
        }

        //判断是否完全停止
        int j = 0;
        synchronized (Control.threadCountsManager) {
            for (; j <= 4; j++) {
                if (Control.threadCountsManager[j] == true || Control.threadState[j] == true) {
                    break;
                }
            }
            if (j > 4) {
                EventQueue.invokeLater(() -> {
                    Control.setStoptxt();
                });
            }
        }
    }

    public void shutdown() {
        this.stop = true;
    }

    //从队列中获取代码并裁判
    public void Judge(Solution s) throws Exception {

        String solutionId = s.getSolutionId();
        String problemId = s.getProblemId();
        String language = s.getLanguage();
        String sourceCode = s.getCode();
        String compiler=s.getCompiler();

        Float timeOut = problemBeanMap.get(problemId).getTimeOut();
        List<ProblemTestCaseBean> testCaseBeanList = problemBeanMap.get(problemId).getTestCaseBeanList();
        
        MySwingWorker myswingworker = new MySwingWorker(threadNo, solutionId, problemId, language,compiler, sourceCode, timeOut, testCaseBeanList, this::changeMessage, this::submitAnswer);
        myswingworker.execute();
        myswingworker.get();
//                      int i =1/0;
    }

    private void getWebServiceProblems(String problemId) throws Exception {
        problemBeanMap = new HashMap<>();
        ProblemBean problemBean = new ProblemBean();
        XmlToProblemBean xtp = new XmlToProblemBean();
        ProblemsCachManager problemsCachManager = ProblemsCachManager
                .getInstance();
        String problem = (String) problemsCachManager
                .getObject("problemId" + problemId);
        if (problem == null) {
            try{
                problem = Control.getWebService().getProblem(Integer.parseInt(problemId));
                problemsCachManager.putObject("problemId" + problemId,
                    problem);
            }catch(Exception e){
                Result.status = Const.SE;
                CompileInfo.remark = "获取题目信息失败！请联系管理人员。错误信息："+e.getMessage();
                e.printStackTrace();
                 EventQueue.invokeLater(() -> {
                    Control.addExceptionInfo(threadNo, e.toString());
                });
            }

        }
       if(problem.isEmpty()){
                     Result.status = Const.CE;
            CompileInfo.remark = "获取题目信息失败！";
            
            return;
        }
//                System.out.println(Integer.parseInt(problemId));

//                System.out.println(xml);
       try{
            xtp.readXmlString(problem);
            problemBean = xtp.convertXML();
            Control.addJudgeInfo(threadNo, "get problemId: " + problemId);
            problemBeanMap.put(problemId, problemBean);
       }catch(Exception e){
                Result.status = Const.SE;
                CompileInfo.remark = "解析题目信息失败！请联系管理人员。错误信息："+"problemId:"+problemId+e.getMessage();
                e.printStackTrace();
                 EventQueue.invokeLater(() -> {
                    Control.addExceptionInfo(threadNo, e.toString());
                });
        }

    }

    //
    public void changeMessage(String message) {
        Control.addJudgeInfo(threadNo, message);
        Log.writeInfo(threadNo + ":" + message);
    }
    //

    public void submitAnswer(Answer answer) {
        System.out.println("提交结果");
        String xml = null;
        try {
            ResultBean rb = new ResultBean();
            rb.setSolutionId(answer.getSolutionId());
            rb.setProblemId(answer.getProblemId());
            rb.setStatus(answer.getStatus());
            rb.setCorrectCaseIds(answer.getCorrectCaseIds());
            rb.setRemark(answer.getRemark());
            rb.setLanguage(answer.getLanguage());
            rb.setSourceCode(answer.getSourceCode());
            List<ProblemTestCaseBean> wrongCase = new ArrayList<>();
            List<String> testCaseList = Arrays.asList(answer.getTestCaseId());
            List<String> correctList = Arrays.asList(answer.getCorrectCaseIds().split(","));
            for (int i = 0; i < testCaseList.size(); i++) {
                if (!correctList.contains(testCaseList.get(i))) {
                    ProblemTestCaseBean testCase = new ProblemTestCaseBean();
                    testCase.setId(Integer.parseInt(testCaseList.get(i)));
                    testCase.setOutput(answer.getUsersOutput()[i]);
                    wrongCase.add(testCase);
                }
            }
            rb.setWrongCase(wrongCase);
            AnswerToXml rtx = new AnswerToXml(rb);
            xml = rtx.convertXML();
            xml=xml.replace("&#", "&amp;#");
            //System.out.println(xml);
            String request = Control.getWebService().updateResult(xml);
            //System.out.println(request);
            XmlToRequest xtr = new XmlToRequest();
            xtr.readXmlString(request);
            Request req = xtr.convertXML();
            System.out.println("提交完成");
            Control.addJudgeInfo(threadNo, "server result:" + req.getRspMsg());
//            Control.addJudgeInfo("ok");
        } catch (Exception e) {
            EventQueue.invokeLater(() -> {
                    Control.addExceptionInfo(threadNo, e.toString());
                });
            e.printStackTrace();
        }

    }
}
