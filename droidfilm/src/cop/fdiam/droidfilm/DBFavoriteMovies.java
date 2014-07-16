package cop.fdiam.droidfilm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBFavoriteMovies extends SQLiteOpenHelper{

	public static final String FAVORITES = "Favorites";
	public static final String MOVIE_ID= "id";
	public static final String MOVIE_NAME= "name";
	public static final String MOVIE_DATE= "date";
	public static final String MOVIE_NOTE= "note";
	//Gestion d'image
	public static final String MOVIE_IMAGE= "image";
	
	public static final String DATABASE_NAME="Favorite.db";
	
	//create DataBase
	private static final String DATABASE_CREATE = 
			"create table " + FAVORITES
			+ "(" + MOVIE_ID  +" integer NOT NULL primary key,"
			+ MOVIE_NAME + " text not null,"
			+ MOVIE_DATE + " text not null,"
			+ MOVIE_NOTE + " text not null,"
			+ MOVIE_IMAGE + " blob);";
	
	public DBFavoriteMovies(Context context) 
	{
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + FAVORITES);
		onCreate(db);
	}
	

}
