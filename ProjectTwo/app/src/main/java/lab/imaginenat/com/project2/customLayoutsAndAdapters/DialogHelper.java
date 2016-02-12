package lab.imaginenat.com.project2.customLayoutsAndAdapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lab.imaginenat.com.project2.MainActivity;
import lab.imaginenat.com.project2.R;
import lab.imaginenat.com.project2.database.BusinessDbSchema;
import lab.imaginenat.com.project2.models.BusinessManager;

/**
 * Created by nat on 2/11/16.
 */
public class DialogHelper {

    public DialogHelper(){

    }

    /**
     * This method is essentially acts like an onCreate of an activity in that it
     * inflates the xml, fills in the data as well as event listeners
     * @param searchDialog - the dialog to be displayed
     * @param context - the context (Activity) where the dialog is to be displayed
     * @param cursorAdapter - adapter to the adapter used by the list of which this search options dialog can change
     */
    public void onCreateTheSearchDialog(final AlertDialog searchDialog,final Context context,final CursorAdapter cursorAdapter){
        LayoutInflater factory = LayoutInflater.from(context);
        final View searchOptionsDialog = factory.inflate(
                R.layout.advanced_search_options, null);
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

                String searchWhere= BusinessDbSchema.BusinessTable.Cols.BIZ_NAME+" LIKE ?";;
                ArrayList<String> searchArgs= new ArrayList<String>();
                String orderBy = BusinessDbSchema.BusinessTable.Cols.BIZ_NAME+" ASC";
                BusinessManager bm  = BusinessManager.getInstance(context);

                if(searchByNameRB.isChecked()){
                    TextView tv = (TextView)searchOptionsDialog.findViewById(R.id.searchTerm_editText);
                    String s =tv.getText().toString();
                    searchArgs.add("%" + s + "%");
                    Log.d("query", "adding " + s + " to searchArgs");
                    TextView filterTF = (TextView)((Activity)context).findViewById(R.id.current_search_TextView);
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
                //keep track of the last search query, which is used in Main onResume()
                Log.d("query","cursor count "+c.getCount());
                MainActivity.SEARCH_QUERY.searchWhere=searchWhere;
                MainActivity.SEARCH_QUERY.theArgsArray=argsArray;
                MainActivity.SEARCH_QUERY.onOrder=orderBy;
                MainActivity.CURRENT_SEARCH=MainActivity.ADVANCED_SEARCH;
                cursorAdapter.swapCursor(c);
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
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
    }
}
