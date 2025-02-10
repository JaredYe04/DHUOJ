package client.view.frame;

import client.io.xml.Read_xml;
import client.io.xml.StudentExamDetail_io;
import client.io.xml.Change_io;
import client.io.xml.ExamPermit_io;
import client.io.xml.ExamProblem_io;
import client.model.Exam;
import client.model.ExamPermit;
import client.service.DownFileWrite;
import client.util.Control;
import client.service.DownSwingWorker;
import client.service.GetServerTime;
import client.util.SystemUtil;
import client.view.other.TimeLabel;
import client.view.panel.TimePanel;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExamFrame extends javax.swing.JFrame {

    private ExamFrame main;
    private List<client.model.Exam> examlist;
    //private ArrayList<Exam> examlist;
    private int total_p;
    private String str_ContestName[];
    private String str_ContestMessage[];
    private String str_ContestTime[];

    private JPanel JP_contestlist;
    private JPanel JP_contest;
    private JButton JB_contest;
    private JScrollPane JSP_contest;

    private JPanel JP_temp;
    private JLabel JLabel1;
    private JLabel JLabel2;
    private JLabel JLabel3;
    private JLabel JLabel4;
    private JLabel JLabel5;
    private JLabel JLabel6;
    private JLabel JLabel7;

    private TimePanel TP_time;

    public ExamFrame() {
        initComponents();
        Control.setContestFrame(this);
    }

    private void initComponents() {
        this.main = this;
        total_p = Control.getContestNumber();

        this.str_ContestName = Control.getContestName();
        this.str_ContestMessage = Control.getContestMessage();
        this.str_ContestTime = Control.getContestTime();

        this.JP_contest = new JPanel();

        this.JP_contestlist = new JPanel();

        this.setLayout(new BorderLayout());
        setResizable(false);
        setDefaultCloseOperation(3);
        setTitle("考试列表");
        //getContentPane().add(this.TP_time,"North");

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 1000) / 2, (screenSize.height - 500) / 2, 1000, 500);
        this.setLayout(null);
        this.setContest();
    }

    private void setContest() {
        int num = 15;
        // Run_io.beanToXML();
        //Read_xml.beanToXML();
        Read_xml.XMLTobean();
        this.examlist = Read_xml.getExamlist();

        if (examlist.size() > 14) {
            num = examlist.size();
        }
        GridBagLayout layout = new GridBagLayout();
        //this.JP_contest.setLayout(new java.awt.GridLayout(num+1, 7));
        this.JP_contest.setLayout(layout);

        this.JLabel1 = new JLabel("考试编号");
        this.JLabel2 = new JLabel("考试名称         ");
        this.JLabel3 = new JLabel("考试信息                            ");
        this.JLabel4 = new JLabel("考试开始时间");
        this.JLabel5 = new JLabel("考试结束时间");
        this.JLabel6 = new JLabel("考试状态    ");
        this.JLabel7 = new JLabel("  操作  ");

        this.JP_contest.add(this.JLabel1);
        this.JP_contest.add(this.JLabel2);
        this.JP_contest.add(this.JLabel3);
        this.JP_contest.add(this.JLabel4);
        this.JP_contest.add(this.JLabel5);
        this.JP_contest.add(this.JLabel6);
        this.JP_contest.add(this.JLabel7);
        this.JP_contestlist.add(this.JP_contest);

        GridBagConstraints s = new GridBagConstraints();
        s.fill = GridBagConstraints.BOTH;
        s.gridx = 0;
        s.gridy = 0;
        s.insets = new Insets(10, 1, 10, 10);
        layout.setConstraints(this.JLabel1, s);
        s.gridx = 1;
        s.gridy = 0;
        s.insets = new Insets(10, 1, 10, 20);
        layout.setConstraints(this.JLabel2, s);
        s.gridx = 2;
        s.gridy = 0;
        s.insets = new Insets(10, 1, 10, 20);
        layout.setConstraints(this.JLabel3, s);
        s.gridx = 3;
        s.gridy = 0;
        s.insets = new Insets(10, 1, 10, 20);
        layout.setConstraints(this.JLabel4, s);
        s.gridx = 4;
        s.gridy = 0;
        s.insets = new Insets(10, 1, 10, 20);
        layout.setConstraints(this.JLabel5, s);
        s.gridx = 5;
        s.gridy = 0;
        s.insets = new Insets(10, 1, 10, 10);
        layout.setConstraints(this.JLabel6, s);
        s.gridx = 6;
        s.gridy = 0;
        s.insets = new Insets(10, 1, 10, 0);
        layout.setConstraints(this.JLabel7, s);
        for (int i = 0; i < num; i++) {
            //this.JP_contest.setLayout(new java.awt.GridLayout(1, 4));
            if (i < examlist.size()) {
                // by san_san
                if (examlist.get(i).getType().equals("iTraining")) {
                    continue;
                }
                String description = examlist.get(i).getDescription();
                if (examlist.get(i).getLimitTime() != null && examlist.get(i).getLimitTime().length() > 0){
                    description = description + "本场考试限制时间为：" + examlist.get(i).getLimitTime() + "分钟。";
                }
                
                this.JLabel1 = new JLabel(String.valueOf(i + 1));
                this.JLabel2 = new JLabel(getString(examlist.get(i).getName(), 12));
                this.JLabel3 = new JLabel(getString(description, 25));

                this.JLabel4 = new JLabel(getString(examlist.get(i).getStarttime(), 11));
                this.JLabel5 = new JLabel(getString(examlist.get(i).getEndtime(), 11));
                //this.JLabel6 = new JLabel(examlist.get(i).getLanguage());
                this.JB_contest = new JButton();
                this.JB_contest.setActionCommand(i + "");
                this.JB_contest.setEnabled(false);
                if (LoginFrame.getLogin() == true) {
                    Date now = GetServerTime.getNowTime();
                    Date start = new Date();
                    Date end = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        start = sdf.parse(examlist.get(i).getStarttime());
                        end = sdf.parse(examlist.get(i).getEndtime());
                    } catch (ParseException ex) {
                        Logger.getLogger(GetServerTime.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (compareDate(now, start) == -1) {
                        this.JLabel6 = new TimeLabel(examlist.get(i).getStarttime(), examlist.get(i).getEndtime(), this.JB_contest, examlist.get(i).getId());
                    } else if (compareDate(now, start) == 1 && compareDate(now, end) == -1) {
                        this.JLabel6 = new JLabel("进行中");
                        Change_io.changeExamStatus(examlist.get(i).getId(), "进行中");
                        this.JB_contest.setEnabled(true);
                    } else {
                        this.JLabel6 = new JLabel("已结束");
                        Change_io.changeExamStatus(examlist.get(i).getId(), "已结束");
                        this.JB_contest.setEnabled(true);
                    }

                } else {
                    this.JLabel6 = new JLabel(examlist.get(i).getStatus());
                    if (examlist.get(i).getStatus().equals("进行中") || examlist.get(i).getStatus().equals("已结束")) {
                        this.JB_contest.setEnabled(true);
                    }
                }

                this.JP_contest.add(this.JLabel1);
                this.JP_contest.add(this.JLabel2);
                this.JP_contest.add(this.JLabel3);
                this.JP_contest.add(this.JLabel4);
                this.JP_contest.add(this.JLabel5);
                this.JP_contest.add(this.JLabel6);
                this.JP_temp = new JPanel();
                this.JP_temp.setLayout(new FlowLayout(FlowLayout.CENTER));

                this.JB_contest.setText("参加");
                //final int i=j;
                //this.JB_contest.setActionCommand(examlist.get(i).getId());
                this.JB_contest.addActionListener(onclicklister);
                this.JP_temp.add(this.JB_contest);
                this.JP_contest.add(this.JP_temp);
                s.gridx = 0;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 10);
                layout.setConstraints(this.JLabel1, s);
                s.gridx = 1;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 20);
                layout.setConstraints(this.JLabel2, s);
                s.gridx = 2;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 20);
                layout.setConstraints(this.JLabel3, s);
                s.gridx = 3;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 20);
                layout.setConstraints(this.JLabel4, s);
                s.gridx = 4;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 20);
                layout.setConstraints(this.JLabel5, s);
                s.gridx = 5;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 10);
                layout.setConstraints(this.JLabel6, s);
                s.gridx = 6;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 0);
                layout.setConstraints(this.JP_temp, s);
            } else {
                this.JLabel1 = new JLabel(" ");
                this.JLabel2 = new JLabel(" ");
                this.JLabel3 = new JLabel(" ");
                this.JLabel4 = new JLabel(" ");
                this.JLabel5 = new JLabel(" ");
                this.JLabel6 = new JLabel(" ");
                this.JLabel7 = new JLabel(" ");
                this.JP_contest.add(this.JLabel1);
                this.JP_contest.add(this.JLabel2);
                this.JP_contest.add(this.JLabel3);
                this.JP_contest.add(this.JLabel4);
                this.JP_contest.add(this.JLabel5);
                this.JP_contest.add(this.JLabel6);
                this.JP_contest.add(this.JLabel7);

                s.gridx = 0;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 10);
                layout.setConstraints(this.JLabel1, s);
                s.gridx = 1;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 20);
                layout.setConstraints(this.JLabel2, s);
                s.gridx = 2;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 20);
                layout.setConstraints(this.JLabel3, s);
                s.gridx = 3;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 20);
                layout.setConstraints(this.JLabel4, s);
                s.gridx = 4;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 20);
                layout.setConstraints(this.JLabel5, s);
                s.gridx = 5;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 10);
                layout.setConstraints(this.JLabel6, s);
                s.gridx = 6;
                s.gridy = i + 1;
                s.insets = new Insets(1, 1, 1, 0);
                layout.setConstraints(this.JLabel7, s);
            }
        }
        //this.JP_contest.setBounds(0,0,875,440);
        this.JSP_contest = new JScrollPane(this.JP_contest);
        this.JSP_contest.setBounds(10, 10, 975, 430);
        getContentPane().add(this.JSP_contest, "Center");
    }
    private ActionListener onclicklister = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //获取考试相应按钮
            JButton buttonJoin = (JButton) (e.getSource());
            buttonJoin.setEnabled(false);
            try {
                Exam newExamInfo = examlist.get(Integer.parseInt(e.getActionCommand()));
                HashMap<String, Exam> oldExamList = LoginFrame.oldExam;
                Exam oldExamInfo = null;
                if ("true".equals(newExamInfo.getAllowChangeSeat())) {

                    if (oldExamList.containsKey(newExamInfo.getId())) {
                        oldExamInfo = oldExamList.get(newExamInfo.getId());
                    }
                    if (LoginFrame.getLogin() == true) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (compareDate(GetServerTime.getNowTime(), sdf.parse(newExamInfo.getStarttime())) == -1) {
                            new ProgressBarFrame(newExamInfo, main, DownSwingWorker.PROBLEMLIST, "init").setVisible(true);
                        } else {
                            StudentExamDetail_io sed_io = new StudentExamDetail_io(newExamInfo.getId());
                            Control.setoldExamDetail(sed_io.getstudentExamDetaillist());
                            if (oldExamInfo != null) {
                                if (oldExamInfo.getUpdateTime().equals("")) {
                                    oldExamInfo.setUpdateTime("2000-01-01 00:00:00");
                                }
                            }
                            if (newExamInfo.getUpdateTime().equals("")) {
                                newExamInfo.setUpdateTime("2000-01-01 00:00:00");
                            }
                            if (oldExamInfo == null || oldExamInfo.getUpdateTime().compareTo(newExamInfo.getUpdateTime()) < 0) {
                                if (oldExamInfo == null) {
                                    LoginFrame.oldExam.put(newExamInfo.getId(), newExamInfo);
                                } else {
                                    LoginFrame.oldExam.replace(newExamInfo.getId(), oldExamInfo, newExamInfo);
                                }
                                Control.setPrompt(false);
                                new ProgressBarFrame(newExamInfo, main, DownSwingWorker.PROBLEMLIST);
                            } else {
                                File f = new File("./xml/" + Control.getPath() + "/examproblems_" + newExamInfo.getId() + ".dat");
                                if (!f.exists()) {
                                    int r = JOptionPane.showConfirmDialog(null, "未曾下载过题目，是否下载题目？", "提示", JOptionPane.YES_NO_OPTION);

                                    if (r == JOptionPane.YES_OPTION) {
                                        Control.setPrompt(false);
                                        new ProgressBarFrame(newExamInfo, main, DownSwingWorker.PROBLEMLIST);
                                    } else {
                                        buttonJoin.setEnabled(true);
                                    }
                                } else {
                                    try {
                                        //测试能不能解析题目列表
                                        new ExamProblem_io(newExamInfo.getId()).getProblemList();
                                        Control.setPrompt(false);
                                        new ProgressBarFrame(newExamInfo, main, DownSwingWorker.STATUS);
                                    } catch (Exception ex) {
                                        Control.setPrompt(false);
                                        new ProgressBarFrame(newExamInfo, main, DownSwingWorker.PROBLEMLIST);
                                    }

                                }
                            }
                        }
                    } else {
                        File f = new File("./xml/" + Control.getPath() + "/examproblems_" + newExamInfo.getId() + ".dat");
                        if (!f.exists()) {
                            JOptionPane.showMessageDialog(null, "无本场考试内容，请先联机登录下载题目！");
                            buttonJoin.setEnabled(true);
                        } else {
                            new MainFrame(newExamInfo, null);
                            dispose();
                        }
                    }
                } else if (judgeUuid(Integer.parseInt(newExamInfo.getId()))) {
                    if (oldExamList.containsKey(newExamInfo.getId())) {
                        oldExamInfo = oldExamList.get(newExamInfo.getId());
                    }
                    if (LoginFrame.getLogin() == true) {
                        StudentExamDetail_io sed_io = new StudentExamDetail_io(newExamInfo.getId());
                        Control.setoldExamDetail(sed_io.getstudentExamDetaillist());
                        if (oldExamInfo != null) {
                            if (oldExamInfo.getUpdateTime().equals("")) {
                                oldExamInfo.setUpdateTime("2000-01-01 00:00:00");
                            }
                        }
                        if (newExamInfo.getUpdateTime().equals("")) {
                            newExamInfo.setUpdateTime("2000-01-01 00:00:00");
                        }
                        if (oldExamInfo == null || oldExamInfo.getUpdateTime().compareTo(newExamInfo.getUpdateTime()) < 0) {
                            if (oldExamInfo == null) {
                                LoginFrame.oldExam.put(newExamInfo.getId(), newExamInfo);
                            } else {
                                LoginFrame.oldExam.replace(newExamInfo.getId(), oldExamInfo, newExamInfo);
                            }
                            Control.setPrompt(false);
                            new ProgressBarFrame(newExamInfo, main, DownSwingWorker.PROBLEMLIST);
                        } else {
                            File f = new File("./xml/" + Control.getPath() + "/examproblems_" + newExamInfo.getId() + ".dat");
                            if (!f.exists()) {
                                int r = JOptionPane.showConfirmDialog(null, "未曾下载过题目，是否下载题目？", "提示", JOptionPane.YES_NO_OPTION);
                                if (r == JOptionPane.YES_OPTION) {
                                    Control.setPrompt(false);
                                    new ProgressBarFrame(newExamInfo, main, DownSwingWorker.PROBLEMLIST);
                                } else {
                                    buttonJoin.setEnabled(true);
                                }
                            } else {
                                Control.setPrompt(false);
                                new ProgressBarFrame(newExamInfo, main, DownSwingWorker.STATUS);
                            }
                        }
                    } else {
                        File f = new File("./xml/" + Control.getPath() + "/examproblems_" + newExamInfo.getId() + ".dat");
                        if (!f.exists()) {
                            JOptionPane.showMessageDialog(null, "无本场考试内容，请先联机登录下载题目！");
                            buttonJoin.setEnabled(true);
                        } else {
                            new MainFrame(newExamInfo, null);
                            dispose();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "本场考试不允许换座位，请教师到学生管理界面点击允许换座按钮");
                    buttonJoin.setEnabled(true);
                }
            } catch (Exception ex) {
                log.Log.writeExceptionLog(ex.getMessage());
                buttonJoin.setEnabled(true);
            }

        }

    };

    private String getString(String str, int limit) {
        StringBuffer result = new StringBuffer();
        if (str.isEmpty()) {
            return "<html> </html>";
        }
        result.append("<html>");
        int num = str.length() / limit;
        num += str.length() % limit > 0 ? 1 : 0;

        for (int i = 1; i < num; i++) {
            int start = (i - 1) * limit;
            int end = start + limit;
            result.append(new StringBuffer(str).substring(start, end)).append("<br>");
        }
        result.append(new StringBuffer(str).substring((num - 1) * limit, str.length())).append("</html>");
        ////System.out.println(result.toString());
        return result.toString();
    }

    private int compareDate(Date dt1, Date dt2) {
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        } else {//相等
            return 0;
        }
    }

    private boolean judgeUuid(int examId) {

        if (!SystemUtil.isWindows()) {
            JOptionPane.showConfirmDialog(null, "换座功能必须在Windows系统下使用！");
            return false;
        }
        String uuid = SystemUtil.getCPUSerial() + SystemUtil.getHardDiskSN("C") + SystemUtil.getMotherboardSN();
        String res = Control.getWebsService().isPermit(Control.getUser().getUserName(), Control.getUser().getPassword(), examId, uuid);
        System.out.println(res);
        return res.contains("Success");
    }
}
