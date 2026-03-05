package kernel;

import MyCache.Shared;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WriteInfo extends Thread {

    private InputStream is;
    private String info;
    public int exitFlag = 0;
    Thread thread = new Thread(this);

    public WriteInfo(InputStream is) {
        this.is = is;

    }

    @Override
    public void run() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        char[] s = new char[512];//Jared:疑似内存泄漏
        //int c=-1;
        int len = -1;

        StringBuffer buffer = new StringBuffer();
        try {
            while (((len = reader.read(s, 0, 512)) != -1)) {
                if (buffer.length() < 2*Shared.maxOutputLength) { //由于\r的存在，使得输出会变大，但不可能超出两倍的maxoutputlength
                    buffer.append(s, 0, len);
                } 
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        if(Config.DEBUG){
//            System.out.println("output length:"+buffer.length());
//        }
        info = buffer.toString().replace("\r\n", "\n");
        if (info.length() > Shared.maxOutputLength) {
           info = info.substring(0, Shared.maxOutputLength) + "(被截断,还有更多输出)";
        }

        try {
            if (is != null) {
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String returnInfo() {
        return this.info;
    }

}
