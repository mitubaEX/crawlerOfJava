package com.github.mituba.crawler;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class Searcher{
	private String domain;
	private String saveDirectory;
	private List<List<String>> list;
	public Searcher(String domain, String saveDirectory, List<List<String>> list){
		this.domain = domain;
		this.saveDirectory = saveDirectory;
		this.list = list;
	}
	
    public void performDownload(String filePath, String directoryPath){
        new FileDownloader(filePath, directoryPath);
    }

    public List<String> getProductURL(List<String> groupIdList, List<String> artifactIdList, List<String> versionList){
        return IntStream.range(0, groupIdList.size())
            .mapToObj(n -> groupIdList.get(n).replace(".", "/") + "/"
                    + artifactIdList.get(n) + "/" + versionList.get(n) + "/"
                    + artifactIdList.get(n) + "-" + versionList.get(n) + ".jar")
            .collect(Collectors.toList());
    }
    
    public void performSearch(){
        getProductURL(list.get(0), list.get(1), list.get(2))
            .forEach(n -> {
                try{
                    performDownload(domain + n, saveDirectory);
                }catch(Exception e){
                    System.out.println(e);
                }
            });
    }
    
}
