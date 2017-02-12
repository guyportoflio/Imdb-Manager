package preslyinc.myimdbmanager;


//class to manage database functions and its sql commends.

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class Database extends SQLiteOpenHelper{


    private SQLiteDatabase database;



    //constructor for database, set the database name to MoviesDatabase,
// set no parameters for now(null), and version number 1.
    public Database(Context context) {
        super(context, "MoviesDatabase", null, 1);
        //if there is a MoviesDatabase, then we go to onUpgrade, but if this is a new
        //database then the onCreate will start.
    }




    public void onCreate(SQLiteDatabase db) {
        //execSQL creates new table for movies, since no need for return value here we
        // can use execSQL
        db.execSQL("CREATE TABLE Movies(id INTEGER PRIMARY KEY AUTOINCREMENT, ImdbId TEXT, title TEXT, plot TEXT, poster TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //deletes older version and creates new table using onCrate
        db.execSQL("DROP TABLE IF EXISTS Movies");
        onCreate(db);
    }

    //we use our global parameter "database" with the function getWritableDatabase which we
    // inherit from  SQLiteOpenHelper.
    //
    public void open() {

        database = getWritableDatabase();
    }

    //close function
    public void close() {
        super.close();
    }

    //here we create a function that adds a new movie based on how we
    // defined a Movie in the Movie class


    public void insertMovie(Movie movie){
        //using string format that allows us to use external parameters into a string
        String sql = String.format("INSERT INTO Movies(ImdbId,title,plot,poster) " +
                "VALUES('%s','%s','%s','%s')", movie.getImdbId(), movie.getTitle(), movie.getPlot(), movie.getPoster());

        open();
        database.execSQL(sql);
        close();
    }

    //here we update an existing product
    public void updateMovie(Movie movie){
        //using string format that allows us to use external parameters into a string
        String sql = String.format("UPDATE Movies SET ImdbId='%s', " +
                        "title='%s', plot='%s', poster='%s' WHERE id=%d",
                movie.getImdbId(), movie.getTitle(),
                movie.getPlot(), movie.getPoster(), movie.getId());

        open();
        database.execSQL(sql);
        close();
    }


    public void deleteAll(Database movie){
        //string sql in order to delete a movie based on its id
        String sql = String.format("DELETE FROM Movies");

        open();
        database.execSQL(sql);
        close();
    }


    public void deleteMovie(Movie movie){
        //string sql in order to delete a movie based on its id
        String sql = String.format("DELETE FROM Movies WHERE id=%d", movie.getId());

        open();
        database.execSQL(sql);
        close();
    }


    public ArrayList<Movie> getAllMovies() {

        ArrayList<Movie> movies = new ArrayList<>(); //creating a new arraylist which we will soon populate

        open();

        //cursor is used to "point" at a specific line in the database, and we use query to
        // question the database regarding its info
        Cursor cursor = database.query("Movies", null,null,null,null,null,null);

        //since cursor always starts in the line BEFORe the first line we will start the loop
        //moving it one step forword with "movetonext", when there is no next line, the while
        //loop will recieve "false" on "movetonext" and the while loop is over
        while(cursor.moveToNext()){

            int idIndex = cursor.getColumnIndex("id"); //cursor gives the LOCATION of info
            int ImdbIdIndex = cursor.getColumnIndex("ImdbId"); //cursor gives the LOCATION of info
            int titleIndex = cursor.getColumnIndex("title"); //cursor gives the LOCATION of info
            int plotIndex = cursor.getColumnIndex("plot"); //cursor gives the LOCATION of info
            int posterIndex = cursor.getColumnIndex("poster"); //cursor gives the LOCATION of info

            int id = cursor.getInt(idIndex);   //now we get the info FROM the LOCATION of cursor
            String ImdbId = cursor.getString(ImdbIdIndex); //now we get the info FROM the LOCATION of cursor
            String title = cursor.getString(titleIndex); //now we get the info FROM the LOCATION of cursor
            String plot = cursor.getString(plotIndex); //now we get the info FROM the LOCATION of cursor
            String poster = cursor.getString(posterIndex); //now we get the info FROM the LOCATION of cursor

            //now that we have all the information ready we can construct a new movie!!!

            Movie movie = new Movie(id,ImdbId,title,plot,poster);
            movies.add(movie);
        }
        //now that the while loop is over lets close cursor and the database
        // and of course....return the movies arraylist
        cursor.close();
        close();

        return movies;
    }


}