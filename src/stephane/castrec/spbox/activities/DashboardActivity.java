package stephane.castrec.spbox.activities;

import java.lang.reflect.Array;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import stephane.castrec.spbox.objects.Constants;
import stephane.castrec.spbox.objects.Constants.Categories;
import stephane.castrec.spbox.objects.Constants.Pers;
import stephane.castrec.spbox.util.PopupDisplayer;
import stephane.castrec.spbox.util.SoundsGetter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DashboardActivity extends AbstractFramentActivity implements OnClickListener {
	
	private Button mbtnAll;
	private Button mbtnFavorites;
	private Button mbtnSearch;
	private Button mBtnLast;
	private Context mContext;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);
        mContext = this;
        mbtnAll = (Button) findViewById(R.id.dash_all);
        mbtnAll.setOnClickListener(this);
        mbtnFavorites = (Button) findViewById(R.id.dash_favorites);
        mbtnFavorites.setOnClickListener(this);
        mbtnSearch = (Button) findViewById(R.id.dash_search);
        mbtnSearch.setOnClickListener(this);
        mBtnLast = (Button)findViewById(R.id.dash_last);
        mBtnLast.setOnClickListener(this);
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
				startActivity(i);
				break;
			case R.id.dash_favorites:
				i.putExtra(INTENT_KEY, Categories.FAVORITES);
				startActivity(i);
				break;
			case R.id.dash_search:
				search();
				break;
			case R.id.dash_last:
				i.putExtra(INTENT_KEY, Categories.LAST_UPDATE);
				startActivity(i);
				break;
			}
		} catch (Exception e) {
			Log.e("spbox", "DashboardActivity onClick exception",e);
		}		
	}
	
	private void search(){
		try {
			final CharSequence[] items = SoundsGetter.getListPers().toArray(new CharSequence[SoundsGetter.getListPers().size()]);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTitle(this.getString(R.string.select_char));
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	Intent i = new Intent(mContext, ListActivity.class);
					i.putExtra(INTENT_KEY, Categories.BY_NAME);
					i.putExtra(INTENT_NAMES, SoundsGetter.getListPers().get(item));
					startActivity(i);
			    }
			});
			AlertDialog alert = builder.create();	
			alert.show();
		} catch (Exception e) {
			Log.e("spbox", "DashboardActivity search exception",e);
		}
	}
}