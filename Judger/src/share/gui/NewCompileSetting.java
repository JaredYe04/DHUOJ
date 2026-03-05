/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package share.gui;

import common.FileFinder;
import common.Config;
import common.Const;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import log.Log;

/**
 *
 * @author Administrator
 */
public class NewCompileSetting extends javax.swing.JDialog {

    /**
     * Creates new form CompileSetting
     */
    public NewCompileSetting(String languageString, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setResizable(false);

        this.cSystem.setActionCommand("0");
        this.cRegistry.setActionCommand("1");
        this.cSelf.setActionCommand("2");

        this.javaSystem.setActionCommand("0");
        this.javaRegistry.setActionCommand("1");
        this.javaSelf.setActionCommand("2");

        this.jButton2.setVisible(false);
        this.cppDir.setVisible(false);
        this.jLabel3.setVisible(false);
        cDir.setEnabled(false);
        cppDir.setEnabled(false);
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        cRegistryList.setEnabled(false);
        javaDir.setEnabled(false);
        jButton3.setEnabled(false);
        javaRegistryList.setEnabled(false);    
        chooseLanguagePanel(languageString);
        initRegistryMessage();
        this.loadConfig();
    }

    private void saveConfig() {
        try {
            //根据radiobutton的选择情况判断写入
            if (cSystem.isSelected()||cRegistry.isSelected()||cSelf.isSelected()) {
                //路径为自带编译器路径
                Config.getProp().setProperty(Const.MinGWDir, Config.CompilerDir("c",null));
                Config.getProp().setProperty(Const.MinGWRelative, "1");
                Config.getProp().setProperty(Const.MinGWDir, lblCRegistryMessage.getText());
                Config.getProp().setProperty(Const.MinGWRelative, "2");
                Config.getProp().setProperty(Const.MinGWDir, cDir.getText());
                Config.getProp().setProperty(Const.MinGWRelative, "3");
            }
            if (javaSystem.isSelected()||javaRegistry.isSelected()||javaSelf.isSelected()) {
                Config.getProp().setProperty(Const.JavaCompilerDir, Config.CompilerDir("java",null));
                Config.getProp().setProperty(Const.JavaRelative, "1");
                Config.getProp().setProperty(Const.JavaCompilerDir, lblJavaRegistryMessage.getText());
                Config.getProp().setProperty(Const.JavaRelative, "2");
                Config.getProp().setProperty(Const.JavaCompilerDir, javaDir.getText());
                Config.getProp().setProperty(Const.JavaRelative, "3");
            }
            Config.save();
            System.out.println("save config success");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("淇濆瓨缂栬瘧閰嶇疆閿欒 file:Control.java method:saveCompileConfig");
        }
    }
//语言编译器选择
    public void chooseLanguagePanel(String languageString) {
        languageString = languageString.toLowerCase();
        switch (languageString) {
            case "java":
                cJPanel.setVisible(false);
                javaJPanel.setVisible(true);
                break;
            case "c":
            case "c++":
                cJPanel.setVisible(true);
               javaJPanel.setVisible(false);
                break;
            default:
        }
    }

    private void loadConfig() {
        try {
            cRegistryList.removeAllItems();
            javaRegistryList.removeAllItems();
            //第一优先级为内置编译器，第二为注册表搜索到的，最后为用户自定义
            String MinGWDir = Config.CompilerDir("c",null);
            if (!FileFinder.isExistFile(MinGWDir)) {
                cSystem.setEnabled(false);
            }
            HashMap<String, String> tmpHashtable = CRegistryMessage;
            if (tmpHashtable.size() != 0) {
                Iterator iter = tmpHashtable.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    cRegistryList.addItem(key);
                }
            } else {
                lblCRegistryMessage.setText("未检索到您的计算机上安装了MinGW，您可以手动配置路径。");
                setCRegistryGroupEnable(false);
                cRegistry.setEnabled(false);
            }

            String javaDirString = Config.CompilerDir("java",null);   // SXZ530
            if (!FileFinder.isExistFile(javaDirString)) {
                javaSystem.setEnabled(false);
            }
            tmpHashtable = JavaRegistryMessage;
            if (tmpHashtable.size() != 0) {
                Iterator iter = tmpHashtable.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    javaRegistryList.addItem(key);
                }
            } else {
                lblJavaRegistryMessage.setText("未检索到您的计算机上安装了JDK，您可以手动配置路径。");
                setJavaRegistryGroupEnable(false);
                javaRegistry.setEnabled(false);
            }
            this.cRegistryList.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    String item = (String) e.getItem();
                    int state = e.getStateChange();
                    if (state == 1) {
                        //改变选中项
                        HashMap<String, String> tmpHashtable = CRegistryMessage;
                        Iterator iter = tmpHashtable.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();
                            if (item.equals(key)) {
                                lblCRegistryMessage.setText(val);
                                break;
                            }
                        }
                    }
                }
            });
            this.javaRegistryList.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    String item = (String) e.getItem();
                    int state = e.getStateChange();
                    if (state == 1) {
                        //改变选中项
                        HashMap<String, String> tmpHashtable = JavaRegistryMessage;
                        Iterator iter = tmpHashtable.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();
                            if (item.equals(key)) {
                                lblJavaRegistryMessage.setText(val);
                                break;
                            }
                        }
                    }
                }
            });
            this.cRegistryList.setSelectedIndex(-1);
            this.javaRegistryList.setSelectedIndex(-1);
            //根据Relative配置初始化
            if (Config.MinGWRelative == 1)//为系统内置
            {
                cSystem.setSelected(true);
                setCFindPathGroupEnable(false);
                setCRegistryGroupEnable(false);
            } else if (Config.MinGWRelative == 2) {
                //设置保存为选择计算机注册的东西，故路径应该是有的，无视其他情况
                cRegistry.setSelected(true);
                MinGWDir = Config.getCompilerDir("c",null);
                tmpHashtable = CRegistryMessage;
                Iterator iter = tmpHashtable.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    if (val.equals(MinGWDir)) {
                        cRegistryList.setSelectedItem(key);
                    }
                }
                setCFindPathGroupEnable(false);
                setCRegistryGroupEnable(true);
                lblCRegistryMessage.setText(MinGWDir);
            } else if (Config.MinGWRelative == 3) {
                cSystem.setSelected(true);
                MinGWDir = Config.getCompilerDir("c",null);
                cDir.setText(MinGWDir);
                setCFindPathGroupEnable(true);
                setCRegistryGroupEnable(false);
            }

            if (Config.JavaRelative == 1) {
                javaSystem.setSelected(true);
                setJavaFindPathGroupEnable(false);
                setJavaRegistryGroupEnable(false);
            } else if (Config.JavaRelative == 2) {
                javaRegistry.setSelected(true);
                javaDirString = Config.getCompilerDir("java",null);
                tmpHashtable = JavaRegistryMessage;
                Iterator iter = tmpHashtable.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    if (val.equals(javaDirString)) {
                        javaRegistryList.setSelectedItem(key);
                    }
                }
                lblJavaRegistryMessage.setText(javaDirString);
                setJavaFindPathGroupEnable(false);
                setJavaRegistryGroupEnable(true);
            } else if (Config.JavaRelative == 3) {
                javaSystem.setSelected(true);
                javaDirString = Config.getCompilerDir("java",null);
                javaDir.setText(javaDirString);
                setJavaFindPathGroupEnable(true);
                setJavaRegistryGroupEnable(false);
            }
            //装载临时文件路径
//            String tmp = Config.getProp().getProperty(Const.srcDirIdentify);
//            if(tmp != null && !"".equals(tmp))
//                srcDir.setText(tmp);
//            String cCompilePath = Config.getProp().getProperty(Const.cCompilerDirIdentify);
//            if(cCompilePath.isEmpty()) cCompilePath = Config.CompilerDir("c");
//            this.cDir.setText(cCompilePath);
//            
//            String cppCompilePath = Config.getProp().getProperty(Const.cppCompilerDirIdentify);
//            if(cppCompilePath.isEmpty()) cppCompilePath = Config.CompilerDir("cpp");
//            this.cppDir.setText(cppCompilePath);
//            
//            String javaCompilePath = Config.getProp().getProperty(Const.javaCompilerDirIdentify);
//            if(javaCompilePath.isEmpty()) javaCompilePath = Config.CompilerDir("cpp");
//            this.javaDir.setText(javaCompilePath);
//            
//            String srcPath = Config.getProp().getProperty(Const.srcDirIdentify);
//            if(srcPath.isEmpty()) srcPath = System.getProperty("user.dir") + "\\test";;
//            this.srcDir.setText(srcPath);
//            
//            String isRelativeC = Config.getProp().getProperty("isCppRelative");
//            String isRelativeJ = Config.getProp().getProperty("isJavaRelative");
//            boolean isCppRelative = (isRelativeC.equals("true")) ? true : false;
//            boolean isJavaRelative = (isRelativeJ.equals("true")) ? true : false;
//            if (isCppRelative) {
//                this.cSystem.setSelected(true);
//                this.jButton1.setEnabled(false);
//                this.jButton2.setEnabled(false);
//                this.cDir.setEditable(false);
//                this.cDir.setEnabled(false);
//                this.cppDir.setEditable(false);
//                this.cppDir.setEnabled(false);
//                //this.cDir.setText(Config.CompilerDir("c"));
//                //this.cppDir.setText(Config.CompilerDir("cpp"));
//            } else {
//                this.cSelf.setSelected(true);
//                this.cDir.setEditable(true);
//                this.cDir.setEnabled(true);
//                this.cppDir.setEditable(true);
//                this.cppDir.setEnabled(true);
//            }
//            
//            if (isJavaRelative) {
//                this.javaSystem.setSelected(true);
//                this.jButton3.setEnabled(false);
//                this.javaDir.setEditable(false);
//                this.javaDir.setEnabled(false);
//               // this.javaDir.setText(Config.CompilerDir("java"));
//            } else {
//                this.javaSelf.setSelected(true);
//                this.javaDir.setEditable(true);
//                this.javaDir.setEnabled(true);
//            }

            System.out.println("load config success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        cJPanel = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cSystem = new javax.swing.JRadioButton();
        cSelf = new javax.swing.JRadioButton();
        cRegistry = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cDir = new javax.swing.JTextField();
        cppDir = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        cRegistryList = new javax.swing.JComboBox<>();
        lblCRegistryMessage = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        javaJPanel = new javax.swing.JPanel();
        javaSystem = new javax.swing.JRadioButton();
        javaDir = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        javaSelf = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        javaRegistry = new javax.swing.JRadioButton();
        javaRegistryList = new javax.swing.JComboBox<>();
        lblJavaRegistryMessage = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton2.setText("...");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("宋体", 1, 16)); // NOI18N
        jLabel2.setText("C/C++编译器设置");

        buttonGroup1.add(cSystem);
        cSystem.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        cSystem.setText("使用系统内置编译器");
        cSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cSystemActionPerformed(evt);
            }
        });

        buttonGroup1.add(cSelf);
        cSelf.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        cSelf.setText("指定编译器(gcc.exe/g++.exe)所在文件夹");
        cSelf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cSelfActionPerformed(evt);
            }
        });

        buttonGroup1.add(cRegistry);
        cRegistry.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        cRegistry.setText("使用本机注册的编译器");
        cRegistry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRegistryActionPerformed(evt);
            }
        });

        jLabel1.setText("gcc/g++");

        jLabel3.setText("C++");

        jButton1.setText("...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        cRegistryList.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        cRegistryList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cRegistryList.setSelectedIndex(-1);
        cRegistryList.setActionCommand("comboBoxChanged1");
        cRegistryList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRegistryListActionPerformed(evt);
            }
        });

        lblCRegistryMessage.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N

        jButton5.setText("保存");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cJPanelLayout = new javax.swing.GroupLayout(cJPanel);
        cJPanel.setLayout(cJPanelLayout);
        cJPanelLayout.setHorizontalGroup(
            cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cJPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(227, 227, 227))
                    .addGroup(cJPanelLayout.createSequentialGroup()
                        .addGroup(cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cJPanelLayout.createSequentialGroup()
                                .addComponent(cRegistry)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cRegistryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cSystem)
                            .addComponent(cSelf)
                            .addGroup(cJPanelLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblCRegistryMessage)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(cJPanelLayout.createSequentialGroup()
                .addGroup(cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cJPanelLayout.createSequentialGroup()
                        .addGroup(cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cJPanelLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(jLabel3)
                                .addGap(38, 38, 38))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cJPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cJPanelLayout.createSequentialGroup()
                                .addComponent(cDir, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addGroup(cJPanelLayout.createSequentialGroup()
                                .addComponent(cppDir, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))))
                    .addGroup(cJPanelLayout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(jButton5)))
                .addGap(0, 6, Short.MAX_VALUE))
        );
        cJPanelLayout.setVerticalGroup(
            cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(cSystem)
                .addGap(18, 18, 18)
                .addGroup(cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cRegistry)
                    .addComponent(cRegistryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblCRegistryMessage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(cSelf)
                .addGap(7, 7, 7)
                .addGroup(cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(cJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cppDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        buttonGroup2.add(javaSystem);
        javaSystem.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        javaSystem.setText("使用系统内置编译器");
        javaSystem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaSystemActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("宋体", 1, 16)); // NOI18N
        jLabel4.setText("Java编译器设置");

        buttonGroup2.add(javaSelf);
        javaSelf.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        javaSelf.setText("指定编译器(javac.exe)所在文件夹");
        javaSelf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaSelfActionPerformed(evt);
            }
        });

        jLabel5.setText("javac");

        jButton3.setText("...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        buttonGroup2.add(javaRegistry);
        javaRegistry.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        javaRegistry.setText("使用本机注册的编译器");
        javaRegistry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaRegistryActionPerformed(evt);
            }
        });

        javaRegistryList.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N
        javaRegistryList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        javaRegistryList.setSelectedIndex(-1);
        javaRegistryList.setActionCommand("comboBoxChanged2");
        javaRegistryList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                javaRegistryListActionPerformed(evt);
            }
        });

        lblJavaRegistryMessage.setFont(new java.awt.Font("宋体", 0, 14)); // NOI18N

        jButton6.setText("保存");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout javaJPanelLayout = new javax.swing.GroupLayout(javaJPanel);
        javaJPanel.setLayout(javaJPanelLayout);
        javaJPanelLayout.setHorizontalGroup(
            javaJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javaJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(javaJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javaJPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(javaJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(javaSystem)
                            .addGroup(javaJPanelLayout.createSequentialGroup()
                                .addComponent(javaRegistry)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(javaRegistryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javaJPanelLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblJavaRegistryMessage))
                            .addGroup(javaJPanelLayout.createSequentialGroup()
                                .addGroup(javaJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javaJPanelLayout.createSequentialGroup()
                                        .addComponent(javaSelf)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, javaJPanelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(javaDir, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())))
                    .addGroup(javaJPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javaJPanelLayout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jButton6)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        javaJPanelLayout.setVerticalGroup(
            javaJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javaJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addComponent(javaSystem)
                .addGap(18, 18, 18)
                .addGroup(javaJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(javaRegistry)
                    .addComponent(javaRegistryList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblJavaRegistryMessage)
                .addGap(32, 32, 32)
                .addComponent(javaSelf)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(javaJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(javaDir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(javaJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cJPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(javaJPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        javaJPanel.setVisible(false);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void javaSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaSystemActionPerformed
        setJavaFindPathGroupEnable(false);
        setJavaRegistryGroupEnable(false);
    }//GEN-LAST:event_javaSystemActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        this.javaDir.setText(getChooseDirectory("j"));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void javaSelfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaSelfActionPerformed
        setJavaFindPathGroupEnable(true);
        setJavaRegistryGroupEnable(false);
    }//GEN-LAST:event_javaSelfActionPerformed

    private void javaRegistryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaRegistryActionPerformed
        setJavaRegistryGroupEnable(true);
        setJavaFindPathGroupEnable(false);
    }//GEN-LAST:event_javaRegistryActionPerformed

    private void javaRegistryListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_javaRegistryListActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_javaRegistryListActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.saveConfig();
        this.dispose();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void cRegistryListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRegistryListActionPerformed

    }//GEN-LAST:event_cRegistryListActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        this.cDir.setText(getChooseDirectory("c"));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cRegistryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRegistryActionPerformed
        setCFindPathGroupEnable(false);
        setCRegistryGroupEnable(true);
    }//GEN-LAST:event_cRegistryActionPerformed

    private void cSelfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cSelfActionPerformed
        setCFindPathGroupEnable(true);
        setCRegistryGroupEnable(false);
    }//GEN-LAST:event_cSelfActionPerformed

    private void cSystemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cSystemActionPerformed
        setCFindPathGroupEnable(false);
        setCRegistryGroupEnable(false);
    }//GEN-LAST:event_cSystemActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.cppDir.setText(getChooseDirectory("c"));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // 两个保存按钮事件一致
        jButton5ActionPerformed(evt);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void setCRegistryGroupEnable(boolean enable) {
        this.cRegistryList.setEnabled(enable);
        this.lblCRegistryMessage.setEnabled(enable);
    }

    private void setJavaRegistryGroupEnable(boolean enable) {
        this.javaRegistryList.setEnabled(enable);
        this.lblJavaRegistryMessage.setEnabled(enable);
    }

    private void setJavaFindPathGroupEnable(boolean enable) {
        this.jLabel5.setEnabled(enable);
        this.javaDir.setEnabled(enable);
        this.jButton3.setEnabled(enable);
    }

    private void setCFindPathGroupEnable(boolean enable) {
        this.cDir.setEnabled(enable);
        this.cppDir.setEnabled(enable);
        this.jButton1.setEnabled(enable);
        this.jButton2.setEnabled(enable);
        this.jLabel1.setEnabled(enable);
        this.jLabel3.setEnabled(enable);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
      
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NewCompileSetting dialog = new NewCompileSetting("c",new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    public static String getChooseDirectory() {
        return getChooseDirectory("a");
    }

    public static String getChooseDirectory(String lan) {

        try {
            String dirName;
            while (true) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showDialog(new JLabel(), "保存");
                File file = fileChooser.getSelectedFile();
                dirName = file.toString();
                if (lan.equals("c") && dirName.contains(" ")) {
                    JOptionPane.showConfirmDialog(fileChooser,
                            "MinGW璺緞涓嶅緱鍖呭惈绌烘牸锛岃閲嶆柊閫夋嫨", "娴嬭瘯缁撴灉", //ToDo
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    break;
                }
            }
            return dirName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    //查找编译器
    public void initRegistryMessage() {
        initCRegistryMessage();
        initJavaRegistryMessage();
        initBin();
    }

    private HashMap<String, String> CRegistryMessage = new HashMap<>();
    private HashMap<String, String> JavaRegistryMessage = new HashMap<>();

    private void initCRegistryMessage() {
        try {
            CRegistryMessage.clear();
            //查找单独的mingw编译器
            List<String> messageList = execProc("reg query \"HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment\"");
            String s = "reg query \"HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment\"";
            for (int k = 0; k < messageList.size(); k++) {
                String tmp[] = messageList.get(k).trim().split("    ");
                if ("path".equals(tmp[0].toLowerCase())) {
                    //获取path路径值
                    tmp = tmp[2].split(";");
                    for (int l = 0; l < tmp.length; l++) {
                        if (tmp[l].toLowerCase().contains("mingw") && !CRegistryMessage.containsKey("MinGW")) {
                            CRegistryMessage.put("MinGW", tmp[l]);
                            break;
                        }
                    }
                    break;
                }
            }
            if (CRegistryMessage.containsKey("MinGW")) {
                messageList = execProc("reg query \"HKEY_CURRENT_USER\\Environment\"");
                for (int k = 0; k < messageList.size(); k++) {
                    String tmp[] = messageList.get(k).trim().split("    ");
                    if ("path".equals(tmp[0].toLowerCase())) {
                        //获取path路径值
                        tmp = tmp[2].split(";");
                        for (int l = 0; l < tmp.length; l++) {
                            if (tmp[l].toLowerCase().contains("mingw") && !CRegistryMessage.containsKey("MinGW")) {
                                CRegistryMessage.put("MinGW", tmp[l]);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            //获取devcpp内置
            CompilesInRegisty compilesInRegisty = new CompilesInRegisty();
            List<Compiles> devcppList = compilesInRegisty.getCompilePath("Dev-C++");
            if (!devcppList.isEmpty()) {
                devcppList.stream().forEach((Compiles devcpp) -> {
                    CRegistryMessage.put(devcpp.getCompileNameString() + "(" + devcpp.getVersionString() + ")", devcpp.getPathString());
                });
            }
            //获取codeblocks内置
            List<Compiles> codeBlocksList = compilesInRegisty.getCompilePath("CodeBlocks");
            if (!codeBlocksList.isEmpty()) {
                codeBlocksList.stream().forEach((Compiles codeBlocks) -> {
                    CRegistryMessage.put(codeBlocks.getCompileNameString() + "(" + codeBlocks.getVersionString() + ")", codeBlocks.getPathString());
                });
            }
        } catch (Exception e) {
            Log.writeExceptionLog(e.getClass() + e.getMessage());
            // System.out.println("CRegistryError at CONFIG:" + e.getMessage());
        } finally {
            System.out.println("CRegistryMessage:" + CRegistryMessage.size());
        }
    }

    private void initJavaRegistryMessage() {
        try {
            JavaRegistryMessage.clear();
            //查找 32位 jdk
            CompilesInRegisty compilesInRegisty = new CompilesInRegisty();
            List<Compiles> jdk32List = compilesInRegisty.getCompilePath("Java SE");
            for (Compiles jdk : jdk32List) {
                String jdkPathString = jdk.getPathString();
                if (jdkPathString.contains("jdk")) {
                    if (!JavaRegistryMessage.containsValue(jdkPathString)) {
                        JavaRegistryMessage.put("JDK" + jdk.getVersionString(), jdkPathString);
                    }
                }
            }
            System.out.println("");
        } catch (Exception ex) {
            Log.writeExceptionLog(ex.getClass() + ex.getMessage());
        } finally {
            System.out.println("JavaRegistryMessage:" + JavaRegistryMessage.size());
        }

    }

    //把收集到的两个CRegistryMessage和JavaRegistryMessage的目录判断并精确到bin目录
    private void initBin() {
        Iterator iter = CRegistryMessage.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            List<String> subs = FileFinder.getSubs(val);
            boolean has = false;
            for (int i = 0; i < subs.size(); i++) {
                if (subs.get(i).toLowerCase().contains("mingw")) {
                    //获取该mingw下的bin目录
                    String tmp = val + subs.get(i) + File.separator + "bin";
                    if (FileFinder.isExistFile(tmp + File.separator + "gcc.exe") && FileFinder.isExistFile(tmp + File.separator + "g++.exe")) {
                        CRegistryMessage.replace(key, val, tmp);
                        has = true;
                        break;
                    }
                }
            }
            if (!has) {
                CRegistryMessage.remove(key);
            }
        }
        iter = JavaRegistryMessage.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            JavaRegistryMessage.replace(key, val, val + "bin");
        }
//        System.out.println("Registry Find End...");
    }

    private List<String> execProc(String Command) {
        List<String> messageList = new LinkedList<String>();
        try {
            Process ps = null;
            ps = Runtime.getRuntime().exec(Command);
            ps.getOutputStream().close();
            InputStreamReader i = new InputStreamReader(ps.getInputStream());
            String line;
            BufferedReader ir = new BufferedReader(i);
            while ((line = ir.readLine()) != null) {
//                System.out.println(line);
                messageList.add(line);
            }
            ir.close();
            i.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.writeExceptionLog(e.getClass() + e.getMessage());
        } finally {

        }
        return messageList;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JTextField cDir;
    private javax.swing.JPanel cJPanel;
    private javax.swing.JRadioButton cRegistry;
    private javax.swing.JComboBox<String> cRegistryList;
    private javax.swing.JRadioButton cSelf;
    private javax.swing.JRadioButton cSystem;
    private javax.swing.JTextField cppDir;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField javaDir;
    private javax.swing.JPanel javaJPanel;
    private javax.swing.JRadioButton javaRegistry;
    private javax.swing.JComboBox<String> javaRegistryList;
    private javax.swing.JRadioButton javaSelf;
    private javax.swing.JRadioButton javaSystem;
    private javax.swing.JLabel lblCRegistryMessage;
    private javax.swing.JLabel lblJavaRegistryMessage;
    // End of variables declaration//GEN-END:variables
}
