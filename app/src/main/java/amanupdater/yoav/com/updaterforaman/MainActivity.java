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
    private static final int REQUEST_SIGNUP = 0;

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

      //  if (!validate()) {
          //  onLoginFailed();
          //  return;
       // }

      //  bLoggingButton.setEnabled(false);

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
        // TODO: Implement your own authentication logic here.



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        bLoggingButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        bLoggingButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = etIdText.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etIdText.setError("enter a valid email address");
            valid = false;
        } else {
            etIdText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }
}