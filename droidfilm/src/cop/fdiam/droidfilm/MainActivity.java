package cop.fdiam.droidfilm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore.Images;

public class MainActivity extends TabActivity {
	/*private Activity myact = null;
	private ListView maListViewPerso;
	private TabHost tabHost;
	private TabSpec tabSpec;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TabHost tabHost = getTabHost();
        
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("List");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.ic_launcher));
        Intent photosIntent = new Intent(this, secondactivity.class);
        photosIntent.putExtra("main", "test");
        photospec.setContent(photosIntent);
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Favorite");       
        songspec.setIndicator("Songs", getResources().getDrawable(R.drawable.ic_launcher));
        //photosIntent.putExtra("main", false);
        songspec.setContent(photosIntent);
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab // Adding videos tab
	    /*maListViewPerso = (ListView) findViewById(R.id.listviewperso);
		myact=this;
        
		new Thread(new Runnable() {
			public void run() {
				HttpRetriever request = new HttpRetriever();

				JSONParser jParser = new JSONParser();
				final JSONObject json = jParser
						.getJSONFromUrl("https://api.themoviedb.org/3/movie/upcoming?api_key=ca2145f2cd78643fae53b88ee4cf5b2d");
				try {
					JSONArray jsonarray = json.getJSONArray("results");
					Log.v("testjsonarray","testjsonarray "+jsonarray.get(0).toString());
					ArrayList<Movie> list = new ArrayList<Movie>();
					for(int i=0;i<jsonarray.length();i++){
						list.add(new Movie(jsonarray.getJSONObject(i),myact));
		
					}
					
					for(int i=0;i<list.size();i++)
						Log.v("listmovie", "listmovie "+list.get(i).toString());
					
					ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
			        
			        for (Movie movie : list) {
			        	HashMap<String, Object> map;
						 
				       
				        map = new HashMap<String, Object>();

				        map.put("titre", movie.getName());
				      
				        map.put("description", movie.getDate());
				       
				        map.put("img", movie.getAffiche());

				        listItem.add(map);
					}
			        final ArrayList<HashMap<String, Object>> listIem2 = listItem;
			        final ArrayList<Movie> Listfinal = list;
			        runOnUiThread (new Thread(new Runnable() { 
			            public void run() {
			            	ExtendedSimpleAdapter mSchedule = new ExtendedSimpleAdapter (myact.getBaseContext(), listIem2, R.layout.fragment_main,
				               new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description},Listfinal);
				 
				  
				        maListViewPerso.setAdapter(mSchedule);
				  
			            }}));
				        
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				

			}
		}).start();
		
 
 

 
      
 
      
 
       

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

		} catch (IOException ex) {

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

				JSONParser jParser = new JSONParser();
				final JSONObject json = jParser.getJSONFromUrl(path);
				try {
					JSONArray jsonarray = json.getJSONArray("results");
					Log.v("testjsonarray","testjsonarray "+jsonarray.get(0).toString());
					ArrayList<Movie> list = new ArrayList<Movie>();
					for(int i=0;i<jsonarray.length();i++){
						list.add(new Movie(jsonarray.getJSONObject(i),myact));
						
					}
					
					for(int i=0;i<list.size();i++)
						Log.v("listmovie", "listmovie "+list.get(i).toString());
					
					ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
					
			        
			        for (Movie movie : list) {
			        	HashMap<String, Object> map;
						 
				        
				        map = new HashMap<String, Object>();
				       
				        map.put("titre", movie.getName());
				        
				        map.put("description", movie.getDate());
				        
				        map.put("img", movie.getAffiche());
				        listItem.add(map);
					}
			        final ArrayList<HashMap<String, Object>> listIem2 = listItem;
			        final ArrayList<Movie> Listfinal = list;
			        runOnUiThread (new Thread(new Runnable() { 
			            public void run() {
			            	ExtendedSimpleAdapter mSchedule = new ExtendedSimpleAdapter (myact.getBaseContext(), listIem2, R.layout.fragment_main,
				               new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description},Listfinal);
				 

				        maListViewPerso.setAdapter(mSchedule);

			            }}));
				        
				} catch (JSONException e) {
				
					e.printStackTrace();
				}
				
				

			}
		}).start();*/
		
	}
}
