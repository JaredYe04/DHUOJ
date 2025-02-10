/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.util.similarity;

import client.model.Information;
import client.util.Control;
import client.view.frame.LoginFrame;
import client.view.panel.CodePanel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 *
 * @author ëȪ
 */
public class check {

    float similarityThreshold;// = problem.getSimilarityThreshold();
    double theMaxSimilarity = 0;
    int theMaxSolutionId = 0;

    public Similarity check(float similarityThreshold, String sourceCode, String problemId) {
        this.similarityThreshold = similarityThreshold;
        String code1 = sourceCode;
//        Information info =  new Information();
        String username = Control.getUser().getUserName();
        String passwd = Control.getUser().getPassword();

        String toWrite = new String();
        try {
            toWrite = Control.getWebsService().submittedCode(username, passwd, Integer.parseInt(problemId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String route = "./xml/" + Control.getPath() + "/smilarity.xml";
        File writeToXml = new File(route);
        if (writeToXml.exists()) {
            writeToXml.delete();
        }
        TextToFile(route, toWrite);

        Similarity s;
        List<submittedCode> submittedcodeList = new Parsexml().getCode(route);
        if (submittedcodeList.get(0).getId().equals("-1")) {
            s = new Similarity("error", "", "");
        }
        for (int i = 0; i < submittedcodeList.size(); i++) {
            String code2 = submittedcodeList.get(i).getProcessedCode1();
            double similarity = SimilarityByLine.getSimilarity(code1, false, code2, true);
            Double similarityObj = similarity;
            Double theMaxSimilarityObj = theMaxSimilarity;
            if (theMaxSimilarityObj.compareTo(similarityObj) < 0) {
                theMaxSolutionId = Integer.parseInt(submittedcodeList.get(i).getSolutionId());
                theMaxSimilarity = similarity;
            }
        }
        //System.out.println(theMaxSimilarity);
        if (theMaxSimilarity > similarityThreshold) {
            //pMSimilarityObj.setOverSimilarity(true);
            s = new Similarity("true", String.valueOf(theMaxSimilarity), String.valueOf(theMaxSolutionId));
        } else {
            s = new Similarity("false", String.valueOf(theMaxSimilarity), String.valueOf(theMaxSolutionId));
        }
        if (writeToXml.exists()) {
            writeToXml.delete();
        }

        return s;

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
}
