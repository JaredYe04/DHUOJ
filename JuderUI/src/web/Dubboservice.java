/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

/**
 *
 * @author tange
 */
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo // 扫描启动类所在包及其子包中的DubboService
@DubboComponentScan(basePackages = {"web"})
public class Dubboservice {
    public static boolean running=false;
    public static void main(String[] args) {
        SpringApplication.run(Dubboservice.class, args);
    }

}
