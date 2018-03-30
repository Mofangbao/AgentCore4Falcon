/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executors;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

/**
 *
 * @author mofangbao
 */
public class ExecutorFactory {
    
    private static HashMap<String,IExecutorCreater> executors=new HashMap<>();
    
    public static BaseExecutor createExecutor(String absFileName, File pluginWorkingDirFile, int step){
        String ext=absFileName.substring(absFileName.lastIndexOf(".")+1);
        IExecutorCreater creater=executors.get(ext);
        if(creater==null) {
            if(ext.equals("exe") || ext.equals("sh") || ext.equals("bat")){
                return new DefaultExecutor(absFileName, pluginWorkingDirFile, step);
            }else{
                System.out.println("无法找到合适的执行器来执行"+absFileName);
                return null;
            }
        }
        return creater.create(absFileName, pluginWorkingDirFile, step);
    }
    
    public static void registExecutor(String ext,IExecutorCreater creater){
        executors.put(ext, creater);        
    }
}
