/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import MyCache.Shared;
import java.util.List;
import common.Const;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import jdk.nashorn.internal.parser.TokenType;
import kernel.Judger;
import resultData.RunInfo;
import persistence.oj_beans.ExamDetailBean;
import persistence.oj_beans.ProblemBean;
import persistence.oj_beans.ProblemTestCaseBean;
import persistence.oj_beans.SolutionBean;
import resultData.CompileInfo;
import resultData.Result;

/**
 *
 * @author Administrator
 */
public class Process {

    //函数接口
    private Consumer<String> con;

    //资源
    private SolutionBean solutionBean = null;//2
    private ProblemBean problemBean = null;//3
    private List<ProblemTestCaseBean> testCaseBeans = null;//4
    private ExamDetailBean examDetailBean = null;//6
    private Boolean willCheck = false;

    private int[] result = null;
    private String[] output = null;
    private String[] remarks = null;
    private Judger judger;
    private int sumTestcaseNum = 0;
    private int correctNum = 0;
    private String correctCaseIds = "";

    //仅得到输出
    public String GetOutput(String language, String sourceCode, Float timeOut, String testInput) {
        this.willCheck = false;
        this.judger = new Judger();
        Boolean is = this.judger.checkForCompiler();
        this.solutionBean = new SolutionBean();
        this.problemBean = new ProblemBean();
        this.testCaseBeans = new ArrayList<>();

        this.solutionBean.setLanguage(language);
        this.solutionBean.setSourceCode(sourceCode);
        this.problemBean.setTime_limit(timeOut);
        ProblemTestCaseBean temp = new ProblemTestCaseBean();
        temp.setInput(testInput);
        testCaseBeans.add(temp);

        init();
        judgeForAllTestcase();
        if (result[0] == Const.CE || result[0] == Const.SE) {
            return remarks[0];
        } else if (result[0] == Const.RE || result[0] == Const.TLE) {
            output[0] = Const.STATUS[result[0]];
        } else if (result[0] == Const.QUEUE) {
            output[0] = "运行超时，超时时间为：" + timeOut;
        } else if (result[0] == Const.NF) {
            output[0] = "编译器未找到，请检查编译器设置";
        }      
        return output[0];
    }

    //数据库有关
    public Answer Judge(SolutionBean sbean, List<ProblemTestCaseBean> tCase, ProblemBean pBean) {
        CompileInfo.init();
        RunInfo.init();
        Result.status = 0;
        willCheck = true;
        this.judger = new Judger();
        Boolean is = this.judger.checkForCompiler();
        this.problemBean = pBean;
        this.solutionBean = sbean;
        this.testCaseBeans = tCase;

        init();
        judgeForAllTestcase();
        updateSolution();
        problemBean.setSubmit(problemBean.getSubmit() + 1);

        return new Answer(result, output, remarks, solutionBean.getRemark(), solutionBean.getCorrectCaseIds());
    }

    //WebService评判
    public Answer Judge(String solutionId, String problemId, String language,String compiler, String sourceCode, Float timeOut, List<ProblemTestCaseBean> testCaseBeans, Consumer<String> con) {
        CompileInfo.init();
        RunInfo.init();
        this.con = con;
        Result.status = 0;
        this.willCheck = true;
        this.solutionBean = new SolutionBean();
        this.problemBean = new ProblemBean();
        this.testCaseBeans = new ArrayList<ProblemTestCaseBean>();
        this.judger = new Judger();
        Boolean is = this.judger.checkForCompiler();
        this.solutionBean.setLanguage(language);
        this.solutionBean.setCompiler(compiler);
        this.solutionBean.setSourceCode(sourceCode);
        this.problemBean.setTime_limit(timeOut);
        // if(is == false) return ;
        int size = testCaseBeans.size();
        String[] testCaseId = new String[size];
        String[] testCaseOut = new String[size];
        for (int j = 0; j < size; j++) {
            testCaseId[j] = String.valueOf(testCaseBeans.get(j).getId());
            testCaseOut[j] = testCaseBeans.get(j).getOutput();
        }
        this.sumTestcaseNum = size;
        this.testCaseBeans = testCaseBeans;

        init();
        judgeForAllTestcase();
        updateSolution();

        String[] resultTrans = new String[size];
        if (solutionBean.getStatus().equals("NF")) {
            output[0] = "NF";
        } else if (solutionBean.getStatus().equals("CE")) {
            output[0] = new String().valueOf(remarks[0]);
        }
        for (int j = 0; j < size; j++) {
            resultTrans[j] = Const.STATUS[result[j]];
        }

        if (solutionId == null && problemId == null && con == null) {
            return new Answer(testCaseId, output, resultTrans, solutionBean.getStatus(), solutionBean.getRemark(), solutionBean.getCorrectCaseIds());
        } else {
            return new Answer(solutionId, problemId, language, testCaseId, output, resultTrans, solutionBean.getStatus(), solutionBean.getRemark(), solutionBean.getCorrectCaseIds(), testCaseBeans, sourceCode);
        }
    }

    //得到评判结果
    public Answer Judge(String language,String compiler, String sourceCode, Float timeOut, List<ProblemTestCaseBean> testCaseBeans) {
        CompileInfo.init();
        RunInfo.init();
        Result.status = 0;
        this.willCheck = true;
        this.solutionBean = new SolutionBean();
        this.problemBean = new ProblemBean();
        this.testCaseBeans = new ArrayList<ProblemTestCaseBean>();
        this.judger = new Judger();
        //Boolean is = this.judger.checkForCompiler();
        this.solutionBean.setLanguage(language);
        this.solutionBean.setCompiler(compiler);
        this.solutionBean.setSourceCode(sourceCode);
        this.problemBean.setTime_limit(timeOut);
        // if(is == false) return ;
        int size = testCaseBeans.size();
        String[] testCaseId = new String[size];
        String[] testCaseOut = new String[size];
        for (int j = 0; j < size; j++) {
            testCaseId[j] = String.valueOf(testCaseBeans.get(j).getId());
            testCaseOut[j] = testCaseBeans.get(j).getOutput();
        }
        this.sumTestcaseNum = size;
        this.testCaseBeans = testCaseBeans;

        init();
        judgeForAllTestcase();
        updateSolution();

        String[] resultTrans = new String[size];
        if (solutionBean.getStatus().equals("NF")) {
            output[0] = "NF";
        } else if (solutionBean.getStatus().equals("CE")) {
            output[0] = new String().valueOf(remarks[0]);
        }
        for (int j = 0; j < size; j++) {
            resultTrans[j] = Const.STATUS[result[j]];
        }

        return new Answer(testCaseId, output, resultTrans, solutionBean.getStatus(), solutionBean.getRemark(), solutionBean.getCorrectCaseIds());
    }

    //step 1
    private void init() {

        HashMap<String, Object> map = new HashMap();
        map.put("userId=", solutionBean.getUserId());
        map.put("examId=", solutionBean.getExamId());
        map.put("problemId=", solutionBean.getProblemId());
        //examDetailBean = ExamDetailDAO.findOne(map);

        result = new int[testCaseBeans.size()];
        output = new String[testCaseBeans.size()];
        remarks = new String[testCaseBeans.size()];

        sumTestcaseNum = testCaseBeans.size();
        correctNum = 0;
        correctCaseIds = "";

        Result.status = Const.QUEUE;

        //checkHaveNull();
    }
//    
//    private void init(){
//        
//    }

//    private void checkHaveNull() {
//        if (solutionBean == null) {
//            Log.writeExceptionLog("solutionBean==null  File:Process.java Method:checkHaveNull ");
//            Control.addExceptionInfo("solutionBean==null File:Process.java Method:checkHaveNull ");
//        }
//        if (problemBean == null) {
//            Log.writeExceptionLog("problemBean==null  File:Process.java Method:checkHaveNull ");
//            Control.addExceptionInfo("problemBean==null File:Process.java Method:checkHaveNull ");
//        }
//        if (examDetailBean == null) {
//            Log.writeExceptionLog("examDetailBean==null File:Process.java Method:checkHaveNull ");
//            Control.addExceptionInfo("examDetailBean==null File:Process.java Method:checkHaveNull ");
//            examDetailBean = new ExamDetailBean();
//            examDetailBean.setUserId(solutionBean.getUserId());
//            examDetailBean.setExamId(solutionBean.getExamId());
//            examDetailBean.setProblemId(solutionBean.getProblemId());
//            ExamDetailDAO.add(examDetailBean);
//
//        }
//        if (problemBean.getSubmit() == null) {
//            problemBean.setSubmit(0);
//        }
//        if (problemBean.getTime_limit() == null) {
//            problemBean.setTime_limit(0.12f);
//        }
//        if(){
//            
//        }
    //   }
    //step 2
    private void judgeForAllTestcase() {

        if (judger.compileFound() && 0 == judger.compile(solutionBean.getSourceCode(), solutionBean.getLanguage(),solutionBean.getCompiler())) {
            for (int i = 0; i < sumTestcaseNum; i++) {

                ProblemTestCaseBean caseBean = (ProblemTestCaseBean) testCaseBeans.get(i);
                //todo 如果 output为空则1000000 否则长度加100
                if(caseBean.getOutput().length()==0){
                Shared.maxOutputLength=3000000;
                }else{
                Shared.maxOutputLength=caseBean.getOutput().length()+2000;
                }
                if (judger.run(solutionBean.getLanguage(),solutionBean.getCompiler(), caseBean.getInput(), problemBean.getTime_limit().intValue()) == 0) {
                    if (willCheck) {
                        judger.check(caseBean.getOutput());
                    } else {
                        Result.status = Const.AC;
                    }
                    result[i] = Result.status;
                    output[i] = RunInfo.info;
                    remarks[i] = RunInfo.remark;

                    String message = "testcase id=" + ((ProblemTestCaseBean) testCaseBeans.get(i)).getId() + " done" + " result:" + Const.STATUS[result[i]];
                    if (con != null) {
                        con.accept(message);
                    }
                } else {//运行错误
                    result[i] = Result.status;
                    output[i] = "";
                    remarks[i] = RunInfo.remark;
                    String message = "testcase id=" + ((ProblemTestCaseBean) testCaseBeans.get(i)).getId() + " done" + " result:" + Const.STATUS[result[i]];

                    if (con != null) {
                        con.accept(message);
                    }
                    //如果有多个测试数据，运行到第n个测试数据发生超时，则后面的测试数据不再运行。
//                    if (result[i] == Const.TLE) {
//                        break;
//                    }
                }
            }
        } else if (!judger.compileFound()) {
            for (int i = 0; i < sumTestcaseNum; i++) {
                result[i] = 11;
                output[i] = "";
                remarks[i] = "编译器未找到，请检查编译器设置";
                String message = "编译器未找到，请检查编译器设置";
                if (con != null) {
                    con.accept(message);
                }
            }
        } else {//编译错误
            for (int i = 0; i < sumTestcaseNum; i++) {
                result[i] = Result.status;
                output[i] = "";
                remarks[i] = CompileInfo.remark;
                String message = "testcase id=" + (testCaseBeans.get(0)).getId() + " done" + " result:" + Const.STATUS[result[0]];
                if (con != null) {
                    con.accept(message);
                }
            }
        }

    }

    //step 3  ???
//    private void updateWrongCases() {
//        for (int i = 0; i < sumTestcaseNum; i++) {
//            if (result[i] == Const.AC) {
//                correctNum++;
//                correctCaseIds += ((ProblemTestCaseBean) testCaseBeans.get(i)).getId() + ",";
//            } //这四种情况会产生新的wrongcase的记录
//            else if (result[i] == Const.WA || result[i] == Const.PE || result[i] == Const.RE || result[i] == Const.TLE) {
//                WrongCaseBean wrongCaseBean = null;
//                HashMap<String, Object> map = new HashMap();
//                map.put("solutionId=", solutionBean.getId());
//                map.put("caseId=", (testCaseBeans.get(i)).getId());
//                wrongCaseBean = WrongCaseDAO.findOne(map);
//
//                if (wrongCaseBean == null) {
//                    wrongCaseBean = new WrongCaseBean();
//                    wrongCaseBean.setSolutionId(solutionBean.getId());
//                    wrongCaseBean.setCaseId((testCaseBeans.get(i)).getId());
//                    wrongCaseBean.setOutput("null output");
//                    WrongCaseDAO.add(wrongCaseBean);
//                }
//
//                if (result[i] == Const.WA || result[i] == Const.PE) {
//                    wrongCaseBean.setOutput(output[i]);
//                } else if (result[i] == Const.RE || result[i] == Const.TLE) {
//                    wrongCaseBean.setOutput(remarks[i]);
//                }
//                try {
//                    WrongCaseDAO.update(wrongCaseBean);
//                } catch (Exception e) {
//                    System.out.println(e);
//                   // Log.writeExceptionLog("wrongcase update error  File:Process.java Method:updateWrongCases line:215" + "\n" + e.getMessage() + "\n" + e.getStackTrace());
//                    //Control.addExceptionInfo("wrongcase update error  File:Process.java Method:updateWrongCases line:215" + "\n" + e.getMessage() + "\n" + e.getStackTrace());
//                }
//
//            }
//
//        }
//    }
    //step 4
    private void updateSolution() {

        if (!judger.compileFound()) {
            solutionBean.setStatus("NF");
            solutionBean.setRemark("编译器未找到，请检查编译器设置");
        } else {
            for (int i = 0; i < sumTestcaseNum; i++) {
                if (result[i] == Const.AC) {
                    correctNum++;
                    correctCaseIds += ((ProblemTestCaseBean) testCaseBeans.get(i)).getId() + ",";
                }
            }
            if (sumTestcaseNum == correctNum) {
                solutionBean.setStatus("AC");
            } else {
                int maxLevelStatus = Const.QUEUE;
                int index = 0;
                //寻找错误等级最高的结果
                for (int i = 0; i < sumTestcaseNum; i++) {
                    if (result[i] > maxLevelStatus) {
                        maxLevelStatus = result[i];
                        index = i;
                    }
                }
                solutionBean.setStatus(Const.STATUS[maxLevelStatus]);
                //最高等级错误相应的信息
                
                
                
                String message="";
                for (int i = 0; i < remarks.length; i++) {
                    message += "测试用例 " + testCaseBeans.get(i).getId() + "结果为 " + Const.STATUS[result[i]] + ":";
                    message += remarks[i] + "\n";
                }
                solutionBean.setRemark(message);
                
                //old
//                if (maxLevelStatus == Const.CE) {
//                    solutionBean.setRemark(remarks[index]);
//                } else if (result[index] == Const.WA || result[index] == Const.PE) {
////                solutionBean.setRemark("");
//                    String message = new String();
//                    for (int i = 0; i < remarks.length; i++) {
//                        message += "测试用例 " + testCaseBeans.get(i).getId() + "结果为 " + Const.STATUS[result[i]] + ":";
//                        message += remarks[i] + "\n";
//                    }
//                    solutionBean.setRemark(message);
//                } else {//RE,TLE
//                    String wrongCaseIds = "";
//                    for (int j = 0; j < sumTestcaseNum; j++) {
//                        if (result[j] == result[index]) {
//                            wrongCaseIds += ((ProblemTestCaseBean) testCaseBeans.get(j)).getId() + ",";
//                        }
//                    }
//                    solutionBean.setRemark("测试用例ID为" + wrongCaseIds + remarks[index]);
//                    String message = new String();
//                    for (int i = 0; i < remarks.length; i++) {
//                        message += "\n";
//                        message += remarks[i];
//                    }
//                    solutionBean.setRemark(solutionBean.getRemark() + message);
//                }
            }
            solutionBean.setCorrectCaseIds(correctCaseIds);
            //remark转为GBK字符串
            String originalRemark=solutionBean.getRemark();
            try{
                solutionBean.setRemark(new String(solutionBean.getRemark().getBytes("GBK"),"GBK"));
            }catch(Exception e){
                solutionBean.setRemark(originalRemark);
            }
            //Jared
            
            //SolutionDAO.update(solutionBean);
        }
    }

    //step 5
//    private void updateProblemBean() {
//        problemBean.setSubmit(problemBean.getSubmit() + 1);
//        try {
//            ProblemDAO.update(problemBean);
//        } catch (Exception e) {
//            System.out.println(e);
//            //Log.writeExceptionLog("problem update error  File:Process.java Method:updateProblemBean " + "\n" + e.getMessage() + "\n" + e.getStackTrace());
//            //Control.addExceptionInfo("problem update error  File:Process.java Method:updateProblemBean " + "\n" + e.getMessage() + "\n" + e.getStackTrace());
//        }
//    }
////step 6
//    private void updateStudentExamDetail() {
//        try {
//            String status = solutionBean.getStatus();
//            int userId = solutionBean.getUserId();
//            int problemId = solutionBean.getProblemId();
//            int examId = solutionBean.getExamId();
//            int solutionId = solutionBean.getId();
//            int count = CommonDAO.updateDetailByHql(status, userId, problemId, examId, solutionId);
//            if (count == 0) {
//                throw new Exception();
//            }
//        } catch (Exception e) {
//            String message = " studentExamDetail update error  File:Process.java Method:updateExamDetail\n";
//            message += "solutionId:" + solutionBean.getId();
//            System.out.println(message);
//           
//        }
//    }
    private void clear() {
        //资源
        solutionBean = null;//2
        problemBean = null;//3
        testCaseBeans = null;//4

        examDetailBean = null;//6

        result = null;

        sumTestcaseNum = 0;
        correctNum = 0;
        correctCaseIds = "";

        output = null;
        remarks = null;
    }

}
