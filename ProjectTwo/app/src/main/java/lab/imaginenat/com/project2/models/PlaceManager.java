package lab.imaginenat.com.project2.models;

import java.util.ArrayList;

/**
 * PlaceManager manages the Places (which is data for each restaurant returned by google)
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

    public void clearAll(){
        mAllPlacesFound.clear();
    }

    //mark any places as "in database" if it is already in the database
    public void checkAndUpdateIfInDatabase(){
        for(Place p:mAllPlacesFound){
            boolean isAlreadyInDatabase = BusinessManager.isAlreadyInTable(p.getLatitude(),p.getLongitude(),p.getBusinessAddress(),p.getBusinessName());
            p.setIsInDatabase(isAlreadyInDatabase);
        }

    }
    
}
