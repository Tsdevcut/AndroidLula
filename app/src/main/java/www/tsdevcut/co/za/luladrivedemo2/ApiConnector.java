package www.tsdevcut.co.za.luladrivedemo2;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by tsmakhaola on 2015-11-02.
 */
public class ApiConnector {


    public JSONArray AcceptTrip(String usernm, String driveUserID, String lati, String longs){

        int dID = Integer.parseInt(driveUserID);

        String url = null;
        try {
            url = "http://www.tsdevcut.co.za/devcon/luDAccept.php?username="
                    + URLEncoder.encode(usernm,"UTF-8")
                    + "&driverID="
                    + URLEncoder.encode(String.valueOf(dID), "UTF-8")
                    + "&langy="
                    + URLEncoder.encode(lati, "UTF-8")
                    + "&longy="
                    + URLEncoder.encode(longs, "UTF-8");
        }
        catch (Exception er){
            // Log.d("Exception", count + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            er.printStackTrace();
            //Log.d("Exception", er.getLocalizedMessage()== null ? "" : er.getLocalizedMessage());
        }
        //count++;
        HttpEntity httpEntity  = null;

        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse  = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
            // count++;
        }
        catch (ClientProtocolException cex)
        {
            cex.printStackTrace();
        }
        catch (IOException ie)
        {

            ie.printStackTrace();
        }


        //Convert Into JSon Array
        JSONArray jsonArray  = null;
        //count++;
        if(httpEntity != null)
            try{
                String entityResponse = EntityUtils.toString(httpEntity);
                //count++;
                // System.out.println("++++++++++++++++++++++++++++++++++++httpEntity++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                //System.out.println("Entity Response :  " + entityResponse);


                jsonArray = new JSONArray(entityResponse);

            }
            catch (JSONException jex)
            {
                // Log.d("JSONException","++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                // Log.d("JSONException", jex.getLocalizedMessage() == null ? "" : jex.getLocalizedMessage());
                jex.printStackTrace();
            }
            catch (IOException iex)
            {
                // Log.d("IOException","++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                // Log.d("IOException", iex.getLocalizedMessage() == null ? "" : iex.getLocalizedMessage());
                iex.printStackTrace();
            }
        return jsonArray;
    }

    public JSONArray AwaitATrip(){

        String url = "http://www.tsdevcut.co.za/devcon/luBegin.php";

        HttpEntity httpEntity  = null;
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse  = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
        }
        catch (ClientProtocolException cex)
        {
            cex.printStackTrace();
        }
        catch (IOException ie)
        {
            ie.printStackTrace();
        }

        //Convert Into JSon Array
        JSONArray jsonArray  = null;

        if(httpEntity != null)
            try{
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response :  ", entityResponse);

                jsonArray = new JSONArray(entityResponse);

            }
            catch (JSONException jex)
            {
                jex.printStackTrace();
            }
            catch (IOException iex)
            {
                iex.printStackTrace();
            }

        return jsonArray;
    }
}
