package lab.imaginenat.com.project2.models;

/**
 * Created by nat on 2/6/16.
 */
public class Place {

    public static String PLACE_ID_EXTRA = "PLACE_ID_EXTRA";
    String mBusinessName;
    String mBusinessAddress;
    String mBusinessCity;
    String mBusinessState;
    String mZipCode;

    public Place(String businessName){
        mBusinessName=businessName;
    }

    public void setBusinessName(String businessName) {
        mBusinessName = businessName;
    }

    public String getBusinessAddress() {
        return mBusinessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        mBusinessAddress = businessAddress;
    }

    public String getBusinessCity() {
        return mBusinessCity;
    }

    public void setBusinessCity(String businessCity) {
        mBusinessCity = businessCity;
    }

    public String getBusinessState() {
        return mBusinessState;
    }

    public void setBusinessState(String businessState) {
        mBusinessState = businessState;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    public String getBusinessName() {
        return mBusinessName;

    }

    public void setAddress(String s){
        //913 Broadway, New York, NY 10010, United States"
        String[] entireAddress=s.split(",");
        mBusinessAddress= entireAddress[0];
        mBusinessCity=entireAddress[1];
        String[]stateAndZip= entireAddress[2].split(" ");
        mBusinessState=stateAndZip[1];
        mZipCode=stateAndZip[2];

        //Log.d("Place ","Inside setAddress with "+entireAddress[2]+"| "+stateAndZip[0]+" |"+stateAndZip[1]);
    }
}
