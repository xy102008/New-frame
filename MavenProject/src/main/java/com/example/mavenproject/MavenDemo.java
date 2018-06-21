package com.example.mavenproject;
import java.io.File;
import org.apache.poi.ss.usermodel.Workbook;
import static org.assertj.core.api.Assertions.*;

public class MavenDemo {

    public static void main(String args[]){
        // 断言null或为空字符串

        Workbook wb = null;
        File f = new File("");
        System.out.println("hello");
        getResault();
    }
    public static  void getResault(){
        String str = null;
        assertThat(str).isNullOrEmpty();
        System.out.println("断言是否为空...");
        assertThat("").isEmpty();
        assertThat(false).isFalse();
    }
}
