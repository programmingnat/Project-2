package lab.imaginenat.com.project2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lab.imaginenat.com.project2.database.BusinessDbSchema;
import lab.imaginenat.com.project2.models.Business;
import lab.imaginenat.com.project2.models.BusinessManager;

//
public class MainActivity extends AppCompatActivity {



    private CursorAdapter mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                Picasso.with(MainActivity.this).load(fullResource).error(R.drawable.android_placeholder)
                        .placeholder(R.drawable.android_placeholder)
                        .into(thumbnail);

                Button deleteButton = (Button)view.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"Delete this!",Toast.LENGTH_SHORT).show();
                        RelativeLayout rl = (RelativeLayout)v.getParent();
                        TextView tv = (TextView)rl.findViewById(R.id.hiddenTextView);
                        String theID = tv.getText().toString();
                        Log.d("MainActivity", "theID: " + theID);
                        BusinessManager bm = BusinessManager.getInstance(MainActivity.this);
                        bm.removeBusinessById(theID);
                        mCursorAdapter.swapCursor(bm.getAllBusinesses());
                    }
                });

            }
        };

        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MainActivity", "onItemClick");
                Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
                if (deleteButton.getVisibility() == View.VISIBLE) {
                    deleteButton.setVisibility(View.GONE);
                    return;
                }


                //Log.d("MainActivity", "Clicked!! onItemClickListener");
                Toast.makeText(MainActivity.this, "CLICKED FROM onItemClick", Toast.LENGTH_LONG).show();
                Cursor cursor = mCursorAdapter.getCursor();
                cursor.moveToPosition(position);
                int theID = cursor.getInt(cursor.getColumnIndex("_id"));
                // Log.d("MainActivity", "theID received is " + theID + " the position is " + position);
                Log.d("MainActivity", "onItemClick");
                Intent intent = new Intent(MainActivity.this, Detailed_Business_Activity.class);
                intent.putExtra(Business.BUSINESS_ID_KEY, theID);
                startActivity(intent);
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
                startActivity(toSearchOptions);
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {

        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {

        }else {
                Log.d("MainActivity","NOT ACTION SEARCH");
            Cursor c = BusinessManager.getInstance(MainActivity.this).getAllBusinesses();
            mCursorAdapter.swapCursor(c);
        }
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();
        if(selectedItem==R.id.searchDetail) {
            //Toast.makeText(MainActivity.this,"selected detail",Toast.LENGTH_SHORT).show();


            LayoutInflater factory = LayoutInflater.from(this);
            final View searchOptionsDialog = factory.inflate(
                    R.layout.advanced_search_options, null);
            final AlertDialog searchDialog= new AlertDialog.Builder(this).create();
            searchDialog.setView(searchOptionsDialog);
            searchOptionsDialog.findViewById(R.id.cancelSearchButton).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //your business logic
                    searchDialog.dismiss();
                }
            });
            //not using the recommended RadioGroup way, because it doesn't work across Layouts
            final RadioButton searchByNameRB = (RadioButton)searchOptionsDialog.findViewById(R.id.searchByName_radioButton);
            final RadioButton searchByRatingRB = (RadioButton)searchOptionsDialog.findViewById(R.id.searchByRating_radioButton);
            final RadioButton searchByKeywordsRB = (RadioButton)searchOptionsDialog.findViewById(R.id.searchByKeywords_radioButton);
            searchOptionsDialog.findViewById(R.id.performSearchButton).setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    //query by NAME,RATING, OR KEYWORDS

                    String searchWhere=BusinessDbSchema.BusinessTable.Cols.BIZ_NAME+" LIKE ?";;
                    ArrayList<String> searchArgs= new ArrayList<String>();
                    String orderBy = BusinessDbSchema.BusinessTable.Cols.BIZ_NAME+" ASC";
                    BusinessManager bm  = BusinessManager.getInstance(MainActivity.this);

                    if(searchByNameRB.isChecked()){
                        TextView tv = (TextView)searchOptionsDialog.findViewById(R.id.searchTerm_editText);
                        String s =tv.getText().toString();
                        searchArgs.add("%" + s + "%");
                        Log.d("query", "adding " + s + " to searchArgs");
                        TextView filterTF = (TextView)MainActivity.this.findViewById(R.id.current_search_TextView);
                        filterTF.setText(">NAMES similar to "+s);
                    }else if(searchByRatingRB.isChecked()){
                        Spinner spin = (Spinner)searchOptionsDialog.findViewById(R.id.search_rating_spinner);
                        String spinnerValue=spin.getSelectedItem().toString();
                        searchWhere=BusinessDbSchema.BusinessTable.Cols.FAVORITED+">=?";

                        if(spinnerValue.equals("One Star")){
                            searchArgs.add("1");
                        }else if(spinnerValue.equals("Two Stars")){
                            searchArgs.add("2");
                        }else if(spinnerValue.equals("Three Stars")){
                            searchArgs.add("3");
                        }else if(spinnerValue.equals("Four Stars")){
                            searchArgs.add("4");
                        }else if(spinnerValue.equals("Five Stars")){
                            searchArgs.add("5");
                        }else{
                            searchArgs.add("0");
                        }
                        TextView filterTF = (TextView)searchOptionsDialog.findViewById(R.id.current_search_TextView);
                        filterTF.setText(">RANK is at least  "+spinnerValue+" stars ");
                    }else if(searchByKeywordsRB.isChecked()){
                        TextView keyWordTF = (TextView)searchOptionsDialog.findViewById(R.id.search_review_editText);
                        String allKeywords=keyWordTF.getText().toString();
                        String[] keywords=allKeywords.split(" ");
                        searchWhere="";
                        for(int i=0;i<keywords.length;i++){
                            searchArgs.add("%"+keywords[i]+"%");

                            searchWhere+=BusinessDbSchema.BusinessTable.Cols.REVIEW+" LIKE ?";
                            if(i!=keywords.length-1){
                                searchWhere+=" OR ";
                            }

                        }//end of for loop




                    }
                    //order by NAME,DISTANCE,RATINGS
                    if(((RadioButton)searchOptionsDialog.findViewById(R.id.orderByRatings)).isChecked()){
                        orderBy= BusinessDbSchema.BusinessTable.Cols.FAVORITED+" DESC";
                    }
                    String[] argsArray = new String[searchArgs.size()];

                    for( int i=0;i<searchArgs.size();i++){
                        argsArray[i]=searchArgs.get(i);

                    }
                    Cursor c = bm.queryBusinesses(searchWhere,argsArray,orderBy);
                    Log.d("query","cursor count "+c.getCount());
                    mCursorAdapter.swapCursor(c);
                    searchDialog.dismiss();

                }
            });
            Spinner spin = (Spinner) searchOptionsDialog.findViewById(R.id.search_rating_spinner);
            List<String> list = new ArrayList<String>();
            list.add("One Star");
            list.add("Two Stars");
            list.add("Three Stars");
            list.add("Four Stars");
            list.add("Five Stars");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(dataAdapter);



            searchByNameRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchByKeywordsRB.setChecked(false);
                    searchByRatingRB.setChecked(false);
                }
            });
            searchByRatingRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchByNameRB.setChecked(false);
                    searchByKeywordsRB.setChecked(false);
                }
            });
            searchByKeywordsRB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchByNameRB.setChecked(false);
                    searchByRatingRB.setChecked(false);

                }
            });

            searchDialog.show();

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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("MainActivity","query: "+query);
            BusinessManager bm = BusinessManager.getInstance(MainActivity.this);
            Cursor c = bm.getBusinessesByName(query);
            Log.d("MainActivity", "cursor count: " + c.getCount());
            TextView filterTF = (TextView)MainActivity.this.findViewById(R.id.current_search_TextView);
            filterTF.setText(">NAMES similar to " + query);
            mCursorAdapter.swapCursor(c);

            TextView currentSearch = (TextView)findViewById(R.id.current_search_TextView);
            currentSearch.setText(">Search for " + query);

            TextView showAllTextView = (TextView)findViewById(R.id.show_all_textView);
            showAllTextView.setTextColor(Color.BLUE);
        }
    }


}
