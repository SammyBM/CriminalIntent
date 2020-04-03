package com.example.criminalintent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

/* Couldn't use location services, used time zone instead */

public class CrimeFragment extends Fragment {

    private Crime mCrime;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
        UUID crimeID = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        EditText mTitleField = v.findViewById(R.id.crime_title);
        Button mDateButton = v.findViewById(R.id.date_button);
        CheckBox mSolvedCheckBox = v.findViewById(R.id.crime_solved);

        //wiring up
        mTitleField.setText(mCrime.getmTitle());
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mTitleField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Custom DATE format using SimpleDateFormat

        // mFormat acts as a template, and calling .format() applies it
        SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM, HH:mm, z", Locale.US); //
        String coso = mFormat.format(mCrime.getmDate());

        mDateButton.setText(coso);
        mDateButton.setEnabled(false);

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });

        return v;
    }
}
