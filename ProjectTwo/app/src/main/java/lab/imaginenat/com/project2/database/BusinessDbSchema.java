package lab.imaginenat.com.project2.database;

/**
 * Created by nat on 2/3/16.
 */
public class BusinessDbSchema {
    public static final class BusinessTable {
        public static final String NAME = "businesses";
        public static final String[] ALL_COLS = {Cols.UUID, Cols.BIZ_NAME, Cols.ADDRESS, Cols.STATE,Cols.ZIP, Cols.TYPE, Cols.FAVORITED};
        public static final String CREATE_BUSINESS_TABLE = "CREATE TABLE " + BusinessDbSchema.BusinessTable.NAME +
                "(" +
                BusinessDbSchema.BusinessTable.Cols.UUID + " INTEGER PRIMARY KEY, " +
                BusinessDbSchema.BusinessTable.Cols.BIZ_NAME + " TEXT, " +
                BusinessDbSchema.BusinessTable.Cols.ADDRESS + " TEXT, " +
                BusinessDbSchema.BusinessTable.Cols.STATE + " TEXT, " +
                BusinessDbSchema.BusinessTable.Cols.ZIP + " TEXT, " +
                BusinessDbSchema.BusinessTable.Cols.TYPE + " TEXT, " +
                BusinessDbSchema.BusinessTable.Cols.FAVORITED + " TEXT )";

        public static final class Cols {
            public static final String UUID = "_id";
            public static final String BIZ_NAME = "bizName";
            public static final String ADDRESS = "address";
            public static final String STATE = "state";
            public static final String ZIP = "zip";
            public static final String TYPE = "type";
            public static final String FAVORITED = "isFavorite";
        }
    }
}