package com.github.mituba.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by mituba on 2017/06/22.
 */
public class Register {
    private String birthmark;
    private String portNum;
    public Register(String portNum, String birthmark){
        this.birthmark = birthmark;
        this.portNum = portNum;
    }

    public void registerDocument(String filePath){
        try {
            // run shellScript
            Process process = Runtime.getRuntime().exec("sh ./postContent.sh " + birthmark.replace("-", "") + " " + portNum + " " + birthmark);
            new BufferedReader(new InputStreamReader(process.getInputStream())).lines().forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
