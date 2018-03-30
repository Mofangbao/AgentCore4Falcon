package executors;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;
import main.Agent;
import main.LogUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mofangbao
 */
public class PythonExecutor extends BaseExecutor {

    public PythonExecutor(String absFileName, File pluginWorkingDirFile, int step) {
        super(absFileName, pluginWorkingDirFile, step);
    }

    @Override
    public void run() {
        try {
            String s;
            Process process = Runtime.getRuntime().exec(new String[]{"python",absFileName},null,null);
            Scanner reader = new Scanner(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            while (reader.hasNextLine()) {
                builder.append(reader.nextLine());
            }
            process.waitFor();
            s = builder.toString();
            Agent.getInstance().push(s, step);
        } catch (Exception ex) {
            ex.printStackTrace();
            LogUtil.error(ex.getMessage());
        }
    }

}
