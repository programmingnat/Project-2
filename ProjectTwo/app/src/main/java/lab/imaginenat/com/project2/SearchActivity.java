package lab.imaginenat.com.project2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import lab.imaginenat.com.project2.customLayoutsAndAdapters.PlacesAdapter;
import lab.imaginenat.com.project2.models.PlaceManager;
import lab.imaginenat.com.project2.onlineHelpers.JSONReader;
import lab.imaginenat.com.project2.onlineHelpers.NotifyMeWhenDone;

public class SearchActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,NotifyMeWhenDone {
    //STUFF TO CALL GOOGLE API and ATTEMPT TO RESOLVE PROBLEM (from google developer)
    private static final int REQUEST_FINE_LOCATION = 0;

    private GoogleApiClient mGoogleApiClient;
    //TO HANDLE GOOGLE SERVICES CONNECTION ERROR
    //Request cod to use when launging the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    //Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    //bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";


    ///------Using JSON instead of the Google Places PlacePicker class (which doesnt seem to work on emulator)
    public static final String ONLINE_QUERY = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";//?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=AIzaSyBS4nUQRSuaqOYYWYmj7eCWkecFbCTjW1A";
    private String mLatAndLong="40.739885,-73.990082";//GA Coordinates as default


    //GUI related
    RecyclerView mPlaceListView;
    PlacesAdapter mPlacesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //create client because we use google play services for the gms location service, but u
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //in case there is an error, keep trying and note that you are attempting to fix (and not starting new)
        mResolvingError = savedInstanceState != null && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

        Button performSearchButton = (Button) findViewById(R.id.executeSearch_button);
        performSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SearchActivity", "CLICKED");
                PlaceManager.getInstance().clearAll();
                //getLongLat(v);
                JSONReader reader = new JSONReader(mLatAndLong,SearchActivity.this);//"40.7144,-74.006");
                reader.execute();

                //Intent toSearchResults = new Intent(SearchActivity.this, ResultsActivity.class);
                //toSearchResults.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                //startActivity(toSearchResults);
            }
        });

        //GUI
        mPlaceListView = (RecyclerView)findViewById(R.id.placesRecyclerView);
        mPlacesAdapter = new PlacesAdapter(SearchActivity.this);
        mPlaceListView.setAdapter(mPlacesAdapter);
        mPlaceListView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    protected void onStop() {

        mGoogleApiClient.disconnect();
        super.onStop();
    }
    public void onDialogDismissed(){
        mResolvingError=false;
    }

    public void getLongLat(View v){
        Toast.makeText(SearchActivity.this,"Calculating long and lat",Toast.LENGTH_LONG ).show();
        Log.d("SearchActivity", "getLongLat about to be called");
        getLocationTest();
    }

    public void getLocationTest(){
        Log.d("SearchActivity", "getLocationTest inside");
        loadPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        Log.d("MainActivity", "about to be before try");
        try {
            //use getLastLocation instead of requestLocationUpdates because we don't need location in real time
            Location location=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLatAndLong=location.getLatitude()+","+location.getLongitude();
           /* LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, request, new LocationListener() {

                        public void onLocationChanged(Location location) {
                            Log.d("SearchActivity","about to be before text");
                            //TextView t = (TextView)findViewById(R.id.enterAddressText);
                            //t.setText(location.toString());
                            mLatAndLong=location.getLatitude()+","+location.getLongitude();

                            Log.d("SearchActivity","onLocationchanged "+location.toString());
                        }
                    });
                    */
        }catch (SecurityException se){
            Toast.makeText(SearchActivity.this,"BYTE ME 5000 RAN INTO A SECURITY EXCEPTION,TRY AGAIN PLEASE",Toast.LENGTH_LONG).show();
            Log.d("MainActivity","Security exception");
            // se.printStackTrace();
        }
    }

    /*
       This method is used if the user doesn't want to search by current location gps, but rather by and address
       Google api allows you to put in GPS coordinates for search
     */
    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == REQUEST_RESOLVE_ERROR){
            mResolvingError=false;
            if(resultCode == RESULT_OK){
                if(!mGoogleApiClient.isConnected()&&!mGoogleApiClient.isConnected()){
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }
    private void loadPermissions(String perm,int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm},requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                }
                else{
                    // no granted
                }
                return;
            }

        }

    }
    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(SearchActivity.this, "Able to make connection ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            //Already attempting to resolve an error
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                //There was an error with the resolution intent, try again
                mGoogleApiClient.connect();

            }
        } else {
            //show dialog using GOogeApiAvailabiity.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    private void showErrorDialog(int errorCode) {
        //Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        //Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    @Override
    public void completedTask() {
        mPlacesAdapter.notifyDataSetChanged();
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends android.support.v4.app.DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((SearchActivity) getActivity()).onDialogDismissed();
        }
    }

}
