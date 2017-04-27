package com.github.mituba.crawler;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.stream.Stream;
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

    public List<List<String>> getResultList(List<List<String>> list){
        List<List<String>> resultList = new ArrayList<>();
        list.stream()
            .forEach(n -> n.stream().forEach(m ->{
                try{
                    resultList.add(performURLSearch(m));
                }catch(Exception e){ System.out.println(e); }
            }));
        return resultList;
    }

    public void run(String[] args){
        try{
            List<List<String>> tempraryList = new ArrayList<>();
            performURLSearch(args[0])
                .forEach(n -> {
                    try{
                        tempraryList.add(performURLSearch(n));
                    }catch(Exception e){
                        System.out.println(e);
                    }
                });
            while(true){
                List<List<String>> resultList = getResultList(tempraryList);
                if(resultList.stream().allMatch(n -> n.size() == 0))
                    break;
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        new Main().run(args);
    }
}
