package stephane.castrec.spbox.activities;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class AbstractFramentActivity extends FragmentActivity {

	private GoogleAnalyticsTracker tracker = null;

	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu , menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.request:
	        	//Request for a sound
				/* Fill it with Data */
				Intent intent = new Intent();
				intent.setType("plain/text");
				intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"stephane.castrec@gmail.com"});
				intent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
						this.getString(R.string.app_name)+": Demande de sons");
				intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
				/* Send it off to the Activity-Chooser */
				this.startActivity(Intent.createChooser(intent, "Send mail..."));
	        	return true;
	        case R.id.rate:
	        	//Rate app
	        	String appPackageName = this.getApplication().getPackageName().toString();
	        	Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName));
	        	marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	        	startActivity(marketIntent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	public GoogleAnalyticsTracker getTracker(){
		if(tracker == null){	        
			tracker = GoogleAnalyticsTracker.getInstance();
	        tracker.startNewSession("UA-31275559-1", this);
		} return tracker;
	}
	
}
