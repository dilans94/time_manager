package com.timemanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;


import android.os.Bundle;


public class CommonClass extends Activity {
public static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_class);
            ctx=this;
    }

}
