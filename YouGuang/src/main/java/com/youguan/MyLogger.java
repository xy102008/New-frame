package com.youguan;

import java.io.File;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.LogRecord;

public class MyLogger {
    public static Logger log= Logger.getLogger("Mylog");
    public static FileHandler myfileHandler =null;
    static int i=0;

    /**
     * 写入日志 info 级别
     * 参数： 无
     **/
    public static void  info(String msg)  {
        log.setLevel(Level.INFO);
        if(i==0){
            getFileHandler();
            i++;
            System.out.print("-----");
        }
        log.info("[YouguanshangchengTest] "+getDataTime() +" :"+msg);

    }

    /**
     * 写入日志 严重 级别
     * 参数： 无
     **/
    public static void  error(String msg)  {
        log.setLevel(Level.INFO);
        if(i==0){
            getFileHandler();
            i++;
            System.out.print("-----");
        }
        log.severe("[yuntuTVTest] "+getDataTime() + " :"+msg);
    }

    /**
     * 格式化当前日期
     * 参数： 无
     * return 格式化后的日期
     **/
    public static String getDataTime(){
        String dataTimeString = null;
        SimpleDateFormat date=new SimpleDateFormat("<yyyy-MM-dd HH:mm:ss> ");
        dataTimeString=date.format(new Date());
        return dataTimeString;
    }


    /**
     * 创建唯一FileHandler对象
     * 参数： 无
     * return 无
     **/
    public static void  getFileHandler(){
        String path="d:\\1.log";
        File file=new File(path);
        if(file.exists() && myfileHandler == null){
            file.delete();
        }
        if(!file.exists() && myfileHandler == null){
            try {
                myfileHandler = new FileHandler("d:\\1.log");
                myfileHandler.setLevel(Level.INFO);
                myfileHandler.setFormatter(new myFormatter()); //格式化loggr，即去掉XML格式
                log.addHandler(myfileHandler);  //输出到文件  这个必须写在这里，只能调用一次。在其他地方调用会打印几次
            } catch (IOException e) {
                myfileHandler = null;
                e.printStackTrace();
            }
        }else {
            return;
        }

    }

}

//重写formater抽象方法，Formatter是抽象类
class myFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        // TODO Auto-generated method stub
        return record.getLevel() + " : " + record.getMessage()+"\r\n";
    }
}
