package amanupdater.yoav.com.updaterforaman;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import amanupdater.yoav.com.updaterforaman.Server.AmanServer;

/**
 * Created by Yoav on 9/29/2015.
 */
public class LoadingActivity extends Activity {

    private AnimatedCircleLoadingView animatedCircleLoadingView;

    private String id, password, misparMoamad;
    private String errorId;
    private String sessionId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);

        Intent intent = getIntent();
        this.errorId = "-1";
        this.sessionId = null;

         id = intent.getStringExtra("id");
         password = intent.getStringExtra("password");


        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        startLoading();
        startPercentMockThread();
        animatedCircleLoadingView.setAnimationListener(new AnimatedCircleLoadingView.AnimationListener() {
            @Override
            public void onAnimationEnd(boolean success) {
                if(!success) {
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    intent.putExtra("error", errorId);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LoadingActivity.this, AmanMainActivity.class);
                    intent.putExtra("SessionId", sessionId);
                    intent.putExtra("MisparMoamad", misparMoamad);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject jj = AmanServer.login(id, password);

                   // changePercent(100);
                    try {

                        if(jj == null)
                            animatedCircleLoadingView.stopFailure();

                        JSONObject modiinJsonObject = jj.getJSONObject("d");

                        misparMoamad = modiinJsonObject.getString("MisparMoamad");
                        if(misparMoamad.length() < 4)
                        {
                            errorId = misparMoamad;
                            animatedCircleLoadingView.stopFailure();
                        } else {
                            sessionId = jj.getString("SessionId");
                            if(sessionId != null)
                                animatedCircleLoadingView.stopOk();
                            else
                                animatedCircleLoadingView.stopFailure();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                 //   startActivity(intent);
                  //  overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
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
