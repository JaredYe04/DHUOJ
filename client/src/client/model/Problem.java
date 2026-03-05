/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model;

/**
 *
 * @author ytxlo
 * fix by sea ºÏ²¢ProblemºÍProblemId
 */
public class Problem {

    private String id;
    private String title;
    private String description;
    private String memory_limit;
    private String time_limit;
    private String inputRequirement;
    private String outputRequirement;
    private String sample_input;
    private String sample_output;
    private String author;
    private String difficulty;
    private String scoreGrade;
    private String chapterName;
    private String checkSimilarity;
    private String similarityThreshold;
    private String deadlineForClass; // by san_san
    private String bestBefore;
    private String updateTime;
    
    // by san_san
    private String bestBeforeForClass;
    private String scoreCoefForClass;
    

    public Problem() {
    }

    public Problem(String id, String title, String description, String memory_limit, String time_limit, String inputRequirement, String outputRequirement, String sample_input, String sample_output, String author, String difficulty, String scoreGrade, String chapterName, String checkSimilarity, String similarityThreshold, String deadline, String bestBefore, String scoreCoef, String updateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.memory_limit = memory_limit;
        this.time_limit = time_limit;
        this.inputRequirement = inputRequirement;
        this.outputRequirement = outputRequirement;
        this.sample_input = sample_input;
        this.sample_output = sample_output;
        this.author = author;
        this.difficulty = difficulty;
        this.scoreGrade = scoreGrade;
        this.chapterName = chapterName;
        this.checkSimilarity = checkSimilarity;
        this.similarityThreshold = similarityThreshold;
        this.deadlineForClass = deadline;
        this.bestBefore = bestBefore;
        this.scoreCoefForClass = scoreCoef;
        this.updateTime = updateTime;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String getMemory_limit() {
        return this.memory_limit;
    }

    public void setMemory_limit(String str) {
        this.memory_limit = str;
    }

    public String getTime_limit() {
        return this.time_limit;
    }

    public void setTime_limit(String str) {
        this.time_limit = str;
    }

    public String getInputRequirement() {
        return this.inputRequirement;
    }

    public void setInputRequirement(String str) {
        this.inputRequirement = str;
    }

    public String getOutputRequirement() {
        return outputRequirement;
    }

    public void setOutputRequirement(String str) {
        outputRequirement = str;
    }

    public String getSample_input() {
        return sample_input;
    }

    public void setSample_input(String str) {
        sample_input = str;
    }

    public String getSample_output() {
        return sample_output;
    }

    public void setSample_output(String str) {
        sample_output = str;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String str) {
        author = str;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String str) {
        difficulty = str;
    }

    public String getScoreGrade() {
        return scoreGrade;
    }

    public void setScoreGrade(String str) {
        scoreGrade = str;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String str) {
        chapterName = str;
    }

    public String getCheckSimilarity() {
        return checkSimilarity;
    }

    public void setCheckSimilarity(String str) {
        checkSimilarity = str;
    }

    public String getSimilarityThreshold() {
        return similarityThreshold;
    }

    public void setSimilarityThreshold(String str) {
        similarityThreshold = str;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String str) {
        updateTime = str;
    }
    
    public String getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(String bestBefore) {
        this.bestBefore = bestBefore;
    }

    public String getDeadlineForClass() {
        return deadlineForClass;
    }

    public void setDeadlineForClass(String deadlineForClass) {
        this.deadlineForClass = deadlineForClass;
    }

    public String getBestBeforeForClass() {
        return bestBeforeForClass;
    }

    public void setBestBeforeForClass(String bestBeforeForClass) {
        this.bestBeforeForClass = bestBeforeForClass;
    }    
    
    public String getScoreCoefForClass() {
        return scoreCoefForClass;
    }

    public void setScoreCoefForClass(String scoreCoefForClass) {
        this.scoreCoefForClass = scoreCoefForClass;
    }    
}
