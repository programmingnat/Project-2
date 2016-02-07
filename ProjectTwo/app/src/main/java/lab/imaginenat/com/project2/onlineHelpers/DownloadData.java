package lab.imaginenat.com.project2.onlineHelpers;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by nat on 2/6/16.
 */
enum DownloadStatus{IDLE,PROCESSING,NOT_INTIALIZED,FAILED_OR_EMPTY,OK}
public class DownloadData {

    private String LOG_TAG=DownloadData.class.getSimpleName();
    private String mRawURL;
    private String mData;
    private DownloadStatus mDownloadStatus;

    public DownloadData(String url) {
        this.mRawURL = url;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    public void reset(){
        this.mDownloadStatus =DownloadStatus.IDLE;
        this.mRawURL =null;
        this.mDownloadStatus =null;
    }

    public void setRawURL(String nRawURL) {
        this.mRawURL = nRawURL;
    }

    public String getData() {
        return mData;
    }

    public DownloadStatus getDownloadStatus() {
        return mDownloadStatus;
    }

    public void execute(){
        this.mDownloadStatus =DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawURL);
    }

    public class DownloadRawData extends AsyncTask<String,Void,String> {
        protected void onPostExecute(String webData){
            mData = webData;
            Log.v(LOG_TAG,"Data returned was "+ mData);
            if(mData ==null){
                if(mRawURL ==null) {
                    mDownloadStatus = DownloadStatus.NOT_INTIALIZED;
                }else{
                    mDownloadStatus =DownloadStatus.FAILED_OR_EMPTY;
                }
            }else{
                mDownloadStatus =DownloadStatus.OK;
            }
        }

        protected String doInBackground(String... params){
            HttpURLConnection urlConnection =null;
            BufferedReader reader = null;

            if(params == null)
                return null;


            try{
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    return null;
                }
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                //Log.d("GetRawData",buffer.toString());
                return buffer.toString();

            }catch(IOException ioe){
                Log.e(LOG_TAG,"Error",ioe);
            }finally{
                if(urlConnection !=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    }catch(final IOException e){
                        Log.e(LOG_TAG,"error closoing stream",e);
                    }
                }

            }


            return null;
        }
    }
}
