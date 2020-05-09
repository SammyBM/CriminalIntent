package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.criminalintent.CrimeDBSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
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
        /*mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            Crime crime = new Crime();
            crime.setmTitle("Crime # " + i);
            crime.setmSolved(i % 2 == 0);       //make every second crime a solved crime
            crime.setSeriousCrime(i % 3 == 0);  //make every third crime a serious crime
            mCrimes.add(crime);
        }*/
    }

    public void addCrime(Crime c){
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    //method made to delete crimes with ease
    public void deleteCrime(Crime c){
        ContentValues values = getContentValues(c);
        mDatabase.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = " +c.getmId(), new String[] {c.getmId().toString()});
    }

    public UUID getNextId(Crime c){
        CrimeCursorWrapper cursor = (CrimeCursorWrapper) queryCrimes(CrimeTable.Cols.UUID + " < " + c.getmId(), new String[]{c.getmId().toString()});

        try {
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getCrime().getmId();
        }
        finally {
            cursor.close();
        }
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getmId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public CursorWrapper queryCrimes(String whereClause, String whereArgs[]){
       try {
           Cursor cursor = mDatabase.query(
                   CrimeTable.NAME,
                   null, // column - null selects all columns
                   whereClause,
                   whereArgs,
                   null, //Group by
                   null, // Having
                   null //order by
           );
           return new CrimeCursorWrapper(cursor);
       }
       catch (Exception impossibleQuery){
           Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + CrimeTable.NAME + " WHERE " + whereClause, whereArgs);
           Toast toast = Toast.makeText(mContext, "impossible query", Toast.LENGTH_SHORT);
           toast.show();
           return (CursorWrapper) cursor;
       }
    }

    public   List<Crime> getCrimes(){
        //return new ArrayList<>();
        //return mCrimes;
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = (CrimeCursorWrapper) queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id){
        /*for (Crime crime : mCrimes){
            if (crime.getmId().equals(id)){
                return crime;
            }
        }*/
        CrimeCursorWrapper cursor = (CrimeCursorWrapper) queryCrimes(CrimeTable.Cols.UUID + " = " + id,
                new String[]{id.toString()});

        try {
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally {
            cursor.close();
        }
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
