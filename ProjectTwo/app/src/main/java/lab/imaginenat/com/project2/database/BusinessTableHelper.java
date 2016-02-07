package lab.imaginenat.com.project2.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nat on 2/3/16.
 */
public class BusinessTableHelper extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="businessBase.db";
    private static BusinessTableHelper mInstance;

    private BusinessTableHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    public static BusinessTableHelper getInstance(Context c){
        if(mInstance==null){
            mInstance= new BusinessTableHelper(c.getApplicationContext());
        }
        return  mInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(BusinessDbSchema.BusinessTable.CREATE_BUSINESS_TABLE);
        Log.d("BusinessTableHelper","Inside on create "+ BusinessDbSchema.BusinessTable.NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAllBusiness() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BusinessDbSchema.BusinessTable.NAME,//table
                BusinessDbSchema.BusinessTable.ALL_COLS,
                null,//select
                null,//selection args
                null,//group
                null,//having
                null,//order
                null);//limit

        return c;
    }

}
