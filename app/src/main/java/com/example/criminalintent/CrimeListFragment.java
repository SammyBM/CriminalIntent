package com.example.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private Crime mCrime;
    private TextView mEmptySet;

    private ImageView mSolved; //solved logo
    private ImageView mSeriousImg; //serious logo

    private boolean mSubtitleVisible;
    private static final String SAVED_STUBTITLE_VISIBLE = "subtitle";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crime_list_fragment, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mEmptySet = view.findViewById(R.id.empty_set);
        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_STUBTITLE_VISIBLE);


        updateUI();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_STUBTITLE_VISIBLE, mSubtitleVisible);
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter==null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else{
            mAdapter.notifyDataSetChanged();
        }
        if (crimes.size() == 0){
            mEmptySet.setVisibility(View.VISIBLE);

        }
        else{
            mEmptySet.setVisibility(View.GONE);}
        updateSubtitle();

    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;




        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(), mCrime.getmTitle() + " clicked", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getActivity(), CrimeActivity.class);
            //Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getmId());
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getmId());
            startActivity(intent);
        }

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super (inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);
            //wiring up
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolved = itemView.findViewById(R.id.solved_image);
            mSeriousImg = itemView.findViewById(R.id.serious_image);



        }

        public void Bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(crime.getmTitle());
            mDateTextView.setText(crime.getmDate().toString());

            //condition for solved crime
            if (crime.ismSolved())
                mSolved.setVisibility(View.VISIBLE);
            else
                mSolved.setVisibility(View.GONE);

            //condition for serious crimes
            if (crime.isSeriousCrime())
                mSeriousImg.setVisibility(View.VISIBLE);
            else
                mSeriousImg.setVisibility(View.GONE);

            //

        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.Bind(crime);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    @Override
    public void onPause() {
        super.onPause();
        //CrimeLab.get(getActivity()).updateCrime(mCrime);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible)
            subtitleItem.setTitle(R.string.hide_subtitle);
        else
            subtitleItem.setTitle(R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getmId());
                startActivity(intent);

            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void updateSubtitle(){
        CrimeLab crimeLab =  CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);
        if (!mSubtitleVisible)
            subtitle = null;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

}
