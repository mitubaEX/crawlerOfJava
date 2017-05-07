package com.github.mituba.crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class XmlParser{
    private String xmlUrl;
    public XmlParser(String xmlUrl){
        this.xmlUrl = xmlUrl;
    }

    public List<List<String>> getEachIDList(){
        // try(BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileDownloader().getInputStream("http://central.maven.org/maven2/archetype-catalog.xml")))){
        try(BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileDownloader().getInputStream(xmlUrl)))){
            List<List<String>> list = new ArrayList<>();
            list.add(new ArrayList<String>());
            list.add(new ArrayList<String>());
            list.add(new ArrayList<String>());
            bufferedreader.lines()
                .forEach(n -> {
                    if(n.contains("<groupId>"))
                        list.get(0).add(n.replace(" ","").replace("<groupId>","").replace("</groupId>",""));
                    else if(n.contains("<artifactId>"))
                        list.get(1).add(n.replace(" ","").replace("<artifactId>","").replace("</artifactId>",""));
                    else if(n.contains("<version>"))
                        list.get(2).add(n.replace(" ","").replace("<version>","").replace("</version>",""));
                });
            return list;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
}
