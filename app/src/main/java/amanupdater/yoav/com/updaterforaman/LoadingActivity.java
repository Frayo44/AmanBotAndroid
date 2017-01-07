package amanupdater.yoav.com.updaterforaman;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import amanupdater.yoav.com.updaterforaman.Server.JSONParser;

/**
 * Created by Yoav on 9/29/2015.
 */
public class LoadingActivity extends Activity {

    private AnimatedCircleLoadingView animatedCircleLoadingView;

    private String id, password, misparMoamad;
    private String errorId;
    private String sessionId;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);

        if(!isNetworkAvailable())
        {
            new AlertDialog.Builder(this)
                    .setTitle("No internet connection")
                    .setMessage("It look like your internet connection is off.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {

            progress = ProgressDialog.show(this, "Updater For Aman",
                    "Loading...", true);

            Intent intent = getIntent();
            this.errorId = "-1";
            this.sessionId = null;

            id = intent.getStringExtra("id");
            password = intent.getStringExtra("password");

            if(intent == null || id == null || password == null)
            {
                SharedPreferences prefs = getSharedPreferences(Consts.TAG, MODE_PRIVATE);
                String idPrefs = prefs.getString(Consts.KEY_ID, null);
                String passPrefs = prefs.getString(Consts.KEY_PASS, null);
                if (idPrefs == null && idPrefs == null) {
                    Intent loginIntent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                    finish();
                    return;
                } else {
                    id = idPrefs;
                    password = passPrefs;
                }


            }

            requestLogin();

        }
    }

    private void requestLogin() {
        new RequestPost("/Modiin/AuthenticationService.asmx/Authenticate", id, password).execute();
    }

    private class RequestPost extends AsyncTask<String, String, JSONObject> {

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
        protected void onPostExecute(JSONObject amanUserDataJson) {
            try {
                if(amanUserDataJson == null)
                {
                    onFailure();
                    return;
                }
                JSONObject modiinJsonObject = amanUserDataJson.getJSONObject("d");

                misparMoamad = modiinJsonObject.getString("MisparMoamad");
                if(misparMoamad.length() < 4)
                {
                    onFailure();
                } else {
                    sessionId = amanUserDataJson.getString("SessionId");
                    if(sessionId != null)
                    {
                        onSucess();
                    }
                    else
                    {
                        onFailure();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onFailure()
    {
        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        intent.putExtra("error", errorId);
        startActivity(intent);
        finish();
    }

    private void onSucess()
    {
        SharedPreferences.Editor editor = getSharedPreferences(Consts.TAG, MODE_PRIVATE).edit();
        editor.putString(Consts.KEY_ID, id);
        editor.putString(Consts.KEY_PASS, password);
        editor.apply();
        Intent intent = new Intent(LoadingActivity.this, AmanMainActivity.class);
        intent.putExtra("SessionId", sessionId);
        intent.putExtra("MisparMoamad", misparMoamad);
        startActivity(intent);
        finish();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }
}
