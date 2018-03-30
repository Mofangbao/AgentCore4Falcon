package main;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Agent {
    private Agent(){
        config =Config.getDefault();
    }
    private static Agent instance =new Agent();
    public static Agent getInstance(){
        return instance;
    }

    private Config config;

    /**
     * 发送插件输出的单个模型信息，并附加必要信息后输出
     * @param model
     * @param step
     * @return
     */
    public boolean push(Model model,int step){
        ArrayList<Model> models=new ArrayList<Model>();
        models.add(model);
        return push(models,step);
    }

    /**
     * 发送插件输出的模型信息，并附加必要信息后输出
     * @param models
     * @param step
     * @return
     */
    public boolean push(ArrayList<Model> models,int step){
        JSONArray pushArr=new JSONArray();
        for(Model m : models){
            JSONObject pushObj=new JSONObject();
            pushObj.put("endpoint",config.getHostName());
            pushObj.put("metric",m.getMetric());
            pushObj.put("timestamp",new Date().getTime()/1000);
            pushObj.put("step",step);
            pushObj.put("value",m.getValue());
            pushObj.put("counterType",m.getCounterType());
            pushObj.put("tags",m.getTags());
            pushArr.put(pushObj);
        }
        return push(pushArr.toString());
    }

    /**
     * 发送由插件输出的json数组串，会将其余必要信息附加后发出
     * @param jsonArrayString
     * @param step
     * @return
     */
    public boolean push(String jsonArrayString,int step){
        JSONArray arr=new JSONArray(jsonArrayString);
        for(int i=0;i<arr.length();i++){
            JSONObject pushObj=arr.getJSONObject(i);
            pushObj.put("endpoint",config.getHostName());
            pushObj.put("timestamp",new Date().getTime()/1000);
            pushObj.put("step",step);
        }
        return push(arr.toString());
    }

    /**
     * 发送完整的json数组串，不会再次附加任何其他信息
     * @param jsonArrayString
     * @return
     */
    public boolean push(String jsonArrayString){
        List<String> transfers= config.getTransfers();
        boolean succ=true;
        for(String t:transfers){
            succ= succ && push(jsonArrayString,t);
        }
        return succ;
    }

    public boolean push(String params,String url){
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        LogUtil.debug(params+"->"+url);
        String response="";
        try {
            URL httpUrl = null; //HTTP URL类 用这个类来创建连接
            //创建URL
            httpUrl = new URL(url);
            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            //POST请求
            out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            out.write(params);
            out.flush();
            //读取响应
            reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                response+=lines;
            }
            reader.close();
            // 断开连接
            conn.disconnect();
            String result=response.toString();
            return result.equals("success");
        } catch (Exception e) {
            LogUtil.error("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(reader!=null){
                    reader.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return false;
    }

}
