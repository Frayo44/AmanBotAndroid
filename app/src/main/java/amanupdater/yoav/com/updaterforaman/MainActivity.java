package amanupdater.yoav.com.updaterforaman;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;

import android.os.SystemClock;
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

import java.util.Calendar;
import java.util.logging.ConsoleHandler;

import amanupdater.yoav.com.updaterforaman.Server.AmanServer;


public class MainActivity extends AppCompatActivity {

    EditText etIdText;
    EditText etPassword;
    Button bLoggingButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Intent intent = getIntent();

        etIdText = (EditText) findViewById(R.id.etId);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLoggingButton = (Button) findViewById(R.id.btn_login);

        if(intent != null)
        {
            String err = intent.getStringExtra("error");
            if(err != null)
            if(err.equals("-1"))
            {
                etIdText.setError("Incorrect id, try another one");
            } else {
                etPassword.setError("Incorrect password, try another one");
            }
        }


        bLoggingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {

        if (!validate()) {

            return;
        }

        String id = etIdText.getText().toString();
        String password = etPassword.getText().toString();

        Intent intent = new Intent(MainActivity.this, LoadingActivity.class);
        intent.putExtra("password", password);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();


    }

    @Override
    public void onBackPressed() {
        finish();
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