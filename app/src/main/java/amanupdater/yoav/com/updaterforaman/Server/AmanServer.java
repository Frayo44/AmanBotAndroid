package amanupdater.yoav.com.updaterforaman.Server;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

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
            jsonObject = new Request("/Modiin/AuthenticationService.asmx/Authenticate", id, password).execute().get();
            return jsonObject;
        } catch (Exception e) {
            Log.d(Consts.TAG, "Error in getNearPokemonsByCordinates function" + e.getMessage());
            return null;
        }
    }

    private static class Request extends AsyncTask<String, String, JSONObject> {

        String id;
        String password;
        String request;

        public Request(String request, String id, String password) {
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
            map.put("userPass", id);

            System.out.println(Consts.REQUEST_PREFIX + "" + request);
            JSONObject jObj = json.getJSONFromUrl(Consts.REQUEST_PREFIX + "" + request, map);

            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject json) {


        }
    }

}
