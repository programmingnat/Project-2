package lab.imaginenat.com.project2.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import lab.imaginenat.com.project2.Business;
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
    //add business
    public void addBusiness(Business b){
        Log.d("BusinessManager ","info in "+b);
        ContentValues values = getContentValues(b);
        mDatabase.insert(BusinessDbSchema.BusinessTable.NAME,null,values);
    }
    //getAllBusinesses
    public Cursor getAllBusinesses(){
        BusinessTableHelper bth = BusinessTableHelper.getInstance(mContext);
        return bth.getAllBusiness();
    }
    //getBusiness

    //getContentValues
    private static ContentValues getContentValues(Business b){
        ContentValues values = new ContentValues();
        values.put(BusinessDbSchema.BusinessTable.Cols.BIZ_NAME,b.getName());
        values.put(BusinessDbSchema.BusinessTable.Cols.ADDRESS,b.getAddress());
        values.put(BusinessDbSchema.BusinessTable.Cols.STATE,b.getState());
        values.put(BusinessDbSchema.BusinessTable.Cols.ZIP,b.getZip());
        values.put(BusinessDbSchema.BusinessTable.Cols.TYPE,b.getType());
        values.put(BusinessDbSchema.BusinessTable.Cols.FAVORITED,b.isFavorited());

        return values;
    }
}
