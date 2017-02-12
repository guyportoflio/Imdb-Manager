package preslyinc.myimdbmanager;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MoviesAdapter extends ArrayAdapter<Movie> {

    private LayoutInflater layoutInflater;

    public MoviesAdapter(Context context, ArrayList<Movie> movies){
        super(context, 0 , movies);
        // this.movies = movies;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(int position, View ConvertView, ViewGroup parent) {
        //if this item was previuosly built - return this exact item

      //  if(ConvertView != null){  //   canceled this because it made the same result to repeat
      //      return ConvertView;  //      itself on listview, find solution in order to get
      //  }                        //    listview to NOT RELOAD movies that were already loaded

        //this is a new item - create it from scratch:
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.movie_item, null);
        Movie movie = getItem(position);

        TextView textViewTitle = (TextView) linearLayout.findViewById(R.id.movieListTitle);
        textViewTitle.setText(movie.getTitle());

        //   ImageView imageViewPoster = (ImageView) linearLayout.findViewById(R.id.imageViewPoster);

        final ImageView imageViewPoster = (ImageView)linearLayout.findViewById(R.id.movieListPosterImage);

        // imageViewPoster = (ImageView) linearLayout.findViewById(R.id.imageViewPoster);

        ImageDownloader imageDownloader = new ImageDownloader(new ImageDownloader.Callbacks() {
            @Override
            public void onAboutToBegin() {

            }

            @Override
            public void onSuccess(Bitmap downloadedBitmap) {
                imageViewPoster.setImageBitmap(downloadedBitmap);
            }

            @Override
            public void onError(int httpStatusCode, String errorMessage) {

            }
        });
        imageDownloader.execute(movie.getPoster());
        return linearLayout;
    }



}

