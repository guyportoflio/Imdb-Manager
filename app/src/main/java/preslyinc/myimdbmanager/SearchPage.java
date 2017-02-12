package preslyinc.myimdbmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchPage extends AppCompatActivity implements TextDownloader.Callbacks  {
    private EditText searchBarInputText;

    private Database moviesDatabase;  // for the delete all option on menu_main
    private ProgressDialog progressDialog;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ListView searchListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        searchBarInputText = (EditText)findViewById(R.id.searchBarText);
        searchListView = (ListView)findViewById(R.id.listViewSearchResult);

        moviesDatabase = new Database(this); // for the delete all option on menu_main


        // just creating something to see on test
     //   movies.add(new Movie("tt0371746","ironman","cool","poster"));

        MoviesAdapter moviesAdapter = new MoviesAdapter(this, movies);
        searchListView.setAdapter(moviesAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){  // options menu
        MenuInflater menuInflater = getMenuInflater(); // options menu constructor
        menuInflater.inflate(R.menu.menu_main, menu); // options menu inflate
        return super.onCreateOptionsMenu(menu); // options menu create
    }

    public void showSearchResult(View view) {
        String searchbartext = searchBarInputText.getText().toString();
        if(searchbartext.isEmpty()){
            Toast.makeText(this,"you need to enter a value to search", Toast.LENGTH_LONG).show();
        }
        else{
            TextDownloader textDownloader = new TextDownloader(this);

            String fullsearch = "http://www.omdbapi.com?s=" + searchbartext;
            textDownloader.execute(fullsearch);
            //String result = String.valueOf(TextDownloader.Callbacks.class);  //trying to get result
        }


    }

    public void deleteDatabaseOnMenuClick(MenuItem item) { //options menu delete on click
        moviesDatabase.deleteAll(moviesDatabase);
        // repeated these oncreate options as a quick fix to the database update before close/open app issue



    }

    public void closeAppOnMenuClick(MenuItem item) {  //options menu close app on click
     finishAffinity();                                //ok........this works...but at what cost...
                                                      // check it!
    }

    public void onAboutToBegin() {
        progressDialog = new ProgressDialog(this);  // v1
        progressDialog.setTitle(R.string.please_wait);  // v1
        progressDialog.show();
    }

    public void onSuccess(String downloadedText) {
        progressDialog.dismiss();

        try {
            JSONObject jsonSearchObject = new JSONObject(downloadedText);
            JSONArray jsonMoviesArray = jsonSearchObject.getJSONArray("Search");

            for(int i = 0 ; i < jsonMoviesArray.length(); i++){
                JSONObject jsonMovieObject = jsonMoviesArray.getJSONObject(i);
                String movieTitle = jsonMovieObject.getString("Title");
                String moviePoster = jsonMovieObject.getString("Poster");
                String movieImdbId = jsonMovieObject.getString("imdbID");

                Movie movie = new Movie(i ,movieImdbId, movieTitle, null, moviePoster);
                movies.add(movie);
            }
            MoviesAdapter moviesAdapter = new MoviesAdapter(this, movies);
            searchListView.setAdapter(moviesAdapter);

        } catch (JSONException e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public void onError(int httpStatusCode, String errorMessage) {
        progressDialog.dismiss();
        Toast.makeText(this, "Error Code: " + httpStatusCode + "\nError Message: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    public void onItemClickedOnListView(View view) {
     int position = searchListView.getPositionForView(view); // set position value  based on click
     String title = movies.get(position).getTitle().toString(); // get title based on position in list
     String imdbId = movies.get(position).getImdbId().toString(); // get imdbId based on position in list
     String poster = movies.get(position).getPoster().toString(); // get poster based on position in list
     int id = movies.get(position).getId();

        Intent intent = new Intent(this,EditMovie.class);
        intent.putExtra("title", title);
        intent.putExtra("poster", poster);
        intent.putExtra("imdbId", imdbId);
        intent.putExtra("id", id);
        startActivity(intent);
    }
    }



