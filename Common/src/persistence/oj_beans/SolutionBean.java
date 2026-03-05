/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence.oj_beans;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class SolutionBean {
    Integer id;
    Integer examId;
    Integer problemId;
    Integer userId;
    Date submitTime;
    String language;
    //NEW
    String compiler;
    //
    String SourceCode;
    Integer codelength;
    String status;
    String correctCaseIds;
    Float similarity;
    Integer similarId;
    Float score;
    String remark;


    public SolutionBean() {
        this.id = 1;
        this.examId = 1;
        this.problemId = 1;
        this.userId = 1;
        this.submitTime = new Date();
        this.language = "";
        this.compiler="";
        this.SourceCode = "";
        this.codelength = 3;
        this.status = "";
        this.correctCaseIds = "";
        this.similarity = 1.0f;
        this.similarId = 2;
        this.score = 1f;
        this.remark = "";
    }
        
    public void setCompiler(String compiler){
        this.compiler=compiler;
    }
    public String getCompiler(){
        return compiler;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSourceCode() {
        return SourceCode;
    }

    public void setSourceCode(String SourceCode) {
        this.SourceCode = SourceCode;
    }

    public Integer getCodelength() {
        return codelength;
    }

    public void setCodelength(Integer codelength) {
        this.codelength = codelength;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCorrectCaseIds() {
        return correctCaseIds;
    }

    public void setCorrectCaseIds(String correctCaseIds) {
        this.correctCaseIds = correctCaseIds;
    }

    public Float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }

    public Integer getSimilarId() {
        return similarId;
    }

    public void setSimilarId(Integer similarId) {
        this.similarId = similarId;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    
}
