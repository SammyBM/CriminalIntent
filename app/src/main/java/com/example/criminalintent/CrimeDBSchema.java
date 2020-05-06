package com.example.criminalintent;

public class CrimeDBSchema {
    public static final class CrimeTable{
        public static final String NAME = "crimes";

        public static final class Cols<x> {
            public static String UUID= "uuid";
            public static String TITLE = "title";
            public static String DATE = "date";
            public static String SOLVED = "solved";
        }

    }
}
