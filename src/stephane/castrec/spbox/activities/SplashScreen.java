package stephane.castrec.spbox.activities;

import stephane.castrec.spbox.util.SoundsGetter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashScreen extends Activity {
	
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 1000;
	
	private SplashScreen mThis;
	
	private Intent i;

	private Runnable splashRunnable = new Runnable() {
		@Override
		public void run() {
			SoundsGetter.initSounds(mThis);
	        Message msg = new Message();
	        msg.what = STOPSPLASH;
	        quitHandler.sendMessageDelayed(msg, SPLASHTIME);			
		}
	};
	
	private Handler quitHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {	
			case STOPSPLASH:	
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
        Thread t = new Thread(splashRunnable);
        t.start();
    }
}
