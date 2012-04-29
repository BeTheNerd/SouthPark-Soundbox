package stephane.castrec.spbox.fragment;

import java.io.IOException;
import java.util.List;

import stephane.castrec.spbox.activities.AbstractFramentActivity;
import stephane.castrec.spbox.activities.R;
import stephane.castrec.spbox.adapter.SoundsAdapter;
import stephane.castrec.spbox.database.ManageFavoritesBDD;
import stephane.castrec.spbox.objects.Sounds;
import stephane.castrec.spbox.objects.Constants.Categories;
import stephane.castrec.spbox.util.PopupDisplayer;
import stephane.castrec.spbox.util.SoundsGetter;
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
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.Toast;

public class ListSoundFragment extends ListFragment implements OnClickListener, OnPreparedListener, OnCompletionListener {

	private Categories mCategory = Categories.ALL;
	private List<Sounds> mSounds;
	private MediaPlayer mplayer = null;
	private Button mClickedButton = null;
	private ManageFavoritesBDD mBdd = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public void updateList(Categories pCat){
		mCategory = pCat;
		switch (mCategory) {
		case ALL:
			mSounds = SoundsGetter.getSoundsList(this.getActivity(), false);
			break;			
		case FAVORITES:
			mSounds = SoundsGetter.getSoundsList(this.getActivity(), true);
			break;
		}
		if(mSounds.size() == 0){
			Toast.makeText(this.getActivity(), this.getActivity().getString(R.string.no_fav), Toast.LENGTH_LONG).show();
			this.getActivity().finish();
		} else {
			setListAdapter(new SoundsAdapter(this.getActivity(), this, mSounds));
			PopupDisplayer.displayLoadingDialog(this.getActivity(), false);	
		}
	}

	@Override
	public void onClick(View v) {
		try {
			Sounds lsound = mSounds.get(Integer.parseInt(v.getTag().toString()));
			if(v.getClass().equals(ImageButton.class)){
				mBdd = new ManageFavoritesBDD(this.getActivity());
				if(lsound.getIsFavorite()) {
					//rm from favorite
					if(mBdd.addFavorite(lsound) != -1){
						mBdd.removeFavorite(lsound);
						lsound.setIsFavorite(false);
					} else {
						//TODO error
					}
					((ImageButton)v).setImageResource(R.drawable.btn_star_big_off);
				} else {
					//add to favorite
					if(mBdd.addFavorite(lsound) != -1){
						((ImageButton)v).setImageResource(R.drawable.btn_star_big_on_pressed);
						lsound.setIsFavorite(true);
					} else {
						//TODO Error
					}
				}
			} else {
				if(mplayer != null && mplayer.isPlaying()){
					//same button clicked
					if(v.equals(mClickedButton)){
						//stop pressed
						mplayer.stop();
						((Button)v).setBackgroundResource(R.drawable.play);
					} else {
						//start another sound
						playSound(lsound.getPath());
						((Button)v).setBackgroundResource(R.drawable.stop);
					}
				} else {
					//play clicked
					playSound(lsound.getPath());
					((Button)v).setBackgroundResource(R.drawable.stop);
				}
				mClickedButton = (Button)v;
			}
		} catch (Exception e) {
			Log.e("SPBox", "onCLick",e);
		}		
	}

	private void playSound(String path){
		try {
			
			AssetFileDescriptor afd = this.getActivity().getAssets().openFd(path);
			//getSystemService(Context.AUDIO_SERVICE);
			this.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
		mClickedButton.setBackgroundResource(R.drawable.play);		
	}
}
