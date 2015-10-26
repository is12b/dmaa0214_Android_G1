package dk.ucn.dmaa0214.group1.meteringapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TiltAngleActivityFragment extends Fragment {

    private Button btn_save;
    private TextView current_textView;
    private SensorEventListener mEventListener;

    public TiltAngleActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tilt_angle, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);


        btn_save = (Button) getActivity().findViewById(R.id.angle_button_save);
        current_textView = (TextView) getActivity().findViewById(R.id.angle_textview_current);

        mEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = fixValues(event);

                int i = (int) ((182D * Math.atan(values[0] / values[1]) / 3.1415926535897931D));

                int l = i;
                int j = l;

                if (l >= 90) {
                    j = 90;
                }

                if (Math.abs(values[3]) < 0.9F) {
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

        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Angle", current_textView.getText());
                clipboard.setPrimaryClip(clip);

                Intent output = new Intent();
                output.putExtra(MainActivityFragment.ANGLE_VALUE, current_textView.getText());
                getActivity().setResult(getActivity().RESULT_OK, output);
                getActivity().finish();
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
