package stephane.castrec.spbox.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import stephane.castrec.spbox.objects.Sounds;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.RingtonePreference;
import android.provider.MediaStore;
import android.util.Log;

public class Ringtones {

	/**
	 * path to store file
	 */
	private static String pathRingtones = "/media/audio/ringtones/";

	final public static String pathSouthParkFolder = "/southpark/sounds/";



	static public boolean setAsRingtone(Context pContext, Sounds sound){
		try {
			copyFileToSdCard(pContext, sound, pathRingtones);

			File k = new File(pathRingtones, sound.getfileName()); // path is a file to /sdcard/media/ringtone

			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
			values.put(MediaStore.MediaColumns.TITLE, sound.getfileName().replace("."+sound.getExtension(), ""));
			values.put(MediaStore.MediaColumns.SIZE, k.length());

			values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/"+sound.getExtension());
			values.put(MediaStore.Audio.Media.ARTIST, "South Park");
			//values.put(MediaStore.Audio.Media.DURATION, 2300);
			values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
			values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
			values.put(MediaStore.Audio.Media.IS_ALARM, true);
			values.put(MediaStore.Audio.Media.IS_MUSIC, true);
			
			//Insert it into the database
			Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
			try{
				pContext.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + k.getAbsolutePath() + "\"", null);
			} catch (Exception e) {	}
			Uri newUri = pContext.getContentResolver().insert(uri, values);

			RingtoneManager.setActualDefaultRingtoneUri(pContext,
					RingtoneManager.TYPE_RINGTONE, newUri);
			return true;
		} catch (Exception e) {
			Log.e("SPBox", "Ringtones setAsRingtones exception",e);
		}
		return false;
	}

	static public boolean copyFileToSdCard(Context pContext, Sounds sound, String path){
		File f = new File(Environment.getExternalStorageDirectory() + path);
		if(!f.isDirectory()) {
			f.mkdirs();
		} 
		AssetManager assetManager = pContext.getAssets();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(sound.getPathToPlay());
			out = new FileOutputStream(Environment.getExternalStorageDirectory()+path+sound.getfileName());
			copyFile(in, out);
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
			return true;
		} catch(Exception e) {
			Log.e("tag", e.getMessage());
		}  
		return false;
	}
	private static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}

}
