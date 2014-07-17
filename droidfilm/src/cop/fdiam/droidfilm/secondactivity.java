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
import android.os.Handler;
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

public class secondactivity extends Activity {
	private Activity myact = null;
	private ListView maListViewPerso;
	private TabHost tabHost;
	private TabSpec tabSpec;
	private boolean onfavorite;
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		maListViewPerso = (ListView) findViewById(R.id.listviewperso);
		myact = this;
		handler = new Handler();
		final LinearLayout searchlayout = (LinearLayout) findViewById(R.id.searchlayout);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			onfavorite = true;
		}
		final ManageApi api = new ManageApi(this, maListViewPerso,this);
		if (onfavorite) {
			searchlayout.setVisibility(View.GONE);
			api.getAllFavoris();
			MainActivity.listfavorite=maListViewPerso;
			MainActivity.context = this;
		} else {
			api.getAllNewMovies();
		}

	}

	public void search(View v) {
		EditText search = (EditText) findViewById(R.id.search);
		ManageApi api = new ManageApi(this, maListViewPerso,this);
		String key = search.getText().toString();
		if (!key.equals("") && havechar(key))
			api.getMovieByKey(search.getText().toString());
		else
			api.getAllNewMovies();
	}

	private boolean havechar(String key) {
		for(int i=0;i<key.length();i++)
			if(key.charAt(i)!=' ')
				return true;
		return false;
	}
	
	/*public void startHandler() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!isBusy)
					callAysncTask();

				if (!stop)
					startHandler();
			}
		}, 1000);
	}*/
}
