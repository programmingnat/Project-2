package lab.imaginenat.com.project2.customLayoutsAndAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import lab.imaginenat.com.project2.AddNewBusinessActivity;
import lab.imaginenat.com.project2.R;
import lab.imaginenat.com.project2.models.Place;
import lab.imaginenat.com.project2.models.PlaceManager;

/**
 * Created by nat on 2/8/16.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceItemHolder> {


    Context mContext;
    PlaceManager mPlaceManager;

    public PlacesAdapter(Context c ){

        mPlaceManager = PlaceManager.getInstance();
        mContext =c;

    }
    @Override
    public PlacesAdapter.PlaceItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View listItemView = inflater.inflate(R.layout.place_item_view, parent, false);

        // Return a new holder instance
        PlaceItemHolder viewHolder = new PlaceItemHolder(listItemView);
        return viewHolder;
    }
    public void onBindViewHolder(PlaceItemHolder holder, int position) {
        Place place = mPlaceManager.getPlaceAtIndex(position);
        holder.bindListItem(place);
        //holder.mTitleTextView.setText(player.getName());
    }

    @Override
    public int getItemCount() {
        return mPlaceManager.getSize();
    }


    ////---------THE VIEW HOLDER (for recyeler view)------------------------
    public class PlaceItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public TextView mPlaceNameTextView;
        private Place mPlace;
        private LinearLayout mLinearLayout;

        public PlaceItemHolder(View v){
            super(v);


            mLinearLayout = (LinearLayout)v.findViewById(R.id.place_item_layout);
            mLinearLayout.setOnClickListener(this);
            mPlaceNameTextView = (TextView)v.findViewById(R.id.place_name_textView);




            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            mLinearLayout.setBackgroundColor(color);

        }


        public void bindListItem(Place p){
            mPlace =p;
            mPlaceNameTextView.setText(mPlace.getBusinessName());

        }

        public void onClick(View v){

            Intent toAddIntent = new Intent(((Activity)mContext), AddNewBusinessActivity.class);
            int theIndex=PlaceManager.getInstance().getIndexOf(mPlace);
            toAddIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            toAddIntent.putExtra(Place.PLACE_ID_EXTRA,theIndex);
            mContext.startActivity(toAddIntent);

        }

        public void showDeleteButton(int v){

        }

    }

}