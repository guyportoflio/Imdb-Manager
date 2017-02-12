package preslyinc.myimdbmanager;


public class Movie {
    //the relavent parameters for each movie

    private int id;
    private String ImdbId;
    private String title;
    private String plot;
    private String poster;

    //constructor that doesnt need to be given an id .....never used in the end
  // public Movie(String ImdbId, String title, String plot, String poster){
  //      this(0, ImdbId, title, plot, poster);
  //  }

    //constructor with Id
    public Movie(int id, String ImdbId, String title, String plot, String poster){
        setId(id);
        setImdbId(ImdbId);
        setTitle(title);
        setPlot(plot);
        setPoster(poster);
    }

    //all the getters and setters required to manipulate the values of a movie.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdbId() {
        return ImdbId;
    }

    public void setImdbId(String ImdbId) {
        this.ImdbId = ImdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }


    public void setPoster(String poster) {
        this.poster = poster;
    }

    // a string output of values,
    // just for checking along the program if all is working well.

    public String toString() {
        return "id: " + id + " , " + "imdbId: " + ImdbId + " , " +
                "title: " + title + " , " + "plot: " + plot + " , "
                + "poster: " + poster + " , ";
    }
}
