package stephane.castrec.spbox.adapter;

import java.util.List;

import stephane.castrec.spbox.activities.R;
import stephane.castrec.spbox.database.ManageFavoritesBDD;
import stephane.castrec.spbox.objects.Constants.Categories;
import stephane.castrec.spbox.objects.Sounds;
import stephane.castrec.spbox.util.SoundsGetter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class SoundsAdapter extends BaseAdapter {
	
	private List<Sounds> mSounds;
	private Context mContext;
	private Fragment mFragment;
	
	private ImageButton mRatingBar;
	private ImageView mImg;
	private TextView mTitle;
	private Button mPlay;
	

	
	public SoundsAdapter(Context pContext, Fragment pFrag, List<Sounds> pSounds){
		mContext = pContext;
		mSounds = pSounds;
		mFragment = pFrag;
	}

	@Override
	public int getCount() {
		return mSounds.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mSounds.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View pView, ViewGroup parent) {
		if (pView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			pView = inflater.inflate(R.layout.itemsounds, null);
		} 
		pView.setTag("item");//set tag to the view to recognize it and know the position 
		mTitle = (TextView)pView.findViewById(R.id.item_title);//get the title component
		mImg = (ImageView)pView.findViewById(R.id.item_pers);//get the title component
		mRatingBar = (ImageButton)pView.findViewById(R.id.item_ratingBar);//get the title component
		mPlay = (Button) pView.findViewById(R.id.item_play);
		mRatingBar.setOnClickListener((OnClickListener)mFragment);
		mRatingBar.setTag(position);
		if(mSounds.get(position).getIsFavorite()){
			mRatingBar.setImageResource(R.drawable.btn_star_big_on_pressed);
		} else {	
			mRatingBar.setImageResource(R.drawable.btn_star_big_off);
		}
			
		mPlay.setOnClickListener((OnClickListener)mFragment);
		mPlay.setTag(position);
		String title = mSounds.get(position).getName();
		if(title.length() > 15){
			title = title.substring(0, 16)+"...";
		}
		mTitle.setText(title);
		switch (mSounds.get(position).getPersCode()) {
		case KENNY:
			mImg.setImageResource(R.drawable.kenny);
			break;
		case KYLE:
			mImg.setImageResource(R.drawable.kyle);
			break;
		case MACKEY:
			mImg.setImageResource(R.drawable.mckey);
			break;
		case CARTMAN:
			mImg.setImageResource(R.drawable.cartman);
			break;
		case OTHERS:
			mImg.setImageResource(R.drawable.icon);
			break;
		case STAN:
			mImg.setImageResource(R.drawable.stan);
			break;
		case GARRISON:
			mImg.setImageResource(R.drawable.garrison);
			break;
		}
		
		return pView;
	}

}
