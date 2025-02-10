package client.view.panel;

import client.service.GetServerTime;
import client.view.frame.LoginFrame;
import javax.swing.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;
import java.text.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimePanel extends JPanel{
	private JLabel JL_time;
	private JLabel JL_display;
	private JLabel JLabel1;
	private JLabel JLabel2;
	private JLabel JLabel3;
	private String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private String time;
      private Date endDate;
      private Long lefttime;
	private int ONE_SECOND = 1000;
	public TimePanel(String endtime){
            this.JLabel1 = new JLabel("");
            this.JLabel2 = new JLabel("");
            this.JLabel3 = new JLabel("");
            this.JL_time = new JLabel();
            this.JL_display = new JLabel();
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
            try {
                Date newDate = dateFormatter.parse(endtime);
                this.endDate = newDate;
            } catch (ParseException ex) {
                Logger.getLogger(TimePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(LoginFrame.getLogin()==true){
                configTimeArea();
            }
            else{
                this.JL_time.setText("");
            }
            //this.setLayout(new java.awt.GridLayout(1, 3));

            this.JL_display.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

            this.add(this.JL_display);
            //this.add(this.JLabel1);
            //this.add(this.JLabel2);
            //this.add(this.JLabel3);
	}
        
      public TimePanel(Long lefttime){
            this.JLabel1 = new JLabel("");
            this.JLabel2 = new JLabel("");
            this.JLabel3 = new JLabel("");
            this.JL_time = new JLabel();
            this.JL_display = new JLabel();
            this.lefttime = lefttime;
            if(LoginFrame.getLogin()==true){
                
                configTimeArea();
            }
            else{
                this.JL_time.setText("");
            }

            this.JL_display.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

            this.add(this.JL_display);
	}
                
	private void configTimeArea(){
		Timer Ter = new Timer();
		Ter.scheduleAtFixedRate(new JLabelTimerTask(),new Date(), ONE_SECOND);
	}
      
      // by san_san
      public void updateTimeDisplay(Long lefttime){
            this.lefttime = lefttime;
      }
        
	protected class JLabelTimerTask extends TimerTask{
            
            public void run() {
                if (lefttime <= 0){
                    time = "考试时间已到";
                }
                else{
                    long day = lefttime /(1000 * 86400);
                    long hour = (lefttime - day*(1000 * 86400))/(1000*3600);
                    long m=(lefttime - day*(1000 * 86400))/1000/60%60;//分
                    long s=(lefttime - day*(1000 * 86400))/1000%60;//秒
                    lefttime -= 1000;
                    time = "距结束："+day+"天  "+hour+":"+m+":"+s;
                }
                JL_display.setText(time);

            }
	}
}
