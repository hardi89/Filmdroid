package cop.fdiam.droidfilm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ExtendedSimpleAdapter extends SimpleAdapter {
	List<HashMap<String, Object>> map;
	String[] from;
	int layout;
	int[] to;
	Context context;
	LayoutInflater mInflater;
	ArrayList<Movie> Listfinal;
	Activity myact;

	public ExtendedSimpleAdapter(Context context,
			List<HashMap<String, Object>> data, int resource, String[] from,
			int[] to, ArrayList<Movie> Listfinal, Activity myact) {
		super(context, data, resource, from, to);
		layout = resource;
		map = data;
		this.from = from;
		this.to = to;
		this.context = context;
		this.Listfinal = Listfinal;
		this.myact = myact;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = this.createViewFromResource(position, convertView, parent,
				layout);
		final LinearLayout checkmovie = (LinearLayout) view
				.findViewById(R.id.checkmovie);
		final Movie newmovie = Listfinal.get(position);
		final ImageView img = (ImageView) view.findViewById(R.id.img);
		if (newmovie.getAffiche() == null) {
			loadimage(newmovie, img);
		} else
			img.setImageBitmap(Utils.scaleDownBitmap(newmovie.getAffiche(),
					100, context));

		final ImageView addfav = (ImageView) view.findViewById(R.id.addfav);

		final DBManageFavorite manage = new DBManageFavorite(context);
		if (manage.searchMovie(newmovie))
			addfav.setImageResource(R.drawable.favori);
		else
			addfav.setImageResource(R.drawable.nofavori);
		final OnClickListener onclick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == addfav.getId()) {
					if (manage.searchMovie(newmovie)) {
						new AlertDialog.Builder(context)
								.setTitle("Déjà présent en favori")
								.setMessage("Voulez-vous le supprimer?")
								.setPositiveButton(android.R.string.yes,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												manage.deleteMovie(newmovie);
												refreshfavoritelist();
												addfav.setImageResource(R.drawable.nofavori);
											}

										})
								.setNegativeButton(android.R.string.no,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {

											}
										})
								.setIcon(android.R.drawable.ic_dialog_alert)
								.show();
					} else {
						addfav.setImageResource(R.drawable.favori);
						manage.addMovie(newmovie);
						refreshfavoritelist();
					}
				} else {
					if(newmovie.getAffiche()!=null){
					Intent i = new Intent(context, DetailMovie.class);
					i.putExtra("movie", newmovie);
					DetailMovie.idimagelist = addfav;
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					}
					else{
						Toast.makeText(context, "Veuillez attendre la fin de chargement de l'image", Toast.LENGTH_LONG).show();
					}
				}

			}
		};

		checkmovie.setOnClickListener(onclick);
		addfav.setOnClickListener(onclick);

		return view;
	}

	private void loadimage(final Movie newmovie, final ImageView img) {
		new Thread() {
			public void run() {
				HttpRetriever ret = new HttpRetriever();
				try {

					newmovie.setAffiche(Utils.scaleDownBitmap(
							ret.retrieveBitmap(newmovie.urlforimage), 300,
							context));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					myact.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							img.setImageBitmap(Utils.scaleDownBitmap(
									newmovie.getAffiche(), 100, context));
						}
					});
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

	private View createViewFromResource(int position, View convertView,
			ViewGroup parent, int resource) {
		View v;
		if (convertView == null) {
			v = mInflater.inflate(resource, parent, false);
		} else {
			v = convertView;
		}

		this.bindView(position, v);

		return v;
	}

	private void bindView(int position, View view) {
		final Map dataSet = map.get(position);
		if (dataSet == null) {
			return;
		}

		final ViewBinder binder = super.getViewBinder();
		final int count = to.length;

		for (int i = 0; i < count; i++) {
			final View v = view.findViewById(to[i]);
			if (v != null) {
				final Object data = dataSet.get(from[i]);
				String text = data == null ? "" : data.toString();
				if (text == null) {
					text = "";
				}

				boolean bound = false;
				if (binder != null) {
					bound = binder.setViewValue(v, data, text);
				}

				if (!bound) {
					if (v instanceof Checkable) {
						if (data instanceof Boolean) {
							((Checkable) v).setChecked((Boolean) data);
						} else if (v instanceof TextView) {
							// Note: keep the instanceof TextView check at the
							// bottom of these
							// ifs since a lot of views are TextViews (e.g.
							// CheckBoxes).
							setViewText((TextView) v, text);
						} else {
							throw new IllegalStateException(v.getClass()
									.getName()
									+ " should be bound to a Boolean, not a "
									+ (data == null ? "<unknown type>"
											: data.getClass()));
						}
					} else if (v instanceof TextView) {
						// Note: keep the instanceof TextView check at the
						// bottom of these
						// ifs since a lot of views are TextViews (e.g.
						// CheckBoxes).
						setViewText((TextView) v, text);
					} else if (v instanceof ImageView) {
						if (data instanceof Integer) {
							setViewImage((ImageView) v, (Integer) data);
						} else if (data instanceof Bitmap) {
							setViewImage((ImageView) v, (Bitmap) data);
						} else {
							setViewImage((ImageView) v, text);
						}
					} else {
						throw new IllegalStateException(
								v.getClass().getName()
										+ " is not a "
										+ " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}

	private void setViewImage(ImageView v, Bitmap bmp) {
		v.setImageBitmap(bmp);
	}

	private void refreshfavoritelist() {
		Context contextfavorite = MainActivity.context;
		ListView listfavorites = MainActivity.listfavorite;
		if (contextfavorite != null && listfavorites != null) {
			ManageApi api = new ManageApi(contextfavorite, listfavorites, myact);
			api.getAllFavoris();
		}

	}

}
