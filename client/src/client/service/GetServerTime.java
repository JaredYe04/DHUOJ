/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.management.timer.Timer.ONE_SECOND;
import client.io.xml.Exam_io;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author ytxlo
 */
public class GetServerTime {

    private static Date serverTime;
    private static Date refreshTime;
    private String stime;

    public GetServerTime() {
    }

    public void getTime() {
        Exam_io exo = new Exam_io();
        stime = exo.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            serverTime = sdf.parse(stime);
            refreshTime = sdf.parse(stime);
        } catch (ParseException ex) {
            Logger.getLogger(GetServerTime.class.getName()).log(Level.SEVERE, null, ex);
        }
        configTimeArea();
    }

    public static void setServerTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            serverTime = sdf.parse(time);
        } catch (ParseException ex) {
            Logger.getLogger(GetServerTime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void configTimeArea() {
        new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverTime.setTime(serverTime.getTime() + 1000);
                if (serverTime.getTime() - refreshTime.getTime() > 900000) { 
                    /*
                更新考试信息
                     */
                    new DoBackground().compareUpdateTime();
                    System.out.println("done");
                    refreshTime.setTime(serverTime.getTime());
                }
            }
        }).start();
    }


    public static Date getNowTime() {
        return serverTime;
    }
}
