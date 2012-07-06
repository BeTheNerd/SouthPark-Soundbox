package stephane.castrec.spbox.fragment;

import java.io.IOException;
import java.util.List;

import stephane.castrec.spbox.activities.AbstractFramentActivity;
import stephane.castrec.spbox.activities.ListActivity;
import stephane.castrec.spbox.activities.R;
import stephane.castrec.spbox.adapter.SoundsAdapter;
import stephane.castrec.spbox.database.ManageFavoritesBDD;
import stephane.castrec.spbox.objects.Sounds;
import stephane.castrec.spbox.objects.Constants.Categories;
import stephane.castrec.spbox.util.PopupDisplayer;
import stephane.castrec.spbox.util.Ringtones;
import stephane.castrec.spbox.util.SoundsGetter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;

public class ListSoundFragment extends ListFragment implements OnClickListener, OnLongClickListener, OnPreparedListener, OnCompletionListener {

	private Categories mCategory = Categories.ALL;
	private List<Sounds> mSounds;
	private MediaPlayer mplayer = null;
	private Button mClickedButton = null;
	private ManageFavoritesBDD mBdd = null;
	private Context mContext = null;
	
	static private final String URL_SOUNDCLOUD = "http://soundcloud.com/stephane-11/";
	static private final String URL_STORE = "https://play.google.com/store/apps/details?id=stephane.castrec.spbox.activities";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContext = this.getActivity();
	}

	public void updateList(Categories pCat, String pObj){
		mCategory = pCat;
		switch (mCategory) {
		case ALL:
			mSounds = SoundsGetter.getSoundsList();
			break;			
		case FAVORITES:
			mSounds = SoundsGetter.getFavoritesSoundsList();
			if(mSounds.size() == 0){
				Toast.makeText(this.getActivity(), this.getActivity().getString(R.string.no_fav), Toast.LENGTH_LONG).show();
				this.getActivity().finish();
				return;
			}
			break;
		case BY_NAME:
			mSounds = SoundsGetter.getSoundFromName(pObj);
			break;
		case LAST_UPDATE:
			mSounds = SoundsGetter.getLastUpdateSoundsList();
		}
		setListAdapter(new SoundsAdapter(this.getActivity(), this, mSounds));
		PopupDisplayer.displayLoadingDialog(this.getActivity(), false);	

	}


	@Override
	public void onClick(View v) {
		try {
			Sounds lsound = mSounds.get(Integer.parseInt(v.getTag().toString()));
			switch (v.getId()) {
			case R.id.item_play:
				managePlayer(v, lsound);				
				break;
			case R.id.item_ratingBar:
				manageFavorite(v, lsound);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			Log.e("SPBox", "onCLick",e);
		}		
	}

	@Override
	public boolean onLongClick(View v) {
		Sounds lsound = mSounds.get(Integer.parseInt(v.getTag().toString()));
		moreActions(lsound);
		return false;
	}

	private void moreActions(final Sounds pSound){
		final CharSequence[] items = {/*this.getActivity().getString(R.string.more_actions_ringtones),*/ 
				this.getActivity().getString(R.string.more_actions_copy), this.getActivity().getString(R.string.more_share)};

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		builder.setTitle(this.getString(R.string.more_actions));
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				String msg = "";
				switch (item) {
				/*case 0:
			    	if(Ringtones.setAsRingtone(mContext, pSound)){
			    		msg = mContext.getString(R.string.msg_ringtone_ok);
			    	} else {
			    		msg = mContext.getString(R.string.msg_ringtone_error);
			    	}
					break;*/
				case 0:
					if(Ringtones.copyFileToSdCard(mContext, pSound, Ringtones.pathSouthParkFolder)){
						msg = mContext.getString(R.string.msg_copy_ok)+" "+Ringtones.pathSouthParkFolder+pSound.getfileName();
					} else {
						msg = mContext.getString(R.string.msg_copy_error);
					}
					break;
				case 1:
					shareSound(pSound);
					break;

				default:
					break;
				}
				Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog alert = builder.create();	
		alert.show();
	}

	private void shareSound(Sounds pSound) {
		try {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			String pathSound = pSound.getfileName().replaceAll("_", "-").replace("."+pSound.getExtension(), "");
			if(pathSound.length() > 30){
				pathSound = pathSound.substring(0, pathSound.lastIndexOf("-"));
			}
			intent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name));
			intent.putExtra(Intent.EXTRA_TEXT,URL_SOUNDCLOUD+pathSound+"  "+mContext.getString(R.string.share_msg)+" "+URL_STORE);
			startActivity(Intent.createChooser(intent, mContext.getString(R.string.share_title)));
		} catch (Exception e) {
			Log.e("SPBox", "shareSound exception",e);
		}		
	}

	private void managePlayer(View v, Sounds pSound){
		try {
			if(mplayer != null && mplayer.isPlaying()){
				//same button clicked
				if(v.equals(mClickedButton)){
					//stop pressed
					stopCurrentSong();
				} else {
					//start sound
					stopCurrentSong();

					playSound(pSound.getPathToPlay());
					((Button)v).setBackgroundResource(R.drawable.stop);
				}
			} else {
				//play clicked
				playSound(pSound.getPathToPlay());
				((Button)v).setBackgroundResource(R.drawable.stop);
			}
			mClickedButton = (Button)v;
		} catch (Exception e) {
			Log.e("SPBox", "managePlayer",e);
		}
	}

	/**
	 * manageFavorite
	 * @param v
	 * @param pSound
	 */
	private void manageFavorite(View v, Sounds pSound){
		try{
			mBdd = new ManageFavoritesBDD(mContext);
			if(pSound.getIsFavorite()) {
				//rm from favorite
				if(mBdd.addFavorite(pSound) != -1){
					mBdd.removeFavorite(pSound);
					pSound.setIsFavorite(false);
				} else {
					//TODO error
				}
				((ImageButton)v).setImageResource(R.drawable.btn_star_big_off);
			} else {
				//add to favorite
				if(mBdd.addFavorite(pSound) != -1){
					((ImageButton)v).setImageResource(R.drawable.btn_star_big_on_pressed);
					pSound.setIsFavorite(true);
				} else {
					//TODO Error
				}
			}
		} catch(Exception e) {
			Log.e("SPBox", "manageFavorite",e);
		}
	}

	private void stopCurrentSong(){
		try {
			mplayer.stop();
			mClickedButton.setBackgroundResource(R.drawable.play);
		} catch (Exception e) {
			Log.e("SPBox", "stopCurrentSong",e);
		}
	}

	private void playSound(String path){
		try {
			AssetFileDescriptor afd = this.getActivity().getAssets().openFd(path);
			//getSystemService(Context.AUDIO_SERVICE);
			this.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
			if(mplayer != null){
				mplayer.release();
			}
			mplayer = new MediaPlayer();
			mplayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			mplayer.setOnPreparedListener(this);
			//mplayer.setOnSeekCompleteListener(this);
			mplayer.setOnCompletionListener(this);
			mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//mplayer.setVolume(MediaPlayer., rightVolume)
			mplayer.prepare();

			((AbstractFramentActivity)this.getActivity()).getTracker().trackPageView(path);

		} catch (IOException e) {
			Log.e("SPBox", "playSound",e);
			if(mClickedButton != null){
				mClickedButton.setBackgroundResource(R.drawable.play);
			}
		}
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		try{
			mplayer.start();
		} catch (Exception e) {
			Log.e("SPBox", "onPrepared",e);
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if(mClickedButton != null){
			mClickedButton.setBackgroundResource(R.drawable.play);
		}	
	}
}
