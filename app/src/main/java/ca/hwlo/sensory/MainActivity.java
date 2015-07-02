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

    private SensorManager sensorManager;

    private ArrayList<String> sensorNameList;

    private ListView list;

    private static int REQUEST_CODE = 5;

    private Button snd, mov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        snd = (Button) findViewById(R.id.soundBTN);
        snd.setOnClickListener(this);
        mov = (Button) findViewById(R.id.moveBTN);
        mov.setOnClickListener(this);
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
    public void onClick(View v) {
        if(v.getId() == R.id.soundBTN){
            //start Intent
            Intent intent = new Intent(this, VoiceDetection.class);

            //request with request code
            startActivity(intent);
        }
        if(v.getId() == R.id.moveBTN){
            //start Intent
            Intent intent = new Intent(this, MoveDetection.class);

            //request with request code
            startActivity(intent);
        }
    }
}
