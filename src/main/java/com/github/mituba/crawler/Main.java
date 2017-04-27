package com.github.mituba.crawler;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.stream.Stream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

public class Main {
    public List<String> getListOfHrefTag(Document doc){
        List<String> list = new ArrayList<>();
        for(Element element : doc.getElementsByAttribute("href"))list.add(element.text());
        return list;
    }

    public Document getHtmlDocument(String url) throws IOException{
        return Jsoup.parse(
                Jsoup.connect(url)
                .get().html());
    }

    public String performDownload(String filePath, String directoryPath){
        new FileDownloader(filePath, directoryPath);
        return "getFileComplete";
    }

    public List<String> filterFile(Stream<String> stream, String url){
        Pattern p = Pattern.compile(".jar$");
        return stream.map(n -> p.matcher(n).find() ? performDownload(url + n, "downloadJar") : url + n)
            .collect(Collectors.toList());
    }

    public List<String> performURLSearch(String url) throws IOException{
        return filterFile(getListOfHrefTag(getHtmlDocument(url)).stream(), url).stream()
            .filter(n -> !n.contains("getFileComplete"))
            .collect(Collectors.toList());
    }

    public List<String> getProductURL(List<String> groupIdList, List<String> artifactIdList, List<String> versionList){
        return IntStream.range(0, groupIdList.size())
            .mapToObj(n -> groupIdList.get(n).replace(".", "/") + "/"
                    + artifactIdList.get(n) + "/" + versionList.get(n) + "/"
                    + artifactIdList.get(n) + "-" + versionList.get(n) + ".jar")
            .collect(Collectors.toList());
    }

    public void run(String[] args){
        try{
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileDownloader().getInputStream(args[0])));
            List<String> groupIdList = new ArrayList<>();
            List<String> artifactIdList = new ArrayList<>();
            List<String> versionList = new ArrayList<>();
            bufferedreader.lines()
                .forEach(n -> {
                    if(n.contains("<groupId>"))
                        groupIdList.add(n.replace(" ","").replace("<groupId>","").replace("</groupId>",""));
                    else if(n.contains("<artifactId>"))
                        artifactIdList.add(n.replace(" ","").replace("<artifactId>","").replace("</artifactId>",""));
                    else if(n.contains("<version>"))
                        versionList.add(n.replace(" ","").replace("<version>","").replace("</version>",""));
                });
            getProductURL(groupIdList, artifactIdList, versionList)
                .forEach(n -> {
                    try{
                        performDownload("http://central.maven.org/maven2/"+ n, "downloadJar");
                    }catch(Exception e){
                        System.out.println(e);
                    }
                });
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        new Main().run(args);
    }
}
