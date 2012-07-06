package stephane.castrec.spbox.database;

import java.util.LinkedList;
import java.util.List;

import stephane.castrec.spbox.objects.Sounds;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ManageFavoritesBDD {
	private static final String NOM_BDD = "spbox.db";

	private static final String TABLE_PRODUCT = "favorite_product";
	private static final String COL_ID = "id";
	private static final String COL_PATH = "path";
	private static final String COL_PERS = "pers";



	private SQLiteDatabase _bdd;

	private  BaseSQLite _BaseSqlite;

	public ManageFavoritesBDD(Context context){
		//On crŽer la BDD et sa table
		_BaseSqlite = new BaseSQLite(context, NOM_BDD, null, 1);
		//_BaseSqlite.getWritableDatabase();
	}

	private void open(Boolean read){
		//on ouvre la BDD en Žcriture
		/*if(read)
			_bdd = _BaseSqlite.getReadableDatabase();
		else*/
			_bdd = _BaseSqlite.getWritableDatabase();
	}

	public void close(){
		//on ferme l'accs ˆ la BDD
		try {
			if(_bdd.isOpen()){
				_bdd.close();
			}
		} catch (Exception e) {
			Log.e("spbox","ManageFavoritesBdd close exception",e);
		}
	}

	public SQLiteDatabase getBDD(){
		return _bdd;
	}

	public long addFavorite(Sounds pSound){
		try	{
			open(false);
			//CrŽation d'un ContentValues (fonctionne comme une HashMap)
			ContentValues values = new ContentValues();
			//on lui ajoute une valeur associŽ ˆ une clŽ (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
			values.put(COL_PATH, pSound.getfileName());
			values.put(COL_PERS, pSound.getPers());
			//on insre l'objet dans la BDD via le ContentValues
			long id = _bdd.insert(TABLE_PRODUCT, null, values);
			close();
			return id;
		}
		catch (Exception e) {
			Log.e("spbox", "ManageFavoritesBDD insertProduct exception",e);
		}
		return -1;
	}

	public long removeFavorite(Sounds pSound){
		try	{
			open(false);

			//on insre l'objet dans la BDD via le ContentValues
			long id =  _bdd.delete(TABLE_PRODUCT, COL_PATH + " = \"" +pSound.getfileName()+"\"", null);
			return id;
		} catch (SQLException e) {
			Log.i("spbox", "ManageFavoritesBDD insertProduct "+e.getMessage());
		}
		catch (Exception e) {
			Log.e("spbox", "ManageFavoritesBDD insertProduct exception",e);
		}
		close();
		return -1;
	}

	public boolean isFavorite(Sounds pSounds){
		try {
			open(true);
			//SQLiteDatabase sqlDB = _BaseSqlite.getWritableDatabase();
			Cursor c = _bdd.query(TABLE_PRODUCT, new String[] {COL_ID, COL_PATH }, "path=\""+pSounds.getfileName()+"\"", null, null, null, null);
			if(c.getCount()>0){
				close();
				return true;
			}

		} catch (Exception e) {
			Log.e("spbox", "isFavorite  exception",e);
		}
		close();
		return false;
	}

	public List<Sounds> getSoundsFromPers(Sounds pSounds){
		LinkedList<Sounds> list = new LinkedList<Sounds>();
		try {
			open(true);
			//SQLiteDatabase sqlDB = _BaseSqlite.getWritableDatabase();
			Cursor c = _bdd.query(TABLE_PRODUCT, new String[] {COL_ID, COL_PATH, COL_PERS }, "pers=\""+pSounds.getPers()+"\"", null, null, null, null);
			for(int i=0; i< c.getCount(); i++){
				list.add(cursorToProduct(c, i));
			}
			close();

		} catch (Exception e) {
			Log.e("spbox", "isFavorite  exception",e);
		}
		close();
		return list;
	}

	//Cette mŽthode permet de convertir un cursor en un sound
	private Sounds cursorToProduct(Cursor c, int i){
		try	{
			//si aucun ŽlŽment n'a ŽtŽ retournŽ dans la requte, on renvoie null
			if (c.getCount() == 0)
				return null;
			//Sinon on se place sur le premier ŽlŽment
			c.moveToPosition(i);
			Sounds s = new Sounds();
			s.setfileName(c.getString(1));
			s.setPers(c.getString(2));
			return s;
		}
		catch (Exception e) {
			Log.e("spbox", "ManageFavoritesBdd cursorToProduct exception",e);
		}
		return null;
	}

	public List<String> getAllFavorites() {
		LinkedList<String> list = new LinkedList<String>();
		try {
			open(true);
			//SQLiteDatabase sqlDB = _BaseSqlite.getWritableDatabase();
			Cursor c = _bdd.query(TABLE_PRODUCT, new String[] {COL_ID, COL_PATH, COL_PERS }, null, null, null, null, null);
			for(int i=0; i< c.getCount(); i++){
				list.add(cursorToProduct(c, i).getfileName());
			}
			close();
		} catch (Exception e) {
			Log.e("spbox", "isFavorite  exception",e);
		}
		return list;
	}
}
