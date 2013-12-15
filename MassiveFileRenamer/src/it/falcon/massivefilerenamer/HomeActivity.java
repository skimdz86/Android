package it.falcon.massivefilerenamer;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/***
 * 
 * @author Marco De Zorzi
 *
 */
public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
//		Map<String, String> evnVars = System.getenv();
//		for(String s: evnVars.keySet()){
//			System.out.println("key: "+s+"; VALUE: "+evnVars.get(s));
//		}
//		Toast.makeText(getApplicationContext(),	System.getenv("EXTERNAL_STORAGE"), Toast.LENGTH_SHORT).show();
//		System.out.println();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.quit://solo per testing, nella versione definitiva niente exit
	      this.finish();
	      System.exit(0);
	      break;
	    default:
	    	super.onOptionsItemSelected(item);
	      break;
	    }
	    return true;
	  }
	
	public void listInternalStorage(View v){
		Intent i = new Intent(getApplicationContext(), ListFilesActivity.class);
		i.putExtra("searchType", "internal");
		startActivity(i);
	}
	public void listExternalStorage(View v){
		Intent i = new Intent(getApplicationContext(), ListFilesActivity.class);
		i.putExtra("searchType", "external");
		startActivity(i);
	}
	public void listSystemDirectories(View v){
		Intent i = new Intent(getApplicationContext(), ListFilesActivity.class);
		i.putExtra("searchType", "system");
		startActivity(i);
	}
}
