/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.service;

import client.util.CreateProblemHtml;
import client.util.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ytxlo
 */
public class ProblemURL {
    private String code;
    private List<String> url_net;
    private List<String> url_local;
    private String in = ".";
    private String url = null;
    public ProblemURL(String code) throws Exception{
        url = Config.getURLip()+":"+Config.getURLport();
        this.code = code;
        url_net = new ArrayList<String>();
        url_local = new ArrayList<String>();
        runToGetUrl();
        runToGetUrl_l();
        
    }
    private void runToGetUrl() throws Exception{
        url_net = new ArrayList<String>();
        url_local = new ArrayList<String>();
        
        String strContent = code;
        ////System.out.println(code);
        String regex = "/oj/upload/(image|file)/[^[A-Za-z0-9\\._\\?%&+\\-=/#]]*";   
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strContent);   
        StringBuffer result = new StringBuffer();   
        while (matcher.find()) {   
            String urlStr=matcher.group();  
            System.out.println(urlStr);
            StringBuffer replace = new StringBuffer(urlStr);
            StringBuffer net = new StringBuffer();
            net.append("http://").append(url).append(urlStr);
            ////System.out.println(replace.toString());
            replace.delete(0, 10);   
            ////System.out.println(replace.toString());
            replace.insert(0,in);
            url_net.add(net.toString());
            url_local.add(replace.toString());
            //replace.delete(0, 6);
            System.out.println(net.toString());
            System.out.println(replace.toString());
            matcher.appendReplacement(result, replace.toString());   
        }   
        matcher.appendTail(result);   
        DownloadFile df = new DownloadFile(url_net, url_local);
        this.code = result.toString();
        
       // System.out.println(result); 
    }
    private void runToGetUrl_l() throws Exception{
        url_net = new ArrayList<String>();
        url_local = new ArrayList<String>();
        String strContent = code;
        
        String regex = "/oj/ueditor/[^[A-Za-z0-9\\._\\?%&+\\-=/#]]*";   
        Pattern pattern = Pattern.compile(regex);   
        Matcher matcher = pattern.matcher(strContent);   
        StringBuffer result = new StringBuffer();   
        while (matcher.find()) {   
            String urlStr=matcher.group();  
            StringBuffer replace = new StringBuffer(urlStr);
            StringBuffer net = new StringBuffer();
            net.append("http://").append(url).append(urlStr);
            replace.delete(0, 11);   
            replace.insert(0,in);
            url_net.add(net.toString());
            url_local.add(replace.toString());
            //replace.delete(0, 6);
            //System.out.println(net.toString());
            //System.out.println(replace.toString());
            matcher.appendReplacement(result, replace.toString());   
        }   
        matcher.appendTail(result);   
        DownloadFile df = new DownloadFile(url_net, url_local);
        this.code = result.toString();
//        CreateProblemHtml cph = new CreateProblemHtml(code);
//ÌâÄ¿µÄxml        
//        System.out.println(result); 
    }
    public String getCode(){
        return this.code;
    }
}
