package com.example.projetoteste.projetoteste;

import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseDatabase mDatabase;

    public FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;

    }
}
