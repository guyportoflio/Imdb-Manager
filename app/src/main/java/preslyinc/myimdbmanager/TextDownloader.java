package preslyinc.myimdbmanager;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TextDownloader extends AsyncTask<String, Void, String> {

    private Callbacks callbacks;
    private int httpStatusCode;
    private String errorMessage;

    public TextDownloader(Callbacks callbacks) {
        this.callbacks = callbacks;
    }



    protected void onPreExecute() {
        callbacks.onAboutToBegin();
    }

    protected String doInBackground(String... params) {

        InputStream inputStream = null; // in case of input to send server? not sure if i need it
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        try {

            String link = params[0];   //setting params into a string

            URL url = new URL(link);                 //setting URl command with the string

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();  //declaring connection

            httpStatusCode = connection.getResponseCode();  // getting status code response from server

            if(httpStatusCode != HttpURLConnection.HTTP_OK) {
                errorMessage = connection.getResponseMessage();
                return null;
            }
          //setting reading option for incoming info from server
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String downloadedText = "";           //inserting response into string
            String oneLine = bufferedReader.readLine();      //reading one line at a time
            while(oneLine != null) {
                downloadedText += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }

            return downloadedText;   //return response to the class that called it
        }
        catch (Exception ex) {
            errorMessage = ex.getMessage();  // in case of error with server/client
            return null;
        }
        //closing everything to save memory(i think thats the reason)
        finally {
            if(bufferedReader != null) {
                try { bufferedReader.close(); } catch (Exception ex) {}
            }
            if(inputStreamReader != null) {
                try { inputStreamReader.close(); } catch (Exception ex) {}
            }
            if(bufferedReader != null) {
                try { bufferedReader.close(); } catch (Exception ex) {}
            }
        }
    }

    protected void onPostExecute(String downloadedText) {
        if(downloadedText != null) {
            callbacks.onSuccess(downloadedText);
        }
        else {
            callbacks.onError(httpStatusCode, errorMessage);
        }
    }

    public interface Callbacks {
        void onAboutToBegin();
        void onSuccess(String downloadedText);
        void onError(int httpStatusCode, String errorMessage);
    }
}

