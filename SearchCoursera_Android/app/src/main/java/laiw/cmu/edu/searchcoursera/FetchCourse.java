package laiw.cmu.edu.searchcoursera;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Roy on 11/20/15.
 */
public class FetchCourse {
    SearchCoursera 	searchCoursera = null;

    /*
     * search is the public FetchCourse method.  Its arguments are the search term, and the SearchCoursera object that called it.  This provides a callback
     * path such that the searchReady method in that object is called when the result is available from the search.
     */
    public void search(String searchTerm, SearchCoursera searchCoursera) {
        this.searchCoursera = searchCoursera;
        new AsyncFlickrSearch().execute(searchTerm);
    }

    /*
     * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
     * doInBackground is run in the helper thread.
     * onPostExecute is run in the UI thread, allowing for safe UI updates.
     */
    private class AsyncFlickrSearch extends AsyncTask<String, Void, String[]> {
        protected String[] doInBackground(String... urls) {
            return search(urls[0]);
        }

        protected void onPostExecute(String[] results) {
            searchCoursera.searchReady(results);
        }

        /*
         * Search Coursera.com for the searchTerm argument, and return a string array that can be put in an text view
         */
        private String[] search(String searchTerm) {
            String[] results = new String[5];

            //split spaces, and fill with %20 according to coursera API for empty space
            String[] querys = searchTerm.split(" ");
            String input = "";
            for (String s : querys) {
                input = input + s + "%20";
            }
            System.out.println(input);
            try {
                URL searchURL = new URL("http://fast-bayou-9414.herokuapp.com/FetchCourse/" + input);
                HttpURLConnection urlConnection = (HttpURLConnection) searchURL.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                StringBuilder apiResponse;
                apiResponse = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    apiResponse.append(line);
                    apiResponse.append('\r');
                }
                rd.close();
                JSONObject obj = new JSONObject(apiResponse.toString());
                System.out.println(apiResponse);
                //if there is 5 items in Json resposne, put it into result array
                //if not, fill rest items with empty string
                for(int i = 0; i<5;i++){
                        results[i] = " ";
                }
                for(int i = 0; i<obj.length();i++){
                    if(obj.getString(Integer.toString(i+1))!=null){
                        results[i]=obj.getString(Integer.toString(i+1));
                    }
                    System.out.println(results[i]);
                }



            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            return results;

        }




    }
}
