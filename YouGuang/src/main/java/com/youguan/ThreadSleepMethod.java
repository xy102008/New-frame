package com.youguan;

public class ThreadSleepMethod {

    public static void threadSleep(int sleepSecond){
        try{
            Thread.sleep(sleepSecond);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
