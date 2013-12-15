package it.falcon.massivefilerenamer;

import it.falcon.massivefilerenamer.filemanager.FileWriterManager;
import it.falcon.massivefilerenamer.utils.Constants;
import it.falcon.massivefilerenamer.utils.Constants.RENAME_TYPES;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/***
 * 
 * @author Marco De Zorzi
 *
 */
public class RenameActivity extends Activity {

	private String currentParentDir = "";
	private String currentDir = "";
	private String currentSearchType = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true); //FIXME: NON VA
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("Rename files");
		getActionBar().show();
		
		Intent i = getIntent();
		currentParentDir = i.getStringExtra("parentDir");
		currentDir = i.getStringExtra("currDir");
		currentSearchType = i.getStringExtra("searchType");
		
		setContentView(R.layout.activity_rename);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rename, menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
          // go to previous screen when app icon in action bar is clicked
	    	Intent backIntent = new Intent(this, ListFilesActivity.class);
	    	backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    	backIntent.putExtra("currDir", currentDir);
	    	backIntent.putExtra("parentDir", currentParentDir);
	    	backIntent.putExtra("searchType", currentSearchType);
	        startActivity(backIntent);
	        return true;
	    default:
	    	super.onOptionsItemSelected(item);
	      break;
	    }
	    return true;
	  }

	public void renameFiles(View v){
		FileWriterManager mgr = new FileWriterManager();
		Intent i = getIntent();
		Toast.makeText(getApplicationContext(), "Renamed! "+currentDir, Toast.LENGTH_LONG).show();
		
		/*Get user input*/
		EditText filterField = (EditText) findViewById(R.id.editText_list_filter1);  
		String filter = filterField.getText().toString();//TODO: da capire come usarlo
		EditText extFilterField = (EditText) findViewById(R.id.editText_ext_filter1);  
		String extFilter = extFilterField.getText().toString();//FIXME: Da rendere multicheck
		Spinner renameTypeSpinner = (Spinner) findViewById(R.id.spinner_rename_type);  
		String renameType = renameTypeSpinner.getSelectedItem().toString();
		EditText renamePatternField = (EditText) findViewById(R.id.editText_rename_pattern);  
		String renamePattern = renamePatternField.getText().toString();
		EditText renameToField = (EditText) findViewById(R.id.editText_rename_to);  
		String renameTo = renameToField.getText().toString();
		
		if(currentDir==null) {
			Toast.makeText(getApplicationContext(), "Error: the directory does not exist", Toast.LENGTH_LONG).show();
			return;
		}
		
		mgr.renameFiles(currentDir, extFilter.equals("")?null:new String[]{extFilter}, decodeRenameSpinner(renameType), renamePattern, renameTo);
		Intent backIntent = new Intent(this, ListFilesActivity.class);
    	backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	backIntent.putExtra("currDir", currentDir);
    	backIntent.putExtra("parentDir", currentParentDir);
    	backIntent.putExtra("searchType", currentSearchType);
        startActivity(backIntent);
	}

	private RENAME_TYPES decodeRenameSpinner(String renameType) {
		// TODO Auto-generated method stub
		Constants.RENAME_TYPES retType = null;
		if(renameType!=null){
			if(renameType.equals(getResources().getString(R.string.rename_type1))) retType = Constants.RENAME_TYPES.COUNTER_BACK;
			if(renameType.equals(getResources().getString(R.string.rename_type2))) retType = Constants.RENAME_TYPES.COUNTER_FWD;
			if(renameType.equals(getResources().getString(R.string.rename_type3))) retType = Constants.RENAME_TYPES.PREFIX;
			if(renameType.equals(getResources().getString(R.string.rename_type4))) retType = Constants.RENAME_TYPES.SUFFIX;
			if(renameType.equals(getResources().getString(R.string.rename_type5))) retType = Constants.RENAME_TYPES.CONTAINS;
		}
		return retType;		
	}
}
