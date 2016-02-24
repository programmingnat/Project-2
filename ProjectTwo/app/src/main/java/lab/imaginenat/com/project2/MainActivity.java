package lab.imaginenat.com.project2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import lab.imaginenat.com.project2.customLayoutsAndAdapters.DialogHelper;
import lab.imaginenat.com.project2.database.BusinessDbSchema;
import lab.imaginenat.com.project2.models.Business;
import lab.imaginenat.com.project2.models.BusinessManager;
import lab.imaginenat.com.project2.models.SearchQuery;

public class MainActivity extends AppCompatActivity {


    //This is (quick)fix to remember what to show when the back button is pressed
    //There are only three states the main activity search could be in NOT SEARCHING,QUICK SEARCHING,ADVANCED SEARCH
    //There are four  places where this info needs to be set onCreate,handlerIntent,when search dialog open,and the text button on the MainActivity
    public static final int NO_SEARCH=0;
    public static final int QUICK_SEARCH=1;
    public static final int ADVANCED_SEARCH=2;
    public static int CURRENT_SEARCH=NO_SEARCH;
    public static SearchQuery SEARCH_QUERY = new SearchQuery();

    private CursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CURRENT_SEARCH=NO_SEARCH;
        Log.d("MainActivity", BusinessDbSchema.BusinessTable.CREATE_BUSINESS_TABLE);


        ListView listView = (ListView)findViewById(R.id.favorites_listView);

        Cursor c1 = BusinessManager.getInstance(MainActivity.this).getAllBusinesses();
        mCursorAdapter = new CursorAdapter(MainActivity.this,c1,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.favorite_list_item,parent,false);
            }

            @Override
            public void bindView(View view, Context context,  Cursor cursor) {

                TextView t = (TextView)view.findViewById(R.id.businessName_TextView);
                t.setText(cursor.getString(cursor.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.BIZ_NAME)));

                TextView ht = (TextView)view.findViewById(R.id.hiddenTextView);
                ht.setText(cursor.getString(cursor.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.UUID)));

                String baseURL="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
                String endURL="&key=AIzaSyBS4nUQRSuaqOYYWYmj7eCWkecFbCTjW1A";

                ImageView thumbnail = (ImageView)view.findViewById(R.id.thumbnailImg);
                //image name saved in table and save as resourceID
                int columnCount= cursor.getColumnCount();
                Log.d("MainActivity","getting column count as "+columnCount);
                String imageResource = cursor.getString(cursor.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.IMAGE_REF));
                String fullResource =baseURL+imageResource+endURL;
                String address = cursor.getString(cursor.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.ADDRESS));

                TextView addressTV=(TextView)view.findViewById(R.id.address_textView);
                addressTV.setText(address);
                //Log.d("MainActivity ","FULL:"+fullResource);
//
//
                RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);
                ratingBar.setRating(cursor.getFloat(cursor.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.FAVORITED)));

                if(imageResource.equals("chipolteImage")){
                    thumbnail.setImageResource(R.drawable.chipolte);
                }else{
                    Picasso.with(MainActivity.this).load(fullResource).error(R.drawable.android_placeholder)
                            .placeholder(R.drawable.android_placeholder)
                            .into(thumbnail);
                }



                Button deleteButton = (Button)view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"Delete this!",Toast.LENGTH_SHORT).show();
                        RelativeLayout rl = (RelativeLayout)v.getParent();
                        TextView tv = (TextView)rl.findViewById(R.id.hiddenTextView);
                        String theID = tv.getText().toString();
                        Log.d("MainActivity", "theID: " + theID);

                        DeleteBusiness deleteBusiness = new DeleteBusiness();
                        deleteBusiness.execute(theID);
                        // BusinessManager bm = BusinessManager.getInstance(MainActivity.this);
                        //bm.removeBusinessById(theID);
                        // mCursorAdapter.swapCursor(bm.getAllBusinesses());

                        Button deleteButton = (Button)rl.findViewById(R.id.deleteButton);
                        deleteButton.setVisibility(View.GONE);

                    }
                });

            }
        };

        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("MainActivity", "onItemClick");
                Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
                if (deleteButton.getVisibility() == View.VISIBLE) {
                    deleteButton.setVisibility(View.GONE);
                    return;
                }


                //Log.d("MainActivity", "Clicked!! onItemClickListener");
               // Toast.makeText(MainActivity.this, "CLICKED FROM onItemClick", Toast.LENGTH_LONG).show();
                Cursor cursor = mCursorAdapter.getCursor();
                cursor.moveToPosition(position);
                int theID = cursor.getInt(cursor.getColumnIndex("_id"));
                // Log.d("MainActivity", "theID received is " + theID + " the position is " + position);
                //Log.d("MainActivity", "onItemClick");
                Intent intent = new Intent(MainActivity.this, Detailed_Business_Activity.class);
                intent.putExtra(Business.BUSINESS_ID_KEY, theID);
                startActivityForResult(intent, 100);
                //Log.d("MainActivity","onItemClick");
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Button deleteButton = (Button)view.findViewById(R.id.deleteButton);
                deleteButton.setVisibility(View.VISIBLE);
                return true;
            }
        });




        TextView showAll = (TextView)findViewById(R.id.show_all_textView);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c =BusinessManager.getInstance(MainActivity.this).getAllBusinesses();
                mCursorAdapter.swapCursor(c);
                CURRENT_SEARCH=NO_SEARCH;
                //clear the text that shows the current search t
                TextView currentSearch = (TextView)findViewById(R.id.current_search_TextView);
                currentSearch.setText("");

                //don't make the word "ALL" look clickable (color blue)
                ((TextView)v).setTextColor(Color.BLACK);
            }
        });



        Button toSearchButton = (Button)findViewById(R.id.searchButton);
        toSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toSearchOptions = new Intent(MainActivity.this,SearchActivity.class);
                toSearchOptions.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(toSearchOptions, 200);
            }
        });

        handleIntent(getIntent());
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        if(selectedItem==R.id.searchDetail) {

            //Display a dialog that lets the user enter search options
            final AlertDialog searchDialog= new AlertDialog.Builder(this).create();
            DialogHelper dh = new DialogHelper();
            dh.onCreateTheSearchDialog(searchDialog, MainActivity.this, mCursorAdapter);
            searchDialog.show();

        }else if(selectedItem==R.id.showMap){
            //The google map does not work, but left the activity in to debug at future date
            //in the
            //Intent toMap = new Intent(MainActivity.this,MapsActivity.class);
            //startActivity(toMap);
            Intent toFakeMap = new Intent(MainActivity.this,PlaceHolderMapActivity.class);
            startActivity(toFakeMap);

        }
            return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity","onResume "+CURRENT_SEARCH);
        //Code here is used to deal with situation when the back button is pressed and returns to main screen
        if(CURRENT_SEARCH==QUICK_SEARCH){
            BusinessManager bm = BusinessManager.getInstance(MainActivity.this);
            CURRENT_SEARCH=QUICK_SEARCH;
            Cursor c = bm.getBusinessesByName(SEARCH_QUERY.quickSearch);
            //Log.d("MainActivity", "cursor count: " + c.getCount());
            mCursorAdapter.swapCursor(c);
        }else if(CURRENT_SEARCH==ADVANCED_SEARCH){
            BusinessManager bm = BusinessManager.getInstance(MainActivity.this);
            Cursor c = bm.queryBusinesses(SEARCH_QUERY.searchWhere,SEARCH_QUERY.theArgsArray
                    ,SEARCH_QUERY.onOrder);

            mCursorAdapter.swapCursor(c);
        }else{
            Cursor c =BusinessManager.getInstance(MainActivity.this).getAllBusinesses();
            mCursorAdapter.swapCursor(c);
        }
        super.onResume();


    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            CURRENT_SEARCH=QUICK_SEARCH;
            //The first part handles the query
            String query = intent.getStringExtra(SearchManager.QUERY);
            //moved this code to onResume (for back Button press)
//            //Log.d("MainActivity","query: "+query);
//            BusinessManager bm = BusinessManager.getInstance(MainActivity.this);
//            CURRENT_SEARCH=QUICK_SEARCH;
//            Cursor c = bm.getBusinessesByName(query);
//            //Log.d("MainActivity", "cursor count: " + c.getCount());
//            mCursorAdapter.swapCursor(c);
            SEARCH_QUERY.quickSearch=query;


            //The second part handles the TEXT at the top of the ListView
            //this allows the user to see if they are looking at the full list or a filtered one
            TextView currentSearch = (TextView)findViewById(R.id.current_search_TextView);
            currentSearch.setText(">Search for " + query);

            //change the color of the text so it looks "clickable"
            //clicking this text reviews removes all filters(searches) on the list
            TextView showAllTextView = (TextView)findViewById(R.id.show_all_textView);
            showAllTextView.setTextColor(Color.BLUE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==100){
            //Cursor c =BusinessManager.getInstance(MainActivity.this).getAllBusinesses();
            //mCursorAdapter.swapCursor(c);
        }else if(requestCode==200){
            //back from adding to list or canceling
            //Cursor c =BusinessManager.getInstance(MainActivity.this).getAllBusinesses();
            //mCursorAdapter.swapCursor(c);

        }
    }
    private class DeleteBusiness extends AsyncTask<String,Void,Void> {
        String businessName,address,state,zip;

        @Override
        protected Void doInBackground(String... params) {
            BusinessManager bm = BusinessManager.getInstance(MainActivity.this);
            bm.removeBusinessById(params[0]);

            publishProgress();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar progressBar = (ProgressBar)findViewById(R.id.deleteBusinessProgressBar);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            BusinessManager bm = BusinessManager.getInstance(MainActivity.this);
            mCursorAdapter.swapCursor(bm.getAllBusinesses());
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.deleteBusinessProgressBar);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
