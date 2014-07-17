package cop.fdiam.droidfilm;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class loadimageformovie extends AsyncTask<Void, Void, Void>{
	private ImageView imgload;
	private Movie movie;
	private Bitmap affiche;
	private Context context;
	public loadimageformovie(Context context,ImageView img,Movie movie){
		imgload = img;
		this.movie=movie;
		this.context=context;
	}
	@Override
	protected Void doInBackground(Void... params) {
		final HttpRetriever ret = new HttpRetriever();
		try {
			affiche = Utils.scaleDownBitmap(ret.retrieveBitmap(movie.urlforimage),200,context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		movie.setAffiche(affiche);
		imgload.setImageBitmap(affiche);
		super.onPostExecute(result);
	}
}
