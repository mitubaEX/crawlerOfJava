package com.github.mituba.crawler;

import java.io.File;
import java.util.Arrays;


import com.github.pochi.runner.scripts.ScriptRunner;
import com.github.pochi.runner.scripts.ScriptRunnerBuilder;

public class Extractor {
	private String kindOfBirthmark;

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
            String[] arg = { "./extract.js", kindOfBirthmark, file.getPath(), "./" + extractDirectory + "/" + file.getName() + "-"+ kindOfBirthmark +".txt" };
			if(file.length() == 0)
				return;
            if(file.getName().contains(".jar") || file.getName().contains(".class")){}
                runner.runsScript(arg);
		} catch (Exception e) {
			 e.printStackTrace();
		}
    }

	public void performExtract(String saveDirectory, String extractDirectory){
		File file = new File(saveDirectory);
		createBirthmarkDirectory(extractDirectory);
		Arrays.stream(file.listFiles())
			.filter(n -> n.getName().contains(".jar"))
            .forEach(n -> extract(n, extractDirectory));
	}
}
