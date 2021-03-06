package lab.imaginenat.com.project2.onlineHelpers;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import lab.imaginenat.com.project2.models.Place;
import lab.imaginenat.com.project2.models.PlaceManager;

/**
 * Created by nat on 2/6/16.
 */
public class JSONReader extends DownloadData {
    private String LOG_TAG=JSONReader.class.getSimpleName();
    private Uri mDestinationUri;
    private NotifyMeWhenDone notifyMe;


    public JSONReader(String searchCriteria,NotifyMeWhenDone nm){
        super(null);
        notifyMe=nm;
        Log.d("SearchActivity", "search criteria " + searchCriteria);
        createAndUpdateUri(searchCriteria);

    }

    public void execute(){
        super.setRawURL(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Build URI " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());

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
                .appendQueryParameter(ARG_RADIUS,"550")//represents meters
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
        final String LOCATION="location";
        final String ADDRESS="formatted_address";
        final String LATITUDE="lat";
        final String LONGITUDE="lng";
        final String PHOTO="photos";

        try{

            JSONObject jsonData = new JSONObject(getData());
            JSONArray resultsArray  = jsonData.getJSONArray(RESULTS);
            //while it is a search result from google places it is managed by the PlaceManager
            //once it is in the database, it is managed by the businessManager.

            PlaceManager placeManager = PlaceManager.getInstance();

            for(int i=0;i<resultsArray.length();i++){
                JSONObject dataObject =resultsArray.getJSONObject(i);
                String name = dataObject.getString(NAME);
                String address=dataObject.getString(ADDRESS);

                //for coordinates
                JSONObject geometryObject = dataObject.getJSONObject(GEOMETRY);
                JSONObject locationObject = geometryObject.getJSONObject(LOCATION);
                String lat = locationObject.getString(LATITUDE);
                String lng = locationObject.getString(LONGITUDE);

                //get random photo
                JSONArray photosArray = dataObject.getJSONArray(PHOTO);
                int numberOfPhotos = photosArray.length();
                Random r = new Random();
                int photoIndex=r.nextInt(numberOfPhotos);
                JSONObject photoObject = photosArray.getJSONObject(photoIndex);
                String photoReference = photoObject.getString("photo_reference");


                Log.d("JSONREADER","name "+name);


                Place place = new Place(name);
                //place.setAddress parses the string returned by google to our format (that's why we call place.getBusinessAddress() instead of just putting in address returned)
                place.setAddress(address);
                place.setImageResource(photoReference);
                place.setLatitude(lat);
                place.setLongitude(lng);
                placeManager.addPlace(place);

                //We check if all places is already in the database (to mark the place object as already in database)
                placeManager.checkAndUpdateIfInDatabase();
            }

            notifyMe.completedTask();
        }catch(JSONException je) {
            je.printStackTrace();
            Log.e(LOG_TAG,"!!!!!!!!!!!!Error processing Json data ");
        }
    }
}
