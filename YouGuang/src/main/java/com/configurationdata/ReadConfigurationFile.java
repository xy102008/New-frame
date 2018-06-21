package com.configurationdata;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;

/**
 * create on 2018/6/19
* 读取xml配置文件获取文件路径
* */
public class ReadConfigurationFile {

    public static String elementIDPath;
    public static String parameterPath;
    public static String xmlConfiguration;
    public static ReadConfigurationFile rf = null ;
    //邮箱用户名称
    private String emailName;
    //邮箱密码
    private String emailPassWord;
    //邮箱域名
    private String MailServerHost;
    //收件人邮箱地址
    private String ToAddress;
    //发件人邮箱
    private String FromAddress;

    private ReadConfigurationFile(){
        readXml();
    }

    public static ReadConfigurationFile getInstance(){
        if (rf == null){
            rf= new ReadConfigurationFile();
        }
        return rf;
    }

    public void setEmailName(String str){
        this.emailName = str;
    }

    public String getEmailName(){
        return emailName;
    }

    public void setEmailPassWord(String str){
        this.emailPassWord = str;
    }

    public String getEmailPassWord(){
        return emailPassWord;
    }

    public void setMailServerHost(String str){
        this.MailServerHost = str;
    }

    public String getMailServerHost(){
        return MailServerHost;
    }

    public void setToAddress(String str){
        this.ToAddress = str;
    }

    public String getToAddress(){
        return ToAddress;
    }

    public void setFromAddress(String str){
        this.FromAddress = str;
    }

    public String getFromAddress(){
        return  FromAddress;
    }

    public   void readXml() {
        long lasting = System.currentTimeMillis();
        try {
            File f = new File(getXmlPath());
            SAXReader reader = new SAXReader();
            Document doc = reader.read(f);
            Element root = doc.getRootElement();
            Element foo;
            System.out.println("开始遍历...");
            for (Iterator i = root.elementIterator("book"); i.hasNext(); ) {
             //   System.out.println("---...");
                foo = (Element) i.next();
                switch(foo.attributeValue("name")){
                    case "element" :
                        elementIDPath = foo.elementText("evaluate");
                        System.out.println("字段值: " + foo.elementText("evaluate"));
                        break;
                    case "parameter" :
                        parameterPath = foo.elementText("evaluate");
                        System.out.println("字段值: " + foo.elementText("evaluate"));
                        break;
                    case "emailname ":
                        setEmailName(foo.elementText("evaluate"));
                        break;
                    case "password ":
                        setEmailPassWord(foo.elementText("evaluate"));
                        break;
                    case "MailServerHost ":
                        setFromAddress(foo.elementText("evaluate"));
                        break;
                    case "ToAddress ":
                        setToAddress(foo.elementText("evaluate"));
                        break;
                    case "FromAddress ":
                        setFromAddress(foo.elementText("evaluate"));
                        break;
//                    default:
//                        System.out.println("迭代完成~~");
//                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        /* 先取得配置文件xml的路径*/
        private static String  getXmlPath(){
            File directory = new File("");//参数为空
            String courseFile = null;
            try{
                courseFile = directory.getCanonicalPath() ;
            }catch (Exception e){
                e.printStackTrace();
            }
            xmlConfiguration = courseFile +"\\YouGuang\\conf\\configuration.xml";
         //   elementIDPath = courseFile +"\\YouGuang\\conf\\ElementID.xls";
            System.out.println(xmlConfiguration);
            return xmlConfiguration;
        }
}
