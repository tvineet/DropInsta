package com.inerun.dropinsta.sqldb;

import android.util.Log;

import com.inerun.dropinsta.DropInsta;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sqlcipher.SQLCipherOpenHelper;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;


public class SQLCipherHelperImpl extends SQLCipherOpenHelper {

    private DropInsta context;

    public SQLCipherHelperImpl(DatabaseDefinition databaseDefinition, DatabaseHelperListener listener, DropInsta context) {
        super(databaseDefinition, listener);
        this.context = context;
    }

    @Override
    protected String getCipherSecret() {

        return generateCipherKey();
    }

    private String generateCipherKey() {

        String randomkey = "dbflow-rules";
//        if (context.fcsPref.hasSqlKey()) {
//            randomkey = context.fcsPref.sqlKey();
//        } else {
//            randomkey = randomkey + Math.random();
//            context.fcsPref.saveSqlKey(randomkey);
//        }
        Log.i("CipherKey",""+randomkey);
        return randomkey;
    }


}