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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.widget.Toast.makeText;

/* Couldn't use location services, used time zone instead */

public class CrimeFragment extends Fragment {

    private Crime mCrime;
    private List<Crime> crimeList;
    private ViewPager crimePager;
    private static final String ARG_Crime_ID = "crime_id";



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
        Button mDateButton = v.findViewById(R.id.date_button);
        CheckBox mSolvedCheckBox = v.findViewById(R.id.crime_solved);

        crimePager = v.findViewById(R.id.crime_view_pager);
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
        if (mCrime.getmId().equals(crimeList.get(0).getmId()))
            mFirst.setVisibility(View.GONE);
        else{
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
        else{
            mLast.setVisibility(View.VISIBLE);
            mLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CrimePagerActivity.goToPosition(crimeList.size()-1);
                }
            });
        }
        if (mCrime.isSeriousCrime()) {
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

    public static CrimeFragment newInstance (UUID crime_id){
        Bundle args = new Bundle();
        args.putSerializable(ARG_Crime_ID, crime_id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }



}
