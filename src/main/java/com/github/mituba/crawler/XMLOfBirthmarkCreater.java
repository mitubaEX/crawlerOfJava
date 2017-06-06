package com.github.mituba.crawler;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mituba on 2017/06/06.
 */
public class XMLOfBirthmarkCreater {
    public void writeFile(String body, String filename){
        try {
            System.out.println(filename);
            new File(filename).createNewFile();
            FileWriter fileWriter = new FileWriter(new File(filename));
            fileWriter.write(body);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getMatchString(String str){
        try {
            Matcher m = Pattern.compile("/.+?!").matcher(str);
            m.find();
            String[] array = m.group().split("/");
            return array[array.length - 1].replace("!","");
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public String createXMLTable(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            return String.join(",", br.lines()
                    .map(n -> n.split(",", 4))
                    .filter(n -> n.length >= 4)
                    .map(n -> "{ \n\"filename\":\""+ n[0] + "\",\n\"jar\":\"" + getMatchString(n[1]) + "\",\n\"kindOfBirthmark\":\"" + n[2] + "\",\n\"birthmark\":\"" + n[3] + "\"\n}\n")
                    .distinct()
                    .collect(Collectors.toList()));
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return "";
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }

    public List<File> getFileList(String dirName){
        return Arrays.asList(new File(dirName).listFiles());
    }

    public String getXMLBody(String birthmarkDirectory){
        List<File> fileList = getFileList(birthmarkDirectory);
        List<String> strList = fileList.stream().map(n -> createXMLTable(n)).filter(n -> n.length() != 0).collect(Collectors.toList());
        return "["+ String.join(",\n" ,  strList) + "]";
    }

    public void createXML(String XMLDirectory, String kindOfBirthmark){
        String xmlBody = getXMLBody("birthmark");
        File xmldir = new File(XMLDirectory);
        if(!xmldir.exists()) xmldir.mkdir();
        writeFile(xmlBody, XMLDirectory + "/" + kindOfBirthmark + ".json");
    }
}
