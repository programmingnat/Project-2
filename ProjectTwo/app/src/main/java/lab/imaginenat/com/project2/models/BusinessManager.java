package lab.imaginenat.com.project2.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import lab.imaginenat.com.project2.database.BusinessDbSchema;
import lab.imaginenat.com.project2.database.BusinessTableHelper;

/**
 * Created by nat on 2/3/16.
 */
public class BusinessManager {
    private static BusinessManager mInstance;

    private Context mContext;
    private SQLiteDatabase mDatabase;


    private BusinessManager(Context context){
        mContext = context;
        mDatabase =  BusinessTableHelper.getInstance(mContext).getWritableDatabase();
    }

    public static BusinessManager getInstance(Context c){
        if(mInstance==null){
            mInstance = new BusinessManager(c.getApplicationContext());
        }
        return mInstance;
    }

    public Business getBusinessById(int id){
        Cursor c =BusinessTableHelper.getInstance(mContext).getBusinessById(id);
        c.moveToFirst();
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
        Cursor c = bth.searchBusinessByName(s,orderBy);
        return c;
    }
    public Cursor getBusinessesByName(String s){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        Cursor c = bth.searchBusinessByName(s);
        Log.d("BusinessManager","count from find my "+c.getCount());
        return c;
    }
    //remove business
    public void removeBusinessById(String id){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        bth.deleteBusinessById(id);
    }
    //add business
    public void addBusiness(Business b){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        bth.insertEntry(b);
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


}
