/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package executors;

import java.io.File;

/**
 *
 * @author mofangbao
 */
public interface IExecutorCreater {
    BaseExecutor create(String absFileName,File pluginWorkingDirFile,int step);
}
