package preslyinc.myimdbmanager;

import android.content.Intent;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
  private ListView listviewfordatabase;
    private Database moviesDatabase;
    private ArrayList<Movie> allMovies;
    private  MoviesAdapter moviesAdapter;
    private int currentposition; // onitemclicked needs this to work? not sure why it wont without it



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listviewfordatabase = (ListView)findViewById(R.id.databaseListView);

        //database object
        moviesDatabase = new Database(this);

        //get data from database

        allMovies = moviesDatabase.getAllMovies();

        //creating adapter
         moviesAdapter = new MoviesAdapter(this, allMovies);

        //connecting adapter to listview
        listviewfordatabase.setAdapter(moviesAdapter);

      //register click event on listview
        listviewfordatabase.setOnItemClickListener(this);



    }

    public boolean onCreateOptionsMenu(Menu menu){  // options menu
        MenuInflater menuInflater = getMenuInflater(); // options menu constructor
        menuInflater.inflate(R.menu.menu_main, menu); // options menu inflate
        return super.onCreateOptionsMenu(menu); // options menu create
    }

    public void showSearchPage(View view){   // the main page PLUS popup menu on click
       PopupMenu popupMenu = new PopupMenu(this, view);
       popupMenu.inflate(R.menu.popup_menu);
       popupMenu.show();

 }



    //will be invoked when child activity is closed
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "request code: " +
         requestCode + ", result code: " +
         resultCode, Toast.LENGTH_LONG).show();

        if(requestCode == 1 && requestCode == RESULT_OK){
            String message = data.getStringExtra("message");

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void goToMovieEditLayout(MenuItem item) {
        Intent intent = new Intent(this,EditMovie.class);

        startActivity(intent);
    }

    public void goToMovieSearchLayout(MenuItem item) {
        //create an object for intent
        Intent intent = new Intent(this, SearchPage.class);


        //show second activity
        startActivity(intent);

        //  startActivityForResult(intent, 1); //1 = request code
    }

     public void deleteDatabaseOnMenuClick(MenuItem item) { //options menu delete on click
        moviesDatabase.deleteAll(moviesDatabase);

        // repeated these oncreate options as a quick fix to the database update before close/open app issue

        //database object
        moviesDatabase = new Database(this);

        //get data from database
        allMovies = moviesDatabase.getAllMovies();

        //creating adapter
        moviesAdapter = new MoviesAdapter(this, allMovies);

        //connecting adapter to listview
        listviewfordatabase.setAdapter(moviesAdapter);

     }

     public void closeAppOnMenuClick(MenuItem item) {  //optios menu close app on click
        finishAffinity();  //ok........this works...but at what cost...check it!
     }

     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          currentposition = position;

     }

     public void onItemClickedOnListView(View view){

        int position = listviewfordatabase.getPositionForView(view); // set position calue  based on click
        String title = allMovies.get(position).getTitle().toString(); // get title based on position in list
        String imdbId = allMovies.get(position).getImdbId().toString(); // get imdbId based on position in list
        String poster = allMovies.get(position).getPoster().toString();  // get plot based on position in list
        String plot = allMovies.get(position).getPlot().toString();
        // get poster based on position in list
        int id = allMovies.get(position).getId();

        Intent intent = new Intent(this,EditMovie.class);
        intent.putExtra("title", title);
        intent.putExtra("poster", poster);
        intent.putExtra("imdbId", imdbId);
        intent.putExtra("id", id);
        intent.putExtra("plot", plot);
        intent.putExtra("position", position);
        startActivity(intent);
     }


}
