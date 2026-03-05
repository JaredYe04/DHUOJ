/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.service.web;

import static client.util.Control.getLoginframe;
import client.view.frame.LoginFrame;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import edu.dhu.ws.OJWS;
import edu.dhu.ws.OJWS_Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.xml.namespace.QName;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

/**
 *
 * @author ytxlo
 */
@DubboService
public class Webservice implements java.rmi.Remote{
    private OJWS_Service webs;  
    private OJWS servicePort;
    public static boolean ENABLE_DUBBO=true;
    public static boolean existDubbo=false;

    public OJWS dubboPort;
    common.Logger logger;
    public static OJWS initDubboPort(String url) {
        // 应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("consumer");

        // 引用远程服务
        ReferenceConfig<OJWS> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setInterface(OJWS.class);
        reference.setUrl(url); // 设置远程服务的 URL

        // 初始化
        return reference.get();
    }
    private void setDubbo(){
        LoginFrame lf=getLoginframe();
         logger = common.Logger.getInstance();
         String url = "dubbo://localhost:8080/edu.dhu.ws.OJWS";
         JTextField ip=lf.JTF_ip;
         JTextField port=lf.JTF_port;
         if(ip!=null)//测试时获取不到控件
            url=String.format("dubbo://%s:%s/edu.dhu.ws.OJWS",ip.getText(),port.getText());
        try
        {
            //JEditorPane infoPane=getJudgeInfoEditorPane(0);
            if(!existDubbo){
                //infoPane.setText(infoPane.getText()+LocalTime.now().toString()+"正在请求Dubbo服务...\n");
                logger.log("请求dubbo服务", common.LogLevel.INFO);
            }
            dubboPort = initDubboPort(url);
            //infoPane.setText(infoPane.getText()+LocalTime.now().toString()+"测试test请求：..."+dubboPort.test("aa")+"\n");
            
        }
        catch(Exception e){
                //JEditorPane infoPane=getJudgeInfoEditorPane(1);
                //infoPane.setText(infoPane.getText()+"\n"+LocalTime.now().toString()+e.getMessage()+"\n");
            e.printStackTrace();
        }
        if(dubboPort!=null){
             //JEditorPane infoPane=getJudgeInfoEditorPane(0);
             //if(!existDubbo&&infoPane!=null)
             //   infoPane.setText(infoPane.getText()+LocalTime.now().toString()+"\nDubbo服务连接成功！URL:"+url+"\n");
             existDubbo=true;
            servicePort=dubboPort;
        }else{
            existDubbo=false;
            //JEditorPane infoPane=getJudgeInfoEditorPane(1);
            logger.log("请求dubbo服务失败", common.LogLevel.ERROR);
            //infoPane.setText(infoPane.getText()+LocalTime.now().toString()+"\nDubbo服务连接失败！URL:"+url+"\n");
        }
            
        //如果能使用dubbo服务就使用，不能的话就用原来的
        
    }
    public Webservice(){
        try {
            //DisableHostnameVerifier.disable();
        } catch (Exception ex) {
            Logger.getLogger(Webservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!ENABLE_DUBBO){
            try{
                
                    webs = new OJWS_Service();
                    servicePort = webs.getOJWSImplPort();
                }
            catch(Exception e){
               logger.log(e.getMessage(), common.LogLevel.ERROR);
               e.printStackTrace();
            }
        }
        else
            setDubbo();
    }
    public Webservice(URL url,QName qname)throws java.rmi.RemoteException, MalformedURLException{
        url=new URL("https",url.getHost(),url.getPort(),url.getFile());
        try {
            //DisableHostnameVerifier.disable();
        } catch (Exception ex) {
            Logger.getLogger(Webservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(!ENABLE_DUBBO){
            try{
                webs = new OJWS_Service(url,qname);
                servicePort = webs.getOJWSImplPort();

            }
            catch(Exception e){
                //JEditorPane infoPane=getJudgeInfoEditorPane(1);
                //Control.addExceptionInfo(0, LocalTime.now().toString()+e.getMessage()+"\n");
            }
        }
        else
            setDubbo();
    }
    
//    public Webservice(){
//        webs = new OJWS_Service();
//         try{
//            servicePort = webs.getOJWSImplPort();
//
//        }
//        catch(Exception e){}
//        try
//        {
//            String url = "dubbo://192.168.1.11:20880/edu.dhu.ws.OJWS";//todo:需要改成用户自己输入
//            dubboPort = initDubboPort(url);
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        if(dubboPort!=null){
//            
//            servicePort=dubboPort;
//        }
//            
//        //如果能使用dubbo服务就使用，不能的话就用原来的
//    }
//    
//    public Webservice(URL url,QName qname){
//        webs = new OJWS_Service(url,qname);
//        servicePort = webs.getOJWSImplPort();     
//    }
    
    public String login(String username,String password){
        String re = servicePort.wsLogin(username, password);
        return re;
    }
    public String getExamList(String username,String password){
        String re = servicePort.wsGetExamList(username, password);
        return re;
    }
    public String getExamProblems(String username,String password,int examid){
        String re = servicePort.wsGetExamProblems(username, password, examid);
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
    public String getExamById(String username,String password,int examid){
        String re = servicePort.wsGetExamById(username, password, examid);
        return re;
    }
    public String getExamProblemStatus(String username,String password,int examid,int problemid){
        String re = servicePort.wsGetExamProblemStatus(username, password, examid, problemid);
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
    public String submitCode(String username,String password,String codeXml){
        String re = servicePort.wsSubmitCode(username, password, codeXml);
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
