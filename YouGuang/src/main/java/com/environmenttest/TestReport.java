package com.environmenttest;

import com.youguan.MyLogger;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.testng.collections.Objects;

public class TestReport extends TestListenerAdapter {
    public static String reportPath;  //测试报告路径
    private static String allRunTime;  //总测试时间
    private static int caseNum;         //用例数量
    private static String failNum;
    private static String scuessNum;
    private static String scuessRate;  //成功率
    private static String failRate;     //失败率

    private static int serialNumber = 1; //序号
    private static String caseName;  //用例名字
    private static String expectedResult; // 期待结果
    private static String actualResult;  //实际结果


    //固定头内容
    @Override
    public  void onStart(ITestContext context)
    {
        File htmlReportDir = new File("D:/FTP/TestResult");
        if (!htmlReportDir.exists()) {
            htmlReportDir.mkdirs();
        }
        String reportName = formateDate()+".html";
        reportPath = htmlReportDir+"\\"+reportName;
        File report = new File(htmlReportDir,reportName);
        if(report.exists()==false){
            try {
                report.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        MyLogger.info("测试报告路径为："+reportPath);
        StringBuilder sb = new StringBuilder("<html><head><meta  content=\"text/html; charset=utf-8\" />"
                + "<title >UI自动化测试报告</title></head><body>"
                + "<div id=\"top\"><h3>Appium测试报告</h3>"
                + "<table>"
                + "<thead>"
                + "<tr>"
                + "<td>测试序号</td>"
                + "<td>用例描述</td>"
                + "<td>期望结果</td>"
                + "<td>实际结果</td>"
                + "<td>执行时间</td>"
                + "<td>测试结果</td>"
                + "</tr>"
                + "</thead>"
                +"</tbody>");
        String res = sb.toString();
        try {
            Files.write((Paths.get(reportPath)),res.getBytes("utf-8"), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //测试成功
    @Override
    public  void onTestSuccess(ITestResult result) {
        StringBuilder sb = new StringBuilder("<tr>");
        sb.append("<td>"+serialNumber+"</td>");
        sb.append("<td>"+result.getMethod().getDescription()+"</td>");
        sb.append("<td>"+expectedResult+"</td>");
        sb.append("<td>"+actualResult+"</td>");
        sb.append("<td>"+formatDate(result.getStartMillis())+"</td>");
        sb.append("<td class =\"sucesss\">Pass</td>");
        sb.append("</tr>");

        String res = sb.toString();
        try {
            Files.write((Paths.get(reportPath)),res.getBytes("utf-8"),StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serialNumber++;
    }

    //测试失败
    @Override
    public  void onTestFailure(ITestResult result) {
        StringBuilder sb = new StringBuilder("<tr>");
        sb.append("<td>"+serialNumber+"</td>");
        sb.append("<td>"+result.getMethod().getDescription()+"</td>");
        sb.append("<td>"+expectedResult+"</td>");
        sb.append("<td>"+actualResult+"</td>");
        sb.append("<td>"+formatDate(result.getStartMillis())+"</td>");
        sb.append("<td class =\"fail\">Fail</td>");
        sb.append("</tr>");

        String res = sb.toString();
        try {
            Files.write((Paths.get(reportPath)),res.getBytes("utf-8"),StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        serialNumber++;
    }

    //测试完成
    @Override
    public  void onFinish(ITestContext testContext) {
        caseNum = testContext.getPassedTests().size()+testContext.getFailedTests().size();
        //allRunTime = this.formatDate(testContext.getEndDate()) - this.formatDate(testContext.getStartDate());
        //百分比运算
        double failPercent = (float)(testContext.getPassedTests().size())/caseNum;
        double sucessPercent = (float)(testContext.getFailedTests().size())/caseNum;
        scuessRate = this.formatFailRate(failPercent);
        failRate = this.formatFailRate(sucessPercent);
        allRunTime = this.differTime(testContext.getEndDate().getTime()-testContext.getStartDate().getTime());
        StringBuilder sb = new StringBuilder("</tbody></table>"
                + "<div class =\"note\">"
                + "<P>开始时间: <span>"+this.formatDate(testContext.getStartDate()) +"</span></p>"
                + "<P>结束时间: <span>"+this.formatDate(testContext.getEndDate())+"</span></p>"
                + "<P>运行时间: <span>"+allRunTime+"</span></p>"
                + "<P>执行用例数: <span>"+caseNum+"</span></p>"
                + "<P>用例成功: <span>"+testContext.getPassedTests().size()+"</span></p>"
                + "<P>用例失败: <span>"+testContext.getFailedTests().size()+"</span></p>"
                + "<P>成&nbsp;功&nbsp;率 : <span>"+scuessRate+"</span></p>"
                + "<P>失&nbsp;败&nbsp;率: <span>"+failRate+"</span></p>"
                + "</div></div></body>");
        sb.append("<style type =\"text/css\">");
        try {
            sb.append(cssFormat());
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        sb.append("</style></html>");
        String msg = sb.toString();
        try {
            Files.write((Paths.get(reportPath)),msg.getBytes("utf-8"),StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //格式化当前的日期时间
    public static String formateDate(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return sf.format(date);
    }

    //格式化获得的执行日期时间
    private String formatDate(long date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    //格式化获得的开始结束测试日期时间
    private String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    //百分比转换函数
    public String formatFailRate(double decimal){
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);

        return nt.format(decimal);
    }

    //计算测试时间差
    public String differTime(long s){
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        day = s / (24 * 60 * 60 * 1000);
        hour = (s / (60 * 60 * 1000) - day * 24);
        min = ((s / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (s/1000-day*24*60*60-hour*60*60-min*60);
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass())
                .add("passed", getPassedTests().size())
                .add("failed", getFailedTests().size())
                .add("skipped", getSkippedTests().size())
                .toString();
    }

    //css 样式代码
    public StringBuilder cssFormat() throws Exception{
        StringBuilder sb=new StringBuilder();

        File cssfile = new File("D:/FTP/TestResult/css.txt");
        InputStream input = new FileInputStream(cssfile);
        InputStreamReader is = new InputStreamReader(input);
        BufferedReader bufr = new BufferedReader(is);
        int t;
        while((t = bufr.read())!=-1){
            String str = bufr.readLine();
            sb.append(str);
        }
        bufr.close();
        is.close();
        return sb;
    }
}
