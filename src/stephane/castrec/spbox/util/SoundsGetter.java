package stephane.castrec.spbox.util;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import stephane.castrec.spbox.activities.R;
import stephane.castrec.spbox.database.ManageFavoritesBDD;
import stephane.castrec.spbox.objects.Constants.Pers;
import stephane.castrec.spbox.objects.Sounds;

public class SoundsGetter {

	private static ManageFavoritesBDD mBdd = null;
	
	public List<Sounds> mSounds = null;

	public static List<Sounds> getSoundsList(Context pContext, boolean getOnlyFavorites){
		List<Sounds> lSounds = new LinkedList<Sounds>();
		Sounds lsound = new Sounds();
		mBdd = new ManageFavoritesBDD(pContext);
		try {
			AssetManager mgr = pContext.getAssets();
			String[] files = mgr.list("");
			for (String file : files){
				if(file.contains(".mp3") || file.contains(".wav") ){
					Log.i("spbox", "file: "+file);
					try {
						lsound = new Sounds();
						lsound.setIsFavorite(false);
						lsound.setName(getName(file));
						lsound.setPath(file);
						setPersInfo(pContext, file.substring(0, file.indexOf("_")), lsound);
						List<String> lFavorites = mBdd.getAllFavorites();
						if(lFavorites.contains(lsound.getPath())){
							lsound.setIsFavorite(true);
						} else {
							lsound.setIsFavorite(false);
						}
						if(!getOnlyFavorites){
							lSounds.add(lsound);
						} else {
							if(lsound.getIsFavorite()){
								lSounds.add(lsound);
							}
						}
					} catch (Exception e) {
						Log.e("spbox", "SoundsGetter getSoundsList exception1",e);
					}
				}
			}
		} catch (Exception e) {
			Log.e("spbox", "SoundsGetter getSoundsList exception",e);
		}

		return lSounds;
	}

	private static String getName(String pFileName){
		int index = pFileName.indexOf("_");
		String name = pFileName.substring(index, pFileName.length()).replace("_", " ");
		name = name.replace(".mp3", "");
		return name.replace(".wav", "");
	}

	private static boolean setPersInfo(Context pContext, String pName, Sounds pSounds){
		if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.cartman))){
			pSounds.setPers(pContext.getResources().getString(R.string.cartman));
			pSounds.setPersCode(Pers.CARTMAN);
			return true;
		}else if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.kenny))){
			pSounds.setPers(pContext.getResources().getString(R.string.kenny));
			pSounds.setPersCode(Pers.KENNY);
			return true;
		}else if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.kyle))){
			pSounds.setPers(pContext.getResources().getString(R.string.kyle));
			pSounds.setPersCode(Pers.KYLE);
			return true;
		}else if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.stan))){
			pSounds.setPers(pContext.getResources().getString(R.string.stan));
			pSounds.setPersCode(Pers.STAN);
			return true;
		}else if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.mackey))){
			pSounds.setPers(pContext.getResources().getString(R.string.mackey));
			pSounds.setPersCode(Pers.MACKEY);
			return true;
		}else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.garrison))) {
			pSounds.setPers(pContext.getResources().getString(R.string.garrison));
			pSounds.setPersCode(Pers.GARRISON);
			return true;
		} else {
			pSounds.setPers(pContext.getResources().getString(R.string.other));
			pSounds.setPersCode(Pers.OTHERS);
			return true;
		}
	}
}
