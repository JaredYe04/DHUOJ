/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

public enum LogLevel{
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR");
    
    private final String info;
    private LogLevel(String info){
        this.info=info;
    }
    public String getString(){
        return info;
    }
}