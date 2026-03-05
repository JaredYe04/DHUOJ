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
public class Exam {
    
    private String id;

    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    private String starttime;
   
    private String endtime;
    
    private String description;
    
    private String problemNum;
    
    private String canGetHint;
    
    private String partialScore;
    
    private String submitOnlyAC;
    
    private String language;
    
    private String teacherId;

    private String updateTime;
 
    private String status;
    
    private String allowChangeSeat;
    
    private String uuid;
    
    private String bestbefore;
    
    private String scorecoef;
    
    private String studentViewScore;
    
    private String limittime; // by san_san
    
    public Exam(){
        super();
    }
    public Exam(String id,String name,String starttime,String endtime,String description,String problemNum,String canGetHint,String partialScore,String submitOnlyAC,String language,String teacherId,String updateTime,String status, String limittime){
        super();
        this.id = id;
        this.name = name;
        this.starttime = starttime;
        this.endtime = endtime;
        this.description = description;
        this.problemNum = problemNum;
        this.canGetHint = canGetHint;
        this.partialScore = partialScore;
        this.submitOnlyAC = submitOnlyAC;
        this.language = language;
        this.teacherId = teacherId;
        this.updateTime = updateTime;
        this.status = status;
        this.limittime = limittime;
    }
    public String getId(){
        return id;
    }
    public void setId(String str){
        id=str;
    }
    public String getName(){
        return name;
    } 
    public void setName(String str){
        name=str;
    }
    public String getStarttime(){
        return starttime;
    }
    public void setStarttime(String str){
        starttime=str;
    }
    public String getEndtime(){
        return endtime;
    }
    public void setEndtime(String str){
        endtime=str;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String str){
        description=str;
    }
    public String getProblemNum(){
        return problemNum;
    }
    public void setProblemNum(String str){
        problemNum=str;
    } 
    public String getCanGetHint(){
        return canGetHint;
    }
    public void setCanGetHint(String str){
        canGetHint=str;
    } 
    public String getPartialScore(){
        return partialScore;
    }
    public void setPartialScore(String str){
        partialScore=str;
    }
    public String getSubmitOnlyAC(){
        return submitOnlyAC;
    }
    public void setSubmitOnlyAC(String str){
        submitOnlyAC=str;
    }

    public String getLanguage(){
        return language;
    }
    public void setLanguage(String str){
        language=str;
    }
    public String getTeacherId(){
        return teacherId;
    }
    public void setTeacherId(String str){
        teacherId=str;
    }
    public String getUpdateTime(){
        return updateTime;
    }
    public void setUpdateTime(String str){
        updateTime=str;
    }
    public String getStatus(){
        return status;
    }
        public void setStatus(String str){
        status=str;
    }
        
    // by san_san
    public void setLimitTime(String str){
        this.limittime = str;
    }
    public String getLimitTime(){
        return this.limittime;
    }
    public void setStudentViewScore(String str){
        this.studentViewScore = str;
    }
    public String getStudentViewScore(){
        return this.studentViewScore;
    }
    
    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the allowChangeSeat
     */
    public String getAllowChangeSeat() {
        return allowChangeSeat;
    }

    /**
     * @param allowChangeSeat the allowChangeSeat to set
     */
    public void setAllowChangeSeat(String allowChangeSeat) {
        this.allowChangeSeat = allowChangeSeat;
    }
    
    public void setBestBefore(String str) {
        this.bestbefore=str;
    }
    
    public String getBestBefore() {
        return bestbefore;
    }
    
    public void setScoreCoef(String str) {
        this.scorecoef=str;
    }
    
    public String getScoreCoef() {
        return scorecoef;
    }
}
