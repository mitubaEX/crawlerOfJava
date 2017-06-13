package com.github.mituba.crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

public class Extractor {
	private String kindOfBirthmark;
	private List<File> list = new ArrayList<File>();

	public Extractor(String kindOfBirthmark){
		this.kindOfBirthmark = kindOfBirthmark;
	}

	public void createBirthmarkDirectory(String filePath){
		File file = new File(filePath);
		if(file.exists())
			new Updater().deleteDir(filePath);
		file.mkdir();
	}

	public void extract(File file, String extractDirectory){
		try {
            ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
            ScriptRunner runner = builder.build();
//            System.out.println(file.getName() + ":filePath");
            String[] arg = { "./extract.js", kindOfBirthmark, file.getPath(), file.getParent()+ "/"+ file.getName() + "-"+ kindOfBirthmark +".txt" };
			if(file.length() == 0)
				return;
            if(file.getName().contains(".jar") || file.getName().contains(".class"))
                runner.runsScript(arg);
		} catch (Exception e) {
			 e.printStackTrace();
		}
    }

    public void searchFile(File file){
		try {
			if (file == null) return;
			Arrays.stream(file.listFiles())
					.forEach(n -> {
						if (!n.exists()) System.out.println("ファイルナス");
						else if (n.isDirectory()) searchFile(n);
						else if (n.getPath().endsWith(".jar")) list.add(n);
					});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void performExtract(String saveDirectory, String extractDirectory){
		File file = new File(saveDirectory);
//		createBirthmarkDirectory(extractDirectory);
		list = new ArrayList<>();
		searchFile(file);
		System.out.println("検索終わったあああああああああああああああああああああああああああああああああ");
		list.stream()
			.filter(n -> n.getName().contains(".jar"))
            .forEach(n -> extract(n, extractDirectory));
	}
}
