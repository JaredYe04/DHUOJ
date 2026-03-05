package edu.dhu.ws;

public interface OJWS {

	//测试
	public String test(String message);
	// 将登陆人的信息转换成xml格式
	public String wsLogin(String userName, String userPassword);

	// 将考试列表转换成xml格式
	public String wsGetExamList(String userName, String password);

	// 通过Id将考试列表转换成xml格式
	public String wsGetExamById(String userName, String password, int examId);

	// 根据problemId查出problems和problemtestcases的相关信息转换成xml格式
	public byte[] wsGetProblem(String userName, String password,int examId, int problemId);

	// 根据examId查出examProblems的相关信息转换成xml格式
	public String wsGetExamProblems(String userName, String password,
			int examId, int page, int rows);
	
	// 根据用户名，密码，examId获取examdetail的相关信息转换成xml格式
	public String wsGetExamDetail(String userName, String password, int examId, int page, int rows);

	// 根据用户提交的代码对WrongCases,Solution,Problems,StudentExamDetail的相关信息,返回solutionId
	public String wsSubmitCode(String userName, String password, String codeXml);

	// 提交本题操作
	public String wsSubmitThisProblem(String userName, String password,
			String codeXml);

	// 获取submittedcode的信息用来检查相似度
	public String wsSubmittedCode(String userName, String password,
			int problemId);

	// 查看测试数据,获取wrongcases的信息
	public String wsViewWrongCase(String userName, String password,
			int examId, int problemId, int caseId, boolean caseInfo);

	// 获取最新状态
	public String wsGetExamProblemStatus(String userName, String password,
			int examId, int problemId);

	// 裁判机获取solution等信息
	public String wsGetSolutions(String account,String password,int number);

	// 更新裁判后返回的结果
	public String wsUpdateResult(String userName, String password,byte[] result);

	// 是否允许换座
	public String wsIsPermit(String userName, String password, int examId,
			String UUID);
	
	// 裁判机获取题目信息
	public byte[] wsGetProblem4Judge(String userName, String password, int problemId);
	
	
	// 根据examId获取智能训练题目类别
	public String wsGetExamProCatagorys(String userName,String password,int examId);
	
	//智能训练页面点击"开始做题"操作
	public String wsCanDoCategory(String userName, String password, int examId);
	
	//智能训练页面点击"我要抽题"操作
	public byte[] wsDrawProblem(String userName, String password, int examId,int catId);
	
	//智能训练模块做题记录页面数据
	public String wsSubmitHistory(String userName, String password, int examId);
	
	//智能训练模块"提交代码"操作
	public String wsItrainSubmitCode(String userName, String password,int catId,String codeXml);
	
	//智能训练模块"暂时跳过本题"操作
	public String wsSkipThisProblem(String userName, String password, int examId,int catId,int problemId);
	
	//智能训练模块"提交本题"操作
	public String wsItrainSubmitThisProblem(String userName, String password,int catId,String codeXml,String continueTrain); 

	//智能训练模块"我要通关"操作
	public String wsPassThisCategory(String userName, String password, int examId,int catId);
}
