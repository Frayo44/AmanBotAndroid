package amanupdater.yoav.com.updaterforaman.Server;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
            Log.d(Consts.TAG, "Error in getNearPokemonsByCordinates function" + e.getMessage());
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

    private static class RequestGet extends AsyncTask<String, String, JSONObject> {

        String request;
        String sessionId, uid;

        public RequestGet(String request, String sessionId, String uid) {
            this.request = request;
            this.sessionId = sessionId;
            this.uid = uid;
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            String myUrl = Consts.REQUEST_PREFIX + "" + request;
            try {
                URL url = new URL(myUrl);
                URLConnection urlConnection = url.openConnection();
                InputStream in = urlConnection.getInputStream();

                String result = IOUtils.toString(in, StandardCharsets.UTF_8);

                try {
                    JSONObject obj = new JSONObject(result);
                    Log.d(Consts.TAG, obj.toString());
                    return obj;
                } catch (Throwable t) {
                    Log.e(Consts.TAG, "Could not parse malformed JSON: \"" + json + "\"");
                }
            } catch (ClientProtocolException e) {
                Log.d(Consts.TAG , e.toString() + myUrl);
            } catch (IOException e) {
                Log.d(Consts.TAG , e.toString() + myUrl);
            }

            return null;

        }



        @Override
        protected void onPostExecute(JSONObject json) {


        }
    }

}
