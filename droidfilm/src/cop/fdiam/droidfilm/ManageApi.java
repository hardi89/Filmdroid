package cop.fdiam.droidfilm;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ManageApi extends AsyncTask<Void, Void, Void> {
	private Context myact;
	private final String apikey = "ca2145f2cd78643fae53b88ee4cf5b2d";
	private String url;
	private HttpRetriever request;
	JSONParser jParser;
	private ListView maListViewPerso;
	ArrayList<Movie> list;
	ArrayList<HashMap<String, Object>> listItem;
	Activity myactivity;
	public ManageApi(Context context, ListView list,Activity act) {
		this.myact = context;
		request = new HttpRetriever();
		jParser = new JSONParser();
		this.maListViewPerso = list;
		this.myactivity=act;
	}

	public void getAllNewMovies() {
		url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + apikey;
		this.execute();
	}

	public void getMovieByKey(String key) {
		url = "https://api.themoviedb.org/3/search/movie?api_key=" + apikey
				+ "&query=" + key;
		this.execute();
	}
	
	public void getAllFavoris() {
		url = null;
		this.execute();
	}

	@Override
	protected void onPostExecute(Void result) {
		ExtendedSimpleAdapter mSchedule = new ExtendedSimpleAdapter(myact,
				listItem, R.layout.fragment_main, new String[] { "img",
						"titre", "description" }, new int[] { R.id.img,
						R.id.titre, R.id.description }, list,myactivity);

		// On attribut � notre listView l'adapter que l'on vient de
		// cr�er
		maListViewPerso.setAdapter(mSchedule);
		super.onPostExecute(result);
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (url != null) {
			url=url.replace(" ", "%20");
			final JSONObject json = jParser.getJSONFromUrl(url);
			try {
				JSONArray jsonarray = json.getJSONArray("results");
				list = new ArrayList<Movie>();
				for (int i = 0; i < jsonarray.length(); i++) {
					Movie newmovie = new Movie(jsonarray.getJSONObject(i), myact);				
					list.add(newmovie);		
				}
				createlistarray();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			DBManageFavorite manage = new DBManageFavorite(myact);
			list = manage.getAllMovies();
			createlistarray();

		}
		return null;
	}

	public static String getdescriptionformovie(int id) {
		JSONParser jParser=new JSONParser();
		String newurl = "https://api.themoviedb.org/3/movie/"+String.valueOf(id)+"?api_key=ca2145f2cd78643fae53b88ee4cf5b2d";
		JSONObject json = jParser.getJSONFromUrl(newurl);
		String returnvalue = null;
		try {
			returnvalue=json.getString("overview");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnvalue;
	}

	private void createlistarray() {
		listItem = new ArrayList<HashMap<String, Object>>();

		for (Movie movie : list) {
			HashMap<String, Object> map;

			map = new HashMap<String, Object>();

			map.put("titre", movie.getName());

			map.put("description", movie.getDate());

			map.put("img", movie.getAffiche());
			listItem.add(map);
		}

	}
}
