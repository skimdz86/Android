package it.falcon.massivefilerenamer;

import it.falcon.massivefilerenamer.listener.SwipeDetector;
import it.falcon.massivefilerenamer.thread.AsyncFileList;
import it.falcon.massivefilerenamer.thread.FileListParams;
import it.falcon.massivefilerenamer.thread.FileListResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/***
 * 
 * @author Marco De Zorzi
 *
 */
public class ListFilesActivity extends Activity{

	private static final int SWIPE_MIN_DISTANCE = 100;//pixel
	private static final int SWIPE_MAX_OFF_PATH = 300;//pixel
	private static final int SWIPE_THRESHOLD_VELOCITY = 10;//pixels per second
	private GestureDetector gestureScanner;
	
	private String currentParentDir = "";
	private String currentDir = "";
	private String currentSearchType = "";//TODO: da togliere, è solo per non far spaccare il back nel rename
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_list_files);
		getActionBar().setDisplayHomeAsUpEnabled(true); //servirà quando avro una home
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("List files");
		getActionBar().show();
		
		String baseDir = null;
		
		Intent i = getIntent();

		
		if(i.getStringExtra("searchType")!=null) currentSearchType = i.getStringExtra("searchType"); 
		
		if(i.getStringExtra("currDir")!=null) currentDir = i.getStringExtra("currDir");
		if(i.getStringExtra("parentDir")!=null) currentParentDir = i.getStringExtra("parentDir");
		
		if(currentDir == null || currentDir.equals("")){
			if(currentSearchType.equals("external")){
				//TODO: qui per farla bene bisognerebbe usare la env var SECONDARY_STORAGE
				baseDir = Environment.getExternalStorageDirectory().getParent() + File.separator + "extSdCard";
			}
			else if(currentSearchType.equals("internal")) {
				baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
			}
			else if(currentSearchType.equals("system")) {
				baseDir = Environment.getRootDirectory().getAbsolutePath();
			}
			else {
				//nothing
				Toast.makeText(getApplicationContext(),	"Error", Toast.LENGTH_SHORT).show();
			}
		}
		else baseDir = currentDir;
		
		if(baseDir!=null){
			currentParentDir = baseDir;
			currentDir = baseDir;
			getFileList(baseDir, null);
		}
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_files, menu);
		return super.onCreateOptionsMenu(menu);
	}


	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_up_one_level:
	      getFileList(currentParentDir, null);
	      break;
	    case R.id.action_rename:
	    	Intent renameIntent = new Intent(this, RenameActivity.class);
            renameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            renameIntent.putExtra("currDir", currentDir);
            renameIntent.putExtra("parentDir", currentParentDir);
            renameIntent.putExtra("searchType", currentSearchType);
            startActivity(renameIntent);
            return true;
	    case android.R.id.home:
            // go to previous screen when app icon in action bar is clicked
	    	Intent intent = new Intent(this, HomeActivity.class);
	        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
	        return true;
	    default:
	    	super.onOptionsItemSelected(item);
	      break;
	    }
	    return true;
	  }
	
	
	private void getFileList(String baseDir, String[] extensions){
		
		int separatorIndex = baseDir.lastIndexOf(File.separator);
		if(separatorIndex==-1 || !(new File(baseDir).exists())){
			Toast.makeText(getApplicationContext(),	"Invalid folder", Toast.LENGTH_SHORT).show();
		}
		else {
			currentDir = baseDir;
			currentParentDir = baseDir.substring(0, separatorIndex);
			
			TextView pathBarView = (TextView)findViewById(R.id.pathBar1);
			pathBarView.setText(currentDir);
			
//			FileReaderManager readManager = new FileReaderManager();
			
			/* Gestione lista con task asincrono */
			AsyncFileList asyncFileListCall = new AsyncFileList();
			FileListParams params = new FileListParams();
			params.setBaseDir(baseDir);
			params.setExtensions(extensions);
			AsyncTask<FileListParams, Integer, FileListResult> asyncCallExecute = asyncFileListCall.execute(params);
			List<File> fileList = new ArrayList<File>();
			
			FileListResult fileResult;
			try {
				fileResult = asyncCallExecute.get();
				if(fileResult!=null) fileList = fileResult.getFileList();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(),	"Error retrieving files", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(),	"Error retrieving files", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			
			
			//non usato, ora uso gli asyncTask (se dà problemi, torno alla gestione semplice sincrona)
//			List<File> fileList = readManager.listFiles(false, baseDir, extensions);
			
			 ArrayAdapter<File> directoryList = new ArrayAdapter<File>(this, android.R.layout.simple_list_item_1, fileList);
		     ListView listView = (ListView)findViewById(R.id.listView1);
		     listView.setAdapter(directoryList); 
		     final SwipeDetector swipeDetector = new SwipeDetector();
		     listView.setOnTouchListener(swipeDetector);
		     
		     listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (swipeDetector.swipeDetected()){
		                    // do the onSwipe action
//							Toast.makeText(getApplicationContext(),	"Swiiiiiiiiiiiipe", Toast.LENGTH_SHORT).show();
							if(swipeDetector.leftToRightSwipeDetected()) getFileList(currentParentDir, null);
		                } else {
		                    // do the onItemClick action
//		                	Toast.makeText(getApplicationContext(),	((TextView) view).getText(), Toast.LENGTH_SHORT).show();
						    File fd = new File(((TextView) view).getText().toString());
						    if(fd.exists() && fd.isDirectory()) getFileList(fd.getPath(), null);
		                }
					}
				});
		}
		
	}

	
}
