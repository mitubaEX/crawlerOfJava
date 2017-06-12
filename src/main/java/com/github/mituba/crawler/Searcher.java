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

    public void performDownloadGroupAndArtifact(String groupID, String artifactID, String version, String directoryPath) {new FileDownloader(groupID, artifactID, version, directoryPath);}

    public List<String> getProductURL(List<String> groupIdList, List<String> artifactIdList, List<String> versionList){
        return IntStream.range(0, groupIdList.size())
            .mapToObj(n -> groupIdList.get(n).replace(".", "/") + "/"
                    + artifactIdList.get(n) + "/" + versionList.get(n) + "/"
                    + artifactIdList.get(n) + "-" + versionList.get(n) + ".jar")
            .collect(Collectors.toList());
    }

    public void performSearch(){
        int i = 0;
        for(String n : getProductURL(list.get(0), list.get(1), list.get(2))) {
            try {
                performDownload(domain + n,
                        saveDirectory + "/" + n.replace("-", "/").replace(".jar", ""));
                performDownloadGroupAndArtifact(list.get(0).get(i), list.get(1).get(i), list.get(2).get(i), saveDirectory + "/" + n.replace("-", "/").replace(".jar", ""));
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
