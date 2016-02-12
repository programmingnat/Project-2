package lab.imaginenat.com.project2.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;

import lab.imaginenat.com.project2.database.BusinessDbSchema;
import lab.imaginenat.com.project2.database.BusinessTableHelper;

/**
 * Created by nat on 2/3/16.
 */
public class BusinessManager {
    private static BusinessManager mInstance;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    //The hash map keep tracks of entries, fast way to check if attempting to add duplicate entry without having to query database each time
    private static HashMap<String,String>mRecordKeeper = new HashMap<>();

    private BusinessManager(Context context){
        mContext = context;
        mDatabase =  BusinessTableHelper.getInstance(mContext).getWritableDatabase();

    }


    public static BusinessManager getInstance(Context c){
        if(mInstance==null){
            mInstance = new BusinessManager(c.getApplicationContext());
            //call the update record keeper when first initialized
            mInstance.updateRecordKeeper();
        }
        return mInstance;
    }

    //method is used to updateRecordKeeper when the class is first initialized
    //the record keeper ensures that no duplicates are added
    private void updateRecordKeeper(){
       Cursor c = getAllBusinesses();
        c.moveToFirst();
        while(c.isAfterLast()==false){
            String name=c.getString(c.getColumnIndex("bizName"));
            String address=c.getString(c.getColumnIndex("address"));
            String lat=c.getString(c.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.LATITUDE));
            String lng=c.getString(c.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.LONGITUDE));
            String key =name;//lat+lng+address+name;
            mRecordKeeper.put(key,name);
            Log.d("BusinessManager","Inserting "+name+" into record keeper");
            c.moveToNext();
        }
    }
    public Business getBusinessById(int id){
        //get the data from the table, and fill out a new instance of business class
        Cursor c =BusinessTableHelper.getInstance(mContext).getBusinessById(id);
        c.moveToFirst();
        if(c.getCount()==0){
            return null;
        }
        String name=c.getString(c.getColumnIndex("bizName"));
        String address=c.getString(c.getColumnIndex("address"));
        String state=c.getString(c.getColumnIndex("state"));
        String zip=c.getString(c.getColumnIndex("zip"));
        Business b = new Business(name,address,state,zip,"default");
        b.setId(id);
        b.setImageResource(c.getString(c.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.IMAGE_REF)));
        b.setDescription(c.getString(c.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.REVIEW)));
        b.setRatings(c.getFloat(c.getColumnIndex(BusinessDbSchema.BusinessTable.Cols.FAVORITED)));
        return b;

    }

    public Cursor queryBusinesses(String whereString,String[] whereArgs,String orderBy){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        return bth.queryBusiness(whereString,whereArgs,orderBy);

    }
    public Cursor getBusinessByName(String s,String orderBy){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        Cursor c = bth.searchBusinessByName(s, orderBy);
        return c;
    }
    public Cursor getBusinessesByName(String s){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        Cursor c = bth.searchBusinessByName(s);
        Log.d("BusinessManager", "count from find my " + c.getCount());
        return c;
    }
    //remove business
    public void removeBusinessById(String id){
        //need a reference to see if it is in record keeper
        Business b =getBusinessById(Integer.parseInt(id));
        //remove from record keeper
        String key = b.getName();//b.getLat()+b.getLong()+b.getAddress()+b.getName();
        mRecordKeeper.remove(key);
        //remove it from table
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        bth.deleteBusinessById(id);
    }
    //add business
    public void addBusiness(Business b){
        //uses mRecordKeeper (the hash map) to prevent duplicate entries
        String key = b.getName();//b.getLat()+b.getLong()+b.getAddress()+b.getName();
        if(mRecordKeeper.get(key)==null) {
            BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
            bth.insertEntry(b);
            mRecordKeeper.put(key,b.getName());
        }
    }
    //getAllBusinesses
    public Cursor getAllBusinesses(){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        return bth.getAllBusiness();
    }

    //update business
    public void updateBusiness(Business b){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        bth.updateEntry(b);

    }

    public static boolean isAlreadyInTable(String lat, String lng,String address,String name){
        Log.d("BusinessManager","Checking if "+name+" is already in table");
        String key = name;//lat+lng+address+name;
        String result = mRecordKeeper.get(key);
        Log.d("BusinessManager","using key "+key+" to check ");
        if(result==null ){
            Log.d("BusinessManager","returning false");
            return false;
        }else{
            Log.d("BusinessManager","found: "+result+"at key("+key+") returning true");
            return true;
        }
    }


}
