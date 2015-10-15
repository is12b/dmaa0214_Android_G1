package dk.ucn.dmaa0214.group1.meteringapp;

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
public class LuxActivityFragment extends Fragment {
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

    public LuxActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView_current = (TextView) getActivity().findViewById(R.id.lux_textview_current);
        textView_min = (TextView) getActivity(). findViewById(R.id.lux_textview_min);
        textView_max = (TextView) getActivity(). findViewById(R.id.lux_textview_max);

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            sensorManager.registerListener(
                    LIGHTSENSOR_LISTENER,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            textView_current.setText(R.string.lux_lightsensor_not_available);
        }

        Button button_reset = (Button) getActivity().findViewById(R.id.lux_button_reset);
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_max.setText("0");
                textView_min.setText("0");
            }
        });
        Button button_save = (Button) getActivity().findViewById(R.id.lux_button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();

                output.putExtra(MainActivityFragment.LUX_VALUE, textView_max.getText());
                getActivity().setResult(getActivity().RESULT_OK, output);
                getActivity().finish();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lux, container, false);
    }
}
