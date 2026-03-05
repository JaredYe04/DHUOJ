package client.view.panel;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import client.model.Problem;
import client.model.ExamProblem;
import client.util.CreateProblemHtml;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.swing.*;

// JavaFX
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class PaperPanel extends JPanel{

    private final JFXPanel jfxPanel;
    private WebView webView;
//    final JWebBrowser webBrowser;

	public PaperPanel(){
//            this.webBrowser = new JWebBrowser();
//            initComponents();

        Platform.setImplicitExit(false);
        setLayout(new BorderLayout());

        // 初始化JavaFX面板
        jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);

        // 在JavaFX线程中初始化WebView
        Platform.runLater(() -> {
            webView = new WebView();
            Scene scene = new Scene(webView);
            jfxPanel.setScene(scene);
        });
	}
        public void initComponents(){
//            this.setLayout(new BorderLayout());
//
//            webBrowser.setBarsVisible(false);
//            webBrowser.setButtonBarVisible(false);
//            webBrowser.setStatusBarVisible(false);
//            webBrowser.setPreferredSize(null);
//
//            setMinimumSize(new Dimension(200, 200));
//
//            this.add(webBrowser,BorderLayout.CENTER);
	}
	public void setPaper(Problem pro,int row ,ExamProblem epro) {
		try {
                    pro.setBestBefore(epro.getBestBefore());
                    // by san_san
                    pro.setDeadlineForClass(epro.getDeadlineForClass());
                    pro.setBestBeforeForClass(epro.getBestBeforeForClass());
                    pro.setScoreCoefForClass(epro.getScoreCoefForClass());
                    //
                    String result = this.prpblemView(pro,row);

            System.out.println("getDeadlineForClass" + epro.getDeadlineForClass());
            System.out.println("getBestBeforeForClass" + epro.getBestBeforeForClass());
            System.out.println("getScoreCoefForClass" + epro.getScoreCoefForClass());

                    // 添加GBK编码声明的HTML头
                    result = "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<meta charset=\"GBK\">\n" +
                            "<title>Problem View</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            result +
                            "</body>\n" +
                            "</html>";

                    // 使用GBK编码写入文件
                    try (Writer writer = new OutputStreamWriter(
                            new FileOutputStream("./problem.html"), "GBK")) {
                        writer.write(result);
                    }

                    File file = new File("./problem.html");
                    String path = file.getAbsolutePath();

                    // 在JavaFX线程中加载URL
                    Platform.runLater(() -> {
                        webView.getEngine().load("file://" + path);
                    });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

        private String prpblemView(Problem pro,int row){
            
//            String p1 = "<center><h1 style=\"font-family:verdana;color:blue\">"+String.valueOf(row+1)+"."+pro.getTitle()+"</h1></center>";
////            String p2 = "<center><p>晚于"+pro.getBestBeforeForClass()+"提交成绩乘以系数"+pro.getScoreCoefForClass() + "</p></center>";
////            if (pro.getBestBeforeForClass().equals("")||pro.getScoreCoefForClass().equals("")){
//               String p2="";
////            }
//            String p3 = "<center><p>"+"&nbsp&nbsp时间限制："+pro.getTime_limit()+"&nbsp&nbsp章节:"+pro.getChapterName()+"</p></center>";
//            String p4 = "<center><p>晚于 "+pro.getBestBeforeForClass()+" 提交成绩乘以系数 "+pro.getScoreCoefForClass() + "</p></center>";
//            if (pro.getBestBeforeForClass().equals("")||pro.getScoreCoefForClass().equals("")){
//                  p4="";
//            }
//            String p5 = "<center><p>截止时间："+pro.getDeadlineForClass()+"</p></center>";
//            if (pro.getDeadlineForClass().equals("")) {
//                p5 = "";
//            }
//            String str1 = "<h2 style=\"font-family:verdana;color:blue\">问题描述</h2>"+pro.getDescription();
//            String str2 = "<h2 style=\"font-family:verdana;color:blue\">输入说明</h2>"+pro.getInputRequirement();
//            String str3 = "<h2 style=\"font-family:verdana;color:blue\">输出说明</h2>"+pro.getOutputRequirement();
//            String temp = new String(pro.getSample_input());
//            int rowN = countN(temp);
//            String str4 = "<h2 style=\"font-family:verdana;color:blue\">输入范例</h2>"+"<textarea wrap=\"off\" style=\"font-family:'宋体';width:100%\" rows=\""+String.valueOf(rowN+1)+"\">"+pro.getSample_input()+"</textarea>";
//            temp = new String(pro.getSample_output());
//            rowN = countN(temp);
//            String str5 = "<h2 style=\"font-family:verdana;color:blue\">输出范例</h2>"+"<textarea wrap=\"off\" style=\"font-family:'宋体';width:100%\" rows=\""+String.valueOf(rowN+1)+"\">"+pro.getSample_output()+"</textarea>";
//
//            String result= p1 + p2 + p3 + p4 + p5 + str1 + str2 + str3 + str4 + str5;

            StringBuilder sb = new StringBuilder();

            sb.append(String.format("<center><h1 style=\"font-family:SimHei,Microsoft YaHei;color:blue\">%d.%s</h1></center>",
                    row + 1, pro.getTitle()));

            sb.append("<center><p>&nbsp;&nbsp;时间限制：").append(pro.getTime_limit())
                    .append("&nbsp;&nbsp;章节:").append(pro.getChapterName()).append("</p></center>");

            if (!pro.getBestBeforeForClass().isEmpty() && !pro.getScoreCoefForClass().isEmpty()) {
                sb.append("<center><p>晚于 ").append(pro.getBestBeforeForClass())
                        .append(" 提交成绩乘以系数 ").append(pro.getScoreCoefForClass()).append("% </p></center>");
            }

            if (!pro.getDeadlineForClass().isEmpty()) {
                sb.append("<center><p>截止时间：").append(pro.getDeadlineForClass()).append("</p></center>");
            }

            sb.append("<h2 style=\"font-family:SimHei,Microsoft YaHei;color:blue\">问题描述</h2>")
                    .append(pro.getDescription());

            sb.append("<h2 style=\"font-family:SimHei,Microsoft YaHei;color:blue\">输入说明</h2>")
                    .append(pro.getInputRequirement());

            sb.append("<h2 style=\"font-family:SimHei,Microsoft YaHei;color:blue\">输出说明</h2>")
                    .append(pro.getOutputRequirement());

            int inputRows = countN(pro.getSample_input());
            sb.append("<h2 style=\"font-family:SimHei,Microsoft YaHei;color:blue\">输入范例</h2>")
                    .append("<textarea wrap=\"off\" style=\"font-family:SimSun;width:100%\" rows=\"")
                    .append(inputRows + 1).append("\">").append(pro.getSample_input()).append("</textarea>");

            int outputRows = countN(pro.getSample_output());
            sb.append("<h2 style=\"font-family:SimHei,Microsoft YaHei;color:blue\">输出范例</h2>")
                    .append("<textarea wrap=\"off\" style=\"font-family:SimSun;width:100%\" rows=\"")
                    .append(outputRows + 1).append("\">").append(pro.getSample_output()).append("</textarea>");

            return sb.toString();

//            return result;
        }
        private int countN(String str){
            int count = 1;
            for(int i = 0;i<str.length();i++){
                if (str.charAt(i)=='\n'){
                    count++;
                }
            }
            if (count>20){
                count = 20;
                return count;
            }
            else{
                return count;
            }
        }
}
