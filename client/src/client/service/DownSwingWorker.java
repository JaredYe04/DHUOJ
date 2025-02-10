/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.service;

import client.util.Control;
import client.util.Decrypt;
import client.model.Exam;
import client.model.ExamProblem;
import client.model.Problem;
import client.model.Problemlist;
import client.model.StudentExamDetail;
import client.model.User;
import client.view.frame.ExamFrame;
import client.view.frame.LoginFrame;
import client.view.frame.MainFrame;
import client.view.frame.ProgressBarFrame;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.apache.cxf.endpoint.Client;
import client.io.xml.Compute_xml;
import client.io.xml.ExamProblem_io;
import client.io.xml.StudentExamDetail_io;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author ytxlo
 */
public class DownSwingWorker extends SwingWorker<List<Integer>, String> {

    private int numall;
    private JLabel jLabel;
    private JProgressBar jpb;
    private Exam exam;
    private ExamFrame ef;
    private ProgressBarFrame pbf;
    private String status;
    private String examid;
    private String problemid;
    public static String ALL = "all";
    public static String STATUS = "status";
    public static String PROBLEM = "problem";
    public static String NOWPROBLEM = "nowproblem";
    public static String PROBLEMLIST = "problemlist";
    public static String EXAM = "exam";
    public List<Problem> probleml;
    private CompareStatus com;
    private String flag = "";

    public DownSwingWorker(JLabel jlabel, JProgressBar jpb,Exam exam, String NowProblem, ProgressBarFrame pbf, String status) {
        this.jLabel = jlabel;
        this.jpb = jpb;
        this.pbf = pbf;
        this.exam = exam;
        this.examid = exam.getId();
        this.status = status;
        this.problemid = NowProblem;
        this.ef = null;
    }

    public DownSwingWorker(JLabel jlabel, JProgressBar jpb, Exam exam, ProgressBarFrame pbf, String status) {
        this.jLabel = jlabel;
        this.jpb = jpb;
        this.exam = exam;
        this.pbf = pbf;
        this.status = status;
        this.examid = exam.getId();
        this.ef = null;
        //flag = "add";
    }

    public DownSwingWorker(JLabel jlabel, JProgressBar jpb, Exam exam, ExamFrame ef, ProgressBarFrame pbf, String status) {
        this.jLabel = jlabel;
        this.jpb = jpb;
        this.exam = exam;
        this.ef = ef;
        this.pbf = pbf;
        this.status = status;
        this.examid = exam.getId();
    }

    @Override
    protected List<Integer> doInBackground() throws Exception {
        List<Integer> val = new ArrayList<Integer>();
        if (status.equals(ALL)) {
            this.jpb.setMaximum(Integer.parseInt(exam.getProblemNum()) + 2);
            this.numall = Integer.parseInt(exam.getProblemNum()) + 2;
            val = this.downloadProblem();
            val.addAll(this.downloadStatus());
        } else if (status.equals(STATUS)) {
            this.jpb.setMaximum(2);
            this.numall = 2;
            val = this.downloadStatus();
        } else if (status.equals(PROBLEM)) {
            this.jpb.setMaximum(Integer.parseInt(exam.getProblemNum()) + 1);
            this.numall = Integer.parseInt(exam.getProblemNum()) + 1;
            val = this.downloadProblem();
        } else if (status.equals(NOWPROBLEM)) {
            this.jpb.setMaximum(1);
            this.numall = 1;
            val = this.downloadNowProblem();
        } else if (status.equals(PROBLEMLIST)) {
            this.jpb.setMaximum(3);
            this.numall = 3;
            val = this.downloadProblemList();
            val.addAll(this.downloadStatus());
        } else if (status.equals(EXAM)) {
            this.jpb.setMaximum(1);
            this.numall = 1;
            val = this.downloadExam();
        }
        return val;
    }

    @Override
    protected void process(List<String> chunks) {
        jLabel.setText(chunks.get(chunks.size() - 1));
        int x = Integer.parseInt(chunks.get(chunks.size() - 1).substring(0, chunks.get(chunks.size() - 1).indexOf("/")).trim());
        jpb.setValue(x);
    }

    @Override
    protected void done() {
        pbf.dispose();
        try {
            get();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(ef, "考试未开始，请重新登陆以更新考试信息！");
            return;
        }
        if (this.ef != null) {
            System.out.println(Calendar.getInstance().getTime());
            ef.dispose();
            new MainFrame(exam, com);
            System.out.println(Calendar.getInstance().getTime());
            
        } else if (status.equals(NOWPROBLEM)) {
            Control.getMainFrame().chooseProblem();
        } else {
            Control.getMainFrame().setProblemlist(true);
        }
        
    }

    private int progressValue = 1;

    //更新题目列表 并下载所有题目
    private List<Integer> downloadProblem() {
        
        System.out.println("downloadProblem");
        
        List<Integer> val = new ArrayList<Integer>();
        User user = Control.getUser();
        String username = user.getUserName();
        String passwd = user.getPassword();
        String id = user.getId();
        String key = username + id;
        if (key.length() < 10) {
            key = key.concat(new String("0000000000").substring(0, 10 - key.length()));
        }
        HashMap<String, ExamProblem> oldepl = new HashMap<String, ExamProblem>();
        try {
            List<ExamProblem> epll = new ExamProblem_io(examid).getProblemList();
            for (int i = 0; i < epll.size(); i++) {
                oldepl.put(epll.get(i).getProblemId(), epll.get(i));
            }
            try {
                String str = Control.getWebsService().getExamProblems(username, passwd, Integer.parseInt(examid));
//                System.out.println(str);
                val.add(Integer.valueOf(1));
                publish((progressValue++) + "/" + numall + ":题目列表已下载");
                new DownFileWrite().write("./xml/" + Control.getPath() + "/examproblems_" + examid + ".dat", str, Control.getKey());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }

        try {
            List<ExamProblem> epl = new ExamProblem_io(examid).getProblemList();
            for (int i = 0; i < epl.size(); i++) {
                ExamProblem newExamProblemInfo = epl.get(i);
                ExamProblem oldExamProblemInfo = null;
                int problemId = Integer.valueOf(newExamProblemInfo.getProblemId()).intValue();
                File f = new File("./xml/"+Control.getPath()+"/problem_"+Control.getExamId()+"-"+problemId+".dat");
                if (oldepl.containsKey(newExamProblemInfo.getProblemId())) {
                    oldExamProblemInfo = oldepl.get(newExamProblemInfo.getProblemId());
                }
                if (oldExamProblemInfo != null) {
                    if (oldExamProblemInfo.getUpdateTime().equals("")) {
                        oldExamProblemInfo.setUpdateTime("2000-01-01 00:00:00");
                    }
                }
                if (newExamProblemInfo.getUpdateTime().equals("")) {
                    newExamProblemInfo.setUpdateTime("2000-01-01 00:00:00");
                }
                if (oldExamProblemInfo == null || oldExamProblemInfo.getUpdateTime().compareTo(newExamProblemInfo.getUpdateTime()) < 0 || !f.exists()) {
                    byte[] by = Control.getWebsService().getProblem(username, passwd, Integer.parseInt(examid), problemId);                  
//                    Object[] obj =client.invoke("WS_GetProblem",username,passwd,problemId);
                    String pro = Decrypt.decrypt(key, by);  
//                    String prob = pro.replaceFirst("GBK", "UTF-8");
                    val.add(Integer.valueOf(progressValue));
                    publish((progressValue++) + "/" + numall + ":第" + (i + 1) + "题已下载");
                    ProblemURL rUrl = new ProblemURL(pro);
                    String prob = rUrl.getCode();
                    new DownFileWrite().write("./xml/"+Control.getPath()+"/problem_"+Control.getExamId()+"-"+problemId+".dat", prob, Control.getKey());
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return val;
    }

    private List<Integer> downloadStatus() {
        System.out.println("downloadStatus");
        List<Integer> val = new ArrayList<Integer>();
        User user = Control.getUser();;
        String username = user.getUserName();
        String passwd = user.getPassword();
        Client client = LoginFrame.client;
        try {
            String str = Control.getWebsService().getExamDetil(username, passwd, Integer.parseInt(examid));

//            Object[] obj =client.invoke("WS_GetExamDetail",username,passwd,Integer.parseInt(examid));
            val.add(Integer.valueOf(1));
            publish((progressValue++) + "/" + numall + ":题目状态已下载");
            new DownFileWrite().write("./xml/" + Control.getPath() + "/studentExamDetail_" + examid + ".dat", str, Control.getKey());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<Problemlist> problemlist = Compute_xml.getProblemlist(examid);
        List<StudentExamDetail> newStatus = new StudentExamDetail_io(examid).getstudentExamDetaillist();
        com = new CompareStatus();
        com.compare(Control.getoldExamDetail(), newStatus, problemlist, examid);
        val.add(Integer.valueOf(1));
        publish((progressValue++) + "/" + numall + ":学生代码已下载");
        return val;
    }

    private List<Integer> downloadNowProblem() {
        System.out.println("downloadNowProblem");
        List<Integer> val = new ArrayList<Integer>();
        User user = Control.getUser();
        String username = user.getUserName();
        String passwd = user.getPassword();
        String id = user.getId();
        String key = username + id;
        if (key.length() < 10) {
            key = key.concat(new String("0000000000").substring(0, 10 - key.length()));
        }
        Client client = LoginFrame.client;
        int problemId = Integer.valueOf(this.problemid);
        try {
            byte[] by = Control.getWebsService().getProblem(username, passwd, Integer.parseInt(examid), problemId); 
            
//            Object[] obj =client.invoke("WS_GetProblem",username,passwd,problemId);
            String pro = Decrypt.decrypt(key, by);
            System.out.println(pro);
            val.add(Integer.valueOf(progressValue));
            publish((progressValue++) + "/" + numall + " 题已重新下载");
            ProblemURL rUrl = new ProblemURL(pro);
            String prob = rUrl.getCode();
            new DownFileWrite().write("./xml/"+Control.getPath()+"/problem_"+Control.getExamId()+"-"+problemId+".dat", prob, Control.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return val;
    }

    //下载题目列表
    private List<Integer> downloadProblemList() throws Exception{
//        System.out.println("downloadProblemList");
        List<Integer> val = new ArrayList<Integer>();
        User user = Control.getUser();
        String username = user.getUserName();
        String passwd = user.getPassword();
        String id = user.getId();
        String key = username + id;
        if (key.length() < 10) {
            key = key.concat(new String("0000000000").substring(0, 10 - key.length()));
        }
//        try {
            String str = Control.getWebsService().getExamProblems(username, passwd, Integer.parseInt(examid));
//            System.out.println(str);
            if(str.contains("考试未开始！")){
                throw new Exception("考试未开始！");
            }
            val.add(Integer.valueOf(1));
            publish((progressValue++) + "/" + numall + ":题目列表已重新下载");
            new DownFileWrite().write("./xml/" + Control.getPath() + "/examproblems_" + examid + ".dat", str, Control.getKey());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return val;
    }

    //下载考试信息
    private List<Integer> downloadExam() {
//        System.out.println("downloadExam!");
        List<Integer> val = new ArrayList<Integer>();
        User user = Control.getUser();
        String username = user.getUserName();
        String passwd = user.getPassword();
        String id = user.getId();
        String key = username + id;
        if (key.length() < 10) {
            key = key.concat(new String("0000000000").substring(0, 10 - key.length()));
        }
        try {
            String str = Control.getWebsService().getExamList(username, passwd);

//            System.out.println(str);

            val.add(1);
            publish((progressValue++) + "/" + numall + ":题目列表已重新下载");
            new DownFileWrite().write("./xml/" + Control.getPath() + "/examlist.dat", str, Control.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;
    }
    
    
}
