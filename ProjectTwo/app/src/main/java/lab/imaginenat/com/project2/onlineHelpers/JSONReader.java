package lab.imaginenat.com.project2.onlineHelpers;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lab.imaginenat.com.project2.models.Place;

/**
 * Created by nat on 2/6/16.
 */
public class JSONReader extends DownloadData {
    private String LOG_TAG=JSONReader.class.getSimpleName();
    private List<Place> mPlaces;
    private Uri mDestinationUri;

    public JSONReader(String searchCriteria){
        super(null);
        Log.d("SearchActivity","search criteria "+searchCriteria);
        createAndUpdateUri(searchCriteria);
        mPlaces = new ArrayList<>();
    }

    public void execute(){
        super.setRawURL(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Build URI " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());

    }

    public List<Place> getPlaces(){
        return mPlaces;
    }
    public boolean createAndUpdateUri(String longLatLocation){
        //final String ONLINE_QUERY="https://maps.googleapis.com/maps/api/place/nearbysearch/json";//?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=AIzaSyBS4nUQRSuaqOYYWYmj7eCWkecFbCTjW1A";
        final String ONLINE_QUERY="https://maps.googleapis.com/maps/api/place/textsearch/json";//
        final String ARG_LOCATION="location";
        final String ARG_RADIUS="radius";
        final String ARG_TYPES="types";
        final String ARG_KEY="key";
        final String ARG_QUERY="query";//for text result

        mDestinationUri =Uri.parse(ONLINE_QUERY).buildUpon()
                .appendQueryParameter(ARG_QUERY,"restaurant")//required for text result
                .appendQueryParameter(ARG_LOCATION,longLatLocation)
                .appendQueryParameter(ARG_RADIUS,"500")
                .appendQueryParameter(ARG_TYPES,"restaurant|cafe|bar")
                .appendQueryParameter(ARG_KEY,"AIzaSyBS4nUQRSuaqOYYWYmj7eCWkecFbCTjW1A")
                .build();

        return mDestinationUri !=null;
    }

    public class DownloadJsonData extends DownloadRawData{
        protected void onPostExecute(String webData){
            super.onPostExecute(webData);
            processResult();
        }

        protected String doInBackground(String... params){
            String[] par = {mDestinationUri.toString()};
            return super.doInBackground(par);
        }
    }

    public void processResult(){
        if(getDownloadStatus()!=DownloadStatus.OK){
            Log.e(LOG_TAG,"Error downloading raw file");
            return;
        }

        //id parts of json file to process
        final String RESULTS ="results";
        final String NAME ="name";
        final String GEOMETRY="geometry";
        final String ADDRESS="formatted_address";

        try{

            JSONObject jsonData = new JSONObject(getData());
            JSONArray resultsArray  = jsonData.getJSONArray(RESULTS);

            for(int i=0;i<resultsArray.length();i++){
                JSONObject dataObject =resultsArray.getJSONObject(i);
                String name = dataObject.getString(NAME);
                String address=dataObject.getString(ADDRESS);
                Log.d("JSONREADER","name "+name);

                Place place = new Place(name);
                place.setAddress(address);
                this.mPlaces.add(place);
            }


        }catch(JSONException je) {
            je.printStackTrace();
            Log.e(LOG_TAG,"!!!!!!!!!!!!Error processing Json data ");
        }
    }
}
