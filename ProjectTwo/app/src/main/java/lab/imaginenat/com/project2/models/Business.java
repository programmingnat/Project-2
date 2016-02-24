package lab.imaginenat.com.project2.models;
import java.util.UUID;

/**
 * The Business class is used to represent the data in the favorites list
 * on the main screen (as opposed to the Place class, that holds the data returned from the Google Places API
 * Created by nat on 2/3/16.
 */
public class Business {


    public static String BUSINESS_ID_KEY="BUSINESS_ID";
    private String mName;
    private UUID mUUID;
    private String mAddress;
    private String mState;
    private String mZip;
    private String mType;
    private String mImageResource;
    private String mLat,mLong;
    private String mDescription;
    private float mRatings;
    private int mId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public float getRatings() {
        return mRatings;
    }

    public void setRatings(float ratings) {
        mRatings = ratings;
    }

    public String getImageResource() {
        return mImageResource;
    }

    public void setImageResource(String imageResource) {
        mImageResource = imageResource;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getLong() {
        return mLong;
    }

    public void setLong(String aLong) {
        mLong = aLong;
    }

    private boolean mIsFavorited;


    public Business(String name,  String address, String state, String zip, String type) {
        mName = name;
        mAddress = address;
        mState = state;
        mZip = zip;
        mType = type;
        mUUID=UUID.randomUUID();
        mIsFavorited=false;

    }

    public void setName(String name) {
        mName = name;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getZip() {
        return mZip;
    }

    public void setZip(String zip) {
        mZip = zip;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public boolean isFavorited() {
        return mIsFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        mIsFavorited = isFavorited;
    }



    public String getName() {
        return mName;
    }
}