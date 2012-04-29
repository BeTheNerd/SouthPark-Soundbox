package stephane.castrec.spbox.activities;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import stephane.castrec.spbox.objects.Constants.Categories;
import stephane.castrec.spbox.util.PopupDisplayer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DashboardActivity extends AbstractFramentActivity implements OnClickListener {
	
	static public final String INTENT_KEY = "list";
	private Button mbtnAll;
	private Button mbtnFavorites;
	private Button mbtnSearch;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        
        mbtnAll = (Button) findViewById(R.id.dash_all);
        mbtnAll.setOnClickListener(this);
        mbtnFavorites = (Button) findViewById(R.id.dash_favorites);
        mbtnFavorites.setOnClickListener(this);
        mbtnSearch = (Button) findViewById(R.id.dash_search);
        mbtnSearch.setOnClickListener(this);
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	PopupDisplayer.displayLoadingDialog(this, false);
    }

	@Override
	public void onClick(View v) {
		try {
			PopupDisplayer.displayLoadingDialog(this, true);
			Intent i = new Intent(this, ListActivity.class);
			switch (v.getId()) {
			case R.id.dash_all:
				i.putExtra(INTENT_KEY, Categories.ALL);
				break;
			case R.id.dash_favorites:
				i.putExtra(INTENT_KEY, Categories.FAVORITES);
				break;
			case R.id.dash_search:
				
				break;
			}
			startActivity(i);
		} catch (Exception e) {
			Log.e("spbox", "DashboardActivity onClick exception",e);
		}
		
	}
}