package com.arroyo.nolberto.placeswithfriends.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.arroyo.nolberto.placeswithfriends.Activities.MainActivity;
import com.arroyo.nolberto.placeswithfriends.R;

/**
 * Created by nolbertoarroyo on 8/30/16.
 */
public class EnterCityDialogFragment extends DialogFragment {
    private EditText enterCity;
    private Button submitButton;

    public EnterCityDialogFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_enter_city,container);
        getDialog().setTitle("enter city");
        enterCity = (EditText)root.findViewById(R.id.fragment_enter_name);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        submitButton = (Button)root.findViewById(R.id.fragment_submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = enterCity.getText().toString();
                Log.d("Quantity: ", value);
                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.onUserSelectValue(value);
                dismiss();
            }
        });
        return root;
    }
}
