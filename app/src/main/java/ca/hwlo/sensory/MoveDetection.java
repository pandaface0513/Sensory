package ca.hwlo.sensory;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MoveDetection extends ActionBarActivity implements View.OnClickListener, SensorEventListener{

    private Boolean moveOn;
    private TextView moveText;
    private Button moveBtn;

    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_detection);

        moveText = (TextView) findViewById(R.id.moveText);

        moveBtn = (Button) findViewById(R.id.moveBTN);
        moveBtn.setOnClickListener(this);

        moveOn = false;

        //set up sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //get reference to the respective sensor
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_move_detection, menu);
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
        if(v.getId() == R.id.moveBTN){
            if(moveOn){
                moveOn = false;
            }else{
                moveOn = true;
                moveText.setText("Loop Off");
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            if(moveOn){
                float x, y, z;
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                float diff = (float) Math.sqrt(x * x + y * y + z * z);
                Log.d("MOVE", Float.toString(diff));
                if(diff > 1){
                    moveText.setText("Device is moving!!!");
                }else{
                    moveText.setText("Device is still.");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
