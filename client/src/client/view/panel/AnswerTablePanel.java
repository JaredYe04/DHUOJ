/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view.panel;

import client.io.xml.Exam_io;
import client.view.frame.WrongDetailDialog;
import client.model.Information;
import client.model.ProblemTestCase;
import client.util.Control;
import client.util.Tips;
import client.view.frame.LoginFrame;
import client.view.frame.MainFrame;
import client.model.StudentExamDetail;
import main.Answer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import persistence.oj_beans.ProblemTestCaseBean;
import client.util.similarity.Similarity;
import client.util.similarity.check;
import client.io.xml.ProblemTestCase_io;
import client.io.xml.RefreshSolution;
import client.io.xml.SolutionCode;
import client.io.xml.StudentExamDetail_io;
import client.io.xml.SubmitProblem;
import client.io.xml.WrongCase;

/**
 *
 * @author 毛泉
 */
public class AnswerTablePanel extends JPanel {

    JLabel runInfo;
    JLabel subTime;
    JLabel score;
    JLabel rightID;
    JLabel wrongID;
    JLabel detailInfo;
    JLabel runInfoOut;
    JLabel subTimeOut;
    JLabel scoreOut;
    ArrayList<JLabel> rightIDOut;
    ArrayList<JButton> wrongIDOut;
    ArrayList<JLabel> wrongIDLabel;
    ArrayList<JPanel> panellist;
    JLabel detailInfoOut;
    JPanel info;
    JPanel subTimepanel;
    JPanel scorepanel;
    JPanel correct;
    JPanel wrong;
    JPanel detail;
    JPanel wrongTestCase;
    Answer answer;
    ActionListener wrongDetail;
    ActionListener submitPro;
    ActionListener Refresh;
    String[] testCaseIn;
    String[] testCaseOut;
    Map<String, Integer> wrongCase;
    JButton submit;
    JScrollPane JSP_YourOut;
    JTextArea JEP_YourOut;
    String proId;
    String examId;
    Similarity simi;
    Boolean submitOnlyAC;
    Boolean checkOrNot;
    Float similarity;
    int row;
    JButton refresh;

    public AnswerTablePanel() {
        this.runInfo = new JLabel();
        this.subTime = new JLabel();
        this.score = new JLabel();
        this.rightID = new JLabel();
        this.wrongID = new JLabel();
        this.detailInfo = new JLabel();
        this.runInfoOut = new JLabel();
        this.subTimeOut = new JLabel();
        this.scoreOut = new JLabel();
        this.rightIDOut = new ArrayList<JLabel>();
        this.wrongIDLabel = new ArrayList<JLabel>();
        this.wrongIDOut = new ArrayList<JButton>();
        this.panellist = new ArrayList<JPanel>();
        this.detailInfoOut = new JLabel();
        this.wrongTestCase = new JPanel();
        this.info = new JPanel();
        this.subTimepanel = new JPanel();
        this.scorepanel = new JPanel();
        this.correct = new JPanel();
        this.wrong = new JPanel();
        this.detail = new JPanel();
        this.submit = new JButton("提交计分");
        this.refresh = new JButton("获取最新状态");

        this.submit.setPreferredSize(new Dimension(70, 30));
        wrongDetail = new Wrongdetail();
        this.submitPro = new SubmitPro();
        this.Refresh = new Refresh();
        runInfo.setText("运行状态：");
        subTime.setText("提交次数：");
        score.setText("分数：");
        rightID.setText("正确用例：");
        wrongID.setText("错误用例：");
        detailInfo.setText("详细信息：");
        this.JSP_YourOut = new JScrollPane();
        this.JEP_YourOut = new JTextArea(6, 20);
        JSP_YourOut.setViewportView(JEP_YourOut);
        this.JSP_YourOut.setPreferredSize(new Dimension(400, 300));
        this.setLayout(new BorderLayout());

        int col = 11;
//        info.setLayout(new GridLayout(11, 5));
        info.setLayout(new GridLayout(11, col));
        info.setPreferredSize(new Dimension(400, 400));
        info.add(runInfo);
        info.add(runInfoOut);
        addBlank(2);
        info.add(this.refresh);
//        addBlank(2);
        addBlank(col - 6);
        info.add(subTime);
        info.add(subTimeOut);
//        addBlank(3);
        addBlank(col - 3);
        info.add(score);
        info.add(scoreOut);
//        addBlank(3);
        addBlank(col - 3);
        info.add(rightID);

        int nums = 3 * (col - 1) - 1;
        Icon icon = new ImageIcon("./image/questionmark.png");
        for (int j = 0; j < nums; j++) {
            rightIDOut.add(new JLabel());
            wrongIDOut.add(new JButton(icon));
            panellist.add(new JPanel());
            wrongIDLabel.add(new JLabel());
        }

        int blanks = nums - rightIDOut.size();
        for (int j = 0; j < rightIDOut.size(); j++) {
            info.add(rightIDOut.get(j));
        }
        addBlank(blanks);
        info.add(wrongID);
        blanks = nums - wrongIDOut.size();
        for (int j = 0; j < wrongIDOut.size(); j++) {
            wrongIDLabel.get(j).setFont(new Font("DialogInput", 0, 22));

            panellist.get(j).add(wrongIDLabel.get(j));
            panellist.get(j).add(wrongIDOut.get(j));

            info.add(panellist.get(j));
            wrongIDOut.get(j).addActionListener(wrongDetail);
            wrongIDOut.get(j).setBorderPainted(false);
            wrongIDOut.get(j).setContentAreaFilled(false);
            wrongIDOut.get(j).setToolTipText("测试数据提示");
            wrongIDOut.get(j).setPreferredSize(new Dimension(20, 20));
            wrongIDOut.get(j).setVisible(false);
        }

        addBlank(blanks);
        info.add(this.submit);

        addBlank(col - 2);
        addBlank(col - 1);
//        info.add(detail);
//        info.add(JSP_YourOut);

        add(info, BorderLayout.NORTH);
        add(JSP_YourOut, BorderLayout.CENTER);
        if (LoginFrame.getLogin() == false) {
            this.submit.setEnabled(false);
            this.refresh.setEnabled(false);
        }
        this.submit.addActionListener(submitPro);
        this.refresh.addActionListener(Refresh);

        //add(submit);
    }

    public void data(String s, String c, String Id, String exam, Float simi) {
        if (s.equals("true")) {
            this.submitOnlyAC = true;
        } else {
            this.submitOnlyAC = false;
        }
        if (c.equals("true")) {
            this.checkOrNot = true;
        } else {
            this.checkOrNot = false;
        }
        this.proId = Id;
        this.examId = exam;
        this.similarity = simi;
    }

    public void addBlank(int num) {
        for (int j = 0; j < num; j++) {
            info.add(new JLabel());
        }
    }

//    public JPanel wrongPanel(){
//        JPanel wrong = new JPanel();
//        JLabel wrongId = new JLabel();
//        JButton wrongInfo = new Circlebutton("i");
//        wrongInfo.addActionListener(wrongDetail);
//        wrong.add(wrongId);
//        wrong.add(wrongInfo);
//        return wrong;
//    }
    public void renew() {
        this.subTimeOut.setText("");
        this.runInfoOut.setText("");
        this.JEP_YourOut.setText("");
        this.scoreOut.setText("");
        int rightnum = 0, wrongnum = 0;
        for (int j = 0; j < rightIDOut.size(); j++) {
            rightIDOut.get(j).setVisible(false);
        }
        for (int j = 0; j < panellist.size(); j++) {
            panellist.get(j).setVisible(false);
            //wrongIDOut.get(j).setVisible(false);
        }
        this.refresh.setEnabled(true);
        if (LoginFrame.getLogin() == false) {
            this.submit.setEnabled(false);
            this.refresh.setEnabled(false);
        }
    }

    public void renew(Answer answers, String[] testCaseIn, String[] testCaseOut, String score, String proID, String submittime) {
        this.testCaseIn = testCaseIn;
        this.testCaseOut = testCaseOut;
        wrongCase = new HashMap<String, Integer>();
        this.proId = proID;
        this.answer = answers;
        this.simi = simi;
        this.subTimeOut.setText(submittime);

        int rightnum = 0, wrongnum = 0;
        for (int j = 0; j < rightIDOut.size(); j++) {
            rightIDOut.get(j).setVisible(false);
        }
        for (int j = 0; j < panellist.size(); j++) {
            panellist.get(j).setVisible(false);
        }

        for (int j = 0; j < testCaseIn.length; j++) {
            if (answer.getStatusOfTestCase()[j].equals("AC")) {
                if (rightnum > 13) {
                    continue;
                }
                rightIDOut.get(rightnum).setText(answers.getTestCaseId()[j]);
                rightIDOut.get(rightnum).setVisible(true);
                rightnum++;
                if (rightnum == 9 || rightnum == 4) {
                    rightnum++;
                }
            } else if (answer.getStatusOfTestCase()[j].equals("CE")) {

            } else {
                if (wrongnum > 13) {
                    continue;
                }
                wrongIDLabel.get(wrongnum).setText(answer.getTestCaseId()[j]);
                wrongIDOut.get(wrongnum).setActionCommand(answer.getTestCaseId()[j]);
                wrongIDOut.get(wrongnum).setVisible(true);
                panellist.get(wrongnum).setVisible(true);
                if (LoginFrame.getLogin() == false) {
                    wrongIDOut.get(wrongnum).setEnabled(false);
                }
                wrongCase.put(String.valueOf(answers.getTestCaseId()[j]), j);
                wrongnum++;
                if (wrongnum == 9) {
                    wrongnum++;
                }
            }
        }
        this.runInfoOut.setText(answer.getStatus());
        String o = new Tips().getTips(answer.getStatus());
        if (answer.getStatus().equals("CE")) {
            this.JEP_YourOut.setText(o + "\n具体信息如下:\n" + answer.getUsersOutput()[0]);
        } else {
            o += "\n" + answer.getRemark();
            this.JEP_YourOut.setText(o);
        }
        this.scoreOut.setText(score);
        this.refresh.setEnabled(true);
        if (LoginFrame.getLogin() == false) {
            this.submit.setEnabled(false);
            this.refresh.setEnabled(false);
        }
        File tmp = new File("./xml/" + Control.getPath() + "/tmp1.xml");
        tmp.delete();
        //this.updateUI();
    }

    public void renew(Answer answers, int submittimes, String[] testCaseIn, String[] testCaseOut, String score, String proID, String exam, Boolean SubmitOnlyAC, Boolean checkornot, int row) {
        SolutionCode sc = new SolutionCode();
        sc.init();
        this.submitOnlyAC = SubmitOnlyAC;
        this.checkOrNot = checkornot;
        this.testCaseIn = testCaseIn;
        this.testCaseOut = testCaseOut;
        this.row = row;
        wrongCase = new HashMap<String, Integer>();
        this.proId = proID;
        this.answer = answers;

        this.examId = exam;
        int rightnum = 0, wrongnum = 0;
        for (int j = 0; j < rightIDOut.size(); j++) {
            rightIDOut.get(j).setVisible(false);
        }
        for (int j = 0; j < panellist.size(); j++) {
            panellist.get(j).setVisible(false);
        }

        for (int j = 0; j < testCaseIn.length; j++) {

            if (answer.getStatusOfTestCase()[j].equals("AC")) {
                if (rightnum > 13) {
                    continue;
                }
                rightIDOut.get(rightnum).setText(answers.getTestCaseId()[j]);
                rightIDOut.get(rightnum).setVisible(true);
                rightnum++;
                if (rightnum == 9) {
                    rightnum++;
                }
            } else if (answer.getStatusOfTestCase()[j].equals("CE")) {

            } else {
//               JLabel tmp = (JLabel)wrongIDOut.get(wrongnum).getComponent(0);
//               tmp.setText(answers.getTestCaseId()[j]);
                if (wrongnum > 13) {
                    continue;
                }
                wrongIDLabel.get(wrongnum).setText(answer.getTestCaseId()[j]);
                wrongIDOut.get(wrongnum).setActionCommand(answer.getTestCaseId()[j]);
                wrongIDOut.get(wrongnum).setVisible(true);
                panellist.get(wrongnum).setVisible(true);
                if (LoginFrame.getLogin() == false) {
                    wrongIDOut.get(wrongnum).setEnabled(false);
                }
                wrongCase.put(String.valueOf(answers.getTestCaseId()[j]), j);
                wrongnum++;
                if (wrongnum == 9) {
                    wrongnum++;
                }
            }
        }

        this.subTimeOut.setText(String.valueOf(submittimes));
        this.runInfoOut.setText(answer.getStatus());
        String o = new Tips().getTips(answer.getStatus());
        if (answer.getStatus().equals("CE")) {
            this.JEP_YourOut.setText(o + "\n具体信息如下:\n" + answer.getUsersOutput()[0]);
        } else {
            o += "\n" + answer.getRemark();
            this.JEP_YourOut.setText(o);
        }
        this.scoreOut.setText(score);
        this.refresh.setEnabled(true);
        if (LoginFrame.getLogin() == false) {
            this.submit.setEnabled(false);
            this.refresh.setEnabled(false);
        }
        Control.getMainFrame().setProblemlist(true);
        File tmp = new File("./xml/" + Control.getPath() + "/tmp1.xml");
        tmp.delete();
        //this.updateUI();
    }

    private class Refresh implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            int chooseRow = Control.getMainFrame().chooseRow();
            String username = Control.getUser().getUserName();
            String passwd = Control.getUser().getPassword();
            String toWrite = new String();
            try {
                toWrite = Control.getWebsService().getExamProblemStatus(username, passwd, Integer.parseInt(examId), Integer.parseInt(proId));
                System.out.println("getExamProblemStatus:\n" + toWrite);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String backFile = "./xml/" + Control.getPath() + "/afterRefresh.xml";
            File tmpFile = new File(backFile);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            TextToFile(backFile, toWrite);
            RefreshSolution rf = new RefreshSolution();
            rf.init();
            SolutionCode sc = new SolutionCode();
            sc.init();
            String msg = rf.hasSubmitted(backFile);
            if (msg.equals("true")) {
                if (rf.isNew(backFile, proId) || !rf.getStatus(backFile).equals(sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"))) {
                    if (sc.isSaved("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml")) {
                        String message = "本操作将会覆盖您现在保存的代码，是否继续？";
                        int selection = JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                                message, "提示",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (selection == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }
                    fresh(backFile);

                    JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                            "最新状态更新完毕", "提示",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

                } else {
                    JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                            "本地已为最新状态，无更新", "提示",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            } else {

                JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                        "无法更新状态，可能原因有：\n1.服务器中无提交记录\n 2.请检查您的网络连接", "提示",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
            }
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            Control.getCode().setFreshCode(proId);
            // Control.getMainFrame().collection(row);
            Control.getMainFrame().collection(chooseRow);
        }
    }

    public void fresh(String routing) {
        List<ProblemTestCase> testCaseBeansTmp = new ArrayList<ProblemTestCase>();
        List<ProblemTestCaseBean> testCaseBeans = new ArrayList<ProblemTestCaseBean>();
        testCaseBeansTmp = new ProblemTestCase_io(proId).getProblemTestCaselist();
        String[] testCaseIn = new String[testCaseBeansTmp.size()];
        String[] testCaseOut = new String[testCaseBeansTmp.size()];
        for (int j = 0; j < testCaseBeansTmp.size(); j++) {
            testCaseBeans.add(new ProblemTestCaseBean());
            testCaseBeans.get(j).setId(Integer.parseInt(testCaseBeansTmp.get(j).getId()));
            testCaseBeans.get(j).setInput(testCaseBeansTmp.get(j).getInput());
            testCaseBeans.get(j).setOutput(testCaseBeansTmp.get(j).getOutput());
            testCaseBeans.get(j).setProblemId(Integer.parseInt(testCaseBeansTmp.get(j).getProblemId()));
            testCaseIn[j] = testCaseBeansTmp.get(j).getInput();
            testCaseOut[j] = testCaseBeansTmp.get(j).getOutput();
        }
        RefreshSolution rf = new RefreshSolution();
        rf.init();
        List<WrongCase> wrongCases = rf.getWrongCases(routing);
        String status = rf.getStatus(routing);

        int size = testCaseBeans.size();
        String[] testCaseIds = new String[size];
        String[] userOutput = new String[size];
        String[] statusofTestCase = new String[size];
        int k = 0;

        for (int i = 0; i < size; i++) {
            if (k < wrongCases.size() && testCaseBeansTmp.get(i).getId().equals(wrongCases.get(k).getId())) {
                testCaseIds[i] = testCaseBeansTmp.get(i).getId();
                userOutput[i] = wrongCases.get(k).getOutput();
                statusofTestCase[i] = wrongCases.get(k).getStatus();
                k++;
            } else if (!status.equals("CE")) {
                testCaseIds[i] = testCaseBeansTmp.get(i).getId();
                userOutput[i] = "";
                statusofTestCase[i] = "AC";

            } else {
                testCaseIds[i] = testCaseBeansTmp.get(i).getId();
                userOutput[i] = "";
                statusofTestCase[i] = "CE";
            }
        }
        String code = rf.getSourceCode(routing);
        String Id = rf.getSolutionId(routing);
        String score = String.valueOf(rf.getScore(routing));
        String finished = String.valueOf(rf.isFinished(routing));
        String remark = rf.getRemark(routing);
        String correctCaseIds = rf.getCorrectIds(routing);
        String submittimes = rf.getSubmitTimes(routing);
        System.out.println("2\n" + remark);

        Answer answer = new Answer(testCaseIds, userOutput, statusofTestCase, status, remark, correctCaseIds);
        SolutionCode sc = new SolutionCode();
        sc.init();
        String backFile = "./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml";
        File tmpFile = new File(backFile);
        if (!tmpFile.exists()) {
            sc.createXml(backFile);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String date = df.format(new Date());
        sc.updateXml("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml", date, Id, answer, score, code, Integer.parseInt(submittimes));
        //if(finished.equals("true")){
        sc.setFinished(backFile, finished);
        // }
        if (sc.isFinished(backFile)) {
            this.submit.setEnabled(false);
            Control.getCode().setSubmit(false);
        } else {
            this.submit.setEnabled(true);
            Control.getCode().setSubmit(true);
        }
        renew(answer, testCaseIn, testCaseOut, score, proId, submittimes);
        HashMap<String, StudentExamDetail> hash = new HashMap<String, StudentExamDetail>();
        List<StudentExamDetail> sedlist = new StudentExamDetail_io(examId).getstudentExamDetaillist();
        for (int i = 0; i < sedlist.size(); i++) {
            hash.put(sedlist.get(i).getProblemId(), sedlist.get(i));
        }
        if (hash.containsKey(proId)) {
            StudentExamDetail_io student = new StudentExamDetail_io(examId);
            student.changeStatus(String.valueOf(proId), sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), String.valueOf(sc.isFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml")), sc.getId("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"));
        } else {
            //StudentExamDetail(String id,String examId,String problemId,String submit,String status,String hintCases,String score,String elapsedTime,String finished,String SolutionId)
            StudentExamDetail sed = new StudentExamDetail("1", examId, proId, "", sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), "1", "1", "1", "1", "1");
            new StudentExamDetail_io(examId).add(sed);
            StudentExamDetail_io student = new StudentExamDetail_io(examId);
            student.changeStatus(String.valueOf(proId), sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), String.valueOf(sc.isFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml")), sc.getId("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"));
        }
        Control.getMainFrame().setProblemlist(true);
    }

    private class Wrongdetail implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Window parentWindow = SwingUtilities.windowForComponent(AnswerTablePanel.this);
            JFrame parentFrame = null;
            if (parentFrame instanceof JFrame) {
                parentFrame = (JFrame) parentWindow;
            }
            //加是否查看的弹窗
            if("false".equals(Control.getExam().getCanGetHint())){
                JOptionPane.showMessageDialog(AnswerTablePanel.this, "该场考试不允许获取提示！");
                return;
            }
            int selection = JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                    "查看当前题目的正确测试用例会减少该题相应的得分。\n"
                    + "\n" + "你确认要查看正确的测试用例吗？",
                    "查看测试用例",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (selection == JOptionPane.OK_OPTION) {

                String username = Control.getUser().getUserName();
                String passwd = Control.getUser().getPassword();
                String wrongnum = event.getActionCommand();
                String toWrite = new String();
                try {
                    ////System.out.println(problemXml);
                    //System.out.println(wrongnum);
                    toWrite = Control.getWebsService().viewWrongCase(username, passwd, Integer.parseInt(examId), Integer.parseInt(proId), Integer.parseInt(wrongnum), true);
                    System.out.println("viewWrongCase:\n" + toWrite);
                    //                    Object[] obj =client.invoke("WS_ViewWrongCase",username,passwd,Integer.parseInt(examId),Integer.parseInt(proId),Integer.parseInt(wrongnum),true);
//                    System.out.println(obj[0]);
//                    toWrite = (String)obj[0];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String backFile = "./xml/" + Control.getPath() + "/afterSubmitProblem.xml";
                File tmpFile = new File(backFile);
                if (tmpFile.exists()) {
                    tmpFile.delete();
                }
                TextToFile(backFile, toWrite);
                SubmitProblem sb = new SubmitProblem();
                sb.init();
                String msg = sb.isSubmitted(backFile);
                if (msg.equals("true")) {
                    int wronginnum = wrongCase.get(wrongnum);
                    WrongDetailDialog ss = new WrongDetailDialog(parentFrame, AnswerTablePanel.this.answer, wronginnum, testCaseIn, testCaseOut, wrongnum);
                    ss.setVisible(true);
                    AnswerTablePanel.this.scoreOut.setText(sb.getScore(backFile));
                } else {
                    String message = msg;
                    JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                            message, "提示",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
            String backFile = "./xml/" + Control.getPath() + "/afterSubmitProblem.xml";
            File tmpFile = new File(backFile);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
        }

    }

    public void setNoSubmit() {
        this.submit.setEnabled(false);
    }

    private void TextToFile(String strFilename, String strBuffer) {
        try {
            File fileText = new File(strFilename);
            FileWriter fileWriter = new FileWriter(fileText);
            fileWriter.write(strBuffer);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //点击提交本题之后
    private class SubmitPro implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            submitProblem();
        }
    }

    public void submitProblem() {
        String proId = this.proId;
        String problemXml;
        Boolean flag = true;
        if (submitOnlyAC && !answer.getStatus().equals("AC")) {
            String message = "本次考试仅在AC时能提交";
            int selection = JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                    message, "提示",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        SolutionCode sc = new SolutionCode();
        sc.init();
        if (checkOrNot == true) {
            this.simi = new check().check(similarity, sc.getCode("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), String.valueOf(proId));
            if (this.simi.getIsCopied().equals("error")) {
                String message = "无法进行评判，请检查网络连接";
                JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                        message, "提示",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
            }
            sc.setSimi("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml", this.simi.getIsCopied());
        } else {
            this.simi = new Similarity("-1", "", "");
            sc.setSimi("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml", "-1");
        }

        SubmitProblem sub = new SubmitProblem();
        if (sc.isCopied("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml").equals("true")) {
            String message = "此代码涉嫌抄袭，是否提交？";
            int selection = JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                    message, "提示",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (selection == JOptionPane.NO_OPTION) {
                //String SubmitProblem(String examId,String problemId,String siminum,String simiId,String iscopied,String submit)
                problemXml = sub.SubmitProblem(examId, proId, simi.getMaxnum(), simi.getMaxId(), "true", "false");
                flag = false;
                Control.getMainFrame().setProblemlist(true);
            } else {
                problemXml = sub.SubmitProblem(examId, proId, simi.getMaxnum(), simi.getMaxId(), "true", "true");
            }
        } else if (sc.isCopied("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml").equals("false")) {
            problemXml = sub.SubmitProblem(examId, proId, simi.getMaxnum(), simi.getMaxId(), "false", "true");
        } else {
            problemXml = sub.SubmitProblem(examId, proId, "-1", "", "false", "true");
        }

        if (!answer.getStatus().equals("AC")) {
            problemXml = sub.SubmitProblem(examId, proId, "-1", "", "false", "true");
        }

        String username = Control.getUser().getUserName();
        String passwd = Control.getUser().getPassword();
        String toWrite = new String();
        try {
            toWrite = Control.getWebsService().submitThisProblem(username, passwd, problemXml);
            System.out.println("submitThisProblem:\n" + toWrite);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String backFile = "./xml/" + Control.getPath() + "/afterSubmitProblem.xml";
        File tmpFile = new File(backFile);
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        TextToFile(backFile, toWrite);
        SubmitProblem sb = new SubmitProblem();
        sb.init();
        String msg = sb.isSubmitted(backFile);
        if (msg.equals("true")) {
            if (flag) {
                String message = "成功提交本题";
                JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                        message, "提示",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                SolutionCode c = new SolutionCode();
                c.init();
                c.setFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml", "true");
                //变灰
                this.setNoSubmit();
                Control.getMainFrame().setProblemlist(true);
            }

        } else if (msg.equals("不能重复提交")) {
            JOptionPane.showConfirmDialog(AnswerTablePanel.this, "请注意：\n您已经提交过本题，不可再提交。", "注意", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        } else {
            String message = msg;
            JOptionPane.showConfirmDialog(AnswerTablePanel.this,
                    message, "提示",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
        }
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        HashMap<String, StudentExamDetail> hash = new HashMap<String, StudentExamDetail>();
        List<StudentExamDetail> sedlist = new StudentExamDetail_io(examId).getstudentExamDetaillist();
        for (int i = 0; i < sedlist.size(); i++) {
            hash.put(sedlist.get(i).getProblemId(), sedlist.get(i));
        }
        if (hash.containsKey(proId)) {
            StudentExamDetail_io student = new StudentExamDetail_io(examId);
            student.changeStatus(String.valueOf(proId), sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), String.valueOf(sc.isFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml")), sc.getId("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"));
        } else {
            //StudentExamDetail(String id,String examId,String problemId,String submit,String status,String hintCases,String score,String elapsedTime,String finished,String SolutionId)
            StudentExamDetail sed = new StudentExamDetail("1", examId, proId, "", sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), "1", "1", "1", "1", "1");
            new StudentExamDetail_io(examId).add(sed);
            StudentExamDetail_io student = new StudentExamDetail_io(examId);
            student.changeStatus(String.valueOf(proId), sc.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"), String.valueOf(sc.isFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml")), sc.getId("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml"));
        }
        Control.getMainFrame().setProblemlist(true);
        //Control.getMainFrame().collection(row);
        File tmp = new File("./xml/" + Control.getPath() + "/tmp1.xml");
        tmp.delete();
    }
    
    public void refreshResult(String proId){
        this.proId = proId;
        String username = Control.getUser().getUserName();
        String passwd = Control.getUser().getPassword();
        String toWrite = new String();
        try {
            toWrite = Control.getWebsService().getExamProblemStatus(username, passwd, Integer.parseInt(Control.getExamId()), Integer.parseInt(proId));
            System.out.println("getExamProblemStatus:\n" + toWrite);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String backFile = "./xml/" + Control.getPath() + "/afterRefresh.xml";
        File tmpFile = new File(backFile);
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        TextToFile(backFile, toWrite);
        RefreshSolution rf = new RefreshSolution();
        rf.init();
        SolutionCode sc = new SolutionCode();
        sc.init();
        String msg = rf.hasSubmitted(backFile);
        if (msg.equals("true")) {
            fresh(backFile);
        }
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        Control.getCode().setFreshCode(proId);
    }
}
