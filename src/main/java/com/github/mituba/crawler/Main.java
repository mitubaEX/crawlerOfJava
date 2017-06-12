package com.github.mituba.crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
	public void startProcess(String saveDirectory){
        try(BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileDownloader().getInputStream("http://central.maven.org/maven2/archetype-catalog.xml")))){
            List<List<String>> list = new XmlParser("http://central.maven.org/maven2/archetype-catalog.xml").getEachIDList();
            new Searcher("http://central.maven.org/maven2/", saveDirectory, list).performSearch();
        }catch(Exception e){
            System.out.println(e);
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
        //new Extractor("2-gram").performExtract("downloadJar", "birthmark");
        new XMLOfBirthmarkCreater().createXML("json", "2-gram"); // XMLを作成する
    }

    public static void main(String[] args){
        new Main().run();
    }
}
