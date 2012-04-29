package stephane.castrec.spbox.fragment;

import stephane.castrec.spbox.activities.DashboardActivity;
import stephane.castrec.spbox.activities.R;
import stephane.castrec.spbox.activities.R.id;
import stephane.castrec.spbox.activities.R.layout;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ActionBarFragment extends Fragment implements OnClickListener {

	private ImageView mHome;
	private Context mContext;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.actionbar, container, false);
		mHome = (ImageView) view.findViewById(R.id.actionbar_home);
		mHome.setOnClickListener(this);
		mContext = inflater.getContext();
		return view;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_home:
			startActivity(new Intent(mContext, DashboardActivity.class));
			break;
		default:
			break;
		}
	}
}
