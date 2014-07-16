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
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.os.Build;
import android.provider.MediaStore.Images;

public class MainActivity extends Activity {
	private Activity myact = null;
	private ListView maListViewPerso;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testlayout);
	    maListViewPerso = (ListView) findViewById(R.id.listviewperso);
		myact=this;
        //Création de la ArrayList qui nous permettra de remplire la listView
        
		new Thread(new Runnable() {
			public void run() {
				HttpRetriever request = new HttpRetriever();
				/*final InputStream test = request
						.retrieveStream("https://api.themoviedb.org/3/discover/movie?api_key=ca2145f2cd78643fae53b88ee4cf5b2d");*/
				final TextView testview = (TextView) findViewById(R.id.testview);
				JSONParser jParser = new JSONParser();
				final JSONObject json = jParser
						.getJSONFromUrl("https://api.themoviedb.org/3/movie/upcoming?api_key=ca2145f2cd78643fae53b88ee4cf5b2d");
				try {
					JSONArray jsonarray = json.getJSONArray("results");
					Log.v("testjsonarray","testjsonarray "+jsonarray.get(0).toString());
					ArrayList<Movie> list = new ArrayList<Movie>();
					for(int i=0;i<jsonarray.length();i++){
						list.add(new Movie(jsonarray.getJSONObject(i),(ImageView)findViewById(R.id.imageview),myact,testview));
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
					 
			        //On déclare la HashMap qui contiendra les informations pour un item
			        
			        for (Movie movie : list) {
			        	HashMap<String, Object> map;
						 
				        //Création d'une HashMap pour insérer les informations du premier item de notre listView
				        map = new HashMap<String, Object>();
				        //on insère un élément titre que l'on récupérera dans le textView titre créé dans le fichier affichageitem.xml
				        map.put("titre", movie.getName());
				        //on insère un élément description que l'on récupérera dans le textView description créé dans le fichier affichageitem.xml
				        map.put("description", movie.getDate());
				        //on insère la référence à l'image (convertit en String car normalement c'est un int) que l'on récupérera dans l'imageView créé dans le fichier affichageitem.xml
				        //map.put("img", getImageUri(myact, movie.getAffiche()));
				        map.put("img", movie.getAffiche());
				        //enfin on ajoute cette hashMap dans la arrayList
				        listItem.add(map);
					}
			        final ArrayList<HashMap<String, Object>> listIem2 = listItem;
			        runOnUiThread (new Thread(new Runnable() { 
			            public void run() {
			            	ExtendedSimpleAdapter mSchedule = new ExtendedSimpleAdapter (myact.getBaseContext(), listIem2, R.layout.fragment_main,
				               new String[] {"img", "titre", "description"}, new int[] {R.id.img, R.id.titre, R.id.description});
				 
				        //On attribut à notre listView l'adapter que l'on vient de créer
				        maListViewPerso.setAdapter(mSchedule);
				 
				        //Enfin on met un écouteur d'évènement sur notre listView
				        maListViewPerso.setOnItemClickListener(new OnItemClickListener() {
							@Override
				        	@SuppressWarnings("unchecked")
				         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
								//on récupère la HashMap contenant les infos de notre item (titre, description, img)
				        		HashMap<String, String> map = (HashMap<String, String>) maListViewPerso.getItemAtPosition(position);
				        		//on créer une boite de dialogue
				        		AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
				        		//on attribut un titre à notre boite de dialogue
				        		adb.setTitle("Sélection Item");
				        		//on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
				        		adb.setMessage("Votre choix : "+map.get("titre"));
				        		//on indique que l'on veut le bouton ok à notre boite de dialogue
				        		adb.setPositiveButton("Ok", null);
				        		//on affiche la boite de dialogue
				        		adb.show();
				        	}
				         });
			            }}));
				        
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONObject jsonarray = json.getJSONObject("results");
					Log.v("testjsonobjet","testjsonobjet "+jsonarray);
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
		
 
 

 
        //On refait la manip plusieurs fois avec des données différentes pour former les items de notre ListView
 
      
 
       

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

}
