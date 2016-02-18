package laiw.cmu.edu.searchcoursera;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchCoursera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding result from Coursera, it
         * can callback to this object with the result.  The "this" of the OnClick will be the OnClickListener.
         */
        final SearchCoursera searchCoursera = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button)findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
                if(searchTerm.matches("[A-Za-z0-9][A-Za-z0-9 ]+")){
                    FetchCourse fetchCourse = new FetchCourse();
                    fetchCourse.search(searchTerm, searchCoursera); // Done asynchronously in another thread.  It calls searchReady() in this thread when complete.
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
     * This is called by the FetchCourse object when the result is ready.  This allows for passing back the Result
     */
    public void searchReady(String[] results) {
        //get textviews for result display
        TextView result1 = (TextView)findViewById(R.id.result1);
        TextView result2 = (TextView)findViewById(R.id.result2);
        TextView result3 = (TextView)findViewById(R.id.result3);
        TextView result4 = (TextView)findViewById(R.id.result4);
        TextView result5 = (TextView)findViewById(R.id.result5);

        //clear text first
        result1.setText("");
        result2.setText("");
        result3.setText("");
        result4.setText("");
        result5.setText("");

        //put search result in  text views
        result1.setText(results[0]);
        result2.setText(results[1]);
        result3.setText(results[2]);
        result4.setText(results[3]);
        result5.setText(results[4]);

    }

}
