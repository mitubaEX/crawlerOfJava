package com.github.mituba.crawler;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    public String createXMLTable(File file, File information){
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            BufferedReader brInformation = new BufferedReader(new FileReader(information));
            String[] classInformation = brInformation.readLine().split(",");
            return String.join(",", br.lines()
                    .map(n -> n.split(",", 4))
                    .filter(n -> n.length >= 4)
                    .map(n -> "{ \n\"filename\":\""+ n[0] + "\"," +
                            "\n\"jar\":\"" + getMatchString(n[1]) + "\"," +
                            "\n\"kindOfBirthmark\":\"" + n[2] + "\"," +
                            "\n\"groupID\":\"" + classInformation[0] + "\"," +
                            "\n\"artifactID\":\"" + classInformation[1] + "\"," +
                            "\n\"version\":\"" + classInformation[2].replace(".", "_") + "\"," +
                            "\n\"birthmark_t\":\"" + n[3] + "\"\n}\n")
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
        searchFile(new File(dirName), "2-gram");
        return list;
    }

    public String getXMLBody(String birthmarkDirectory){
        searchFile(new File(birthmarkDirectory), "2-gram");
//        searchCSVFile(new File(birthmarkDirectory));
//        System.out.println(list.size() + " " + listCSV.size());
        List<String> strList = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            strList.add(createXMLTable(list.get(i),listCSV.get(i)));
        }
//        List<String> strList = fileList.stream().map(n -> createXMLTable(n)).filter(n -> n.length() != 0).collect(Collectors.toList());
        return "["+ String.join(",\n" ,  strList.stream()
                .filter(n -> !Objects.equals(n,"")).collect(Collectors.toList())) + "]";
    }

    public void createXML(String XMLDirectory, String kindOfBirthmark){
        String xmlBody = getXMLBody("downloadJar");
        File xmldir = new File(XMLDirectory);
        if(!xmldir.exists()) xmldir.mkdir();
        writeFile(xmlBody, XMLDirectory + "/" + kindOfBirthmark + ".json");
    }

    List<File> list = new ArrayList<>();
    List<File> listCSV = new ArrayList<>();
    public void searchFile(File file, String kindOfBirthmark){
        try {
            if (file == null) return;
            Arrays.stream(file.listFiles())
                    .forEach(n -> {
                        if (!n.exists()) System.out.println("ファイルナス");
                        else if (n.isDirectory()) searchFile(n, kindOfBirthmark);
                        else if (n.getName().contains(".txt") && n.getName().contains(kindOfBirthmark)){
                            list.add(n);
                            if(new File(n.getAbsolutePath()
                                    .replace("-2-gram","")
                                    .replace(".jar","")
                                    .replace(".txt",".csv")).exists())
                                listCSV.add(new File(n.getAbsolutePath()
                                        .replace("-2-gram","")
                                        .replace(".jar","")
                                        .replace(".txt",".csv")));
                            else
                                listCSV.add(new File(""));
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void searchCSVFile(File file){
        try {
            if (file == null) return;
            Arrays.stream(file.listFiles())
                    .forEach(n -> {
                        if (!n.exists()) System.out.println("ファイルナス");
                        else if (n.isDirectory()) searchCSVFile(n);
                        else if (n.getName().contains(".csv")) listCSV.add(n);
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
