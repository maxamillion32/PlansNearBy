package com.arroyo.b.plansnearby.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.arroyo.b.plansnearby.Activities.MainActivity;
import com.arroyo.b.plansnearby.R;

/**
 * Created by nolbertoarroyo on 8/30/16.
 * dialog allows user to insert city
 */
public class EnterCityDialogFragment extends DialogFragment {
    public EnterCityDialogFragment() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), STYLE_NORMAL);
        builder.setTitle(R.string.dialog_enter_city);
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        builder.setPositiveButton(R.string.city_dialog_submit_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String value = input.getText().toString();
                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.onUserSelectValue(value);

            }
        })
                .setNegativeButton(R.string.city_dialog_cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EnterCityDialogFragment.this.getDialog().cancel();
                    }
                });
        AlertDialog cityDialog = builder.create();
        cityDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return cityDialog;
    }
}
