package amanupdater.yoav.com.updaterforaman;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.ConsoleHandler;

import amanupdater.yoav.com.updaterforaman.Server.AmanServer;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    EditText etIdText;
    EditText etPassword;
    Button bLoggingButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etIdText = (EditText) findViewById(R.id.etId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLoggingButton = (Button) findViewById(R.id.btn_login);

        bLoggingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {

            return;
       }

        String email = etIdText.getText().toString();
        String password = etPassword.getText().toString();

        JSONObject jj = AmanServer.login(email, password);

        try {

            JSONObject modiinJsonObject = jj.getJSONObject("d");
            String isConnectrd = modiinJsonObject.getString("MisparMoamad");
            if(isConnectrd.equals("-1") || isConnectrd.equals("-2"))
            {
                Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Yay! Connected", Toast.LENGTH_SHORT).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public boolean validate() {
        boolean valid = true;

        String email = etIdText.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty()) {
            etIdText.setError("enter a valid id");
            valid = false;
        } else {
            etIdText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            etPassword.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }
}