package edu.cmu.laiw;

import java.io.*;
import java.net.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;

/**
 * This is a restful web service that takes a search term and search cousres on courseara.com
 * It returns at most top 5 search results as a json object
 * @author Lai Wei
 */
@WebServlet(name = "FetchCourse", urlPatterns = {"/FetchCourse/*"})
public class FetchCourse extends HttpServlet {

    // GET returns a value given a key
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Console: doGET visited");

        String result = "";

        // The name is on the path /query so skip over the '/'
        String query = (request.getPathInfo()).substring(1);
        
        //Form the response json object
        JSONObject returnObj = new JSONObject();
        
        
        
        //if parameter is empty nothing to search
        if (query.equals("")) {
            System.out.println("No search term");
            returnObj.put("1", "No thing to search");            
        } else {
            //get query string and form query url
            //split spaces, and fill with %20 according to coursera API for empty space
            String[] querys = query.split(" ");
            String input = "";
            for (String s : querys) {
                input = input + s + "%20";
            }
            System.out.println(input);

            //for the url
            String targetURL = "https://api.coursera.org/api/courses.v1?q=search&query=" + input;
            URL url = new URL(targetURL);
            //make http conection to coursera
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //get response input stream and convert to string
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder apiResponse;
            apiResponse = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                apiResponse.append(line);
                apiResponse.append('\r');
            }
            rd.close();
            String courseraResult = apiResponse.toString();
            //parse the response json string to json object
            JSONObject obj = new JSONObject(courseraResult);
            //get json content
            JSONArray array = obj.getJSONArray("elements");
            //if there is no content, no search result of the query term
            if (array.length() == 0) {
                System.out.println("no result");
                returnObj.put("1", "No Search Result of "+query);
            } else {
                //get the first 5 courses' names of the search result
                for (int i = 0; i < array.length(); i++) {
                    if(i==5) break;
                    JSONObject obj2 = (JSONObject) array.get(i);
                    if (obj2 != null) {
                        returnObj.put(Integer.toString(i+1),obj2.getString("name"));
                        System.out.println(obj2.getString("name"));
                    } 
                    if(i== (array.length()-1)){
                        //if not enough 5 results
                        returnObj.put(Integer.toString(i+2),"Only "+(i+1)+" search results");
                        System.out.println("not enough result");
                        break;
                    }
                }
            }
            
            // Things went well so set the HTTP response code to 200 OK
            response.setStatus(200);
            // tell the client the type of the response
            response.setContentType("text/plain;charset=UTF-8");

            // return the value from a GET request
            result = returnObj.toString();
            PrintWriter out = response.getWriter();
            out.println(result);
        }
    }

}
