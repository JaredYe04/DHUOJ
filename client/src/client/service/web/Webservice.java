/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.service.web;

import static client.util.Control.getLoginframe;

import edu.dhu.ws.OJWS;
import client.view.frame.LoginFrame;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import javax.xml.namespace.QName;

import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 * @author ytxlo
 */
@DubboService
public class Webservice implements java.rmi.Remote{
    private OJWS servicePort;
    public static boolean ENABLE_DUBBO=true;
    public static boolean existDubbo=false;

    public OJWS dubboPort;
    common.Logger logger;

    public static OJWS initDubboPort(String url) {
        System.setProperty("dubbo.application.qos-enable", "false");

        // 应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("consumer");

        // 引用远程服务
        ReferenceConfig<OJWS> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setInterface(OJWS.class);
        reference.setUrl(url); // 设置远程服务的 URL
//        reference.setVersion("3.2.0");
        reference.setTimeout(600000000);
        // 初始化
        return reference.get();
    }
    private void setDubbo(){
         LoginFrame lf=getLoginframe();
         logger = common.Logger.getInstance();
         String url = "dubbo://106.15.36.190:8080/edu.dhu.ws.OJWS";
         JTextField ip=lf.JTF_ip;
         JTextField port=lf.JTF_port;
         if(ip!=null){
             url=String.format("dubbo://%s:%s/edu.dhu.ws.OJWS",ip.getText(),port.getText());
         }

        try {
            if(!existDubbo){
                logger.log("connect to dubbo,url:"+url, common.LogLevel.INFO);
                dubboPort = initDubboPort(url);
            }
        } catch(Exception e) {
            logger.log("faild to get dubbo"+e.getMessage(), common.LogLevel.ERROR);
            e.printStackTrace();
        }

        if(dubboPort!=null){
            //JEditorPane infoPane=getJudgeInfoEditorPane(0);
            //if(!existDubbo&&infoPane!=null)
            //   infoPane.setText(infoPane.getText()+LocalTime.now().toString()+"\nDubbo服务连接成功！URL:"+url+"\n");
            existDubbo=true;
            servicePort=dubboPort;
            logger.log("请求dubbo服务成功", common.LogLevel.INFO);
        }else{
            existDubbo=false;
            //JEditorPane infoPane=getJudgeInfoEditorPane(1);
            logger.log("请求dubbo服务失败", common.LogLevel.ERROR);
            //infoPane.setText(infoPane.getText()+LocalTime.now().toString()+"\nDubbo服务连接失败！URL:"+url+"\n");
        }
    }
    public Webservice(){
        try {
        } catch (Exception ex) {
            Logger.getLogger(Webservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        setDubbo();
    }
    public Webservice(URL url,QName qname)throws java.rmi.RemoteException, MalformedURLException{
        url=new URL("https",url.getHost(),url.getPort(),url.getFile());
        try {
            //DisableHostnameVerifier.disable();
        } catch (Exception ex) {
            Logger.getLogger(Webservice.class.getName()).log(Level.SEVERE, null, ex);
        }

        setDubbo();
    }
    
    public String login(String username,String password){
        String re = servicePort.wsLogin(username, password);
        return re;
    }
    public String getExamList(String username,String password){
        String re = servicePort.wsGetExamList(username, password);
        return re;
    }
    public String getExamProblems(String username,String password,int examid){
        String re = servicePort.wsGetExamProblems(username, password, examid, 1, 100);
        return re;
    }
    public byte[] getProblem(String username,String password,int examId,int problemId){
        byte[] re = servicePort.wsGetProblem(username, password,examId, problemId);
        return re;
    }
    
    public String getExamDetil(String username,String password,int examid){
        String re = servicePort.wsGetExamDetail(username, password, examid);
        return re;
    }
    
    public String getExamRank(String username,String password,int examid){
        String re = servicePort.wsGetExamRank(username, password, examid);
        return re;
    }
    
    public String getExamById(String username,String password,int examid){
        String re = servicePort.wsGetExamById(username, password, examid);
        return re;
    }
    
    public String getExamProblemStatus(String username,String password,int examid,int problemid){
        String re = servicePort.wsGetExamProblemStatus(username, password, examid, problemid);
        return re;
    }
    
    // by san_san
    public Long getExamDeadline(String username, String password, int examid){
        Long re = servicePort.wsGetExamDeadline(username, password, examid);
        return re;
    }

    public String submitThisProblem(String username,String password,String problemXml){
        String re = servicePort.wsSubmitThisProblem(username, password, problemXml);
        return re;
    }
    public String viewWrongCase(String username,String password,int examid,int problemid,int wrongnum,boolean bool){
        String re = servicePort.wsViewWrongCase(username, password, examid, problemid, wrongnum, bool);
        return re;
    }
    public String submitCode(String username,String password,String codeXml, String examId){
        String re = servicePort.wsSubmitCode(username, password, codeXml, examId);
        return re;
    }
    public String submittedCode(String username,String password,int problemid){
        String re = servicePort.wsSubmittedCode(username, password, problemid);
        return re;
    }
    public String isPermit(String username,String password,int examId,String uuid){
        String re = servicePort.wsIsPermit(username, password, examId, uuid);
        return re;
    }
    
    public static void main(String[] args) {
        Webservice ws=new Webservice();
        //System.out.println(ws.dubboPort.test("a"));
   }
}
