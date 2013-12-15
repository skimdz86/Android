package it.falcon.massivefilerenamer.filemanager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 
 * @author Marco De Zorzi
 *
 */
public class FileReaderManager {



	public List<File> listFiles(String base, String[] extensions) {
		
		String basePath = (base==null?"/":base);
		File baseDir = new File(basePath);
		
		final String[] finalExt = extensions==null?new String[0]:extensions;
		
//		String extensionRegex = "(";
//		for (String s: extensions){
//			extensionRegex+="|"+s.toLowerCase();
//		}
//		extensionRegex = extensionRegex.replaceFirst("\\(\\|", "(") + ")";
//		extensionRegex = "[^\\s]+\\." + extensionRegex;
//		System.out.println("REGEX = "+extensionRegex);
		
		File[] fileList = baseDir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				if(finalExt.length==0) return true;
				boolean fileAccept = false;
				for(String s: finalExt){
					if(name.toLowerCase(Locale.ENGLISH).endsWith(s)|| new File(dir+name).isDirectory()) {
						fileAccept = true;
						break;
					}
				}
		        return fileAccept;
		    }
		});
		
		return fileList!=null?Arrays.asList(fileList):new ArrayList<File>();
	}

	//esclude le directory
	public List<File> listOnlyFiles(String base, String[] extensions) {
		
		String basePath = (base==null?"/":base);
		File baseDir = new File(basePath);
		
		final String[] finalExt = extensions==null?new String[0]:extensions;
			
		File[] fileList = baseDir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {
				if(finalExt.length==0) return true;
				boolean fileAccept = false;
				for(String s: finalExt){
					if(name.toLowerCase(Locale.ENGLISH).endsWith(s) && new File(dir+name).isFile()) {
						fileAccept = true;
						break;
					}
				}
		        return fileAccept;
		    }
		});
		
		return Arrays.asList(fileList);
	}
	
}
