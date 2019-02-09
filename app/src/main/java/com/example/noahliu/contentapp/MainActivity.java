package com.example.noahliu.contentapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static android.Manifest.permission.*;

import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_CONTACTS, WRITE_CONTACTS}, REQUEST_CONTACTS);
        } else {
            readContacts();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CONTACTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("必須允許聯絡人權限才能顯示資料")
                            .setPositiveButton("OK", null)
                            .show();
                }
                return;
        }
    }

    private void readContacts() {
        ContentResolver resolver = getContentResolver();
        String[] projection = {Contacts._ID,
                Contacts.DISPLAY_NAME,
                Phone.NUMBER};
        Cursor cursor = resolver.query(
                Phone.CONTENT_URI,
                projection,
                null,
                null,
                null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(
                    ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME));
            Log.d("RECORD", id + "/" + name);
        }
        ListView list = (ListView) findViewById(R.id.list);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{Contacts.DISPLAY_NAME,Phone.NUMBER},
                new int[]{android.R.id.text1, android.R.id.text2},
                1);
        list.setAdapter(adapter);
    }
}
