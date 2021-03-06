package com.example.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    public UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean seriousCrime; //solution to challenge 2

    public boolean isSeriousCrime() {
        return seriousCrime;
    }

    public void setSeriousCrime(boolean seriousCrime) {
        this.seriousCrime = seriousCrime;
    }

    public Crime (){
        mId = UUID.randomUUID();
        mTitle = null;
        mDate = new Date();
        mSolved = false;
    }

    public Crime (UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }
}
