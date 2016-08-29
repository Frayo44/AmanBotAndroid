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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import amanupdater.yoav.com.updaterforaman.Server.AmanServer;

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
        TextView tvCourses = (TextView) findViewById(R.id.tvAmanCourses);
        TextView tvLastUpdate = (TextView) findViewById(R.id.tvLastUpdate);



        String html = AmanServer.requestData(sessionID, misparMoamad);

        Document doc = Jsoup.parse(html);
        // For maslolim
        Element element = doc.getElementById("ctl00_ctl00_cphMain_CPHMainContent_divSection1");
        Elements coursesNames = element.getElementsByClass("CourseName");
        Elements coursesStatus = element.getElementsByClass("CourseStatus");
        Element lastUpdate = doc.getElementById("ctl00_ctl00_cphMain_CPHMainContent_lblLastUpdate");

        for(int i = 0; i < coursesNames.size(); i++)
        {
            tvCourses.setText(tvCourses.getText() + "\n" + "CourseName: " + coursesNames.get(i).text() +
                    "CourseStatus: " + coursesStatus.get(i).text() );
        }

        tvLastUpdate.setText("Last Update: " + lastUpdate.text() + '\n');

      //  Toast.makeText(this, "Recieved: " + html, Toast.LENGTH_SHORT).show();


    }


}
