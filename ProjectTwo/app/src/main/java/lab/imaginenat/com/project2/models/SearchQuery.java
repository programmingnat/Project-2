package lab.imaginenat.com.project2.models;

/**
 * This class is used as a fix to store the last query info
 * As of now, accesed by the onResume method in the MainActivity
 * but the values are set in the search options
 * Created by nat on 2/12/16.
 */
public class SearchQuery {

    public String searchWhere=null;
    public String[] theArgsArray=null;
    public String onOrder=null;
    public String quickSearch=null;

}
