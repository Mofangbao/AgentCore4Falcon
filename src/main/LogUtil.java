package main;

public class LogUtil {

    public static void info(String content){
        System.out.println(content);
    }
    public static void warn(String content){
        System.out.println(content);
    }
    public static void error(String content){
        System.out.println(content);
    }
    public static void debug(String content){
        if(Config.getDefault().isDebug())
        System.out.println(content);
    }
}
