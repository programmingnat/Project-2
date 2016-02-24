package lab.imaginenat.com.project2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import lab.imaginenat.com.project2.models.Business;
import lab.imaginenat.com.project2.models.BusinessManager;
import lab.imaginenat.com.project2.models.Place;
import lab.imaginenat.com.project2.models.PlaceManager;

public class AddNewBusinessActivity extends AppCompatActivity {

    private Place mPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_business);


        int foundID = getIntent().getIntExtra(Place.PLACE_ID_EXTRA,-1);
        if(foundID>-1){
            Place p = PlaceManager.getInstance().getPlaceAtIndex(foundID);
            //pre populat the text fields
            EditText nameTF = (EditText)findViewById(R.id.businessName_EditText);
            nameTF.setText(p.getBusinessName());
            EditText addressTF = (EditText)findViewById(R.id.businessAddress_EditText);
            addressTF.setText(p.getBusinessAddress());
            EditText stateTF = (EditText)findViewById(R.id.businessState_EditText);
            stateTF.setText(p.getBusinessState());
            EditText zipTF = (EditText)findViewById(R.id.businessZip_EditText);
            zipTF.setText(p.getZipCode());
            mPlace=p;
        }
        Button addButton = (Button)findViewById(R.id.addToDB_Button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //From Place (which is returned in internet search) to Business (which is kept locally in database)


                //create new business from place (I know classes are kind of similar)
                //initially was going to use places to put marks on google map activity
                AddBusiness addBusiness = new AddBusiness();
                addBusiness.execute();
                finish();
            }

        });

        Button cancelButton = (Button)findViewById(R.id.cancel_Button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private class AddBusiness extends AsyncTask<Void,Void,Void> {
        String businessName,address,state,zip;

        @Override
        protected Void doInBackground(Void... params) {
            Business b = new Business(businessName,address, state,zip,"default");
            b.setImageResource(mPlace.getImageResource());
            b.setLat(mPlace.getLatitude());
            b.setLong(mPlace.getLongitude());
            BusinessManager manager=BusinessManager.getInstance(AddNewBusinessActivity.this);
            manager.addBusiness(b);
            publishProgress();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             EditText nameTF = (EditText)findViewById(R.id.businessName_EditText);
             EditText addressTF = (EditText)findViewById(R.id.businessAddress_EditText);
             EditText stateTF = (EditText)findViewById(R.id.businessState_EditText);
             EditText zipTF = (EditText)findViewById(R.id.businessZip_EditText);
            businessName = nameTF.getText().toString();
            address=addressTF.getText().toString();
            state = stateTF.getText().toString();
            zip = zipTF.getText().toString();
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.addBusinessProgressBar);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ProgressBar progressBar = (ProgressBar)findViewById(R.id.addBusinessProgressBar);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
