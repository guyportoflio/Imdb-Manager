package preslyinc.myimdbmanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.R.attr.id;
import static android.R.attr.targetId;
import static android.R.attr.toId;

public class EditMovie extends AppCompatActivity implements TextDownloader.Callbacks {
    private EditText titleedittext;
    private EditText plotedittext ;
    private EditText posteredittext;
    private ProgressDialog progressDialog;
    private ImageView posterimage;
    private Database moviesDatabase;
    private String constructorImdbId  = ""; // setting empty in case of edit without IMDB
    private String constructorTitle ;
    private String constructorPlot ;
    private String constructorPoster ;
    private int constructorId;
    private String originalTitle;
    private String originalPlot;
    private String originalPoster;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);
        titleedittext = (EditText)findViewById(R.id.editMovieTitleTextField);
        plotedittext = (EditText)findViewById(R.id.editMoviePlotTextField);
        posteredittext = (EditText)findViewById(R.id.editMoviePosterTextField);
        posterimage = (ImageView)findViewById(R.id.posterImageOnScreen);
        moviesDatabase = new Database(this);

        originalTitle = titleedittext.toString();
        originalPoster = posteredittext.toString();
        originalPlot = plotedittext.toString();
        originalPoster = posterimage.toString();


        //setting poster image
        ImageDownloader imageDownloader = new ImageDownloader(new ImageDownloader.Callbacks() {

            @Override
            public void onAboutToBegin() {

            }

            @Override
            public void onSuccess(Bitmap downloadedBitmap) {
                posterimage.setImageBitmap(downloadedBitmap);

            }

            @Override
            public void onError(int httpStatusCode, String errorMessage) {

            }
        }
        );

        Intent intent = getIntent();
        String intentTitle = intent.getStringExtra("title");
        String intentPoster = intent.getStringExtra("poster");
        String tempImdbId = intent.getStringExtra("imdbId");
        int tempPosition = intent.getIntExtra("position", 0);
        String intentPlot = intent.getStringExtra("plot");
        int intentId = intent.getIntExtra("id", id);
        titleedittext.setText(intentTitle);
        posteredittext.setText(intentPoster);
        //setting all var to strings
        constructorImdbId = tempImdbId;
        constructorTitle = intentTitle;
        constructorPoster = intentPoster;
        constructorId = intentId;


        imageDownloader.execute(constructorPoster); //setting image for poster

        //making sure manual adjustment to plot is not over-written
    if(tempPosition == 0){
            TextDownloader textDownloader = new TextDownloader(this);
             String fullsearch = "http://www.omdbapi.com?i=" + tempImdbId;
            textDownloader.execute(fullsearch);
    }

      plotedittext.setText(intentPlot);
      constructorPlot =  plotedittext.toString(); //setting plot AFTER  "plotedittext.setText(plot);"

    }

    public boolean onCreateOptionsMenu(Menu menu){  // options menu
        MenuInflater menuInflater = getMenuInflater(); // options menu constructor
        menuInflater.inflate(R.menu.menu_main, menu); // options menu inflate
        return super.onCreateOptionsMenu(menu); // options menu create
    }




    public void deleteDatabaseOnMenuClick(MenuItem item) { //options menu delete on click

        moviesDatabase.deleteAll(moviesDatabase);
        // repeated these oncreate options as a quick fix to the database update before close/open app issue


    }

    public void closeAppOnMenuClick(MenuItem item) {  //optios menu close app on click
        finishAffinity();  //ok........this works...but at what cost...check it!
    }

    @Override
    public void onAboutToBegin() {
        progressDialog = new ProgressDialog(this);  // v1
        progressDialog.setTitle(R.string.please_wait);  // v1
        progressDialog.show();
    }

    @Override
    public void onSuccess(String downloadedText) {
        progressDialog.dismiss();


        try {
            JSONObject jsonobject = new JSONObject(downloadedText);
            String plot = jsonobject.getString("Plot");
                plotedittext.setText(plot);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
        @Override
    public void onError(int httpStatusCode, String errorMessage) {

    }

    public void editMovieDeleteThisMovieOnClick(View view) {

        Movie movie = new Movie(constructorId ,constructorImdbId, constructorTitle, constructorPlot, constructorPoster );

        moviesDatabase.deleteMovie(movie);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void editMovieAddThisMovieOnClick(View view) {

       moviesDatabase.open();


       moviesDatabase.close();

    //first stage is to delete the existing version from the database
        Movie movie = new Movie(constructorId ,constructorImdbId, originalTitle, originalPlot, originalPoster );

        Toast.makeText(this, "Id: " + movie.getId(), Toast.LENGTH_LONG).show();
        moviesDatabase.deleteMovie(movie);



        //second stage is to add the new version to the database
       constructorPlot = plotedittext.toString(); //setting plot AFTER  "plotedittext.setText(plot);"
       constructorTitle = titleedittext.getText().toString();
        constructorPlot = plotedittext.getText().toString();
       constructorPoster = posteredittext.getText().toString();
       // int setid = moviesDatabase.getAllMovies().size();

        // String title = constructorTitle.toString();
        Movie newMovie = new Movie(constructorId ,constructorImdbId, constructorTitle, constructorPlot, constructorPoster);

            moviesDatabase.insertMovie(newMovie);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

    }
}
