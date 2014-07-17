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
	public static ListView listfavorite;
	public static Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TabHost tabHost = getTabHost();
        
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("List");
        // setting Title and Icon for the Tab
        photospec.setIndicator("List", getResources().getDrawable(R.drawable.ic_launcher));
        Intent mainIntent = new Intent(this, secondactivity.class);
        photospec.setContent(mainIntent);
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Favorite");       
        songspec.setIndicator("Favorites", getResources().getDrawable(R.drawable.ic_launcher));
        //photosIntent.putExtra("main", false);
        Intent favoriIntent = new Intent(this, secondactivity.class);
        favoriIntent.putExtra("main", "fav");
        songspec.setContent(favoriIntent);
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab // Adding videos tab
		
	}
}
