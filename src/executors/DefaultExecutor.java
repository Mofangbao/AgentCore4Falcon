/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executors;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Scanner;
import main.Agent;
import main.LogUtil;

/**
 *
 * @author mofangbao
 */
public class DefaultExecutor extends BaseExecutor{

    public DefaultExecutor(String absFileName, File pluginWorkingDirFile, int step) {
        super(absFileName, pluginWorkingDirFile, step);
    }
    
    @Override
    public void run() {
        try {
                    String s;
                    Process process = Runtime.getRuntime().exec(absFileName,null,pluginWorkingDirFile);
                    Scanner reader = new Scanner(new InputStreamReader(process.getInputStream()));
                    StringBuilder builder=new StringBuilder();
                    while(reader.hasNextLine()){
                        builder.append(reader.nextLine());
                    }
                    process.waitFor();
                    s=builder.toString();
                    Agent.getInstance().push(s,step);
                }catch (Exception ex){
                    ex.printStackTrace();
                    LogUtil.error(ex.getMessage());
                }
    }
}
