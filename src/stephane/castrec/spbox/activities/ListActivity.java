package stephane.castrec.spbox.activities;

import stephane.castrec.spbox.fragment.ListSoundFragment;
import stephane.castrec.spbox.objects.Constants.Categories;
import android.os.Bundle;

public class ListActivity extends AbstractFramentActivity {

	private ListSoundFragment mList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listactivitylayout);

		mList = (ListSoundFragment) this.getSupportFragmentManager().findFragmentById(R.id.list);
		if(this.getIntent().getExtras() != null && this.getIntent().getExtras() != null)
			switch ((Categories)this.getIntent().getExtras().get(DashboardActivity.INTENT_KEY)) {
			case ALL:
				mList.updateList(Categories.ALL, "");
				break;
			case FAVORITES:
				mList.updateList(Categories.FAVORITES, "");
				break;
			case LAST_UPDATE:
				mList.updateList(Categories.LAST_UPDATE, "");
				break;
			case BY_NAME:
				mList.updateList(Categories.BY_NAME, this.getIntent().getStringExtra(DashboardActivity.INTENT_NAMES));
				break;
			}
	}


}
