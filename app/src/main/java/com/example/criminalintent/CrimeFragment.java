package com.example.criminalintent;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;



/* Couldn't use location services, used time zone instead */

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private List<Crime> crimeList;
    private static final String ARG_Crime_ID = "crime_id";
    private static final String DIALOG_DATE = "Dialog_Date";
    private static final int REQUEST_DATE = 0;

    Button mDateButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
        //UUID crimeID = (UUID) getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
        UUID crimeID = (UUID) getArguments().getSerializable(ARG_Crime_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
        crimeList = CrimeLab.get(getContext()).getCrimes();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        EditText mTitleField = v.findViewById(R.id.crime_title);
        mDateButton = v.findViewById(R.id.date_button);
        CheckBox mSolvedCheckBox = v.findViewById(R.id.crime_solved);


        //added functions initialization
        Button mFirst = v.findViewById(R.id.begin_pager_btn);
        Button mLast = v.findViewById(R.id.end_pager_btn);
        Button mPolice = v.findViewById(R.id.police_call_btn);
        ImageView mPhoneLogo = v.findViewById(R.id.police_call_logo);

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

        //added functions (goToPosition method is created in CrimePagerActivity.java)
        if (mCrime.getmId().equals(crimeList.get(0).getmId()))
            mFirst.setVisibility(View.GONE);
        else{                                   //If we are not in first position button appears
            mFirst.setVisibility(View.VISIBLE);
            mFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CrimePagerActivity.goToPosition(0);
                }
            });
        }
        if (mCrime.getmId().equals(crimeList.get(crimeList.size()-1).getmId()))
            mLast.setVisibility(View.GONE);
        else{                                   //If we are not in last position button appears
            mLast.setVisibility(View.VISIBLE);
            mLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CrimePagerActivity.goToPosition(crimeList.size()-1);
                }
            });
        }
        if (mCrime.isSeriousCrime()) { //When in a serious crime "Call Police" button appears
            mPolice.setVisibility(View.VISIBLE);
            mPhoneLogo.setVisibility(View.VISIBLE);
            mPolice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "Calling La Chota", Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            mPhoneLogo.setVisibility(View.GONE);
            mPolice.setVisibility(View.GONE);
        }


        //Custom DATE format using SimpleDateFormat

        // mFormat acts as a template, and calling .format() applies it
        SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM, HH:mm, z", Locale.US); //
        String coso = mFormat.format(mCrime.getmDate());

        updateDate(coso);
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog= DatePickerFragment.newInstance(mCrime.getmDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolved(isChecked);
            }
        });

        return v;
    }

    public static CrimeFragment newInstance (UUID crime_id){
        Bundle args = new Bundle();
        args.putSerializable(ARG_Crime_ID, crime_id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateDate(mCrime.getmDate().toString());
        }
    }

    private void updateDate(String s) {
        mDateButton.setText(s);
    }
}
