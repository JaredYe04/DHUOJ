/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view.frame;

import client.io.xml.ExamProblem_io;
import client.io.xml.Problem_io;
import client.io.xml.StudentExamDetail_io;
import client.model.Exam;
import client.model.ExamProblem;
import client.model.Problem;
import client.model.Problemlist;
import client.service.DownSwingWorker;
import client.util.Control;
import client.view.other.TimeLabel;
import client.view.panel.PaperPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author ytxlo
 */
public class ProgressBarFrame extends JFrame {

    private JLabel jl;
    private JLabel jltitle;
    private JProgressBar progressBar; //进度条
    private Exam exam;
    private ExamFrame ef;
    private String status;
    
    private JButton JB_Start;
     private JButton JB_contest;
     private JLabel progressbar;//倒计时
     private JPanel JP_Top;
     private JPanel JP_Panel;
     private JPanel JP_Bottom;
     private JPanel JP_Bottom_Left;
     private JPanel JP_Bottom_Center;
     private JPanel JP_Bottom_Right;
     private JLabel jLabelTitle;
     private JLabel jLabelEmpty;
     private JTextArea JT_Content;
    private Runnable onCompleteCallback;

    // 添加设置回调的方法
    public void setOnCompleteCallback(Runnable callback) {
        this.onCompleteCallback = callback;
    }

    // 在原有的 SwingWorker 的 done() 方法中调用回调
    protected void done() {
        if (onCompleteCallback != null) {
            onCompleteCallback.run();
        }
    }
     
     public ProgressBarFrame(Exam exam,ExamFrame ef,String status,String init)
     {
        this.exam = exam;
        this.ef = ef;
        this.status = status;
        InitComponents();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((screenSize.width - 500) / 2, (screenSize.height - 467) / 2,500,467);
        this.progressbar = new TimeLabel(exam,ef,exam.getStarttime(),exam.getEndtime(),this.progressbar,exam.getId(),this.jl,this.progressBar,this,status,JB_Start);
     }
     
    public ProgressBarFrame(Exam exam,String NowProblem,String status){
        this.status = status;
        init();
        DownSwingWorker sw = new DownSwingWorker(jl, progressBar,exam, NowProblem, this, status);
        sw.execute();
    }

    public ProgressBarFrame(Exam exam, String status, PaperPanel problem, List<Problemlist> problemlist, int row){
        this.exam = exam;
        this.status = status;
        init();
        DownSwingWorker sw = new DownSwingWorker(jl, progressBar,exam, this, status);
        sw.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) &&
                    SwingWorker.StateValue.DONE == evt.getNewValue()) {
                SwingUtilities.invokeLater(() -> {
                    // 更新题目信息
                    Problem_io problem_io = new Problem_io(problemlist.get(row).getProblemId());
                    ExamProblem_io examproblem_io = new ExamProblem_io(problemlist.get(row).getExamId());

                    if (new File(problem_io.getPath()).exists()) {
                        Problem prod = problem_io.getproblem();
                        ExamProblem epro = null;
                        try {
                            epro = examproblem_io.getProblemList().get(row);
                            problem.setPaper(prod, row, epro);
                        } catch (Exception ex) {
                            // Handle exception appropriately
                        }
                    }

                });
            }
        });
        sw.execute();
    }
    public ProgressBarFrame(Exam exam,String status){
        this.exam = exam;
        this.status = status;
        init();
        DownSwingWorker sw = new DownSwingWorker(jl, progressBar, exam, this,status);
        sw.execute();
    }
    public ProgressBarFrame(Exam exam,ExamFrame ef,String status){ 
        this.exam = exam;
        this.ef = ef;
        this.status = status;
        init();
        DownSwingWorker sw = new DownSwingWorker(jl, progressBar, exam, ef, this,status);
        sw.execute();
    } 
    private void init(){
        this.progressBar=new JProgressBar(); 
        this.jl = new JLabel("正在下载");
        this.jltitle = new JLabel("正在下载本场考试内容");
        this.jl.setPreferredSize(new Dimension(400,50));
        this.jltitle.setPreferredSize(new Dimension(400,50));
        this.progressBar.setStringPainted(true); 
        this.progressBar.setPreferredSize(new Dimension(400, 50));
        this.add(this.jltitle,BorderLayout.NORTH);
        this.add(progressBar, BorderLayout.CENTER);//在不指定布局管理器的情况下，默认使用BorderLayout。 若不使用布局管理器，需明确说明setLayout(new BorderLayout())  
        this.add(jl,BorderLayout.SOUTH);
        this.setTitle("下载"); 
        this.pack(); 
        this.setSize(400,200);
        this.setLocationRelativeTo(null);
        this.setVisible(true); 
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);      
    }
    
    private void InitComponents()
    {
          this.jl = new JLabel("正在下载");
          jLabelTitle = new JLabel();
          jLabelEmpty = new JLabel();
          JT_Content = new JTextArea();
          JP_Top = new JPanel();
          JP_Panel = new JPanel();
          JP_Bottom = new JPanel();
          JP_Bottom_Left = new JPanel();
          JP_Bottom_Center = new JPanel();
          JP_Bottom_Right = new JPanel();
          this.progressbar = new JLabel();
          JB_Start = new JButton();
          this.JB_contest = new JButton();
          this.progressBar=new JProgressBar(); 
          
          JP_Panel.setLayout(new java.awt.GridLayout(1, 1));
          JP_Top.setLayout(new java.awt.GridLayout(1, 1));
          jLabelEmpty.setText("");
          jLabelTitle.setText("考试介绍");
          jLabelTitle.setHorizontalAlignment(JLabel.CENTER);
          jLabelTitle.setFont(new Font(null, Font.PLAIN, 25));
          JT_Content.setText( ""+"\n"
                             +"      考试名称："+exam.getName()+"\n"
                             +"      考试内容："+exam.getDescription()+"\n"
                             +"      老师ID："+exam.getTeacherId()+"\n"
                             +"      问题号："+exam.getProblemNum());
          JT_Content.setFont(new Font(null, Font.PLAIN, 18));
          JT_Content.setLineWrap(true);
          JT_Content.setWrapStyleWord(true);
          JP_Bottom.setLayout(new java.awt.GridLayout(1, 3));
          JP_Bottom_Left.setLayout(new java.awt.GridLayout(3, 1));
          JP_Bottom_Center.setLayout(new java.awt.GridLayout(3, 1));
          JP_Bottom_Right.setLayout(new java.awt.GridLayout(3, 1));
          JB_Start.setText("开始考试");
          JB_Start.setEnabled(false);
          JT_Content.setEnabled(false);
          
          JP_Top.add(jLabelTitle);
          JP_Panel.add(JT_Content);
          JP_Bottom_Left.add(jLabelEmpty);
          JP_Bottom_Left.add(jLabelEmpty);
          JP_Bottom_Left.add(this.jl);
          JP_Bottom_Center.add(this.progressbar);
          JP_Bottom_Center.add(JB_Start);
          JP_Bottom_Center.add(progressBar);
          JP_Bottom_Right.add(jLabelEmpty);
          JP_Bottom_Right.add(jLabelEmpty);
          JP_Bottom_Right.add(jLabelEmpty);
          JP_Bottom.add(JP_Bottom_Left, java.awt.BorderLayout.WEST);
          JP_Bottom.add(JP_Bottom_Center, java.awt.BorderLayout.CENTER);
          JP_Bottom.add(JP_Bottom_Right, java.awt.BorderLayout.EAST);
          progressbar.setHorizontalAlignment(JLabel.CENTER);
          progressbar.setForeground(Color.red);
          progressbar.setBackground(Color.red);
          this.progressBar.setStringPainted(true); 
          this.progressBar.setPreferredSize(new Dimension(80, 20));
          progressBar.setVisible(false);
          this.jl.setVisible(false);
          getContentPane().add(JP_Top, java.awt.BorderLayout.NORTH);
          getContentPane().add(JP_Panel, java.awt.BorderLayout.CENTER);
          getContentPane().add(JP_Bottom, java.awt.BorderLayout.SOUTH);
          setResizable(false);
          JB_Start.addKeyListener(new KeyAdapter() {
             public void keyPressed(KeyEvent e){
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                    JB_Start.doClick();
            }
        });
        JB_Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JB_StartActionPerformed();
            }
        });
        //this.progressbar = new TimeLabel(exam.getStarttime(),exam.getEndtime(),this.progressbar,exam.getId(),JB_Start); 
    }
    private void JB_StartActionPerformed()
    {
        File f = new File("./xml/"+Control.getPath()+"/examproblems_"+exam.getId()+".dat");
            if(!f.exists()){
                JOptionPane.showMessageDialog(null, "题目未下载成功！");
            }
            else{
                new MainFrame(exam,null);
                dispose();
            }
    }
}
