package amanupdater.yoav.com.updaterforaman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Yoav on 8/28/2016.
 */
public class AmanMainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aman_main);

        Intent intent = getIntent();
        String sessionID = intent.getStringExtra("SessionId");
        String misparMoamad = intent.getStringExtra("MisparMoamad");
        Toast.makeText(this, "SessionId: " + sessionID, Toast.LENGTH_SHORT).show();
        TextView tvStatus = (TextView) findViewById(R.id.tvAmanStatus);
        tvStatus.setText("Connected! Congrats! Hello: " + misparMoamad);
    }


}
