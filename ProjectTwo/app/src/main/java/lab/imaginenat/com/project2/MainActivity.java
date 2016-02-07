package lab.imaginenat.com.project2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import lab.imaginenat.com.project2.database.BusinessDbSchema;

//
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", BusinessDbSchema.BusinessTable.CREATE_BUSINESS_TABLE);


        ListView listView = (ListView)findViewById(R.id.favorites_listView);
        final Cursor c = BusinessManager.getInstance(MainActivity.this).getAllBusinesses();
        CursorAdapter cursorAdapter = new CursorAdapter(MainActivity.this,c,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.favorite_list_item,parent,false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView t = (TextView)view.findViewById(R.id.businessName_TextView);
                t.setText(cursor.getString(c.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.BIZ_NAME)));
            }
        };

        listView.setAdapter(cursorAdapter);

        Button toSearchButton = (Button)findViewById(R.id.searchButton);
        toSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* DownloadData downloader = new DownloadData(SearchActivity.ONLINE_QUERY);
                downloader.execute();
                String s = downloader.getData();
                if(s==null){
                    Log.d("MainActivity.class","the data is null");
                    return;
                }
                */
                //JSONReader reader = new JSONReader("40.7144,-74.006");
                //reader.execute();
                Intent toSearchOptions = new Intent(MainActivity.this,SearchActivity.class);
                toSearchOptions.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(toSearchOptions);
            }
        });
    }
}
