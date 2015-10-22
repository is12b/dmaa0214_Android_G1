package dk.ucn.dmaa0214.group1.meteringapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TiltAngleActivity extends AppCompatActivity {

   // private Button btn_calibrate;
    private Button btn_save;
    private TextView current_textView;
    private SensorEventListener mEventListener;
    public static int calibrateAngle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tilt_angle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        btn_save = (Button) findViewById(R.id.angle_button_save);
        //btn_calibrate = (Button) findViewById(R.id.angle_button_calibrate);
        current_textView = (TextView) findViewById(R.id.angle_textview_current);

        mEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = fixValues(event);

                int i = (int)(double)(int)((180D * Math.atan(values[0] / values[1]) / 3.1415926535897931D));

                int l = i - calibrateAngle;
                int j = l;

                if(l > 90){
                    j = 90;
                }

                if(Math.abs(values[3]) < 0.9F){
                    current_textView.setText(Integer.toString(Math.abs(j)));
                    return;
                } else {
                    current_textView.setText("---");
                    return;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(1) , 3);
/*
        btn_calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TiltAngleActivity.this, Calibrate.class);
                startActivity(intent);
            }
        });
*/
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Angle", current_textView.getText());
                clipboard.setPrimaryClip(clip);

                Intent output = new Intent();
                output.putExtra(MainActivityFragment.ANGLE_VALUE, current_textView.getText());
                setResult(RESULT_OK, output);
                finish();
            }
        });

    }

    private float[] fixValues(SensorEvent event){
        float[] ret = new float[4];

        float x = event.values[0] / 9.8F;
        float y = event.values[1] / 9.8F;
        float z = event.values[2] / 9.8F;
        float f = x;

        if (x == 0.0F){
            f = 0.01F;
        }

        x = y;

        if (y == 0.0F){
            x = 0.01F;
        }

        y = z;

        if (z == 0.0F){
            y = 0.01F;
        }

        ret[0] = x;
        ret[1] = y;
        ret[2] = z;
        ret[3] = f;

        return ret;
    }

}
