/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.io.xml;

import client.model.Exam;
import client.model.ExamProblem;
import client.model.Problem;
import java.util.*;

/**
 *
 * @author ytxlo
 */
public class Read_xml {

    private static List<Exam> examl;
    private static List<Problem> probleml;

    public static void XMLTobean() {
        Exam_io e = new Exam_io();
        examl = e.getexamlist();

    }

    public static List<Exam> getExamlist() {
        return examl;
    }

    public static List<Problem> getProblemList(String examId) {
        try {
            // for debug
            System.out.println("getProblemList");
            ExamProblem_io ep = new ExamProblem_io(examId);
            List<ExamProblem> epl = ep.getProblemList();
            List<Problem> prol = new ArrayList();
            for (int i = 0; i < epl.size(); i++) {
                Problem pro = new Problem_io(epl.get(i).getProblemId()).getproblem();
                prol.add(pro);
            }
            probleml = prol;
        } catch (Exception e) {
        }
        return probleml;
    }

    public static void setProblemList(List<Problem> prol) {
        for (int i = 0; i < prol.size(); i++) {
            new Problem_io(prol.get(i).getId()).setProblemDesc(prol.get(i));
        }
    }
}
