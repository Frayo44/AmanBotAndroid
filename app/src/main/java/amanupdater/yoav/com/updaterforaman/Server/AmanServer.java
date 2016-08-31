package amanupdater.yoav.com.updaterforaman.Server;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amanupdater.yoav.com.updaterforaman.Consts;

/**
 * Created by Yoav on 8/28/2016.
 */
public class AmanServer {

    public static JSONObject login(String id, String password) {

        JSONObject jsonObject = null;

        try {
            jsonObject = new RequestPost("/Modiin/AuthenticationService.asmx/Authenticate", id, password).execute().get();
            return jsonObject;
        } catch (Exception e) {
            Log.d(Consts.TAG, "Error in login function" + e.getMessage());
            return null;
        }
    }

    public static String requestData(String sessinId, String uid) {

        String result = null;

        try {
            result = new RequestGet("/modiin/Kiosk.aspx?catId=59951", sessinId, uid).execute().get();
            return result;
        } catch (Exception e) {
            Log.d(Consts.TAG, "Error in requestData function" + e.getMessage());
            return null;
        }
    }

    private static class RequestPost extends AsyncTask<String, String, JSONObject> {

        String id;
        String password;
        String request;

        public RequestPost(String request, String id, String password) {
            this.request = request;
            this.id = id;
            this.password = password;
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

            Map<String, String> map = new HashMap<String, String>();

            map.put("misparMoamad", id);
            map.put("userPass", password);

            System.out.println(Consts.REQUEST_PREFIX + "" + request);
            JSONObject jObj = json.getJSONFromUrl(Consts.REQUEST_PREFIX + "" + request, map);

            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject json) {


        }
    }

    private static class RequestGet extends AsyncTask<String, String, String> {

        String request;
        String sessionId, uid;

        public RequestGet(String request, String sessionId, String uid) {
            this.request = request;
            this.sessionId = sessionId;
            this.uid = uid;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... args) {
            String myUrl = Consts.REQUEST_PREFIX + "" + request;
            try {
                URL url = new URL(myUrl);
                URLConnection urlConnection = url.openConnection();
                urlConnection.setRequestProperty("Cookie", "ASPSESSIONIDSGRSSDRS=LBIJJFCDJIBEHCCJDDKBHMAI;" +  sessionId + "ASPSESSIONIDSESTSCRS=CBDPGLMDOJHPHGKPIOEOPINL;" + "ASPSESSIONIDSERRTASS=HHBPNGHAOCNEHPKGOBEJLNHL; _pk_ref.185.ccce=%5B%22%22%2C%22%22%2C1472290412%2C%22https%3A%2F%2Fwww.google.co.il%2F%22%5D; arp_scroll_position=66.66666666666667; _pk_id.185.ccce=a65407994cbf27ce.1465548881.95.1472291036.1472290412.; _pk_ses.185.ccce=*; uid=" + uid + "");
                InputStream in = urlConnection.getInputStream();


                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, "windows-1255"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String result = sb.toString();

                 return result;

            } catch (ClientProtocolException e) {
                Log.d(Consts.TAG , e.toString() + myUrl);
            } catch (IOException e) {
                Log.d(Consts.TAG , e.toString() + myUrl);
            }

            return null;

        }

    }

}
