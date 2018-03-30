/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executors;


import main.LogUtil;
import main.Agent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.TimerTask;

/**
 *
 * @author mofangbao
 */
public abstract class BaseExecutor extends TimerTask{
    public BaseExecutor(String absFileName,File pluginWorkingDirFile,int step){
        this.absFileName=absFileName;
        this.pluginWorkingDirFile=pluginWorkingDirFile;
        this.step=step;
    }
    
    protected String absFileName;
    protected File pluginWorkingDirFile;
    protected int step;
}
