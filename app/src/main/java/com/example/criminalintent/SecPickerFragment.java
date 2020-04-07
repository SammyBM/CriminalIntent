package com.example.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Date;

public class SecPickerFragment extends DialogFragment {

    private final static String ARG_SEC = "sec";
    private NumberPicker mSecPicker;
    private static final String EXTRA_SEC = "extraSec";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Date date = (Date) getArguments().getSerializable(ARG_SEC);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.seconds_dialog, null);

        //wiring up
        mSecPicker = v.findViewById(R.id.dialog_sec_picker);

        //Establish Picker Range
        mSecPicker.setMaxValue(59); //Maximum 59 secs
        mSecPicker.setMinValue(0);  //Minimum 0 secs

        return new AlertDialog.Builder(getActivity()).setView(v).setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //setting seconds
                                date.setSeconds(mSecPicker.getValue());

                                sendResult(Activity.RESULT_OK, date);
                            }
                        }
                )
                .create();
    }




    static SecPickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_SEC, date);
        SecPickerFragment fragment = new SecPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult (int resultCode, Date date){
        if (getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SEC, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }



}
