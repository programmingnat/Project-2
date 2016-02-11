package lab.imaginenat.com.project2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import lab.imaginenat.com.project2.models.Business;

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

    public void deleteBusinessById(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BusinessDbSchema.BusinessTable.NAME,
                "_id=?",
                new String[]{id});
    }
    public Cursor getBusinessById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BusinessDbSchema.BusinessTable.NAME,//table
                BusinessDbSchema.BusinessTable.ALL_COLS,
                " _id=?",//select
                new String[]{Integer.toString(id)},//selection args
                null,//group
                null,//having
                null,//order
                null);//limit

        return c;
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
    public Cursor queryBusiness(String whereStatement,String[] whereArgs,String order) {
        Log.d("queryBusiness","whereStatement "+whereStatement+" orderBy "+order);
        for(int i=0;i<whereArgs.length;i++){
            Log.d("queryBusiness loop","arg:"+whereArgs[i]);
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BusinessDbSchema.BusinessTable.NAME,//table
                BusinessDbSchema.BusinessTable.ALL_COLS,
                whereStatement,//select
                whereArgs,//selection args
                null,//group
                null,//having
                null,//order
                null);//limit

        return c;


    }
    public Cursor searchBusinessByName(String s){
        Log.d("BTH","search string is "+s);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BusinessDbSchema.BusinessTable.NAME,//table
                BusinessDbSchema.BusinessTable.ALL_COLS,
                "bizName LIKE ?",//select
                new String[]{"%"+s+"%"},//selection args
                null,//group
                null,//having
                null,//order
                null);//limit

        return c;
    }
    public Cursor searchBusinessByName(String s,String orderBy){
        Log.d("BTH","search string is "+s);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(BusinessDbSchema.BusinessTable.NAME,//table
                BusinessDbSchema.BusinessTable.ALL_COLS,
                "bizName LIKE ?",//select
                new String[]{"%"+s+"%"},//selection args
                null,//group
                null,//having
                orderBy,//order
                null);//limit

        return c;
    }
    public void updateEntry(Business b){
        Log.d("BusinessTableHelper","Inside updateEntry with "+b.getDescription());
        Log.d("BusinessTableHelper","the id to update is "+b.getId());
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = getContentValues(b);
        db.update(BusinessDbSchema.BusinessTable.NAME,
                cv,
                "_id=?",
                new String[]{Integer.toString(b.getId())});
    }

    public void insertEntry(Business b){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = getContentValues(b);
        db.insert(BusinessDbSchema.BusinessTable.NAME,
                null,
                cv);
    }
    //getContentValues
    private static ContentValues getContentValues(Business b){
        ContentValues values = new ContentValues();
        values.put(BusinessDbSchema.BusinessTable.Cols.BIZ_NAME,b.getName());
        values.put(BusinessDbSchema.BusinessTable.Cols.ADDRESS,b.getAddress());
        values.put(BusinessDbSchema.BusinessTable.Cols.STATE,b.getState());
        values.put(BusinessDbSchema.BusinessTable.Cols.ZIP,b.getZip());
        values.put(BusinessDbSchema.BusinessTable.Cols.IMAGE_REF,b.getImageResource());
        values.put(BusinessDbSchema.BusinessTable.Cols.LATITUDE,b.getLat());
        values.put(BusinessDbSchema.BusinessTable.Cols.LONGITUDE,b.getLong());
        values.put(BusinessDbSchema.BusinessTable.Cols.TYPE,b.getType());
        values.put(BusinessDbSchema.BusinessTable.Cols.FAVORITED,b.getRatings());
        values.put(BusinessDbSchema.BusinessTable.Cols.REVIEW, b.getDescription());

        return values;
    }

}
