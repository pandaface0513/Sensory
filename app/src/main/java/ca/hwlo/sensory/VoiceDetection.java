package ca.hwlo.sensory;

import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;


public class VoiceDetection extends ActionBarActivity implements View.OnClickListener {

    private MediaRecorder mRecorder = null;

    private TextView sound_txt;
    private Button sound_btn;

    private Handler handler;

    private Runnable updateSound;

    private Boolean soundOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_detection);

        setTitle("Sound Level Detector");

        sound_btn = (Button) findViewById(R.id.sound_btn);
        sound_btn.setText("Start Loop");
        sound_btn.setOnClickListener(this);

        soundOn = false;

        sound_txt = (TextView) findViewById(R.id.soundText);

        handler = new Handler();

        updateSound = new Runnable() {
            @Override
            public void run() {
                String val = Double.toString(getAmplitude());
                if(Double.parseDouble(val) > 100.0){
                    sound_txt.setText("There are noise! " + val);
                }else{
                    sound_txt.setText("No noise! " + val);
                }
                handler.postDelayed(updateSound, 1000); //1 second
            }
        };

        if(mRecorder == null){
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  mRecorder.getMaxAmplitude();
        else
            return 0;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voice_detection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sound_btn) {
            if (!soundOn) {
                soundOn = true;
                sound_btn.setText("Loop Started");

                handler.post(updateSound);
            }
        }
    }
}
