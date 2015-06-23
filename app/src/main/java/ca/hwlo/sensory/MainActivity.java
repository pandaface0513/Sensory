package ca.hwlo.sensory;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.TotalCaptureResult;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, SensorEventListener, View.OnClickListener {

    private Button voiceBtn;
    private boolean voiceOn = false;
    private MediaRecorder mRecorder = null;
    private Handler handler;

    private SensorManager sensorManager;

    private ArrayList<String> sensorNameList;

    private ListView list;

    private static int REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

//        if(mRecorder == null){
//            mRecorder = new MediaRecorder();
//            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            mRecorder.setOutputFile("/dev/null");
//            try {
//                mRecorder.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mRecorder.start();
//        }

        voiceBtn = (Button) findViewById(R.id.voiceBtn);
        voiceBtn.setOnClickListener(this);

        //get reference to sensor and attach a listener
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        sensorNameList = new ArrayList<String>();

        for(int i=0; i<sensorList.size(); i++){
            sensorNameList.add(sensorList.get(i).getName());
        }

        list = (ListView) findViewById(R.id.listView);
        ArrayAdapter <String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sensorNameList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
//        if(mRecorder == null){
//            mRecorder = new MediaRecorder();
//            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            mRecorder.setOutputFile("/dev/null");
//            try {
//                mRecorder.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mRecorder.start();
//        }

    }

    @Override
    protected void onPause(){
//        if (mRecorder != null) {
//            mRecorder.stop();
//            mRecorder.release();
//            mRecorder = null;
//        }
        super.onPause();
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView clickedTextView = (TextView) view;
        Toast.makeText(this, "row" + i + ": " + clickedTextView.getText(), Toast.LENGTH_SHORT).show();

        //start Intent
        Intent intent = new Intent(this, SensorActivity.class);
        intent.putExtra("sensor_type", clickedTextView.getText());

        //request with request code
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.voiceBtn){
//            //toggle voice on
//            if(voiceOn){
//                voiceOn = false;
//                mRecorder.stop();
//            }else{
//                voiceOn = true;
//                mRecorder.start();
//                Runnable detectSound = new Runnable() {
//                    @Override
//                    public void run() {
//                        Double volume = getAmplitude();
//                        Toast.makeText(MainActivity.this, Double.toString(volume), Toast.LENGTH_SHORT).show();
//                    }
//                };
//
//                handler.postDelayed(detectSound, 1000);
//            }
        }
    }
}
