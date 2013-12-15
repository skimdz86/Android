package it.falcon.massivefilerenamer.thread;

import it.falcon.massivefilerenamer.filemanager.FileReaderManager;

import java.io.File;
import java.util.List;

import android.os.AsyncTask;

public class AsyncFileList extends AsyncTask<FileListParams, Integer, FileListResult> {

	@Override
	protected FileListResult doInBackground(FileListParams... params) {
		// TODO Auto-generated method stub
		FileReaderManager readManager = new FileReaderManager();
		List<File> fileList = readManager.listFiles(params[0].getBaseDir(), params[0].getExtensions());
		FileListResult res = new FileListResult();
		res.setFileList(fileList);
		return res;
	}

	@Override
	protected void onPostExecute(FileListResult result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	
	
}
