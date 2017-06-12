package com.github.mituba.crawler;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class FileDownloader{
    public FileDownloader(String url, String saveDirectoryPath){
        try{
            downloadJar(url, createDirectory(saveDirectoryPath));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public FileDownloader(String groupID, String artifactID, String version, String directoryPath){
        try {
            writeJarInformation(groupID, artifactID, version, directoryPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public FileDownloader(){}
    public void writeJarInformation(String groupID, String artifactID, String version, String directoryPath) throws IOException{
        FileWriter fileWriter = new FileWriter(new File(directoryPath + "/" + artifactID + "-" + version + ".csv"));
        fileWriter.write(groupID + "," + artifactID + "," + version);
    }

    public InputStream getInputStream(String url) throws MalformedURLException, IOException{
        HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestProperty("Content-Type", "application/octet-stream");
        con.setRequestMethod("GET");
        con.connect();
        return con.getInputStream();
    }

    public void downloadJar(String url, String directory) throws IOException{
        FileOutputStream fileoutputstream = new FileOutputStream(
                new File(directory
                    + "/" + url.substring(url.lastIndexOf('/') + 1)));
        int line = -1;
        InputStream in = getInputStream(url);
        while((line = in.read()) != -1)
            fileoutputstream.write(line);
        fileoutputstream.close();
    }

    public String createDirectory(String directoryPath) throws IOException{
        File directory = new File(directoryPath);
        if(!directory.exists())
            directory.mkdirs();
        return directoryPath;
    }
}
