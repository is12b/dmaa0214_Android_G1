package dk.ucn.dmaa0214.group1.meteringapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView_current;
    TextView textView_min;
    TextView textView_max;
    private final SensorEventListener LIGHTSENSOR_LISTENER = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
                InsertValue(event.values[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void InsertValue(float readedValue) {
        int value = Math.round(readedValue);

        int min = Integer.parseInt(textView_min.getText().toString());
        int max = Integer.parseInt(textView_max.getText().toString());

        textView_current.setText(Integer.toString(value));

        if (value < min || min == 0) {
            textView_min.setText(Integer.toString(value));
        }
        if (value > max) {
            textView_max.setText(Integer.toString(value));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_current = (TextView) findViewById(R.id.lux_textview_current);
        textView_min = (TextView) findViewById(R.id.lux_textview_min);
        textView_max = (TextView) findViewById(R.id.lux_textview_max);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            sensorManager.registerListener(
                    LIGHTSENSOR_LISTENER,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            textView_current.setText(R.string.lux_lightsensor_not_available);
        }
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
}
