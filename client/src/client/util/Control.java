/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.util;

import client.model.Exam;
import client.model.StudentExamDetail;
import client.model.User;
import client.view.frame.ExamFrame;
import client.view.frame.MainFrame;
import client.view.frame.LoginFrame;
import client.view.panel.AnswerTablePanel;
import client.view.panel.CodePanel;
import client.service.web.Webservice;
import client.view.panel.TimePanel;
import java.net.URL;

import javax.swing.JPanel;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author ytxlo
 */
public class Control {
    private static User user;
    private static String key;
    private static String publicKey;
    private static String path;
    private static Webservice ws;

    private static List<StudentExamDetail> oldsedlist;
    private static boolean prompt;
    private static JProgressBar jpb;
    private static JLabel jpb_message;
    private static List<String> languages;
    private static String examId;
    private static Exam exam;
    private static String ip;
    

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Control.ip = ip;
    }
    
    public static TimePanel getTP_time() {
        return TP_time;
    }

    public static void setTP_time(TimePanel TP_time) {
        Control.TP_time = TP_time;
    }

    private static TimePanel TP_time;


    public static Exam getExam() {
        return exam;
    }

    public static void setExam(Exam exam) {
        Control.exam = exam;
    }
    
    public static String getExamId() {
        return examId;
    }

    public static void setExamId(String examId) {
        Control.examId = examId;
    }
    
    
    public static void setWebService(Webservice webs){
       ws = webs;
    }
    public static Webservice getWebsService(){
        return ws;
    }
    public static boolean getPrompt(){
        return prompt;
    }
    public static void setPrompt(boolean bool){
        prompt = bool;
    }
    public static void setoldExamDetail(List<StudentExamDetail> sedlist){
        oldsedlist = sedlist;
    }
    public static List<StudentExamDetail> getoldExamDetail(){
        return oldsedlist;
    }
    public static void init(MainFrame f){
        frame=f;
        submittimes=0;
        allAC=0;
        language = "Cpp";
        allcodecnt = 0;
        model = "Trainer";
    }
    public static MainFrame getMainFrame() {
        return frame;
    }
    public static ExamFrame getContestFrame() {
            return examframe;
    }
    public static int getContestNumber(){
            return contestnumber;
    }
    public static int getPaperNumber(){
            return papernumber;
    }
    public static int getNowpapernum(){
            return nowPaperNum;
    }
    public static String[] getContestName(){
            return contestname;
    }
    public static String[] getContestMessage(){
            return contestmessage;
    }
    public static String[] getContestTime(){
            return contesttime;
    }
    public static String[] getPaperName(){
            return papername;
    }
    public static JPanel getNowPaper(){
            return nowPaper;
    }

    public static void setNowPaper(JPanel p){
            nowPaper = p;
    }
    public static void setContestName(String[] str){
            contestname = str;
    }
    public static void setContestMessge(String[] str){
            contestmessage = str;
    }
    public static void setContestTime(String[] str){
            contesttime= str;
    }
    public static void setPaperName(String[] str){
            papername = str;
    }
    public static void setNowContestNum(int i) {
            nowContestNum = i;
    }
    public static void setMainFrame(MainFrame f) {
            frame = f;
    }
    public static void setLoginFrame(LoginFrame l) {
            loginframe = l;
    }
    public static void setContestFrame(ExamFrame c) {
            examframe = c;
    }
    public static void setModel(String str) {
        model = str;
    }
    public static void setNowpapernum(int x) {
            nowPaperNum = x;
    }
    public static void setPaperNumber(int x) {
            papernumber = x;
    }

    public static int getAllcodecnt() {
            allcodecnt += 1;
            return allcodecnt - 1;
    }

    public static AnswerTablePanel getAns() {
        return ans;
    }

    public static void setAns(AnswerTablePanel ans) {
        Control.ans = ans;
    }

    public static CodePanel getCode() {
        return codep;
    }

    public static void setCode(CodePanel code) {
        Control.codep = code;
    }
    
    
    private static JPanel nowPaper = null;
    private static MainFrame frame = null;
    private static LoginFrame loginframe = null;
    private static ExamFrame examframe = null;
    private static AnswerTablePanel ans = null;
    private static CodePanel codep = null;

    private static int papernumber = 10;
    private static int nowPaperNum = 0;
    private static int contestnumber = 20;
    private static int nowContestNum = 0;
    private static int[] accepted;
    private static int allAC;
    private static int submittimes;
    private static String model;
    private static String language;
    private static String[] contestname={"1","2","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    private static String[] contestmessage={"123","345","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    private static String[] contesttime={"123","456","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
    private static String[] papername={"1","2","3","4","5","6","7","8","9","10"};


    private static int allcodecnt;
    private static String paperNo;
    private static String compileOut;
    private static String pname;
    private static String id;
    private static String psw;
    private static String sip;
    private static String name;
    private static String code;
    private static String result;
    private static String testOut;
    private static boolean islogined;

    private static String paperpath;
    private static int[] paperlist;

    /**
     * @return the loginframe
     */
    public static LoginFrame getLoginframe() {
        return loginframe;
    }

    /**
     * @return the jpb
     */
    public static JProgressBar getJpb() {
        return jpb;
    }

    /**
     * @param aJpb the jpb to set
     */
    public static void setJpb(JProgressBar aJpb) {
        jpb = aJpb;
    }

    /**
     * @return the jpb_message
     */
    public static JLabel getJpb_message() {
        return jpb_message;
    }

    /**
     * @param aJpb_message the jpb_message to set
     */
    public static void setJpb_message(JLabel aJpb_message) {
        jpb_message = aJpb_message;
    }

    /**
     * @return the languages
     */
    public static List<String> getLanguages() {
        return languages;
    }

    /**
     * @param aLanguages the languages to set
     */
    public static void setLanguages(List<String> aLanguages) {
        languages = aLanguages;
    }

    /**
     * @return the key
     */
    public static String getKey() {
        return key;
    }

    /**
     * @param aKey the key to set
     */
    public static void setKey(String aKey) {
        key = aKey;
    }

    /**
     * @return the path
     */
    public static String getPath() {
        return path;
    }

    /**
     * @param aPath the path to set
     */
    public static void setPath(String aPath) {
        path = aPath;
    }

    /**
     * @return the publicKey
     */
    public static String getPublicKey() {
        return publicKey;
    }

    /**
     * @param aPublicKey the publicKey to set
     */
    public static void setPublicKey(String aPublicKey) {
        publicKey = aPublicKey;
    }

    /**
     * @return the user
     */
    public static User getUser() {
        return user;
    }

    /**
     * @param aUser the user to set
     */
    public static void setUser(User aUser) {
        user = aUser;
    }
    
    /**
     * @return http or https
     */
    public static String determineProtocol() {
        String ip = Control.getIp();

        // ÏÈ³¢ÊÔ HTTPS
        if (isHttpsSupported(ip)) {
            return "https";
        }

        // »ØÍËµ½ HTTP
        return "http";
    }

    private static boolean isHttpsSupported(String ip) {
        try {
            URL url = new URL("https://" + ip);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 400;
        } catch (Exception e) {
            return false;
        }
    }

}
