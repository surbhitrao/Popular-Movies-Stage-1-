package com.example.surbhitrao.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GridViewActivity extends AppCompatActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
       // actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#BD2020"));
        actionBar.setBackgroundDrawable(colorDrawable);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                Intent intent = new Intent(GridViewActivity.this, com.example.surbhitrao.myapplication.DetailsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);

                       intent.putExtra("title", item.getTitle()).
                               putExtra("overview", item.getOverview()).
                               putExtra("votes", item.getVotes()).
                               putExtra("r_dates", item.getR_date()).


                               putExtra("image", item.getImage());


                startActivity(intent);
            }
        });


        new AsyncHttpTask().execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=");
        mProgressBar.setVisibility(View.VISIBLE);
    }
AsyncHttpTask o=new AsyncHttpTask();

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        private final String LOG_TAG = GridViewActivity.class.getSimpleName();//Check




        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String baseUrl=params[0];



            String forecastJsonStr = null;


            try {

                String apiKey = "Insert Your API Key here";//Add your API Key Here
                URL url = new URL(baseUrl.concat(apiKey));


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");

                }


                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    result = 0;
                    return null;
                } else {
                    result = 1;
                    forecastJsonStr = buffer.toString();
                    parseResult(forecastJsonStr);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return result;
        }


        @Override


        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Parsing the feed results and get the list
     *
     * @param forecastJsonStr
     */
   private void parseResult(String forecastJsonStr) {
        try {
            JSONObject response = new JSONObject(forecastJsonStr);
            JSONArray posts = response.optJSONArray("results");
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("original_title");
                item = new GridItem();
                item.setTitle(title);

              String image=(post.optString("poster_path"));
              String image2="http://image.tmdb.org/t/p/w500/";
            String image3=image2.concat(image);
                     item.setImage((image3));

               String overview=post.optString("overview");
                item.setOverview(overview);

                 String votes=post.optString("vote_average");
                item.setVotes(votes);

                 String dates=post.optString("release_date");
                item.setR_date(dates);
                mGridData.add(item);}
            } catch (JSONException e1) {
            e1.printStackTrace();
        }
   }
           public boolean onCreateOptionsMenu(Menu menu) {
               // Inflate the menu; this adds items to the action bar if it is present.
               getMenuInflater().inflate(R.menu.menu_main, menu);
               return true;
           }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            mGridAdapter.clear();
            // GridView mGridView;
            new AsyncHttpTask().execute("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=");


        }
        else if (id==R.id.action_settings2){
            mGridAdapter.clear();
            new AsyncHttpTask().execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=");

        }

        return super.onOptionsItemSelected(item);
    }
        }

