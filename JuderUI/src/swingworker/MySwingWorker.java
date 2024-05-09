/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swingworker;

import common.Const;
import gui.Control;
import java.awt.EventQueue;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.SwingWorker;
import main.Answer;
import main.Process;
import persistence.oj_beans.ProblemTestCaseBean;
import resultData.CompileInfo;
import resultData.Result;

/**
 *
 * @author ytxlo
 */
public class MySwingWorker extends SwingWorker<Answer,String>{
    private Consumer<String> con1;
    private Consumer<Answer> con2;
    private String solutionId;
    private String problemId;
    private String language;
    private String compiler;
    
    
    private String sourceCode;
    private Float timeOut;
    private List<ProblemTestCaseBean> testCaseList;
    public MySwingWorker(int threadNo,String solutionId,String problemId,String language,String compiler,String sourceCode,Float timeOut,List<ProblemTestCaseBean> testCaseList,Consumer<String> changeMessage,Consumer<Answer> submitResult){
        this.solutionId = solutionId;
        this.problemId = problemId;
        this.language = language;
        this.compiler=compiler;
        this.sourceCode = sourceCode;
        this.timeOut = timeOut;
        this.testCaseList = testCaseList;
        this.con1 = changeMessage;
        this.con2 = submitResult;
        
    }
    
    @Override
    protected Answer doInBackground() throws Exception {

            return  new Process().Judge(getSolutionId(),getProblemId(),getLanguage(),getCompiler(), getSourceCode(), timeOut, getTestCaseList(),this::writeToGui);

    }
    
    public void writeToGui(String s){
        //System.out.println(s);
        publish(s);
    }

    @Override
    protected void process(List<String> chunks) {
        super.process(chunks); //To change body of generated methods, choose Tools | Templates.
        for(int i=0;i<chunks.size();i++){
            getCon1().accept(chunks.get(i));
        }
    }

    @Override
    protected void done() {
        try {
            super.done(); //To change body of generated methods, choose Tools | Templates.
            getCon2().accept(get());
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void setCompier(String compiler){
        this.compiler=compiler;
    }
    public String getCompiler(){
        return compiler;
    }
    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }
 
    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    } 
    /**
     * @return the sourceCode
     */
    public String getSourceCode() {
        return sourceCode;
    }

    /**
     * @param sourceCode the sourceCode to set
     */
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    /**
     * @return the timeOut
     */
    public Float getTimeOut() {
        return timeOut;
    }

    /**
     * @param timeOut the timeOut to set
     */
    public void setTimeOut(Float timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * @return the testCaseList
     */
    public List<ProblemTestCaseBean> getTestCaseList() {
        return testCaseList;
    }

    /**
     * @param testCaseList the testCaseList to set
     */
    public void setTestCaseList(List<ProblemTestCaseBean> testCaseList) {
        this.testCaseList = testCaseList;
    }

    /**
     * @return the con
     */

    /**
     * @return the con1
     */
    public Consumer<String> getCon1() {
        return con1;
    }

    /**
     * @param con1 the con1 to set
     */
    public void setCon1(Consumer<String> con1) {
        this.con1 = con1;
    }

    /**
     * @return the con2
     */
    public Consumer<Answer> getCon2() {
        return con2;
    }

    /**
     * @param con2 the con2 to set
     */
    public void setCon2(Consumer<Answer> con2) {
        this.con2 = con2;
    }

    /**
     * @return the solutionId
     */
    public String getSolutionId() {
        return solutionId;
    }

    /**
     * @param solutionId the solutionId to set
     */
    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    /**
     * @return the problemId
     */
    public String getProblemId() {
        return problemId;
    }

    /**
     * @param problemId the problemId to set
     */
    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }
    
}
