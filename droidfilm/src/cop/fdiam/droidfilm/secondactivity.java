package cop.fdiam.droidfilm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore.Images;

public class secondactivity  extends Activity{
	private Activity myact = null;
	private ListView maListViewPerso;
	private TabHost tabHost;
	private TabSpec tabSpec;
	private String main;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);       
	    maListViewPerso = (ListView) findViewById(R.id.listviewperso);
		myact=this;
        LinearLayout searchlayout=(LinearLayout)findViewById(R.id.searchlayout);
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    this.main=extras.getString("main");
		}
		new Thread(new Runnable() {
			public void run() {
				HttpRetriever request = new HttpRetriever();
				/*final InputStream test = request
						.retrieveStream("https://api.themoviedb.org/3/discover/movie?api_key=ca2145f2cd78643fae53b88ee4cf5b2d");*/
				JSONParser jParser = new JSONParser();
				final JSONObject json = jParser
						.getJSONFromUrl("https://api.themoviedb.org/3/movie/upcoming?api_key=ca2145f2cd78643fae53b88ee4cf5b2d");
				try {
					JSONArray jsonarray = json.getJSONArray("results");
					Log.v("testjsonarray","testjsonarray "+jsonarray.get(0).toString());
					ArrayList<Movie> list = new ArrayList<Movie>();
					for(int i=0;i<jsonarray.length();i++){
						list.add(new Movie(jsonarray.getJSONObject(i),myact));
						/*try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
					}
					
					for(int i=0;i<list.size();i++)
						Log.v("listmovie", "listmovie "+list.get(i).toString());
					
					ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
					 
			        //On d�clare la HashMap qui contiendra les informations pour un item
			        DBManageFavorite manage = new DBManageFavorite(myact);
			        list= new ArrayList<Movie>();
			        List all = manage.getAllMovies();
			        for (Object object : all) {
						list.add((Movie)object);
					}

			        for (Movie movie : list) {
			        	HashMap<String, Object> map;
						 
				        //Cr�ation d'une HashMap pour ins�rer les informations du premier item de notre listView
				        map = new HashMap<String, Object>();
				        //on ins�re un �l�ment titre que l'on r�cup�rera dans le textView titre cr�� dans le fichier affichageitem.xml
				        map.put("titre", movie.getName());
				        //on ins�re un �l�ment description que l'on r�cup�rera dans le textView description cr�� dans le fichier affichageitem.xml
				        map.put("description", movie.getDate());
				        //on ins�re la r�f�rence � l'image (convertit en String car normalement c'est un int) que l'on r�cup�rera dans l'imageView cr�� dans le fichier affichageitem.xml
				        //map.put("img", getImageUri(myact, movie.getAffiche()));
				        map.put("img", movie.getAffiche());
				        //enfin on ajoute cette hashMap dans la arrayList
				        listItem.add(map);
					}
			        final ArrayList<HashMap<String, Object>> listIem2 = listItem;
			        final ArrayList<Movie> Listfinal = list;
			        runOnUiThread (new Thread(new Runnable() { 
			            public void run() {
			            	ExtendedSimpleAdapter mSchedule = new ExtendedSimpleAdapter (myact.getBaseContext(), listIem2, R.layout.fragment_main,
				               new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description},Listfinal);
				 
				        //On attribut � notre listView l'adapter que l'on vient de cr�er
				        maListViewPerso.setAdapter(mSchedule);
				        //Enfin on met un �couteur d'�v�nement sur notre listView
			            }}));
				        
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//final String slurped = slurp(test, 50);
				/*runOnUiThread(new Runnable() {

					@Override
					public void run() {
						testview.setText(json.toString());
					}
				});*/

			}
		}).start();
		
 
 

 
        //On refait la manip plusieurs fois avec des donn�es diff�rentes pour former les items de notre ListView
 
      
 
       

	}

	public static String slurp(final InputStream is, final int bufferSize) {
		final char[] buffer = new char[bufferSize];
		final StringBuilder out = new StringBuilder();
		try {
			final Reader in = new InputStreamReader(is, "UTF-8");
			try {
				for (;;) {
					int rsz = in.read(buffer, 0, buffer.length);
					if (rsz < 0)
						break;
					out.append(buffer, 0, rsz);
				}
			} finally {
				in.close();
			}
		} catch (UnsupportedEncodingException ex) {
			/* ... */
		} catch (IOException ex) {
			/* ... */
		}
		return out.toString();
	}
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
		  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		  inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		  String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		  return Uri.parse(path);
		}

	public void search(View v){
		EditText search = (EditText)findViewById(R.id.search);
		final String path ="https://api.themoviedb.org/3/search/movie?api_key=ca2145f2cd78643fae53b88ee4cf5b2d&query="+search.getText().toString();
		new Thread(new Runnable() {
			public void run() {
				/*final InputStream test = request
						.retrieveStream("https://api.themoviedb.org/3/discover/movie?api_key=ca2145f2cd78643fae53b88ee4cf5b2d");*/
				JSONParser jParser = new JSONParser();
				final JSONObject json = jParser.getJSONFromUrl(path);
				try {
					JSONArray jsonarray = json.getJSONArray("results");
					Log.v("testjsonarray","testjsonarray "+jsonarray.get(0).toString());
					ArrayList<Movie> list = new ArrayList<Movie>();
					for(int i=0;i<jsonarray.length();i++){
						list.add(new Movie(jsonarray.getJSONObject(i),myact));
						/*try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
					}
					
					for(int i=0;i<list.size();i++)
						Log.v("listmovie", "listmovie "+list.get(i).toString());
					
					ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
					 
			        //On d�clare la HashMap qui contiendra les informations pour un item
			        
			        for (Movie movie : list) {
			        	HashMap<String, Object> map;
						 
				        //Cr�ation d'une HashMap pour ins�rer les informations du premier item de notre listView
				        map = new HashMap<String, Object>();
				        //on ins�re un �l�ment titre que l'on r�cup�rera dans le textView titre cr�� dans le fichier affichageitem.xml
				        map.put("titre", movie.getName());
				        //on ins�re un �l�ment description que l'on r�cup�rera dans le textView description cr�� dans le fichier affichageitem.xml
				        map.put("description", movie.getDate());
						// on ins�re la r�f�rence � l'image (convertit en String
						// car normalement c'est un int) que l'on r�cup�rera dans l'imageView cr�� dans le fichier affichageitem.xml
				        //map.put("img", getImageUri(myact, movie.getAffiche()));
				        map.put("img", movie.getAffiche());
				        //enfin on ajoute cette hashMap dans la arrayList
				        listItem.add(map);
					}
			        final ArrayList<HashMap<String, Object>> listIem2 = listItem;
			        final ArrayList<Movie> Listfinal = list;
			        runOnUiThread (new Thread(new Runnable() { 
			            public void run() {
			            	ExtendedSimpleAdapter mSchedule = new ExtendedSimpleAdapter (myact.getBaseContext(), listIem2, R.layout.fragment_main,
				               new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description},Listfinal);
				 
				        //On attribut � notre listView l'adapter que l'on vient de cr�er
				        maListViewPerso.setAdapter(mSchedule);

			            }}));
				        
				} catch (JSONException e) {

					e.printStackTrace();
				}
				


			}
		}).start();
		
	}
}
