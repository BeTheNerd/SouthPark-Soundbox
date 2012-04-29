package stephane.castrec.spbox.activities;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashScreen extends Activity {
	
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 2000;
	
	private SplashScreen mThis;
	
	private Intent i;

	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {	
			case STOPSPLASH:	
				//remove SplashScreen from view
				startActivity(i);
				mThis.finish();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("spboxfr", "SplashScreen onCreate Start");
        setContentView(R.layout.spashscreen);
        mThis = this;
        i = new Intent (this, DashboardActivity.class);
        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }
}
