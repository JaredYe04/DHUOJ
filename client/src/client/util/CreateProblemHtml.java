/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ytxlo
 */
public class CreateProblemHtml {
    private String code;
    public CreateProblemHtml(String code) {
        this.code = code;
        
//        // for debug
//        System.out.println();
//        System.out.println("code: " + code);

        
        //String pro = this.code.replaceFirst("UTF-8", "GBK");
        StringBuffer result = new StringBuffer("");
        createFile();
    }
    private void createFile(){
        try{    
            File file =new File("./problem.html");
            if(!file.exists()){
                file.createNewFile();
            }
            //true = append file
            FileWriter fileWritter = new FileWriter("./problem.html");
            try(BufferedWriter bufferWritter = new BufferedWriter(fileWritter)){
                 bufferWritter.write(this.code);
            }
           
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
