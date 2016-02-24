package lab.imaginenat.com.project2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import lab.imaginenat.com.project2.models.Business;
import lab.imaginenat.com.project2.models.BusinessManager;

public class Detailed_Business_Activity extends AppCompatActivity {

    EditText mYourNotes_EditText;
    Business mBusiness;
    RatingBar mRatingBar;
    int mPrimaryKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed__business_);

        Intent inComingIntent = getIntent();
        int primaryKey = inComingIntent.getIntExtra(Business.BUSINESS_ID_KEY, -1);
        mPrimaryKey = primaryKey;
        if(primaryKey==-1){
            //reveal error message?
            Log.d("Business_Activity","Error, could not find primaryKey");
        }



        EditBusiness editBusiness = new EditBusiness();
        editBusiness.execute(new Boolean(true));



    }

    @Override
    protected void onPause() {
        super.onPause();
        //save the reviews and the starts
        mBusiness.setDescription(mYourNotes_EditText.getText().toString());
        float rating=mRatingBar.getRating();
        mBusiness.setRatings(rating);
        EditBusiness editBusiness = new EditBusiness();
        editBusiness.execute(new Boolean(false));
//        BusinessManager.getInstance(Detailed_Business_Activity.this).updateBusiness(mBusiness);
        Log.d("Detailed_BusineActivity","the text of the notes "+mBusiness.getDescription());
    }

    @Override
    public void onBackPressed() {
        Intent p = new Intent();
        setResult(100,p);
        finish();
    }


    private class EditBusiness extends AsyncTask<Boolean ,Void,Business> {
        String businessName,address,state,zip;

        @Override
        protected Business doInBackground(Boolean... isOnLoad) {

            if(isOnLoad[0]){
                BusinessManager bm = BusinessManager.getInstance(Detailed_Business_Activity.this);
                mBusiness = bm.getBusinessById(Detailed_Business_Activity.this.mPrimaryKey);
                return mBusiness;
            }else {
                BusinessManager.getInstance(Detailed_Business_Activity.this).updateBusiness(mBusiness);
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ProgressBar progressBar = (ProgressBar)findViewById(R.id.editBusinessProgressBar);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Business business) {
            super.onPostExecute(business);

            if(business!=null){
                TextView textView = (TextView)findViewById(R.id.businessNameTextView);
                textView.setText(mBusiness.getName());

                mYourNotes_EditText = (EditText)findViewById(R.id.yourNotes_editText);
                mYourNotes_EditText.setText(mBusiness.getDescription());
                String baseURL="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
                String endURL="&key=AIzaSyBS4nUQRSuaqOYYWYmj7eCWkecFbCTjW1A";

                ImageView thumbnail = (ImageView)findViewById(R.id.detailImageView);
                thumbnail.setScaleType(ImageView.ScaleType.FIT_XY);
                //image name saved in table and save as resourceID
                String imageResource = mBusiness.getImageResource();
                Log.d("DetaileBusinessActivity", "image resource: " + imageResource);
                String fullResource =baseURL+imageResource+endURL;

                if(imageResource.equals("chipolteImage")){
                    thumbnail.setImageResource(R.drawable.chipolte);
                }else{
                    Picasso.with(Detailed_Business_Activity.this).load(fullResource).error(R.drawable.android_placeholder)
                            .placeholder(R.drawable.android_placeholder)
                            .into(thumbnail);

                }


                Log.d("dba","the number of starts "+mBusiness.getRatings());
                mRatingBar = (RatingBar)findViewById(R.id.ratingBar);
                mRatingBar.setRating(mBusiness.getRatings());
            }
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.editBusinessProgressBar);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
