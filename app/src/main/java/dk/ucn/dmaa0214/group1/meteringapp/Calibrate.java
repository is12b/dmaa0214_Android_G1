package dk.ucn.dmaa0214.group1.meteringapp;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Calibrate extends AppCompatActivity {

    SensorEventListener sensorEventListener;
    SensorManager sensorManager;
    int angle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);

        final AlertDialog calibrateAlert = (new AlertDialog.Builder(this)).create();
        calibrateAlert.setMessage("The Device was calibrated!");
        calibrateAlert.setButton(-2, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TiltAngleActivity.calibrateAngle = angle;
                finish();
            }
        });

        Button btnCalibrate = (Button) findViewById(R.id.btnCalibrate);

        btnCalibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calibrateAlert.show();
            }
        });

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float f = event.values[0] / 9.8F;
                float f1 = event.values[1] / 9.8F;
                float f2 = event.values[2] / 9.8F;

                if (f != 0.0F);
                f = f1;
                if (f1 == 0.0F) {
                    f = 0.01F;
                }

                f1 = f2;
                if (f2 == 0.0F) {
                    f1 = 0.01F;
                }

                int i = (int)(double)(int)((180D * Math.atan(f / f1)) / 3.1415926535897931D);
                angle = i;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(1), 3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calibrate, menu);
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
    protected void onDestroy() {
        sensorManager.unregisterListener(sensorEventListener);
        super.onDestroy();
    }
}
