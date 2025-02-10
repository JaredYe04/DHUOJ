/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.view.frame;

import client.io.xml.User_io;
import client.io.xml.Exam_io;
import client.model.User;
import client.model.Exam;
import client.model.Information;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import client.util.Control;
import client.service.DownFileWrite;
import client.service.GetServerTime;
import client.service.JudgeLogin;
import client.service.myswingworker.MySwingWorker;
import client.service.web.Webservice;
import client.util.Config;
import clientupdater.ClientUpdater;
import common.LogLevel;
import common.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import log.Log;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.service.factory.ServiceConstructionException;

/**
 *
 * @author ytxlo
 */
public class LoginFrame extends JFrame {

    private boolean alreadyClicked = false;//判断是否连续点击标识；
    public static HashMap<String, Exam> oldExam = new HashMap<String, Exam>();
    public static Client client;
    private String wsdl;
    private String username;
    private String password;
    private String message;
    private JButton JB_OnlineLogin;
    private JButton JB_OfflineLogin;

    private JProgressBar progressbar;

    private JTextField JF_UserID;
    private JPasswordField JPF_Password;
    private JPanel JP_Left;
    private JPanel JP_Right;
    private JPanel JP_Bottom;
    private JPanel JP_Bottom_top;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    public JTextField JTF_ip;
    public JTextField JTF_port;
    private User user;
    private Logger logger;
    private static Boolean boollogin;
    private String ip;
    private String port;

    private User getUser() {
        return user;
    }

    public static Boolean getLogin() {
        return boollogin;
    }

    public static void setLogin(Boolean bool) {
        boollogin = bool;
    }

    public LoginFrame() {
        initComponents();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((screenSize.width - 300) / 2, (screenSize.height - 267) / 2, 300, 267);
        //new MainFrame();
        //Control.setContestList();
    }

    private void initComponents() {
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        JTF_ip = new JTextField();
        JTF_port = new JTextField();
        JP_Left = new JPanel();
        JP_Right = new JPanel();
        JP_Bottom = new JPanel();
        JF_UserID = new JTextField();
        JPF_Password = new JPasswordField();
        JB_OnlineLogin = new JButton();
        JB_OfflineLogin = new JButton();
        JP_Bottom_top = new JPanel();
        progressbar = new JProgressBar();
        logger = common.Logger.getInstance();
//        this.progressbar.setIndeterminate(true);

        System.out.println(System.getProperty("file.encoding"));

        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("登录");

        JP_Left.setLayout(new java.awt.GridLayout(6, 1));

        jLabel3.setText("");
        JP_Left.add(jLabel3);

        jLabel8.setText("   服务器IP： ");
        JP_Left.add(jLabel8);

        jLabel9.setText("   服务器PORT：");
        JP_Left.add(jLabel9);

        jLabel1.setText("   账   号:    ");
        JP_Left.add(jLabel1);

        jLabel2.setText("   密   码:    ");
        JP_Left.add(jLabel2);

        jLabel6.setText("");
        JP_Left.add(jLabel6);

        getContentPane().add(JP_Left, java.awt.BorderLayout.WEST);

        JP_Right.setLayout(new java.awt.GridLayout(6, 1));

        jLabel5.setText("");
        JP_Right.add(jLabel5);

        JTF_ip.setColumns(15);
        String url = "";
        String port = "";
        try {
            url = Config.readValue("URLip");
            port = Config.readValue("URLport");
            ////System.out.println(url);
        } catch (IOException ex) {
            Log.writeExceptionLog(ex.getMessage());
            // System.err.println(ex.getMessage());
        }
        JTF_ip.setText(url);
        JTF_port.setText(port);
        JP_Right.add(JTF_ip);
        JP_Right.add(JTF_port);
        JTF_port.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JB_OnlineLogin.doClick();
                }
            }
        });
        JTF_ip.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JB_OnlineLogin.doClick();
                }
            }
        });
        JF_UserID.setColumns(15);
        JP_Right.add(JF_UserID);
        JP_Right.add(JPF_Password);

        jLabel7.setText("");
        JP_Right.add(jLabel7);

        JP_Bottom.setLayout(new GridLayout(3, 1));
        JP_Bottom_top.setLayout(new GridLayout(1, 2));
        this.JF_UserID.setText(Config.getUsername());
//        this.JF_UserID.setText("felix");
//        this.JPF_Password.setText("felix");
        this.JF_UserID.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JB_OnlineLogin.doClick();
                }
            }
        });
        this.JPF_Password.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JB_OnlineLogin.doClick();
                }
            }
        });

        JB_OnlineLogin.setText("登录");
        JB_OnlineLogin.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JB_OnlineLogin.doClick();
                }
            }
        });
        JB_OnlineLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JB_OnlineLoginActionPerformed();
            }
        });
        JP_Bottom_top.add(JB_OnlineLogin);

        JB_OfflineLogin.setText("脱机登录");
        JB_OfflineLogin.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JB_OfflineLogin.doClick();
                }
            }
        });
        JB_OfflineLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JB_OfflineLoginActionPerformed();
            }
        });
        File f = new File("./xml/user.dat");
        if (!f.exists()) {
            JB_OfflineLogin.setEnabled(false);
        }
//        JP_Bottom_top.add(JB_OfflineLogin);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Version-1.6 ");
        JP_Bottom.add(this.JP_Bottom_top);
        JP_Bottom.add(this.progressbar);
        JP_Bottom.add(this.jLabel4);

        getContentPane().add(JP_Right, java.awt.BorderLayout.CENTER);

        getContentPane().add(JP_Bottom, java.awt.BorderLayout.SOUTH);
        pack();
        if (this.JF_UserID.getText().isEmpty()) {
            this.JF_UserID.requestFocus();
        } else {
            this.JPF_Password.requestFocus();
        }
    }

    private void JB_OnlineLoginActionPerformed() {
        if (alreadyClicked) {
            //如果连续点击，提示用户不要反复连续点击
            JOptionPane.showMessageDialog(this, "请不要连续点击");
            return;
        } else {
            alreadyClicked = true;
        }
        username = this.JF_UserID.getText();
        password = String.valueOf(this.JPF_Password.getPassword());
        ip = JTF_ip.getText();
        port = JTF_port.getText();
        if (username.length() == 0) {
            JOptionPane.showMessageDialog(null, "账号不能为空！");
            alreadyClicked = false;
        } else if (password.length() == 0) {
            JOptionPane.showMessageDialog(null, "密码不能为空！");
            //账号和密码为空时释放点击
            alreadyClicked = false;
        } else {
            MySwingWorker swingworker = new MySwingWorker(this::login, this::myDone);
            swingworker.execute();
        }
    }

    public void myDone() {
        if (user.getRspMsg().equals("Success")) {
            //System.out.println("Here,Success!");
            Control.setUser(user);
            new JudgeLogin().loginWrite(user);
//            new DownFileWrite().write("./xml/user.dat", message,Control.getPublicKey());
            getExamListOnline(username, password);
            Config.setURLip(JTF_ip.getText());
            Config.setURLport(JTF_port.getText());
            Config.setUsername(username);
            Config.save();
            boollogin = true;
            new GetServerTime().getTime();
            dispose();

            new ExamFrame().setVisible(true);
        } else {
            alreadyClicked = false;
            JOptionPane.showMessageDialog(null, user.getRspMsg());
            this.progressbar.setIndeterminate(false);
        }
    }

    private void JB_OfflineLoginActionPerformed() {
        if (alreadyClicked) {
            //如果连续点击，提示用户不要反复连续点击
            JOptionPane.showMessageDialog(this, "请不要连续点击");
            return;
        } else {
            alreadyClicked = true;
        }
        username = this.JF_UserID.getText();
        password = String.valueOf(this.JPF_Password.getPassword());
        if (username.length() == 0) {
            JOptionPane.showMessageDialog(null, "账号不能为空！");
        } else if (password.length() == 0) {
            JOptionPane.showMessageDialog(null, "密码不能为空！");
        } else {
            User offlineUser = new User();
            offlineUser.setUserName(username);
            offlineUser.setPassword(password);
            user = new JudgeLogin().loginJudge(offlineUser);
            if (user != null) {
                if (user.getRspMsg().equals("Success")) {
                    Control.setUser(user);
                    setKeyAndPath();
                    this.readExamList();
                    if (this.oldExam.isEmpty()) {
                        int r = JOptionPane.showConfirmDialog(null, "本地无保存的考试信息，是否联机登录？", "提示", JOptionPane.YES_NO_OPTION);
                        if (r == JOptionPane.YES_OPTION) {
                            JB_OnlineLogin.doClick();
                        } else {
                            dispose();
                        }
                    } else {
                        dispose();
                        boollogin = false;
                        new ExamFrame().setVisible(true);
                    }
                } else {
                    alreadyClicked = false; //做完任务只要窗口没有被释放掉，让按键可点
                    JOptionPane.showMessageDialog(null, "用户名或者密码错误！");
                }
            } else {
                alreadyClicked = false;
                JOptionPane.showMessageDialog(null, "请先联机登录成功！");
            }
        }
    }

    private void setUser() {
        User_io io = new User_io();
        user = io.getUser(message);
    }

    public static void main(String args[]) {
//        System.out.println("A".matches("[^[A-Za-z0-9\\._\\?%&+\\-=/#]]*"));
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        NativeInterface.open();
        Control.setPublicKey("1413201160");
        LoginFrame jframe = new LoginFrame();
        jframe.setVisible(true);
        Control.setLoginFrame(jframe);

        NativeInterface.runEventPump();
    }

    public Boolean login() {
        this.progressbar.setIndeterminate(true);
        try {
            //checkUpdate(ip, port); //取消自动更新！！！
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("更新错误啦！");
        }
        try {
            URL url = new URL("http://" + ip + ":" + port + "/oj/webservice/OJWS?wsdl");
            QName qname = new QName("http://ws.dhu.edu/", "OJWS");
            logger.log("尝试建立Web服务", LogLevel.INFO);
            Control.setWebService(new Webservice(url, qname));
            logger.log("建立Web服务成功", LogLevel.INFO);
            message = Control.getWebsService().login(username, password);
//            new Information(ip + ":" + port, username, password);
            this.setUser();
            setKeyAndPath();
            this.progressbar.setIndeterminate(false);
            return true;
        } catch (ServiceConstructionException ex) {
            alreadyClicked = false;
            int r = JOptionPane.showConfirmDialog(null, "未联网，是否脱机登录？", "提示", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                JB_OfflineLogin.doClick();
            } else {
                Control.getLoginframe().dispose();
            }
            return false;
        } catch (Exception e) {
            logger.log("建立Web服务异常" + e.getMessage(), LogLevel.ERROR);
            alreadyClicked = false;
            User offlineUser = new User();
            offlineUser.setUserName(username);
            offlineUser.setPassword(password);
            user = new JudgeLogin().loginJudge(offlineUser);
            if (user != null) {
                if (user.getRspMsg().equals("Success")) {
                    Control.setUser(user);
                    setKeyAndPath();
                    this.readExamList();
                    if (this.oldExam.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "登录失败,请联系管理员");
                    } else {
                        int r = JOptionPane.showConfirmDialog(null, "未联网，是否脱机登录？", "提示", JOptionPane.YES_NO_OPTION);
                        if (r == JOptionPane.YES_OPTION) {
                            JB_OfflineLogin.doClick();
                        }
                    }
                } else {
                    alreadyClicked = false; //做完任务只要窗口没有被释放掉，让按键可点
                    JOptionPane.showMessageDialog(null, "用户名或者密码错误！");
                }
            } else {
                alreadyClicked = false;
                JOptionPane.showMessageDialog(this, "登录失败,请联系管理员");
            }

            this.progressbar.setIndeterminate(false);
            Log.writeExceptionLog(e.getMessage() + e.getClass());
            //System.err.println(e.getMessage());
            return false;
        }
    }

    private void getExamListOnline(String username, String passwd) {
        try {
            this.readExamList();
            String str = Control.getWebsService().getExamList(username, passwd);
            new DownFileWrite().write("./xml/" + Control.getPath() + "/examlist.dat", str, Control.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readExamList() {
        List<Exam> Examlist = new Exam_io().getexamlist();
        HashMap<String, Exam> map = new HashMap<String, Exam>();
        for (int i = 0; i < Examlist.size(); i++) {
            map.put(Examlist.get(i).getId(), Examlist.get(i));
        }
        this.oldExam = map;
    }

    private void setKeyAndPath() {
        String key = user.getId();
        if (key == null) {
            return;
        }
        if (key.length() < 10) {
            key = key.concat(new String("0000000000").substring(0, 10 - key.length()));
        }
        Control.setKey(key);
        Control.setPath(key);
    }

    private void checkUpdate(String ip, String port) {
        String ipandport = ip + ":" + port;
        ClientUpdater clientUpate = new ClientUpdater();
        int isneed = clientUpate.CheckWhetherNeedUpdate(ipandport);
        System.out.println("检查更新后" + System.currentTimeMillis());
        System.out.println(isneed);
        if (1 == isneed) {
            //System.out.println("进入start");
            boolean sucess = clientUpate.startUpdate(ipandport);
            if (sucess) {
                this.setVisible(true);
            } else {
                this.dispose();
            }
        }
//        else if (isneed==-1){
//          JOptionPane.showMessageDialog(this, "检查更新失败");
//        }
    }

    private boolean sysFileDelete() {
        String path = common.Config.getSourcePath();
        File file = new File(path);
        file.deleteOnExit();
        return false;
    }
}
