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

    public void run(String[] args){
        try{
            filterFile(getListOfHrefTag(getHtmlDocument(args[0])).stream(), args[0]);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){
        new Main().run(args);
    }
}
