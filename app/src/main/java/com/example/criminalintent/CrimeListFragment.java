package com.example.criminalintent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private ImageView mSolved; //solved logo
    private ImageView mSeriousImg; //serious logo

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crime_list_fragment, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;



        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mCrime.getmTitle() + " clicked", Toast.LENGTH_SHORT).show();
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


}
