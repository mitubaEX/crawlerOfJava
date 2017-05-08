package com.github.mituba.crawler;

import java.io.BufferedReader;
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
//    	ダウンロードしてない環境の場合，これを実行する
    	// startProcess("downloadJar");
//    	更新確認
//        if(new UpdateChecker().getLastUpdateDay()){
//        	System.out.println(new UpdateChecker().getLastUpdateDay());
//            new Updater().deleteDir("downloadJar");
//        	System.out.println("delete Complete");
//            startProcess("downloadJar");
//        }
        new Extractor("2-gram").performExtract("downloadJar", "birthmark");
    }

    public static void main(String[] args){
        new Main().run();
    }
}
