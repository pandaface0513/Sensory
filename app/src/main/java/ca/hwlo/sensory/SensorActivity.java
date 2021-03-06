package ca.hwlo.sensory;

import android.content.Context;
import android.os.Vibrator;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SensorActivity extends ActionBarActivity implements SensorEventListener{

    private String sensor_name;
    private int sensor_type;
    private SensorManager sensorManager;
    private Sensor sensor;

    private TextView data_txt, info_txt;

    private Uri notif;
    private Ringtone r;
    private Vibrator v;
    private int vibrationCycle, maxVibeCycle;

    private boolean isLight, isFlat, isNoisy, isMoving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //set up sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //grab intent data
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        vibrationCycle = 0;
        maxVibeCycle = 4;

        notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        r = RingtoneManager.getRingtone(getApplicationContext(), notif);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //set up textview
        data_txt = (TextView) findViewById(R.id.sensorData);
        info_txt = (TextView) findViewById(R.id.sensorInfo);

        if(b != null){
            String title = (String) b.get("sensor_type");
            setTitle(title);
            sensor_name = title;

            Log.d("LOL", sensor_name);

            if(sensor_name.contains("Accelerometer")){
                sensor_type = Sensor.TYPE_ACCELEROMETER;
            }else if(sensor_name.contains("Gyro")){
                sensor_type = Sensor.TYPE_GYROSCOPE;
            }else if(sensor_name.contains("Raw Gyro")){
                sensor_type = Sensor.TYPE_GYROSCOPE_UNCALIBRATED;
            }else if(sensor_name.contains("Magnetic")){
                sensor_type =Sensor.TYPE_MAGNETIC_FIELD;
            }else if(sensor_name.contains("Raw Magnetic")){
                sensor_type =Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED;
            }else if(sensor_name.contains("Light")){
                sensor_type = Sensor.TYPE_LIGHT;
                isLight = true;
            }else if (sensor_name.contains("Game")) {
                sensor_type = Sensor.TYPE_GAME_ROTATION_VECTOR;
            }else if (sensor_name.contains("Linear")) {
                sensor_type = Sensor.TYPE_LINEAR_ACCELERATION;
            }else if (sensor_name.contains("Gravity")) {
                sensor_type = Sensor.TYPE_GRAVITY;
            }else if(sensor_name.contains("Proximity")){
                sensor_type = Sensor.TYPE_PROXIMITY;
            }else if(sensor_name.contains("Orientation")){
                sensor_type =Sensor.TYPE_ORIENTATION;
            }else if(sensor_name.contains("Rotation")){
                sensor_type =Sensor.TYPE_ROTATION_VECTOR;
            }else{
                sensor_type = 999;
            }

            Log.d("LOL", Integer.toString(sensor_type));

            //get reference to the respective sensor
            sensor = sensorManager.getDefaultSensor(sensor_type);

            if(sensor_type != 999) {
                info_txt.setText("Name: " + sensor.getName() + '\n'
                        + "Vendor: " + sensor.getVendor() + '\n'
                        + "Version: " + sensor.getVersion() + '\n'
                        + "Resolution: " + sensor.getResolution() + '\n'
                        + "Max Range: " + sensor.getMaximumRange() + '\n'
                        + "Min Delay: " + sensor.getMinDelay() + '\n'
                        + "Max Delay: " + sensor.getMaxDelay() + '\n');
            }

        }

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
        getMenuInflater().inflate(R.menu.menu_sensor, menu);
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

    private void checkIfFlat(float[] vals){
        double normOfVals = Math.sqrt(vals[0] * vals[0] + vals[1] * vals[1] + vals[2] * vals[2]);

        //normalize the accelerometer vector
        vals[2] = vals[2] / (float) normOfVals;

        int inclination = (int) Math.round(Math.toDegrees(Math.acos(vals[2])));

        if(inclination < 25 || inclination > 155){
            //device is flat
            if(vibrationCycle < maxVibeCycle) {
                v.vibrate(1000);
                vibrationCycle++;
            }
            Log.d("FLAT", "FLAT");
        }else{
            //device is not flat
            Log.d("FLAT", "NOT FLAT");
            v.cancel();
            vibrationCycle = 0;
        }

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        float[] vals = sensorEvent.values;

        switch(type) {
            case 999:
                info_txt.setText("This sensor is not supported! Sorry!");
                break;
            case Sensor.TYPE_GYROSCOPE:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2]);
                break;
            case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2]);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2]);
                //check for the tilt
                checkIfFlat(vals);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2]);
                break;
            case Sensor.TYPE_ORIENTATION:
                data_txt.setText("Azimuth: " + vals[0] + "\n" + " Pitch: " + vals[1] + "\n" + " Roll: " + vals[2]);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2] + "Accuracy: " + vals[3] + " radians");
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2] + "Accuracy: " + vals[3] + " radians");
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2]);
                break;
            case Sensor.TYPE_GRAVITY:
                data_txt.setText("X: " + vals[0] + "\n" + " Y: " + vals[1] + "\n" + " Z: " + vals[2]);
                break;
            case Sensor.TYPE_LIGHT:
                data_txt.setText("Light level: " + vals[0] + " lux");
                //check if light sensor covered
                if(vals[0] <= 0){
                    //beeps!
                    try{
                        if(r.isPlaying() == false) {
                            r.play();
                        }
                        Log.d("LOL", "should be beeping");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
