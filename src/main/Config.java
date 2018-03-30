package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private List<String> transfers;
    private String hostName;
    private boolean debug;

    private static String currentPath;
    private static String currentConfigPath;
    private static Config defaultConfig;

    public static String getCurrentPath(){
        if (currentPath ==null) {
            try {
                URL resource=Config.class.getClassLoader().getResource("");
                if (resource==null) {
                    currentPath =System.getProperty("user.dir");
                }else{
                    currentPath  = URLDecoder.decode(ClassLoader.getSystemResource("").getPath(), "utf-8");
                }
            } catch (Exception ex) {
                LogUtil.error(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return currentPath;
    }
    public static String getCurrentConfigPath(){
        if (currentConfigPath ==null) {
            currentConfigPath =getCurrentPath()+ File.separator+"cfg.json";
        }
        return currentConfigPath;
    }
    public static String readToString(String fileName,String encoding) {
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtil.error("未能找到配置文件cfg.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<String> transfers) {
        this.transfers = transfers;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public static Config getDefault(){
        if(defaultConfig==null){
            String jsonContent = readToString(getCurrentConfigPath(), "UTF-8");
            JSONObject obj=new JSONObject(jsonContent);
            JSONArray arr=obj.getJSONArray("transfers");
            defaultConfig=new Config();
            List<String> transfers=new ArrayList<String>();
            for(int i=0;i<arr.length();i++){
                transfers.add(arr.getString(i));
            }
            String hostName=obj.getString("hostName");
            boolean isDebug=false;
            if(obj.has("debug")){
                isDebug=obj.getBoolean("debug");
            }
            defaultConfig.setDebug(isDebug);
            defaultConfig.setTransfers(transfers);
            defaultConfig.setHostName(hostName);
        }
        return defaultConfig;
    }

    /**
     * @return the isDebug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param isDebug the isDebug to set
     */
    public void setDebug(boolean isDebug) {
        this.debug = isDebug;
    }

}
