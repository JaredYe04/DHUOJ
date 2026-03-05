/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;

/**
 *
 * @author ytxlo
 */
public class StudentExamDetail {
    private String id;
    private String examId;
    private String problemId;
    private String submit;
    private String status;
    private String hintCases;
    private String score;
    private String elapsedTime;
    private String finished;
    private String SolutionId;

    public StudentExamDetail(){
        super();
    }
    public StudentExamDetail(String id,String examId,String problemId,String submit,String status,String hintCases,String score,String elapsedTime,String finished,String SolutionId){
        super();
        this.id = id;
        this.examId = examId;
        this.problemId = problemId;
        this.submit = submit;
        this.status = status;
        this.hintCases = hintCases;
        this.score = score;
        this.elapsedTime = elapsedTime;
        this.finished = finished;
        this.SolutionId = SolutionId;
    }
    public String getId(){
        return id;
    }
    public void setId(String str){
        this.id = str;
    }
    public String getExamId(){
        return examId;
    }
    public void setExamId(String str){
        this.examId = str;
    }
    public String getProblemId(){
        return problemId;
    }
    public void setProblemId(String str){
        this.problemId = str;
    }
    public String getSubmit(){
        return submit;
    }
    public void setSubmit(String str){
        this.submit = str;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String str){
        this.status = str;
    }
    public String getHintCases(){
        return hintCases;
    }
    public void setHintCases(String str){
        this.hintCases = str;
    }
    public String getScore(){
        return score;
    }
    public void setScore(String str){
        this.score = str;
    }
    public String getElapsedTime(){
        return elapsedTime;
    }
    public void setElapsedTime(String str){
        this.elapsedTime = str;
    }
    public String getFinished(){
        return finished;
    }
    public void setFinished(String str){
        this.finished = str;
    }
    public String getSolutionId(){
        return SolutionId;
    }
    public void setSolutionId(String str){
        this.SolutionId = str;
    }
}
