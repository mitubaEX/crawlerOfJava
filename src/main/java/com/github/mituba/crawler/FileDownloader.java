package com.github.mituba.crawler;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class FileDownloader{
    public FileDownloader(String url, String saveDirectoryPath){
        try{
            downloadJar(url, createDirectory(saveDirectoryPath));
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public FileDownloader(){}

    public InputStream getInputStream(String url) throws MalformedURLException, IOException{
        HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
        con.setRequestProperty("Content-Type", "application/octet-stream");
        con.setRequestMethod("GET");
        con.connect();
        return con.getInputStream();
    }

    public void downloadJar(String url, File directory) throws IOException{
        FileOutputStream fileoutputstream = new FileOutputStream(
                new File(directory.getName()
                    + "/" + url.substring(url.lastIndexOf('/') + 1)));
        int line = -1;
        InputStream in = getInputStream(url);
        while((line = in.read()) != -1)
            fileoutputstream.write(line);
        fileoutputstream.close();
    }

    public File createDirectory(String directoryPath) throws IOException{
        File directory = new File(directoryPath);
        if(!directory.exists())
            directory.mkdir();
        return directory;
    }
}
