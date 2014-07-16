package cop.fdiam.droidfilm;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class Movie implements Parcelable {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Bitmap getAffiche() {
		return affiche;
	}

	public void setAffiche(Bitmap affiche) {
		this.affiche = affiche;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getNote() {
		return note;
	}

	public void setNote(double note) {
		this.note = note;
	}

	private String date;
	private Bitmap affiche;
	private int id;
	private double note;
	private boolean foradult;
	private double popularite;
	private int nbnote;
	public Movie() {
	
	}
	public Movie(final JSONObject jsonmovie,
			final Activity act) {
		try {
			this.name = jsonmovie.getString("original_title");
			this.date = jsonmovie.getString("release_date");
			this.id = jsonmovie.getInt("id");
			this.note = jsonmovie.getDouble("vote_average");
			this.setForadult(jsonmovie.getBoolean("adult"));
			this.setPopularite(jsonmovie.getDouble("popularity"));
			this.setNbnote(jsonmovie.getInt("vote_count"));
			final HttpRetriever ret = new HttpRetriever();;
			try {
				String path = "http://image.tmdb.org/t/p/w500";
				String path2 = jsonmovie.getString("poster_path");
				if (path2.equals("null"))
					path2 = jsonmovie.getString("backdrop_path");
				String total = path + path2;
				if (!path2.equals("null"))
					affiche = Utils.scaleDownBitmap(ret.retrieveBitmap(total),200,act);
				else
					affiche = BitmapFactory.decodeResource(act.getResources(),
							R.drawable.ic_launcher);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * new Thread(new Runnable() { public void run() {
			 * 
			 * try { affiche =
			 * ret.retrieveBitmap("http://image.tmdb.org/t/p/w500"
			 * +jsonmovie.getString("poster_path")); } catch (JSONException e) {
			 * // TODO Auto-generated catch block e.printStackTrace(); } catch
			 * (Exception e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } act.runOnUiThread(new Runnable() {
			 * 
			 * @Override public void run() {
			 * imageView.setImageBitmap(getResizedBitmap(affiche, 70, 70));
			 * view.setText(toString2()); } });
			 * 
			 * } }).start();
			 */
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		return "Titre " + name + " et date " + date + "et id " + id
				+ " et note" + note;
	}

	public String toString2() {
		return "Titre " + name + " et date " + date + "et id " + id
				+ " et note" + note;
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		affiche.writeToParcel(dest, 0);
		dest.writeString(name);
		dest.writeString(date);
		dest.writeInt(id);
		dest.writeDouble(note);	
	}

	public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
		@Override
		public Movie createFromParcel(Parcel source) {
			return new Movie(source);
		}

		@Override
		public Movie[] newArray(int size) {
			return new Movie[size];
		}
	};

	public Movie(Parcel in) {
		this.affiche = Bitmap.CREATOR.createFromParcel(in);
		this.name = in.readString();
		this.date = in.readString();
		this.id = in.readInt();
		this.note = in.readDouble();
		in.setDataPosition(0); 
	}

	public boolean isonfavori() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isForadult() {
		return foradult;
	}

	public void setForadult(boolean foradult) {
		this.foradult = foradult;
	}

	public double getPopularite() {
		return popularite;
	}

	public void setPopularite(double popularite) {
		this.popularite = popularite;
	}

	public int getNbnote() {
		return nbnote;
	}

	public void setNbnote(int nbnote) {
		this.nbnote = nbnote;
	}
}
