package com.github.mituba.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Main {
	public void startProcess(String saveDirectory){
        try(BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileDownloader().getInputStream("http://central.maven.org/maven2/archetype-catalog.xml")))){
            List<List<String>> list = new XmlParser("http://central.maven.org/maven2/archetype-catalog.xml").getEachIDList();
            new Searcher("http://central.maven.org/maven2/", saveDirectory, list).performSearch();
        }catch(Exception e){
            e.printStackTrace();
        }
	}
    public void run(){
	    if(!new File("downloadJar").exists())
    //    	ダウンロードしてない環境の場合，これを実行する
            startProcess("downloadJar");
//    	更新確認
        if(new UpdateChecker().getLastUpdateDay()){// LastUpdateから1ヶ月経っていたら入る
        	System.out.println(new UpdateChecker().getLastUpdateDay());
            new Updater().deleteDir("downloadJar");
        	System.out.println("delete Complete");
            startProcess("downloadJar");
        }
        Arrays.asList("2-gram", "3-gram", "4-gram", "5-gram", "6-gram", "uc")
                .stream()
                .forEach(n -> {
                    new Extractor(n).performExtract("downloadJar", "downloadJar");
                    new XMLOfBirthmarkCreater().createXML("json", n); // XMLを作成する
                    new Register("8982", n).registerDocument("json/"+ n + ".json");
                });
    }

    public static void main(String[] args){
        new Main().run();
    }
}
