package lab.imaginenat.com.project2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import lab.imaginenat.com.project2.models.Business;
import lab.imaginenat.com.project2.models.BusinessManager;

public class Detailed_Business_Activity extends AppCompatActivity {

    EditText mYourNotes_EditText;
    Business mBusiness;
    RatingBar mRatingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed__business_);

        Intent inComingIntent = getIntent();
        int primaryKey = inComingIntent.getIntExtra(Business.BUSINESS_ID_KEY, -1);
        if(primaryKey==-1){
            //reveal error message?
            Log.d("Business_Activity","Error, could not find primaryKey");
        }




        BusinessManager bm = BusinessManager.getInstance(Detailed_Business_Activity.this);
        mBusiness = bm.getBusinessById(primaryKey);
        Log.d("DetailedBuAc","description: "+mBusiness.getDescription());
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

        Picasso.with(Detailed_Business_Activity.this).load(fullResource).error(R.drawable.android_placeholder)
                .placeholder(R.drawable.android_placeholder)
                .into(thumbnail);

        Log.d("dba","the number of starts "+mBusiness.getRatings());
         mRatingBar = (RatingBar)findViewById(R.id.ratingBar);
        mRatingBar.setRating(mBusiness.getRatings());
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //update the database? or just do it onPause
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //save the reviews and the starts
        mBusiness.setDescription(mYourNotes_EditText.getText().toString());
        float rating=mRatingBar.getRating();
        mBusiness.setRatings(rating);
        BusinessManager.getInstance(Detailed_Business_Activity.this).updateBusiness(mBusiness);
        Log.d("Detailed_BusineActivity","the text of the notes "+mBusiness.getDescription());
    }
}
