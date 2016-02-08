package lab.imaginenat.com.project2.models;

import java.util.ArrayList;

/**
 * Created by nat on 2/8/16.
 */
public class PlaceManager {

    public static PlaceManager mInstance=null;
    private ArrayList<Place>mAllPlacesFound;

    public static PlaceManager getInstance(){
        if(mInstance==null){
            mInstance=new PlaceManager();
        }
        return mInstance;
    }
    private PlaceManager(){
        mAllPlacesFound = new ArrayList<>();
    }

    public ArrayList<Place> getAllPlacesFound() {
        return mAllPlacesFound;
    }

    public void addPlace(Place p){
        mAllPlacesFound.add(p);
    }

    public int getIndexOf(Place p){
        if(mAllPlacesFound.size()==0){
            return -1;
        }
       return  mAllPlacesFound.indexOf(p);
    }
    public Place getPlaceAtIndex(int i){
        if(mAllPlacesFound.size()==0){
            return null;
        }
        return mAllPlacesFound.get(i);
    }

    public int getSize(){
        return mAllPlacesFound.size();
    }
}
