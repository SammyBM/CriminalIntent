package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.criminalintent.CrimeDBSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if (sCrimeLab==null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab (Context context){
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        mCrimes = new ArrayList<>();
        /*for (int i = 0; i < 100; i++){
            Crime crime = new Crime();
            crime.setmTitle("Crime # " + i);
            crime.setmSolved(i % 2 == 0);       //make every second crime a solved crime
            crime.setSeriousCrime(i % 3 == 0);  //make every third crime a serious crime
            mCrimes.add(crime);
        }*/
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
        //ContentValues values = getContentValues(c);
        //mDatabase.insert(CrimeTable.NAME, null, values);
    }

    //method made to delete crimes with ease
    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }

    public UUID getNextId(Crime c){
        for (int x = 0; x<mCrimes.size(); x++){
            if (mCrimes.get(x).getmId().equals(c.getmId())){
                return mCrimes.get(x+1).getmId();
            }
        }
        return null;
    }

    /*public void updateCrime(Crime crime){
        String uuidString = crime.getmId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }*/

    List<Crime> getCrimes(){
        //return new ArrayList<>();
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for (Crime crime : mCrimes){
            if (crime.getmId().equals(id)){
                return crime;
            }
        }
        return null;
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getmId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getmTitle());
        values.put(CrimeTable.Cols.DATE, crime.getmDate().toString());
        values.put(CrimeTable.Cols.SOLVED, crime.ismSolved() ? 1 : 0);

        return values;
    }



}
