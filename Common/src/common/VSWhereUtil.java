/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author tange
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VSWhereUtil {
    /**
     * 从指定的起始目录开始，逐层向上查找 vswhere.exe
     *
     * @param startDir 起始目录
     * @return vswhere.exe 的绝对路径
     * @throws RuntimeException 如果未找到 vswhere.exe
     */
    private static String findVswhereExe(File startDir) {
        File currentDir = startDir;

        while (currentDir != null) {
            File vswhereFile = new File(currentDir, "vswhere.exe");
            if (vswhereFile.exists() && vswhereFile.isFile()) {
                return vswhereFile.getAbsolutePath();
            }
            currentDir = currentDir.getParentFile(); // 向上一级目录
        }

        throw new RuntimeException("未找到 vswhere.exe，请确保该文件存在于程序目录或上层目录中。");
    }
    private static String getVcvarsPath() throws IOException, InterruptedException {
        String vswhereDir=findVswhereExe(new File(System.getProperty("user.dir")));
        ProcessBuilder processBuilder = new ProcessBuilder(vswhereDir,
                "-latest",
                "-products", "*",
                "installationPath",
                "-find", "**/vcvars64.bat");
        processBuilder.redirectErrorStream(true); // 合并标准错误输出
        Process process = processBuilder.start();
        List<String> output = new ArrayList<>();
        try (Scanner scanner = new Scanner(process.getInputStream())) {
            while (scanner.hasNextLine()) {
                output.add(scanner.nextLine());
            }
        }
        process.waitFor();
        if (!output.isEmpty()) {
            return output.get(0);
        }
        return null;
    }



    public static String findClExePath() {
        // 获取当前工作目录
        File currentDir = new File(System.getProperty("user.dir"));
        // 查找 vswhere.exe 路径
        String vswherePath = findVswhereExe(currentDir);

        String clExeDir = null;
        try {
            // 构建命令行调用 vswhere.exe
            ProcessBuilder processBuilder = new ProcessBuilder(vswherePath,
                    "-latest",
                    "-products", "*",
                    "-requires", "Microsoft.VisualStudio.Component.VC.Tools.x86.x64",
                    "-find", "**/Hostx64/x64/cl.exe");

            // 设置工作目录为当前程序目录
            processBuilder.directory(new File(System.getProperty("user.dir")));
            
            // 启动进程
            Process process = processBuilder.start();

            // 读取命令行输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.endsWith("cl.exe")) {
                    clExeDir = line.substring(0, line.lastIndexOf("cl.exe"));
                    break;
                }
            }

            reader.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clExeDir;
    }
    public static String getMsvcCompilerCommand(){
        try {
            return "cmd.exe /c call \""+getVcvarsPath()+"\" && \"$compilerPath$/cl.exe\" \"$sourceFile$\" /O2 /Fe:\"$exeFile$\"";
        } catch (IOException ex) {
            Logger.getLogger(VSWhereUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(VSWhereUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static String getMsvcLinkCommand(){
        return "cmd.exe /c";
    }
    public static void main(String[] args) {
        String clPath = findClExePath();
        if (clPath != null) {
            System.out.println("cl.exe 目录路径: " + clPath);
            System.out.println(getMsvcCompilerCommand());
            
        } else {
            System.out.println("未找到 cl.exe 路径");
        }
    }
}
