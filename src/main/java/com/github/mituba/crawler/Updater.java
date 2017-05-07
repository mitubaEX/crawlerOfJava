package com.github.mituba.crawler;

import java.io.File;
import java.util.Arrays;

public class Updater {
	public void deleteDir(String deleteDirectory){
		File dir = new File(deleteDirectory);
		if(dir.exists()){
            Arrays.stream(dir.listFiles())
                .forEach(n -> n.delete());
            dir.delete();
		}
	}
}
