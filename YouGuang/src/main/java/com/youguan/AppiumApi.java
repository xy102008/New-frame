package com.youguan;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

/**
 * Created by Jiangwei on 2018/6/13 0005.
 * 说明：appium 提供的方法再次封装
 */

public class AppiumApi {

    //获得类名字，方便打印日志
    final String Tag = this.getClass().getName();
    public AndroidDriver driver;
    /**
     * 判断元素是否存在
     * 参数： androiddriver By的对象
     * return 布尔值
     **/

    AppiumApi(AndroidDriver driver){
        this.driver = driver;
    }

    public  boolean isElementPresent(AndroidDriver driver, By by){
        try {
            if(driver==null){
                MyLogger.info(Tag+"driver 对象为空...请检查代码....");
            }
            driver.findElement(by);
            MyLogger.info(Tag+"在当前页面找到元素："+by.toString());
            return true;
        }catch (NoSuchElementException e){
            //e.printStackTrace();
            MyLogger.error(Tag+"在当前页面找不到该元素："+by.toString());
            return false;
        }
    }

    /**
     * 等待元素出现 10s超时，找不到返回null 自定义方法等待
     * 参数： androiddriver By的对象 ，
     * 找到返回true
     **/
    public WebElement waitForElementPresent(AndroidDriver driver, By by){
        WebElement webElement = null;
        for(int sec=0;;sec++){
            if(sec >= 10) throw new NoSuchElementException(Tag+"超过10s元素未找到："+by.toString());
            if(isElementPresent(driver,by)) {
                webElement = driver.findElement(by);
                break;
            }
            MyLogger.info(Tag+"继续尝试查找："+by.toString());
            ThreadSleepMethod.threadSleep(1000);
        }
        return webElement;
    }

    /**
     * 查找元素 显性等待
     * 参数：driver 对象,等待时间,by对象
     * return  webelment对象
     **/
    public WebElement WebElementWait(AndroidDriver driver , int waittime, final By by){
        WebDriverWait wait = new WebDriverWait(driver, waittime);
        WebElement element = wait.until(new ExpectedCondition<WebElement>(){
            @Override
            public WebElement apply(WebDriver d) {
                return
                        d.findElement(by);
            }});
        return element;
    }

    /**
     * 获取当前测试时间
     * 参数：无
     * return  返回当前日期 指定格式
     **/
    public  String getTime(){
        SimpleDateFormat data = new SimpleDateFormat("yyyyMMddhhmm");
        return data.format(new Date());
    }

    /**
     * 截图（路径写死）
     * 参数：截图的图片的名字
     * return  无
     **/
    public void Screenshot(TakesScreenshot drivername){
        String filename=getTime() + ".jpg";
        File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(scrFile,new File("d://ftp"+"\\"+filename));
        } catch (IOException e) {
            MyLogger.error("保存失败");
            e.printStackTrace();
        }
        finally {
            MyLogger.info("Screen shot finished, path in "
                    +"d://ftp");
        }
    }

    /**
     * 截图（可自定义路径）
     * 参数：截图的图片的名字，和pathName 文件路径
     * return  无
     **/
    public void Screenshot(TakesScreenshot drivername,String pathName){
        String filename=getTime() + ".jpg";
        File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(scrFile,new File(pathName+"\\"+filename));
        } catch (IOException e) {
            MyLogger.error("保存失败....");
            e.printStackTrace();
        }
        finally {
            MyLogger.info("Screen shot finished, path in "
                    + pathName);
        }
    }

    /**
     * Created by Administrator on 2017/7/28 0028.
     * Text输入
     * 参数： WebElement 对象和text内容
     **/
    public void setText(WebElement elementEdit, String text){
        elementEdit.sendKeys(text);
    }

    /**
     * Created by Administrator on 2017/7/28 0028.
     * Text获取
     * 参数： WebElement 对象和text内容
     **/
    public String  getText(WebElement elementEdit){
        return elementEdit.getText();
    }

    /**
     * Created by Administrator on 2017/7/28 0028.
     * 清除输入
     * 参数： WebElement
     **/
    public void clearText(WebElement elementEdit){
        elementEdit.clear();
    }


    /**
     * Created by Administrator on 2017/7/28 0028.
     * 长按操作
     * 参数： WebElement 对象driver
     **/
    public void longPressByID(AndroidDriver driver, WebElement items){
        TouchAction tAction=new TouchAction(driver);
        tAction.longPress(items).perform();

    }

    /**
     * toast 获取
     * 参数：
     * 暂未验证，需要升级appium
     **/
    public  boolean getToast(AndroidDriver driver, String toast) {
        try {
            final WebDriverWait wait = new WebDriverWait(driver, 1);
            if (wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@text,'"+toast+"')]")))!=null) {
                MyLogger.info("找到了toast");
                return true;
            }
            return false;
        } catch (Exception e) {
            MyLogger.error("找不到toast:" + toast);
            return false;
        }
    }

    /**
     * 获取屏幕的尺寸
     * 参数：driver对象
     * return  屏幕宽和高
     **/
    public int[] getAppScreen(AndroidDriver driver){
        int widthScreen=driver.manage().window().getSize().getWidth();
        int heightScreen = driver.manage().window().getSize().getHeight();
        int[] appWidthAndHeight={widthScreen,heightScreen };
        return appWidthAndHeight;
    }

    /**
     * 向上滑动
     * 参数：duration 持续时间毫秒单位即滑动速度，num滑动次数
     * return  无
     **/
    public void slideUP(AndroidDriver driver,int duration,int num) {
        int startX = this.getAppScreen(driver)[0]*1/2;
        int startY = this.getAppScreen(driver)[1]*4/5;
        int endY = this.getAppScreen(driver)[1]*2/5;
        try{
            for(int i=0; i<num; i++){
                driver.swipe(startX, startY, startX, endY, duration);
                ThreadSleepMethod.threadSleep(1000);
            }
            MyLogger.info("滑动成功...");

        }catch (Exception e){
            MyLogger.error("滑动失败");
            e.printStackTrace();
        }

    }

    /**
     * 向下滑动
     * 参数：duration 持续时间毫秒单位即滑动速度，num滑动次数
     * return  无
     **/
    public void slideDown(AndroidDriver driver,int duration,int num) {

        int startX = this.getAppScreen(driver)[0]*1/2;
        int startY = this.getAppScreen(driver)[1]*1/5;
        int endY = this.getAppScreen(driver)[1]*4/5;
        try{
            for (int i=0; i<num; i++){
                driver.swipe(startX, startY, startX, endY, duration);
                ThreadSleepMethod.threadSleep(1000);
            }
            MyLogger.info("滑动成功...");
        }catch (Exception e){
            MyLogger.info("滑动失败...");
            e.printStackTrace();
        }

    }

    /**
     * 向左滑动
     * 参数：duration 持续时间毫秒单位即滑动速度，num滑动次数
     * return  无
     **/
    public void slideLeft(AndroidDriver driver,int duration,int num) {

        int startX = this.getAppScreen(driver)[0]*4/5;
        int endX = this.getAppScreen(driver)[0]*1/5;
        int startY = this.getAppScreen(driver)[1]*1/2;
        try{
            for (int i=0; i<num; i++){
                driver.swipe(startX, startY, endX, startY, duration);
                ThreadSleepMethod.threadSleep(1000);
            }
            MyLogger.info("滑动成功...");
        }catch (Exception e){
            MyLogger.info("滑动失败...");
            e.printStackTrace();
        }
    }

    /**
     * 向右滑动
     * 参数：duration 持续时间毫秒单位即滑动速度，num滑动次数
     * return  无
     **/
    public void slideRight(AndroidDriver driver,int duration,int num) {

        int startX = this.getAppScreen(driver)[0]*1/5;
        int endX = this.getAppScreen(driver)[0]*4/5;
        int startY = this.getAppScreen(driver)[1]*1/2;
        try{
            for (int i=0; i<num; i++){
                driver.swipe(startX, startY, endX, startY, duration);
                Thread.sleep(1000);
            }
            MyLogger.info("滑动成功...");
        }catch (Exception e){
            MyLogger.error("滑动失败...");
            e.printStackTrace();
        }
    }

    /**
     * 滑动指定元素
     * 参数：duration 持续时间毫秒单位即滑动速度，num滑动次数
     * return  无
     **/
    public void slidingElement(WebElement element,String direction,int duration, int num){
        //XY为元素起点坐标
        int x = element.getLocation().getX();
        int y = element.getLocation().getY();
        //获取元素高和宽
        int elementWidth = element.getSize().getWidth();
        int elementHeight = element.getSize().getHeight();
        switch (direction){
            case "Left":
                int starX =x+elementWidth*3/4;
                int endX =x+elementWidth/4;
                int startY =elementHeight/2+y;
                int endY =elementHeight/2+y;
                for(int i=0; i<num; i++){
                    driver.swipe(starX, startY, x, endY, duration);
                }
                break;
            case "Right":
                int rRtarX =x+elementWidth/4;
                int rEndX =x+elementWidth*3/4;
                int RStartY =elementHeight/2+y;
                int REndY =elementHeight/2+y;
                for(int i=0; i<num; i++){
                    driver.swipe(rRtarX, RStartY, rEndX, REndY, duration);
                }
                break;
            default:break;

        }

    }


    /**
     * 手势解锁九宫格
     * 0 1 2 3 4 5 6 7 8
     */
    public void swipeToUnlock(AndroidDriver driver, WebElement lockImageView, int[] path) {
        TouchAction touchAction = new TouchAction(driver);
        List<WebElement> lockItems = lockImageView.findElements(By.className("android.view.View"));
        for (int i = 0; i < path.length; i++) {
            if (i == 0) {
                touchAction.press(lockItems.get(path[i])).moveTo(lockItems.get(path[i]));
            } else {
                touchAction.moveTo(lockItems.get(path[i]));
            }
        }
        touchAction.release();
        touchAction.perform();
    }
}
