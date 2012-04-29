package stephane.castrec.spbox.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class PopupDisplayer {

	static private ProgressDialog mpd;


	static public void displayLoadingDialog(Context pContext, boolean pDisplay){
		try {
			if(pDisplay){
				if(mpd != null && !mpd.isShowing()){
					mpd = new ProgressDialog(pContext);
					mpd.show(pContext, "", "Loading");
				}
			} else {
				if(mpd != null){
					mpd.cancel();
					mpd = null;
				}
			}
		} catch (Exception e) {
			Log.e("spbox", "PopupDisplayer displayLoadingDialog exception",e);
		}
	}

}
