package dk.ucn.dmaa0214.group1.meteringapp;

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

    final float[] mValuesMagnet = new float[3];
    final float[] mValuesAccel = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix = new float[9];
    private Button btn_calibrate;
    private Button btn_save;
    private TextView current_textView;
    private SensorEventListener mEventListener;
    private float FlatAngle;


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
        btn_calibrate = (Button) findViewById(R.id.angle_button_calibrate);
        current_textView = (TextView) findViewById(R.id.angle_textview_current);

        mEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER :
                        System.arraycopy(event.values, 0, mValuesAccel, 0 , 3);
                        break;
                    case Sensor.TYPE_MAGNETIC_FIELD :
                        System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
                        break;
                }

                methodForEvent(event);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        setListners(sensorManager, mEventListener);

        btn_calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlatAngle = playerAngle;
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = getSecondTry();//getFirstTry();

                current_textView.setText(str);
            }
        });

    }

    static final float ALPHA = 0.2f;
    //http://stackoverflow.com/questions/4611599/help-smoothing-data-from-a-sensor
    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    private String getSecondTry() {
        int a = Math.round(FlatAngle);
        int b = Math.round(playerAngle);

        int r = (a - b) % 360;

        float angle = playerAngle-FlatAngle;

        return "flat: " + FlatAngle + "\nplayerAngle: " + playerAngle + "\nAngle: " +angle +
                "\nr: " + r;
    }


    private float[] mGravity;
    private float[] mGeomagnetic;
    private float playerAngle;

    private float[] orientation;

    private void methodForEvent(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = lowPass(event.values, mGravity);
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = lowPass(event.values, mGeomagnetic);
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float[] orientationNew = new float[3];
                SensorManager.getOrientation(R, orientationNew);
                orientation = lowPass(orientationNew, orientation);
                playerAngle = (float) Math.toDegrees(Math.atan2(orientation[1], orientation[0]));

            }
        }
    }

    private void setListners(SensorManager sensorManager, SensorEventListener mEventListener) {
        sensorManager.registerListener(mEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(mEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
    }




    public String getFirstTry() {
        SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
        SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);
        String str = "result: " + mValuesOrientation[0] + " " +
                mValuesOrientation[1] + " " +
                mValuesOrientation[2];

        double xAxisData = mValuesOrientation[0];
        double yAxisData = mValuesOrientation[1];
        double zAxisData = mValuesOrientation[2];
        //--Convert raw 0.0 ~ 1.0 sensor datad to degrees.
        double xAngle = Math.atan(xAxisData / (Math.sqrt((yAxisData * yAxisData)) + (zAxisData * zAxisData)));
        double yAngle = Math.atan(yAxisData / (Math.sqrt((xAxisData * xAxisData) +  (zAxisData * zAxisData))));
        double zAngle = Math.atan(Math.sqrt((xAxisData * xAxisData) + (yAxisData * yAxisData)) / zAxisData);
        xAngle = xAngle * 180.00;
        yAngle = yAngle * 180.00;
        zAngle = zAngle * 180.00;
        xAngle = xAngle / 3.141592;
        yAngle = yAngle / 3.141592;
        zAngle = zAngle / 3.141592;

        str = "result: " + xAngle + " " +
                yAngle + " " +
                zAngle;


        return str;
    }
}
