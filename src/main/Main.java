package main;

import executors.BaseExecutor;
import executors.ExecutorFactory;
import executors.IExecutorCreater;
import executors.PythonExecutor;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static String pluginWorkingDir=Config.getCurrentPath()+File.separator+"plugins";
    private static File pluginWorkingDirFile=new File(pluginWorkingDir);
    
    public static void main(String[] args){
        planAllPlugins();
    }
    
    /**
     * 作为库加载时，可以在注册executorCreater之后由程序调用启动
     */
    public static void planAllPlugins(){
        registDefaultExecutors();
        System.out.println(Config.getCurrentPath());
        File plugFiles=new File(pluginWorkingDir);
        if(!plugFiles.exists()){
            LogUtil.warn("未能找到插件目录！");
            return;
        }        
        File[] files=plugFiles.listFiles();
        if(files.length==0){
            LogUtil.warn("未找到任何插件文件！");
            return;
        }
        for(File file:files){
            if(file.isDirectory()) continue;
            String fileName=file.getName();
            planIt(fileName,file.getAbsolutePath());
        }
    }
    
    private static void registDefaultExecutors(){
        IExecutorCreater pythonExeCreator=new IExecutorCreater() {
            @Override
            public BaseExecutor create(String absFileName, File pluginWorkingDirFile, int step) {
                return new PythonExecutor(absFileName, pluginWorkingDirFile, step);
            }
        };
        ExecutorFactory.registExecutor("py", pythonExeCreator);
    }

    private static void planIt(String fileName,final String absFileName){
        String[] fileNameArr=fileName.split("_");
        if(fileNameArr.length<2){
            LogUtil.warn("插件文件名（"+fileName+"）不符合规范，取消调用！");
            return;
        }
        final int step=Integer.valueOf(fileNameArr[0]);
        final BaseExecutor executor=ExecutorFactory.createExecutor(absFileName, pluginWorkingDirFile, step);
        if(executor==null) return;
        Timer t=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                LogUtil.info("["+new Date().toString()+ "] exec:"+absFileName);
                executor.run();
            }
        };
        t.scheduleAtFixedRate(task,new Date(),step*1000);
    }

    private static void testSend(){
        Model model=new Model();
        model.setCounterType("GAUGE");
        model.setMetric("test-metric-online");
        model.setTags("");
        model.setValue(3);
        boolean res=Agent.getInstance().push(model,60);
        System.out.println(res);
    }
}
