package stephane.castrec.spbox.util;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.util.Log;

import stephane.castrec.spbox.activities.R;
import stephane.castrec.spbox.database.ManageFavoritesBDD;
import stephane.castrec.spbox.objects.Constants.Pers;
import stephane.castrec.spbox.objects.Sounds;

public class SoundsGetter {

	private static ManageFavoritesBDD mBdd = null;

	static private List<Sounds> mSounds = null;
	static private List<Sounds> mFavorites = null;
	static private List<Sounds> mLastNamesSearch = null;
	static private List<Sounds> mLastUpdate = null;
	
	static private AssetManager mMgr = null;

	static private List<String> mPers = null;
	static private List<String> mBddFavorites = null;
	
	static private Context mContext = null;
	
	private static int versionCode ;


	static public List<String> getListPers(){
		if(mPers == null)
			mPers = new LinkedList<String>();
		return mPers;
	}
	
	public static boolean initSounds(Context pContext) {
		mContext = pContext;
		try {
			mPers = new LinkedList<String>();
			mSounds = new LinkedList<Sounds>();
			mFavorites = new LinkedList<Sounds>();
			mLastUpdate = new LinkedList<Sounds>();
			
			PackageInfo pinfo = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), 0);
			versionCode = pinfo.versionCode;
			mBdd = new ManageFavoritesBDD(mContext);
			mBddFavorites = mBdd.getAllFavorites();
			mMgr = mContext.getAssets();
			String[] files = mMgr.list("");
			setSoundsFromFilesList(files, null);
			return true;
		} catch (Exception e) {
			Log.e("spbox", "SoundsGetter getSoundsList exception",e);
		}

		return false;
	}
	
	

	public static List<Sounds> getSoundsList(){
		if(mSounds == null){
			mSounds = new LinkedList<Sounds>();
		}
		return mSounds;
	}
	
	public static List<Sounds> getFavoritesSoundsList(){
		if(mFavorites == null){
			mFavorites = new LinkedList<Sounds>();
		}
		return mFavorites;
	}
	
	public static List<Sounds> getLastUpdateSoundsList(){
		if(mLastUpdate == null){
			mLastUpdate = new LinkedList<Sounds>();
		}
		return mLastUpdate;
	}

	/**
	 * checkIfFolder
	 * @param file
	 * @return int : number of reference versionCode 
	 * 				-1 if not a folder
	 */
	private static int checkIfFolder(String file){
		try {
			return Integer.parseInt(file);
		} catch (Exception e) {
			Log.d("spbox", "SoundsGetter checkIfFolder Le path "+ "file" +"n'est pas un dossier");
		}
		return -1;
	}

	private static void setSoundsFromFilesList(String[] files, String folder){
		try {
			for (String file : files){
				if(file.toLowerCase().contains(".mp3") || file.toLowerCase().contains(".wav") ){
					addSound(file, folder);
				} else {
					int codeVersion = checkIfFolder(file);
					if(codeVersion != -1){
						setSoundsFromFilesList(mMgr.list(file), file);
					}
				}
			}			
		} catch (Exception e) {
			Log.e("spbox", "SoundsGetter setSoundsFromFilesList exception",e);
		}
	}

	private static boolean addSound(String file, String folder){
		Sounds lsound = new Sounds();
		try {
			lsound = new Sounds();
			lsound.setIsFavorite(false);
			lsound.setName(getSoundName(file));
			lsound.setfileName(file);
			lsound.setFolder(folder);
			String persName = setPersInfo(mContext, file.substring(0, file.indexOf("_")), lsound);
			if(!mPers.contains(persName)){
				mPers.add(persName);
			}
			//check if add to favorites
			if(mBddFavorites.contains(lsound.getfileName())){
				lsound.setIsFavorite(true);
			} else {
				lsound.setIsFavorite(false);
			}
			//check if add to last update
			if(lsound.getFolder() != null && Integer.parseInt(lsound.getFolder()) == versionCode){
				addLastUpdate(lsound);
			}
			mSounds.add(lsound);
			return true;
		} catch (Exception e) {
			Log.e("spbox", "SoundsGetter getSoundsList exception1",e);
		}
		return false;
	}
	private static String getSoundName(String pFileName){
		int index = pFileName.indexOf("_");
		String name = pFileName.substring(index, pFileName.length()).replace("_", " ");
		name = name.toLowerCase().replace(".mp3", "");
		return name.toLowerCase().replace(".wav", "");
	}


	private static String setPersInfo(Context pContext, String pName, Sounds pSounds){
		if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.cartman))){
			pSounds.setPers(pContext.getResources().getString(R.string.cartman));
			pSounds.setPersCode(Pers.CARTMAN);			
		}else if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.kenny))){
			pSounds.setPers(pContext.getResources().getString(R.string.kenny));
			pSounds.setPersCode(Pers.KENNY);
		}else if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.kyle))){
			pSounds.setPers(pContext.getResources().getString(R.string.kyle));
			pSounds.setPersCode(Pers.KYLE);
		}else if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.stan))){
			pSounds.setPers(pContext.getResources().getString(R.string.stan));
			pSounds.setPersCode(Pers.STAN);
		}else if(pName.equalsIgnoreCase(pContext.getResources().getString(R.string.mackey))){
			pSounds.setPers(pContext.getResources().getString(R.string.mackey));
			pSounds.setPersCode(Pers.MACKEY);
		}else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.garrison))) {
			pSounds.setPers(pContext.getResources().getString(R.string.garrison));
			pSounds.setPersCode(Pers.GARRISON);
		} else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.chef))) {
			pSounds.setPers(pContext.getResources().getString(R.string.chef));
			pSounds.setPersCode(Pers.CHEF);
		} else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.jimbo))) {
			pSounds.setPers(pContext.getResources().getString(R.string.jimbo));
			pSounds.setPersCode(Pers.JIMBO);
		} else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.principale))) {
			pSounds.setPers(pContext.getResources().getString(R.string.principale));
			pSounds.setPersCode(Pers.PRINCIPAL);
		}  else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.barbrady))) {
			pSounds.setPers(pContext.getResources().getString(R.string.barbrady));
			pSounds.setPersCode(Pers.BARBRADY);
		}  else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.servietsky))) {
			pSounds.setPers(pContext.getResources().getString(R.string.servietsky));
			pSounds.setPersCode(Pers.SERVIETSKY);
		}  else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.jesus))) {
			pSounds.setPers(pContext.getResources().getString(R.string.jesus));
			pSounds.setPersCode(Pers.JESUS);
		} else if (pName.equalsIgnoreCase("gdperemarch")) {
			pSounds.setPers(pContext.getResources().getString(R.string.gdperemarch));
			pSounds.setPersCode(Pers.GD_PERE_MARCH);
		} else if (pName.equalsIgnoreCase(pContext.getResources().getString(R.string.jimmy))) {
			pSounds.setPers(pContext.getResources().getString(R.string.jimmy));
			pSounds.setPersCode(Pers.JIMMY);
		} else if (pName.equalsIgnoreCase("algay")) {
			pSounds.setPers(pContext.getResources().getString(R.string.algay));
			pSounds.setPersCode(Pers.ALGAY);
		}  else if (pName.equalsIgnoreCase("timmy")) {
			pSounds.setPers(pContext.getResources().getString(R.string.timmy));
			pSounds.setPersCode(Pers.TIMMY);
		} else {
			pSounds.setPers(pContext.getResources().getString(R.string.other));
			pSounds.setPersCode(Pers.OTHERS);
		} 
		return pSounds.getPers();
	}



	public static List<Sounds> getSoundFromName(String name){
		mLastNamesSearch = new LinkedList<Sounds>();
		try {
			for(int i=0; i<mSounds.size(); i++){
				if(mSounds.get(i).getPers().equalsIgnoreCase(name)){
					mLastNamesSearch.add(mSounds.get(i));
				}
			}
		} catch (Exception e) {
			Log.e("spbox", "getSoundFromName exception",e);
		}
		return mLastNamesSearch;
	}

	private static void addLastUpdate(Sounds pSound){
		try {
			if(mLastUpdate == null){
				mLastUpdate = new LinkedList<Sounds>();
			} 
			mLastUpdate.add(pSound);
		} catch (Exception e) {
			Log.e("spbox", "addLastUpdate exception",e);
		}
	}
	
	public static void addFavorite(Sounds pSound){
		try {
			if(mFavorites == null){
				mFavorites = new LinkedList<Sounds>();
			} 
			if(!mFavorites.contains(pSound)){
				mFavorites.add(pSound);
			}
		} catch (Exception e) {
			Log.e("spbox", "addFavorite exception",e);
		}
	}
	public static void removeFavorite(Sounds pSound){
		try {
			if(mFavorites != null)
				mFavorites.remove(pSound);
		} catch (Exception e) {
			Log.e("spbox", "addFavorite exception",e);
		}
	}
}
