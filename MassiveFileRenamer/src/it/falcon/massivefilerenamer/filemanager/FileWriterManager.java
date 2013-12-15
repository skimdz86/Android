package it.falcon.massivefilerenamer.filemanager;

import it.falcon.massivefilerenamer.utils.Constants;

import java.io.File;
import java.util.List;

/***
 * 
 * @author Marco De Zorzi
 *
 */
public class FileWriterManager {

	/**
	 * @param currentDirectory Directory in cui rinominare i file
	 * @param extensions lista di estensioni su cui fare filtro per il rename
	 * @param renameType Tipo di rename da applicare (la lista è chiusa)
	 * @param renamePattern Espressione da sostituire (opzionale, ad es se il tipo è counter si sostituisce tutto)
	 * @param renameToApply Espressione che verrà sostituita alla attuale
	 * */
	public void renameFiles(String currentDirectory, String[] extensions, Constants.RENAME_TYPES renameType, String renamePattern, String renameToApply){
//		File dir = new File(currentDirectory);
		FileReaderManager mgr = new FileReaderManager();
		List<File> filesToRenameList = mgr.listOnlyFiles(currentDirectory, extensions);
		
		switch (renameType){
			case COUNTER_BACK: {
				loopAndRenameCounter(filesToRenameList, renameToApply, Constants.RENAME_TYPES.COUNTER_BACK);
				break;
			}
			case COUNTER_FWD: {
				loopAndRenameCounter(filesToRenameList, renameToApply, Constants.RENAME_TYPES.COUNTER_FWD);
				break;
			} 
			case PREFIX: {
				loopAndRenameSimple(filesToRenameList, Constants.RENAME_TYPES.PREFIX, null, renameToApply);
				break;
			}
			case SUFFIX: {
				loopAndRenameSimple(filesToRenameList, Constants.RENAME_TYPES.SUFFIX, null, renameToApply);
				break;
			}
			case CONTAINS: {
				loopAndRenameSimple(filesToRenameList, Constants.RENAME_TYPES.CONTAINS, renamePattern, renameToApply);
				break;
			}
			default: break;
		}
		
	}
	
	private void loopAndRenameSimple(List<File> filesToRenameList, Constants.RENAME_TYPES renameType, String renamePattern, String renameToApply){
		for(File f: filesToRenameList){
			
			boolean outcome;
			String newPath = f.getPath();
			String fileName = f.getName();
			int separatorIndex = newPath.lastIndexOf(File.separator);
			if(separatorIndex!=-1){
				newPath = newPath.substring(0, separatorIndex);
			}
			switch (renameType){
				case PREFIX: {
					fileName = renameToApply + fileName;
					break;
				}
				case SUFFIX: {
					fileName = fileName + renameToApply;
					break;
				}
				case CONTAINS: {
					fileName = fileName.replaceAll(renamePattern, renameToApply);
					break;
				}
				default: break;//qui non si verifica mai
			}
			
			newPath = newPath + File.separator + fileName;
			if(!fileName.equals(f.getName())){//solo se è cambiato (se cade nel default no)
				outcome = f.renameTo(new File(newPath));
			}
			
		}
	}
	
	private void loopAndRenameCounter(List<File> filesToRenameList, String renameToApply, Constants.RENAME_TYPES renameType){
		int counter = 1;
		int listSize = filesToRenameList.size();
		for(File f: filesToRenameList){
			
			boolean outcome;
			String newPath = f.getPath();
			int separatorIndex = newPath.lastIndexOf(File.separator);
			if(separatorIndex!=-1){
				newPath = newPath.substring(0, separatorIndex);
			}
			String fileName = f.getName();
			System.out.println("Rename di "+newPath+File.separator+fileName);
			switch(renameType){
				case COUNTER_BACK: {
					String realName = "", extension = "";
					int dotSeparator = fileName.lastIndexOf(".");
					if(dotSeparator!=-1){
						realName = fileName.substring(0, dotSeparator);
						extension = fileName.substring(dotSeparator);
						fileName = realName + "_" + padCounter(counter, listSize) + extension;
					}
					break;
				}
				case COUNTER_FWD: {
					fileName = padCounter(counter, listSize) + "_" + fileName; 
					break;
				}
				default: break;//qui non si verifica mai
			}
			newPath = newPath + File.separator + fileName;
			if(!fileName.equals(f.getName())){//solo se è cambiato (se cade nel default no)
				outcome = f.renameTo(new File(newPath));
			}
			
		}
	}
	
	private String padCounter(Integer currentValue, Integer totalSize){
		int currLength = currentValue.toString().length(); 
		int totLength = totalSize.toString().length();
		String pad = "";
		for (int i=0; i<totLength-currLength; i++) pad+="0";
		return pad+=currentValue.toString();
	}
}
