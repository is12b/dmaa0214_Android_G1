package dk.ucn.dmaa0214.group1.meteringapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private TextView textView_lux;
    private TextView textView_angle;
    private static final int LUX_REQUEST_CODE = 1001;
    private static final int ANGLE_REQUEST_CODE = 1002;
    public static final String LUX_VALUE = "LUX_VALUE";

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textView_lux = (TextView) getActivity().findViewById(R.id.main_textview_lux);
        textView_angle = (TextView) getActivity().findViewById(R.id.main_textview_angle);

        Button button_lux = (Button) getActivity().findViewById(R.id.main_button_lux);
        Button button_angle = (Button) getActivity().findViewById(R.id.main_button_angle);

        button_lux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LuxActivity.class);
                startActivityForResult(intent, LUX_REQUEST_CODE);
            }
        });
        button_angle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TiltAngleActivity.class);
                startActivityForResult(intent, ANGLE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LUX_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            String lux = data.getStringExtra(LUX_VALUE);
            textView_lux.setText(String.format(getResources().getString(R.string.main_lux_formatted),
                    lux));
        }
    }
}
