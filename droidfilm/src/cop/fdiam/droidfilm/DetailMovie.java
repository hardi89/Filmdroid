package cop.fdiam.droidfilm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailMovie extends Activity{
	private Movie movie;
	private boolean onfavori;
	private final String lineseparator=System.getProperty("line.separator");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailmovie);
		TextView titre = (TextView)findViewById(R.id.title);
		TextView detail = (TextView)findViewById(R.id.detail);
		TextView description = (TextView)findViewById(R.id.description);
		ImageView image = (ImageView)findViewById(R.id.image);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    this.movie=extras.getParcelable("movie");
		}
		if(this.movie!=null){
			titre.setText(movie.getName());
			detail.setText("Sorti le : "+movie.getDate()+lineseparator+"Note : "+movie.getNote()+" pour un total de "+movie.getNbnote()+" votes"+lineseparator+"Popularité : "+movie.getPopularite());
			description.setText(String.valueOf(movie.getNote()));
			image.setImageBitmap(movie.getAffiche());
			onfavori=movie.isonfavori();
			if(onfavori)
				image.setImageResource(R.drawable.favori);
		}
	}
	
	public void favori(View v){
		if(onfavori)
			;//delete
		else
			;//add
	}

}
