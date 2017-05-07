package com.github.mituba.crawler;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.script.ScriptException;

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
		ScriptRunnerBuilder builder = new ScriptRunnerBuilder();
		ScriptRunner runner = builder.build();
		String[] arg = { "./extract.js", kindOfBirthmark, file.getPath(), "./" + extractDirectory + "/" + file.getName() + "-"+ kindOfBirthmark +".txt" };
		try {
			System.out.println(file.getPath());
			runner.runsScript(arg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void performExtract(String saveDirectory, String extractDirectory){
		File file = new File(saveDirectory);
		createBirthmarkDirectory(extractDirectory);
		Arrays.stream(file.listFiles())
            .forEach(n -> extract(n, extractDirectory));
	}
}
