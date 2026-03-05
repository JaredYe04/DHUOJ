/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjdom;

import java.util.Date;
import java.util.List;
import myjdom.model.ResultBean;
import org.w3c.dom.Element;
import persistence.oj_beans.ProblemTestCaseBean;

/**
 *
 * @author ytxlo
 */
public class AnswerToXml extends XmlToBase implements XmlConvert<String> {

    private ResultBean result;

    public AnswerToXml(ResultBean result) {
        super();
        this.result = result;
    }

    @Override
    public String convertXML() throws Exception {
        this.createDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);
        Date date = new Date();
//        root.addElement("time").addText(date.toString());
        Element solution = doc.createElement("solution");
        root.appendChild(solution);
//        solution.addElement("language").addText(getResult().getLanguage());
//        solution.addElement("sourceCode").addText(getResult().getSourceCode());
        Element solutionId = doc.createElement("solutionId");
        solution.appendChild(solutionId);
        solutionId.setTextContent(getResult().getSolutionId());

        Element problemId = doc.createElement("problemId");
        solution.appendChild(problemId);
        problemId.setTextContent(getResult().getProblemId());

        Element status = doc.createElement("status");
        solution.appendChild(status);
        status.setTextContent(getResult().getStatus());

        Element correctCaseIds = doc.createElement("correctCaseIds");
        solution.appendChild(correctCaseIds);
        correctCaseIds.setTextContent(getResult().getCorrectCaseIds());
        
         Element remark = doc.createElement("remark");
        solution.appendChild(remark);
        remark.setTextContent(common.EscapeXml.escapeXml(getResult().getRemark()));

        Element wrongCases = doc.createElement("wrongCases");
        solution.appendChild(wrongCases);
        List<ProblemTestCaseBean> wrongList = getResult().getWrongCase();
        for (int i = 0; i < wrongList.size(); i++) {
            Element tcase = doc.createElement("case");
            wrongCases.appendChild(tcase);
            Element caseId = doc.createElement("caseId");
            tcase.appendChild(caseId);
            caseId.setTextContent(String.valueOf(wrongList.get(i).getId()));
            Element output = doc.createElement("output");
            tcase.appendChild(output);
            output.setTextContent(common.EscapeXml.escapeXml(wrongList.get(i).getOutput()));
        }

        return XmlUtil.DocumentToString(doc, "GBK");
    }

    /**
     * @return the result
     */
    public ResultBean getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(ResultBean result) {
        this.result = result;
    }

}
