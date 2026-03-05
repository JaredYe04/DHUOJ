package client.view.panel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import client.model.Information;
import client.model.Problem;
import client.model.ProblemTestCase;
import client.model.StudentExamDetail;
import client.util.Control;
import client.util.Tips;
import client.view.frame.LoginFrame;
import common.LangSelector;
import main.Answer;
import main.Process;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import client.view.frame.MainFrame;
import client.service.myswingworker.MySwingWorker;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import persistence.oj_beans.ProblemTestCaseBean;
import client.io.xml.ProblemTestCase_io;
import client.io.xml.SolutionCode;
import client.io.xml.StudentExamDetail_io;
import client.io.xml.SubmitCode;
import client.io.xml.WrongCase;
import common.Config;
import common.FileFinder;

import java.awt.TrayIcon.MessageType;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import share.gui.NewCompileSetting;

public class CodePanel extends JPanel implements ActionListener {

    private boolean alreadyClicked = false;
    private Integer nowProblemId = 0;
    private String codingWay = "文本";
    private JScrollPane JSP_Code;
    private JToolBar jToolBar;
    private String language = "Cpp";
    private RSyntaxTextArea JEP_Code;
    private JButton JB_Test1;
    private JButton JB_Test2;
    private JButton JB_Submit;
    private JButton JB_Save;
    private int fontsize = 14;
    //Jtoolbar
    private JButton JB_Cut;
    private JButton JB_Copy;
    private JButton JB_Paste;
    private JButton JB_Undo;
    private JButton JB_Redo;
    private JButton JB_upFont;
    private JButton JB_downFont;

    private JComboBox JCB_Language;
    private JComboBox JCB_Coding;
    private JComboBox JCB_Compiler;
    private JComboBox JCB_PythonCompiler;
    private JComboBox JCB_JavaCompiler;
    private JEditorPane JEP_Tmep;

    private JPanel Top_code;
    private JTabbedPane Bottom_code;
    private JPanel Sample_io;
    private JPanel Sample_io_mid;
    private JPanel Mine_io;
    private JPanel Mine_io_mid;

    private JEditorPane JEP_Test_inpnt;
    private JEditorPane JEP_Test_output;
    private JEditorPane JEP_Sample_input;
    private JEditorPane JEP_Sample_output;
    private JEditorPane JEP_Mine_output;
    private JScrollPane JSP_Test_input;
    private JScrollPane JSP_Test_output;

    private JScrollPane JSP_Sample_input;
    private JScrollPane JSP_Sample_output;
    private JScrollPane JSP_Mine_output;
    private JSplitPane jSplitPane;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel compilerLabel; // 添加编译器标签

    private JPanel jpanel1;
    private JPanel jpanel2;
    private JPanel jpanel3;
    private JPanel jpanel4;
    private JPanel jpanel5;
    private JPanel JCY;

    private JPanel compilerPanel; // 用于容纳编译器标签和下拉框的面板
    private ButtonGroup JBG;

    private JToolBar TB_test;
    private JToolBar TB_sample;
    private String routing;
    private String toLoad;
    private String testCaseAns;
    private String testCaseAnsInAscii;
    private String ansInString;
    private String ansInAscii;
    public AnswerTablePanel ans;
    private Boolean isFirst;
    private float similarity;
    private String examId;
    private String isCheckSimilarity;
    private int row;
    String submitOnlyAC;
    private Long lefttime; // by san_san
    private String compiler = "";
    //private AnswerTablePanel answerPage; 

    public CodePanel(AnswerTablePanel ans, Long lefttime) {
        this.language = "c";
        this.ans = ans;
        this.lefttime = lefttime;
        initComponents();
        if (LoginFrame.getLogin() == false) {
            this.JB_Submit.setEnabled(false);
        }
    }

    public void setCode(String str) {
        this.JEP_Code.setText(str);
        JEP_Code.setCaretPosition(0);
    }

    private void initializeLanguageSelection() {
        List<String> availableLanguages = Control.getLanguages();
        if (availableLanguages.size() == 1) {
            this.JCB_Language.setModel(new javax.swing.DefaultComboBoxModel(availableLanguages.toArray()));
            setLanguage(availableLanguages.get(0));
        } else {
            List<String> list = new ArrayList<>(availableLanguages);
            this.JCB_Language.setModel(new javax.swing.DefaultComboBoxModel(list.toArray()));

            // 获取保存的语言ID
            String savedLanguageId = LangSelector.getLastLanguage();
            if (savedLanguageId != null) {
                // 遍历可用语言列表，检查是否有匹配的语言
                for (String availableLang : list) {
                    // 将可用语言转换为标准ID进行比较
                    String availableLangId = LangSelector.getLanguageIdFromAlias(availableLang);
                    if (savedLanguageId.equals(availableLangId)) {
                        // 找到匹配的语言，使用list中的原始形式（保持大小写等一致）
                        setLanguage(availableLang);
                        this.JCB_Language.setSelectedItem(availableLang);
                        return;
                    }
                }
            }

            // 如果没有找到匹配的语言，使用第一个可用的语言
            setLanguage(list.get(0));
        }
    }

    private void initComponents() {
        routing = "./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+String.valueOf(nowProblemId)+".xml";
        this.jToolBar = new JToolBar();
        this.JCB_Language = new JComboBox();
        this.JCB_Coding = new JComboBox();
        this.JSP_Code = new JScrollPane();
        this.JEP_Tmep = new JEditorPane();
        this.JB_Test1 = new JButton();
        this.JB_Test2 = new JButton();

        //工具栏
        this.JB_Submit = new JButton();
        this.JB_Cut = new JButton();
        this.JB_Copy = new JButton();
        this.JB_Redo = new JButton();
        this.JB_Undo = new JButton();
        this.JB_Paste = new JButton();
        this.JB_Save = new JButton();

        this.Top_code = new JPanel();
        this.Bottom_code = new JTabbedPane();
        this.Sample_io = new JPanel();
        this.Sample_io_mid = new JPanel();
        this.Mine_io = new JPanel();
        this.Mine_io_mid = new JPanel();
        this.JEP_Test_inpnt = new JEditorPane();
        this.JEP_Test_output = new JEditorPane();
        this.JEP_Sample_input = new JEditorPane();
        this.JEP_Sample_output = new JEditorPane();
        this.JEP_Mine_output = new JEditorPane();
        this.JB_upFont = new JButton();
        this.JB_downFont = new JButton();

        this.jLabel1 = new JLabel("测试输入");
        this.jLabel2 = new JLabel("测试输出");
        this.jLabel3 = new JLabel("测试用例输入");
        this.jLabel4 = new JLabel("测试用例输出");
        this.jLabel5 = new JLabel("学生测试输出");
        this.jLabel6 = new JLabel("运行结果:");
        this.jLabel7 = new JLabel("编程语言:");
        this.compilerLabel = new JLabel("编译器:");
        this.jpanel1 = new JPanel();
        this.jpanel2 = new JPanel();
        this.jpanel3 = new JPanel();
        this.jpanel4 = new JPanel();
        this.jpanel5 = new JPanel();
        this.compilerPanel = new JPanel();
        this.JCY = new JPanel();
        this.JBG = new ButtonGroup();
        this.jSplitPane = new JSplitPane();
        this.TB_test = new JToolBar();
        this.TB_sample = new JToolBar();
        this.JSP_Test_input = new JScrollPane();
        this.JSP_Test_output = new JScrollPane();
        this.JSP_Sample_input = new JScrollPane();
        this.JSP_Sample_output = new JScrollPane();
        this.JSP_Mine_output = new JScrollPane();
        
        // 编译器下拉框（MinGW、MSVC）by san_san
        String[] options = {"MinGW", "MSVC"};
        this.JCB_Compiler = new JComboBox<>(options);
        String savedCompiler = common.LangSelector.getDefaultCompiler("C++");
        if (savedCompiler != null && !savedCompiler.isEmpty()) {
            this.JCB_Compiler.setSelectedItem(savedCompiler);
        } else {
            this.JCB_Compiler.setSelectedItem("MinGW");
        }

        // 初始化Python编译器下拉框
        String[] pythonOptions = {"Python3.12"};
        this.JCB_PythonCompiler = new JComboBox<>(pythonOptions);
        this.JCB_PythonCompiler.setSelectedItem("Python3.12");
        this.JCB_PythonCompiler.setMaximumSize(new Dimension(100,100));
        this.JCB_PythonCompiler.setPreferredSize(new Dimension(100, jToolBar.getHeight()));

        // 初始化Java编译器下拉框
        String[] javaOptions = {"JDK8"};
        this.JCB_JavaCompiler = new JComboBox<>(javaOptions);
        this.JCB_JavaCompiler.setSelectedItem("JDK8");
        this.JCB_JavaCompiler.setMaximumSize(new Dimension(100,100));
        this.JCB_JavaCompiler.setPreferredSize(new Dimension(100, jToolBar.getHeight()));

        //代码文本区域
        this.JEP_Code = new RSyntaxTextArea(20, 60);
        JEP_Code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
        JEP_Code.setCodeFoldingEnabled(true);
        JEP_Code.setFont(new Font("YaHei Consolas Hybrid", Font.PLAIN, fontsize));
        RTextScrollPane codePanel = new RTextScrollPane(JEP_Code);
        

        this.Top_code.setLayout(new BorderLayout());
        this.jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        
        this.jSplitPane.setResizeWeight(0.8);
        this.jSplitPane.setDividerSize(7);
        this.jSplitPane.setOneTouchExpandable(true);
        this.isFirst = true;
        String recent;
        setLayout(new java.awt.BorderLayout());
        this.jToolBar.setFloatable(false);

        //jtoolBar相关button处理
        // 设置按钮的大小与图片大小一致
        Dimension d = new Dimension(108, 31);
        this.JB_Copy.setSize(d);
        this.JB_Copy.setMaximumSize(d);
        this.JB_Copy.setMinimumSize(d);

        // 设置按钮背景图像 例：复制 copy mix_copy edit_copy
        ImageIcon icon1 = new ImageIcon("./ButtonIconPicture/copy.png");
        ImageIcon icon2 = new ImageIcon("./ButtonIconPicture/mix_copy.png");
        ImageIcon icon3 = new ImageIcon("./ButtonIconPicture/cut.png");
        ImageIcon icon4 = new ImageIcon("./ButtonIconPicture/cut_red.png");
        ImageIcon icon5 = new ImageIcon("./ButtonIconPicture/undo.png");
        ImageIcon icon6 = new ImageIcon("./ButtonIconPicture/mix_undo.png");
        ImageIcon icon9 = new ImageIcon("./ButtonIconPicture/edit_undo.png");
        ImageIcon icon7 = new ImageIcon("./ButtonIconPicture/redo.png");
        ImageIcon icon8 = new ImageIcon("./ButtonIconPicture/mix_redo.png");
        ImageIcon icon10 = new ImageIcon("./ButtonIconPicture/edit_redo.png");
        ImageIcon icon11 = new ImageIcon("./ButtonIconPicture/edit_cut.png");
        ImageIcon icon12 = new ImageIcon("./ButtonIconPicture/paste.png");
        ImageIcon icon13 = new ImageIcon("./ButtonIconPicture/mix_paste.png");
        ImageIcon icon14 = new ImageIcon("./ButtonIconPicture/edit_paste.png");
        ImageIcon icon15 = new ImageIcon("./ButtonIconPicture/edit_copy.png");
        //ImageIcon icon16=new ImageIcon("src/ButtonIconPicture/sizeUp.png");
        ImageIcon icon17 = new ImageIcon("./ButtonIconPicture/mix_sizeup.png");
        ImageIcon icon18 = new ImageIcon("./ButtonIconPicture/edit_sizeup.png");
        //ImageIcon icon19=new ImageIcon("src/ButtonIconPicture/sizeUp.png");
        ImageIcon icon20 = new ImageIcon("./ButtonIconPicture/mix_sizedown.png");
        ImageIcon icon21 = new ImageIcon("./ButtonIconPicture/edit_sizedown.png");
        ImageIcon icon22 = new ImageIcon("./ButtonIconPicture/save.png");
        ImageIcon icon23 = new ImageIcon("./ButtonIconPicture/mix_save.png");
        ImageIcon icon24 = new ImageIcon("./ButtonIconPicture/edit_save.png");

        //工具栏按钮加工 
        makejToolButton(JB_Save, icon22, icon23, icon24, "本地保存");
        makejToolButton(JB_downFont, icon20, icon20, icon21, "缩减字体");
        makejToolButton(JB_upFont, icon17, icon17, icon18, "增大字体");
        makejToolButton(JB_Copy, icon1, icon2, icon15, "复制");
        makejToolButton(JB_Cut, icon3, icon4, icon11, "剪切");
        makejToolButton(JB_Undo, icon5, icon6, icon9, "撤销");
        makejToolButton(JB_Redo, icon7, icon8, icon10, "恢复");
        makejToolButton(JB_Paste, icon12, icon13, icon14, "粘贴");

        //绑定事件
        this.JB_Cut.addActionListener(this);
        this.JB_Copy.addActionListener(this);
        this.JB_Paste.addActionListener(this);
        this.JB_Undo.addActionListener(this);
        this.JB_Redo.addActionListener(this);
        this.JB_upFont.addActionListener(this);
        this.JB_downFont.addActionListener(this);

        JCB_Language.setMaximumSize(new Dimension(300,100));
        JCB_Language.setPreferredSize(new Dimension(200, jToolBar.getHeight()));
        JCB_Compiler.setMaximumSize(new Dimension(100,100));
        JCB_Compiler.setPreferredSize(new Dimension(100, jToolBar.getHeight()));

        jToolBar.add(jLabel7);
        jToolBar.addSeparator(new Dimension(20, jToolBar.getHeight()));
        jToolBar.add(JCB_Language);
        jToolBar.addSeparator(new Dimension(10, jToolBar.getHeight()));

        //jToolBar布局 
        Container container = new Container();
        container.setLayout(new GridLayout(1, 20, 0, 0));

        jToolBar.add(JB_Submit);
        jToolBar.addSeparator(new Dimension(40, jToolBar.getHeight()));
        jToolBar.add(new JPanel());
        jToolBar.add(JB_Save);
        
        JPanel jp3 = new JPanel(new GridLayout(1, 2, 0, 0));
        jp3.add(this.JB_downFont);
        jp3.add(this.JB_upFont);
        jp3.add(this.JB_Cut);
        jp3.add(this.JB_Undo);
        jp3.add(this.JB_Redo);
        jp3.add(this.JB_Copy);
        jp3.add(this.JB_Paste);

        container.add(jp3);
        jToolBar.add(container);

//        Control.getLanguages();
//        String lastLanguage = common.LangSelector.getLastLanguage();
//        if (Control.getLanguages().size() == 1) {
//            this.JCB_Language.setModel(new javax.swing.DefaultComboBoxModel(Control.getLanguages().toArray()));
//            setLanguage(Control.getLanguages().get(0));
//        } else {
//            List<String> list = new ArrayList<>();
//            list.addAll(Control.getLanguages());
//            this.JCB_Language.setModel(new javax.swing.DefaultComboBoxModel(list.toArray()));
//            setLanguage(Control.getLanguages().get(0));
//        }
        initializeLanguageSelection();
        JCB_Compiler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCompiler = (String) JCB_Compiler.getSelectedItem();
                
                if (selectedCompiler == "MSVC"){
                   try {
                        System.out.println("配置文件:" + common.LangSelector.getCompilerPath("C++", "MSVC"));
                        if (!new File(common.LangSelector.getCompilerPath("C++", "MSVC")).exists()) {//如果MSVC路径不存在
                            String clPath = common.VSWhereUtil.findClExePath();
                            System.out.println(clPath);
                            if (clPath != null) {
                                common.LangSelector.setCompilerPath("C++", "MSVC", clPath);
                                common.LangSelector.setCompileCommand("C++", "MSVC", common.VSWhereUtil.getMsvcCompilerCommand());
                                common.LangSelector.setLinkCommand("C++", "MSVC", common.VSWhereUtil.getMsvcLinkCommand());
                                common.LangSelector.save(common.LangSelector.getConfigPath());
                                setCompiler("MSVC");
                                saveCompilerSelection("MSVC");
                            } else {
                                JOptionPane.showMessageDialog(null, "未安装Visual Studio，无法选择MSVC");
                                setCompiler("MinGW");
                                //电脑上没有vs，向用户提示报错，无法选择msvc
                                JCB_Compiler.setSelectedItem("MinGW");
                                saveCompilerSelection("MinGW");
                            }
                        } else {
                            //cl.exe已找到！无需额外操作
                            setCompiler("MSVC");
                            saveCompilerSelection("MSVC");
                        }
                      } catch (Exception ex) {
                          ex.printStackTrace();
                      }
                } else {
                   setCompiler("MinGW");
                   saveCompilerSelection("MinGW");
                }         
            }
        });
        
        this.JCB_Language.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedLanguage = e.getItem().toString();
                    setLanguage(selectedLanguage);
                }
            }
        });
        //this.JCB_Language.setSelectedIndex(1);
        this.JCB_Coding.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"文本", "ASCII码"}));
        this.JCB_Coding.addActionListener((ActionEvent evt) -> {
            CodePanel.this.JCB_CodingActionPerformed(evt);
        });
//                this.jToolBar.add(this.jLabel7);
//		this.jToolBar.add(this.JCB_Language);
//		
//                this.jToolBar.add(this.JB_Save);
        add(this.jToolBar, "North");

        this.JSP_Code.setViewportView(this.JEP_Tmep);

        this.Top_code.add(this.JSP_Code, "Center");

        this.jSplitPane.setTopComponent(codePanel);
        this.TB_test.setFloatable(false);
        this.JB_Test1.setText("测试以下数据");
        //setPre(true);
        this.JB_Test1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerformed_myTest(e);
            }
        });

        this.JB_Submit.setText("  提交代码  ");
        this.JB_Submit.setSize(10, 10);
        this.JB_Submit.setBackground(Color.GREEN);
        this.JB_Submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerformed_submitCode(e);
            }
        });

        this.JB_Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //setPre(false);
                CodePanel.this.toLoad = CodePanel.this.JEP_Code.getText();
                SolutionCode toSave = new SolutionCode();

                if (toSave.init() && toSave.updateXml(routing, CodePanel.this.JEP_Code.getText())) {

                    JOptionPane.showMessageDialog(JEP_Code, "保存成功");
                } else {
                    JOptionPane.showMessageDialog(JEP_Code, "保存失败,请选择一个题目");
                }
                // toSave.updateXml(route, "sssss", "Submit");
            }
        });

        this.TB_test.add(this.JB_Test1);
        this.Sample_io.setLayout(new BorderLayout());
        this.Sample_io_mid.setLayout(new GridLayout(1, 2, 10, 10));
        this.jpanel1.setLayout(new BorderLayout());
        this.jpanel2.setLayout(new BorderLayout());

        this.jpanel1.add(this.jLabel1, BorderLayout.NORTH);
        this.JSP_Test_input.setViewportView(this.JEP_Test_inpnt);
        this.jpanel1.add(this.JSP_Test_input, BorderLayout.CENTER);

        this.jpanel2.add(this.jLabel2, BorderLayout.NORTH);
        this.JSP_Test_output.setViewportView(this.JEP_Test_output);
        this.jpanel2.add(this.JSP_Test_output, BorderLayout.CENTER);

        this.Sample_io_mid.add(this.jpanel1);
        this.Sample_io_mid.add(this.jpanel2);
        this.Sample_io.add(this.TB_test, BorderLayout.NORTH);
        this.Sample_io.add(this.Sample_io_mid, BorderLayout.CENTER);

        this.Mine_io.setLayout(new BorderLayout());
        this.TB_sample.setFloatable(false);
        this.JB_Test2.setText("测试以下数据");
        this.JB_Test2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerformed_sampleTest(e);
            }
        });

        this.TB_sample.add(this.JB_Test2);

        this.TB_sample.add(this.jLabel6);
        //this.TB_sample.add(this,jLabel7);
        addRadioButton();
        this.TB_sample.add(this.JCY);

        this.Mine_io.add(this.TB_sample, BorderLayout.NORTH);
        this.Mine_io_mid.setLayout(new GridLayout(1, 3, 10, 10));
        this.jpanel3.setLayout(new BorderLayout());
        this.jpanel4.setLayout(new BorderLayout());
        this.jpanel5.setLayout(new BorderLayout());
        this.jpanel3.add(this.jLabel3, BorderLayout.NORTH);
        this.JSP_Sample_input.setViewportView(this.JEP_Sample_input);
        this.jpanel3.add(this.JSP_Sample_input, BorderLayout.CENTER);

        this.jpanel4.add(this.jLabel4, BorderLayout.NORTH);
        this.JSP_Sample_output.setViewportView(this.JEP_Sample_output);
        this.jpanel4.add(this.JSP_Sample_output, BorderLayout.CENTER);

        this.jpanel5.add(this.jLabel5, BorderLayout.NORTH);
        this.JSP_Mine_output.setViewportView(this.JEP_Mine_output);
        this.jpanel5.add(this.JSP_Mine_output, BorderLayout.CENTER);
        this.Mine_io_mid.add(this.jpanel3);
        this.Mine_io_mid.add(this.jpanel4);
        this.Mine_io_mid.add(this.jpanel5);
        this.Mine_io.add(this.Mine_io_mid, BorderLayout.CENTER);
        this.Bottom_code.addTab("用范例测试", this.Mine_io);
        this.Bottom_code.addTab("输入数据测试", this.Sample_io);
        this.jSplitPane.setBottomComponent(this.Bottom_code);
        add(this.jSplitPane, BorderLayout.CENTER);
    }

    //工具栏button功能
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == JB_Cut) {
            JEP_Code.cut();
        } else if (e.getSource() == JB_Copy) {
            JEP_Code.copy();
        } else if (e.getSource() == JB_Paste) {
            JEP_Code.paste();
        } else if (e.getSource() == JB_Undo) {
            JEP_Code.undoLastAction();
        } else if (e.getSource() == JB_Redo) {
            JEP_Code.redoLastAction();
        } else if (e.getSource() == JB_upFont) {
            JEP_Code.setFont(new Font("YaHei Consolas Hybrid", Font.PLAIN, ++fontsize));
        } else if (e.getSource() == JB_downFont) {
            JEP_Code.setFont(new Font("YaHei Consolas Hybrid", Font.PLAIN, --fontsize));
        }
    }

    private void setLanguage(String lan) {
        if ("Java".equals(lan)) {
            language = "Java";
            JEP_Code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);

            removeAllCompilers();
            if (jToolBar.getComponentIndex(JCB_JavaCompiler) == -1) {
                jToolBar.add(JCB_JavaCompiler, jToolBar.getComponentIndex(JB_Submit));
                jToolBar.add(compilerLabel, jToolBar.getComponentIndex(JCB_JavaCompiler));
            }
        } else if ("C".equals(lan)) {
            language = "C";
            JEP_Code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);

            removeAllCompilers();
            if (jToolBar.getComponentIndex(JCB_Compiler) == -1) {
                jToolBar.add(JCB_Compiler, jToolBar.getComponentIndex(JB_Submit));
                jToolBar.add(compilerLabel, jToolBar.getComponentIndex(JCB_Compiler));
            }
        } else if ("C++".equals(lan)) {
            language = "C++";
            JEP_Code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);

            removeAllCompilers();
            if (jToolBar.getComponentIndex(JCB_Compiler) == -1) {
                jToolBar.add(JCB_Compiler, jToolBar.getComponentIndex(JB_Submit));
                jToolBar.add(compilerLabel, jToolBar.getComponentIndex(JCB_Compiler));
            }
        } else if ("Python".equals(lan)){
            language = "python";
            JEP_Code.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);

            removeAllCompilers();
            if (jToolBar.getComponentIndex(JCB_PythonCompiler) == -1) {
                jToolBar.add(JCB_PythonCompiler, jToolBar.getComponentIndex(JB_Submit));
                jToolBar.add(compilerLabel, jToolBar.getComponentIndex(JCB_PythonCompiler));
            }
        }
        // 保存语言选择
        common.LangSelector.setLastLanguage(lan);

        jToolBar.revalidate(); // 更新工具栏
        jToolBar.repaint(); // 重绘工具栏
    }

    private void removeAllCompilers() {
        // 移除C/C++编译器下拉框
        if (jToolBar.getComponentIndex(JCB_Compiler) != -1) {
            jToolBar.remove(JCB_Compiler);
            jToolBar.remove(compilerLabel);
        }

        // 移除Python编译器下拉框
        if (jToolBar.getComponentIndex(JCB_PythonCompiler) != -1) {
            jToolBar.remove(JCB_PythonCompiler);
            jToolBar.remove(compilerLabel);
        }

        // 移除Java编译器下拉框
        if (jToolBar.getComponentIndex(JCB_JavaCompiler) != -1) {
            jToolBar.remove(JCB_JavaCompiler);
            jToolBar.remove(compilerLabel);
        }
    }
    
    // 设置时间 by san_san
    public void setLefttime(Long lefttime){
        this.lefttime = lefttime;
    }
    
    // 保存编译器设置 by san_san
    public void setCompiler(String compiler){
        this.compiler = compiler;
    }
    
    // 保存编译器选择至配置文件 by san_san
    private void saveCompilerSelection(String compiler) {
        try {
            common.LangSelector.setDefaultCompiler("C++", compiler);
            common.LangSelector.save(common.LangSelector.getConfigPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean submitService() {
        try {
            int submitProblemId = nowProblemId;
            new Timers(15);
            SolutionCode toSave = new SolutionCode();
            toSave.init();
            File tmpfile = new File(routing);
            List<ProblemTestCaseBean> testCaseBeans = new ArrayList<>();
            List<ProblemTestCase> testCaseBeansTmp = new ProblemTestCase_io(String.valueOf(submitProblemId)).getProblemTestCaselist();
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
            String codeString = CodePanel.this.JEP_Code.getText();
//            System.out.println("ssssss---" + codeString);
            String regex = "[\\x00-\\x08]|[\\x0b-\\x0c]|[\\x0e-\\x1f]|[\\x7f-\\x84]|[\\x86-\\x9f]";
            codeString = codeString.replaceAll(regex, ""); //repalceAll和replace 区别在于前者用于正则表达式，后者用于普通字符串
            CodePanel.this.toLoad = codeString;
//            System.out.println("-----" + codeString);
            Float time_limit = Float.parseFloat(Control.getMainFrame().getInformation(String.valueOf(submitProblemId)).getTime_limit());
             
            //////////todo:自主选择编译器
            //String compiler="";
            System.out.println("调试CodePanel:"+language+";\n"+compiler+";\n"+codeString);



            String selectedCompiler = "";
            if ("C++".equals(language) || "C".equals(language)) {
                selectedCompiler = (String) JCB_Compiler.getSelectedItem();
            } else if ("python".equals(language)) {
                selectedCompiler = (String) JCB_PythonCompiler.getSelectedItem();
            } else if ("Java".equals(language)) {
                selectedCompiler = (String) JCB_JavaCompiler.getSelectedItem();
            }
            this.compiler = selectedCompiler;


            System.out.println("language" + language);
            System.out.println("compiler" + compiler);
            Answer answer = new Process().Judge(language,compiler, codeString, time_limit+2, testCaseBeans); //loss of timelimit
            if (answer.getStatus().equals("NF")) {
                JOptionPane.showMessageDialog(CodePanel.this,
                        "编译器未找到，请检查编译器设置", "提交失败", JOptionPane.ERROR_MESSAGE
                );
            } else if (answer.getStatus().equals("SE")) {
                JOptionPane.showMessageDialog(this, "系统错误，请重试", "提交失败", JOptionPane.ERROR_MESSAGE);
            } else {
                SubmitCode submit = new SubmitCode();
                submit.init();
                toSave.updateXml(routing, codeString);
                String codeXml = submit.SubmitCode(examId, String.valueOf(submitProblemId), language, codeString, answer);
//                System.out.println(codeXml);
                String username = Control.getUser().getUserName();
                String passwd = Control.getUser().getPassword();
//                System.out.println("codeXml:\n" + codeXml);
                
                String userId = Control.getUser().getId();
                String examId = Control.getExamId();
                
                String toWrite = Control.getWebsService().submitCode(username, passwd, codeXml, examId);
                String backFile = "./xml/" + Control.getPath() + "/afterSubmitCode.xml";
                File tmpFile = new File(backFile);
                if (tmpFile.exists()) {
                    tmpFile.delete();
                }
                TextToFile(backFile, toWrite);
                if (submit.isSucceed(backFile).equals("true")) {
                    String Id = submit.getSolutionId(backFile);
                    String score = submit.getScore(backFile);
                    String time = submit.getSubmitTime(backFile);
                    int submitTimes = Integer.parseInt(toSave.getSubmitTimes(routing));
                    toSave.updateXml(routing, time, Id, answer, score, codeString, submitTimes + 1);
                    toSave.delSave(routing);
                    Boolean checkornot;
                    checkornot = isCheckSimilarity.equals("true");
                    if (submitProblemId == nowProblemId) {
                        ans.renew(answer, submitTimes + 1, testCaseIn, testCaseOut, score, "" + submitProblemId, examId, submitOnlyAC.equals("true"), checkornot, row);
                    }
                    HashMap<String, StudentExamDetail> hash = new HashMap<>();
                    List<StudentExamDetail> sedlist = new StudentExamDetail_io(examId).getstudentExamDetaillist();
                    for (int i = 0; i < sedlist.size(); i++) {
                        hash.put(sedlist.get(i).getProblemId(), sedlist.get(i));
                    }
                    if (hash.containsKey(String.valueOf(submitProblemId))) {
                        StudentExamDetail_io student = new StudentExamDetail_io(examId);
                        student.changeStatus(String.valueOf(submitProblemId), answer.getStatus(), String.valueOf(toSave.isFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+submitProblemId+".xml")), toSave.getId("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+submitProblemId+".xml"));
                    } else {
                        //StudentExamDetail(String id,String examId,String problemId,String submit,String status,String hintCases,String score,String elapsedTime,String finished,String SolutionId)
                        StudentExamDetail sed = new StudentExamDetail("1", examId, "" + submitProblemId, "", toSave.getStatus("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+submitProblemId+".xml"), "1", "1", "1", "1", "1");
                        new StudentExamDetail_io(examId).add(sed);
                        StudentExamDetail_io student = new StudentExamDetail_io(examId);
                        //todo 不能使用submitProblemId
                        student.changeStatus(String.valueOf(submitProblemId), answer.getStatus(), String.valueOf(toSave.isFinished("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+submitProblemId+".xml")), toSave.getId("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+submitProblemId+".xml"));
                    }

//                Control.getMainFrame().setProblemlist(true);
                    String message = "提交成功。\n可至“提交结果”页面查看详细信息";
                    Control.getMainFrame().setProblemlist(true);
                    JOptionPane.showConfirmDialog(CodePanel.this,
                            message, "提示",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    if (answer.getStatus().equals("AC")) {
                        String mes = "题号:" + String.valueOf(row + 1) + "\n" + "本题已AC，是否提交本题？\n" + "提交本题后才能将本题得分计入成绩，但一旦提交本题，不可再对本题提交代码\n你确认要自动提交本题吗？";
                        int selection = JOptionPane.showConfirmDialog(CodePanel.this,
                                mes, "提示",
                                JOptionPane.OK_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (selection == JOptionPane.OK_OPTION) {
                            ans.submitProblem();
                        }
                    }
                    Control.getMainFrame().collection(row);
                    File tmp = new File("./xml/" + Control.getPath() + "/tmp.xml");
                    tmp.delete();
                } else {
                    String message = submit.isSucceed(backFile);
                    JOptionPane.showConfirmDialog(CodePanel.this,
                            message, "提示",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                }
                if (tmpFile.exists()) {
                    tmpFile.delete();
                }
                //ans.setVisible(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;
    }

    public void submitDone() {
        alreadyClicked = false;//恢复按键可点状态
        Control.getJpb().setIndeterminate(false);
        Control.getJpb_message().setText("");
    }

    private void actionPerformed_submitCode(ActionEvent e) {
        // by san_san
        if (this.lefttime <= 0){
            JOptionPane.showMessageDialog(CodePanel.this,
                    "考试时间已到，无法再提交代码", "提交失败", JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (!checkForCompile()) {
            return;
        }
        //checkForCompile();
        Control.getJpb().setIndeterminate(true);
        Control.getJpb_message().setText("正在提交");
        MySwingWorker my = new MySwingWorker(this::submitService, this::submitDone);
        my.execute();
    }

    public boolean myTest() {
        JEP_Test_output.setText("判定中");
        Float to = 1.0f;
//                        String out = new String();
        ////System.out.println(language);
        String out = new Process().GetOutput(language, JEP_Code.getText(), to, JEP_Test_inpnt.getText());
//            System.out.println(JEP_Code.getText());
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//            System.out.println(language+"::"+JEP_Code.getText()+"-->"+JEP_Test_inpnt.getText());
//            System.out.println("============================================");
//            System.out.println(out);
        String message;
        message = "运行完成";
        if (out.equals("RE")) {
            out = getTips(out);
        } else if (out.equals("TLE")) {
            out = getTips(out);
        } else if (out.equals("编译器未找到，请检查编译器设置")) {
            message = "编译器未找到，请检查编译器设置";
        }
        JEP_Test_output.setText(out);

        JOptionPane.showConfirmDialog(CodePanel.this,
                message, "测试结果",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );
        return true;
    }

    public void myTestDone() {
        alreadyClicked = false;//恢复按键可点状态
        Control.getJpb().setIndeterminate(false);
        Control.getJpb_message().setText("");
    }

    private boolean checkConditions() {
        //没有选择题目不允许测试
        if (nowProblemId == 0 || nowProblemId == null) {
            JOptionPane.showMessageDialog(this, "请先选择一个题目");
            return false;
        }
        //判断用户已经输入代码
        String strResult = this.JEP_Code.getText().replace("\n", "").replace(" ", "").replace("\t", "").replace("\r", "");
        if (strResult == "" || strResult.isEmpty()) {
            JOptionPane.showMessageDialog(this, "代码不能为空");
            return false;
        }
        //防止用户过快的点击
        if (alreadyClicked) {
            JOptionPane.showMessageDialog(this, "请不要重复点击");
            return false;
        } else {
            alreadyClicked = true; //按键变成已点击状态
        }
        if (JCB_Language.getSelectedItem().toString().equals("请选择")) {
            JOptionPane.showMessageDialog(this, "请选择编程语言");
            alreadyClicked = false;
            return false;
        }
        if (!checkForCompile()) {
            alreadyClicked = false;
            return false;
        }

        return true;
    }

    private void actionPerformed_myTest(ActionEvent e) {
        if (!checkConditions()) {
            return;
        }
        Control.getJpb().setIndeterminate(true);
        Control.getJpb_message().setText("正在测试");
        MySwingWorker my = new MySwingWorker(this::myTest, this::myTestDone);
        my.execute();

    }

    public boolean sampleTest() {
        JEP_Mine_output.setText("  ");
        Float to = Float.parseFloat(Control.getMainFrame().getInformation(String.valueOf(nowProblemId)).getTime_limit());
        String[] out = new String[1];
        Answer outAll = null;
        //System.out.println(language);

        if (CodePanel.this.getCodingWay().equals("ASCII码")) {
            JEP_Sample_output.setText(CodePanel.this.testCaseAnsInAscii);
        } //测试范例取

        List<ProblemTestCaseBean> testCaseBeans = new ArrayList<ProblemTestCaseBean>();
        ProblemTestCaseBean temp = new ProblemTestCaseBean();
        temp.setInput(JEP_Sample_input.getText());
        temp.setOutput(testCaseAns);
        testCaseBeans.add(temp);
        //System.out.println(language+JEP_Code.getText()+to+testCaseBeans);
           
        
        String compiler=this.compiler;
        outAll = new Process().Judge(language, compiler, JEP_Code.getText(), to+2, testCaseBeans);

        //System.out.println(JEP_Code.getText());
        //System.out.println(outAll.getUsersOutput()[0]);
        if (outAll.getRemark().equals("编译器未找到，请检查编译器设置")) {
            JOptionPane.showConfirmDialog(CodePanel.this,
                    "编译器未找到，请检查编译器设置", "测试结果",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            String ans = outAll.getUsersOutput()[0];
            ansInString = ans;
            ansInAscii = stringToAscii(ansInString);
            if (CodePanel.this.getCodingWay().equals("ASCII码")) {
                ans = ansInAscii;
            }
            JEP_Mine_output.setText(ans);
            jLabel6.setText(" " + outAll.getStatus() + " ");
            String remark = outAll.getRemark();
            String message;
            if (outAll.getStatus() == "RE") {
                JEP_Mine_output.setText("测试范例运行时错误");
            } else if (outAll.getStatus() == "CE") {
            } else if (outAll.getStatus() == "TLE") {
                JEP_Mine_output.setText("测试范例运行超时");
            }
            message = getTips(outAll.getStatus());
            log.Log.writeInfo(outAll.getStatus());
            
            // 测试样例AC选择是否提交至服务器
            if ("AC".equals(outAll.getStatus())){
                message = "AC：用范例测试结果正确。\n是否提交本题到服务器？";
                int option = JOptionPane.showConfirmDialog(CodePanel.this,
                        message, "测试结果",
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.INFORMATION_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    actionPerformed_submitCode(null);
                }
            }
        }
        return true;
    }

    public void sampleTestDone() {
        //任务完成让按键可点
        alreadyClicked = false;

        Control.getJpb().setIndeterminate(false);
        Control.getJpb_message().setText("");
    }

    private void actionPerformed_sampleTest(ActionEvent e) {
        if (!checkConditions()) {
            return;
        }
        //设置UI
        Control.getJpb().setIndeterminate(true);
        Control.getJpb_message().setText("正在测试范例");
        //实际异步裁判
        MySwingWorker my = new MySwingWorker(this::sampleTest, this::sampleTestDone);
        my.execute();
    }

    private void JCB_LanguageActionPerformed(ActionEvent evt) {
        this.language = this.JCB_Language.getSelectedItem().toString();
        //this.JEP_Code.setLanguage(this.language);
    }

    /**
     * @see 工具栏按钮加工厂
     * @param icon 显示图片
     * @param mix_icon mouseEnter 图片
     * @param edit_icon mousePress 图片
     * @param text 提示文本
     * @param jb 所要加工button
     */
    public void makejToolButton(JButton jb, ImageIcon icon, ImageIcon mix_icon, ImageIcon edit_icon, String text) {
        jb.setIcon(icon);
        jb.setBorderPainted(false);
        jb.setOpaque(false);
        jb.setBorder(null);
        jb.setContentAreaFilled(false);
        jb.setFocusPainted(false);
        jb.setRolloverEnabled(true);
        jb.setToolTipText(text);
        jb.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (jb.isRolloverEnabled()) {
                    jb.setIcon(edit_icon);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (jb.isRolloverEnabled()) {
                    jb.setIcon(mix_icon);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (jb.isRolloverEnabled()) {
                    jb.setIcon(mix_icon);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (jb.isRolloverEnabled()) {
                    jb.setIcon(icon);
                }
            }
        });
    }

    public void setTimeCount(int secondRemain) {
        this.JB_Submit.setEnabled(false);
        String second = new String();
        second = "提交(" + String.valueOf(secondRemain) + "s)";
        CodePanel.this.JB_Submit.setText(second);
    }

    private void JCB_CodingActionPerformed(ActionEvent evt) {
        String recent = new String(this.codingWay);
        this.codingWay = this.JCB_Coding.getSelectedItem().toString();
        //System.out.println(this.codingWay);
        if (!(this.codingWay.equals(recent))) {
            if (this.codingWay.equals("ASCII码")) {
                this.JEP_Sample_output.setText(this.testCaseAnsInAscii);
                this.JEP_Mine_output.setText(this.ansInAscii);
                recent = "文本";
            } else {
                this.JEP_Sample_output.setText(this.testCaseAns);
                this.JEP_Mine_output.setText(this.ansInString);
                recent = "ASCII码";
            }
        }
    }

    public String getTips(String ans) {
        Tips tips = new Tips();
        return tips.getTips(ans);
    }

    public void setFreshCode(String proId) {
        SolutionCode sc = new SolutionCode();
        sc.init();
        toLoad = sc.getCode("./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+proId+".xml");
        this.JEP_Code.setText(toLoad);
        JEP_Code.setCaretPosition(0);
    }

    public void RunJudge(int submitTimes) {
        List<ProblemTestCase> testCaseBeansTmp = new ArrayList<ProblemTestCase>();
        List<ProblemTestCaseBean> testCaseBeans = new ArrayList<ProblemTestCaseBean>();
        testCaseBeansTmp = new ProblemTestCase_io(String.valueOf(nowProblemId)).getProblemTestCaselist();
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
        SolutionCode sl = new SolutionCode();
        sl.init();
        //sl.delSave(routing);
        List<WrongCase> wrongCases = sl.getWrongCases(routing);
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
            } else {
                testCaseIds[i] = testCaseBeansTmp.get(i).getId();
                userOutput[i] = "";
                statusofTestCase[i] = "AC";
            }
        }

        String score = String.valueOf(sl.getScore(routing));
        String status = sl.getStatus(routing);
        String remark = sl.getRemark(routing);
        String correctCaseIds = sl.getCorrectIds(routing, "String")[0];

        Answer answer = new Answer(testCaseIds, userOutput, statusofTestCase, status, remark, correctCaseIds);
        //Similarity s = sl.getSimi(routing);
        //Answer answer = new Process().Judge(language, JEP_Code.getText(), 1.0f, testCaseBeans); 
        //Answer answer = new Answer();
        Boolean checkornot;
        if (isCheckSimilarity.equals("true")) {
            checkornot = true;
        } else {
            checkornot = false;
        }
        ans.renew(answer, submitTimes, testCaseIn, testCaseOut, score, "" + nowProblemId, examId, submitOnlyAC.equals("true"), checkornot, row);

    }

    public static String stringToAscii(String value) {
        char[] values = value.toCharArray();
        String as = new String();
        for (char s : values) {
            String tmp = String.valueOf((int) s);
            if (tmp.equals("13")) {
                continue;
            } else if (tmp.equals("10")) {
                as += tmp;
                as += "\n";
            } else {
                as += String.valueOf((int) s);
                as += " ";
            }
        }
        //System.out.println(as);
        return as;
    }

    public static String asciiToString(String text) {
        StringBuilder sbu = new StringBuilder();
        String[] line = text.split("\n");
        for (String s : line) {
            String[] chars = s.split(" ");
            for (String char1 : chars) {
                sbu.append((char) Integer.parseInt(char1));
            }
        }
        return sbu.toString();
    }

    public void setNoSubmit() {
        this.JB_Submit.setEnabled(false);
    }

    public Boolean setSample(Problem proId, int papernum, String SubmitOnlyAC, int row) {
        try {
            if (!isFirst && Integer.parseInt(proId.getId()) != this.nowProblemId) {
                SolutionCode toSave = new SolutionCode();
                toSave.init();
                toLoad = toSave.getCode(routing);
//                        System.out.println("=============NaxStart==============");
//                        System.out.println("toLoad-->"+toLoad.replaceAll(" ", "").replaceAll("\n", ""));
//                        System.out.println("-----------------------------------");
//                        System.out.println("toSave-->"+this.JEP_Code.getText().replaceAll(" ", "").replaceAll("\n", ""));
//                        System.out.println("==============NaxEnd===============");
                String toLoadTMP = toLoad.replaceAll(" ", "").replaceAll("\n", "");
                String toSaveTMP = this.JEP_Code.getText().replaceAll(" ", "").replaceAll("\n", "");
                if (!toLoadTMP.equals(toSaveTMP)) {
                    int selection = JOptionPane.showConfirmDialog(CodePanel.this,
                            "您还未保存本题代码，是否保存？", "注意",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (selection == JOptionPane.YES_OPTION) {

                        toSave.updateXml(routing, this.JEP_Code.getText());
                    } else if (selection == JOptionPane.CANCEL_OPTION) {
                        return false;
                    }
                } else //the sub sequence of toLoad and toSave are same
                //then judge if the orignal texts are same
                if (!toLoad.equals(this.JEP_Code.getText())) {
                    //Auto save with no user confirm dialog
                    toSave.updateXml(routing, this.JEP_Code.getText());
                }
            }
            if (Integer.parseInt(proId.getId()) == this.nowProblemId) {
                return false;
            }
            isFirst = false;
            //this.JEP_Problem.setText(probleml.get(n).getDescription());
            this.row = row;
            this.nowProblemId = Integer.parseInt(proId.getId());
            this.isCheckSimilarity = proId.getCheckSimilarity();
            //System.out.println("check:"+this.isCheckSimilarity);
            this.similarity = Float.parseFloat(proId.getSimilarityThreshold());
            this.examId = String.valueOf(papernum);
            this.JEP_Sample_input.setText(proId.getSample_input());
            this.testCaseAns = proId.getSample_output();
            this.testCaseAnsInAscii = stringToAscii(testCaseAns);
            //System.out.println("传入:"+SubmitOnlyAC);
            this.submitOnlyAC = SubmitOnlyAC;
            this.ans.data(submitOnlyAC, isCheckSimilarity, String.valueOf(nowProblemId), this.examId, similarity);
            if (this.codingWay.equals("ASCII码")) {
                this.JEP_Sample_output.setText(testCaseAnsInAscii);
            } else {
                this.JEP_Sample_output.setText(testCaseAns);
            }
            //this.JEP_Problem.setCaretPosition(0);
            this.JEP_Sample_input.setCaretPosition(0);
            this.JEP_Sample_output.setCaretPosition(0);
            this.JEP_Mine_output.setText("");
            this.JEP_Test_output.setText("");
            ansInString = "";
            ansInAscii = "";
            //ans.getnew();
            routing = "./xml/"+Control.getPath()+"/"+Control.getExamId()+"-"+String.valueOf(nowProblemId)+".xml";
            this.JB_Submit.setEnabled(true);
            ans.submit.setEnabled(true);
            setPre(true);
            JEP_Code.setText(toLoad);

            JEP_Code.setCaretPosition(0);

            SolutionCode tmp = new SolutionCode();
            tmp.init();
            ans.renew();
            File tmpfile = new File(routing);
            if (!tmp.isEmpty(routing)) {
                int submitTimes = Integer.parseInt(tmp.getSubmitTimes(routing));
                RunJudge(submitTimes);
                if (tmp.isFinished(routing) || (LoginFrame.getLogin() == false)) {
                    this.JB_Submit.setEnabled(false);
                    ans.submit.setEnabled(false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void TextToFile(final String strFilename, final String strBuffer) {
        try {
            File fileText = new File(strFilename);
            FileWriter fileWriter = new FileWriter(fileText);
            fileWriter.write(strBuffer);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSubmit(Boolean enable) {
        this.JB_Submit.setEnabled(enable);
    }

    public void setPre(Boolean take) {

        SolutionCode tmp = new SolutionCode();
        tmp.init();
        if (new File(routing).exists() && take) {
            toLoad = tmp.getCode(routing);

            if (tmp.isFinished(routing) || (LoginFrame.getLogin() == false)) {
                this.JB_Submit.setEnabled(false);
                ans.submit.setEnabled(false);
            }
        } else {

            tmp.createXml(routing);
            tmp.updateXml(routing, "");
            toLoad = "";

        }
    }

    public String getCodingWay() {
        return codingWay;
    }

    public void setCodingWay(String codingWay) {
        this.codingWay = codingWay;
    }

    public void countEnd() {
        this.JB_Submit.setText("提交代码");
        this.JB_Submit.setEnabled(true);

    }

    private boolean checkForCompile() {
        return true;
    }

    public class Timers {

        int timeToCount;
        Timer timeR;
        long start;
        long end;
        public int remain;

        Timers(int time) {
            this.timeToCount = time;
            timeR = new Timer();
            start = System.currentTimeMillis();
            end = start + time * 1000;
            remain = time;
            timeR.schedule(new TimerTask() {

                public void run() {
                    long show = end - System.currentTimeMillis();
                    CodePanel.this.setTimeCount(timeToCount - 1);
                    timeToCount--;
                }
            }, 0, 1000);

            timeR.schedule(new TimerTask() {
                public void run() {
                    timeR.cancel();
                    CodePanel.this.countEnd();
                }
            }, new Date(end));
        }
    }

    private void addRadioButton() {
        JRadioButton buttona = new JRadioButton("ASCII码", false);
        JRadioButton buttont = new JRadioButton("文本", true);
        buttona.setActionCommand("ASCII码");
        buttont.setActionCommand("文本");
        JBG.add(buttona);
        JCY.add(buttona);
        JBG.add(buttont);
        JCY.add(buttont);

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String recent = new String(CodePanel.this.codingWay);
                String eActionCommand = e.getActionCommand();
                System.out.printf("e.getActionCommand() is %s\n", eActionCommand);
                CodePanel.this.codingWay = eActionCommand;
                if (!(CodePanel.this.codingWay.equals(recent))) {
                    if (CodePanel.this.codingWay.equals("ASCII码")) {
                        CodePanel.this.JEP_Sample_output.setText(CodePanel.this.testCaseAnsInAscii);
                        CodePanel.this.JEP_Mine_output.setText(CodePanel.this.ansInAscii);
                        recent = "文本";
                    } else {
                        CodePanel.this.JEP_Sample_output.setText(CodePanel.this.testCaseAns);
                        CodePanel.this.JEP_Mine_output.setText(CodePanel.this.ansInString);
                        recent = "ASCII码";
                    }
                }
            }
        };

        buttona.addActionListener(actionListener);
        buttont.addActionListener(actionListener);
    }

}
