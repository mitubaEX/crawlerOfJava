package com.github.mituba.crawler;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Locale;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class UpdateChecker{
    public boolean dateCheckr(String[] args) throws IOException,ParseException {
        File file = new File("last_updated.txt");
        SimpleDateFormat sdfBef = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat sdfAft = new SimpleDateFormat("yyyy MM dd HH:mm:ss"); //2012/12/28 18:01:48
        if(!file.exists()){
            file.createNewFile();
            String result = sdfAft.format(sdfBef.parse(args[3] + " " + args[1] + " " + args[6] + " " + args[4]));
            FileWriter filewriter = new FileWriter(file);
            filewriter.write(result);
            filewriter.close();
        }else{
        	BufferedReader br =  new BufferedReader(new FileReader(file));
            String[] readDate = br.readLine().split(" ");
            String[] result = sdfAft.format(sdfBef.parse(args[3] + " " + args[1] + " " + args[6] + " " + args[4])).split(" ");
            br.close();
            if(!Objects.equals(result[0], readDate[0]) || !Objects.equals(result[1],readDate[1]))
            	return true;
        }
        return false;
    }
    public boolean getLastUpdateDay(){
        try(BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileDownloader().getInputStream("http://central.maven.org/maven2/last_updated.txt")))){
            String[] date = bufferedreader.readLine()
                .split(" ");
            return dateCheckr(date);
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
}
