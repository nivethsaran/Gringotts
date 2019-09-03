package com.cseandroid.gringott.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.cseandroid.gringott.R;
import com.cseandroid.gringott.activities.MainActivity;
import com.cseandroid.gringott.activities.NewEntryActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewEntryFragment extends Fragment {
    EditText ed1, ed2, ed3, ed4, ed5;
    Button save_changes_button, add_entry_button, edit_entry_button;
    CheckBox b4;
    String activityType;
    SQLiteDatabase db;

    public NewEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_entry, container, false);
        Intent typeIntent = getActivity().getIntent();
        activityType = typeIntent.getStringExtra("type");
//        Toast.makeText(getContext(),typeIntent.getStringExtra("entry"),Toast.LENGTH_SHORT).show();
        db = getActivity().openOrCreateDatabase
                ("PasswordDB", Context.MODE_PRIVATE, null);
        db.execSQL
                ("CREATE TABLE IF NOT EXISTS " +
                        "password(entryname VARCHAR PRIMARY KEY," +
                        "websiteurl VARCHAR,username VARCHAR,password VARCHAR,note VARCHAR,lastmodified VARCHAR);");


        ed1 = view.findViewById(R.id.ed1);
        ed2 = view.findViewById(R.id.ed2);
        ed3 = view.findViewById(R.id.ed3);
        ed4 = view.findViewById(R.id.ed4);
        ed5 = view.findViewById(R.id.ed5);

        save_changes_button = view.findViewById(R.id.save_changes_button);
        add_entry_button = view.findViewById(R.id.add_entry_button);
        edit_entry_button = view.findViewById(R.id.edit_entry_button);
        b4 = view.findViewById(R.id.button4);


        b4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed4.setTransformationMethod(null);
                } else {
                    ed4.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        save_changes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().equals("") || ed3.getText().toString().equals("") || ed4.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter mandatory fields", Toast.LENGTH_SHORT).show();
                } else {
                    String entrynamet = ed1.getText().toString();
                    String websiteurlt = ed2.getText().toString();
                    String usernamet = ed3.getText().toString();
                    String passwordt = ed4.getText().toString();
                    String notet = ed5.getText().toString();
                    String timet = Long.toString(System.currentTimeMillis());

                    try {
                        db.execSQL("update password set values('" + entrynamet + "','" + websiteurlt + "','" + usernamet + "','" + passwordt + "','" + notet + "','" + timet + "')");
                        ed1.setText("");
                        ed2.setText("");
                        ed3.setText("");
                        ed4.setText("");
                        ed5.setText("");
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Duplicate Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        add_entry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed1.getText().toString().equals("") || ed3.getText().toString().equals("") || ed4.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Enter mandatory fields", Toast.LENGTH_SHORT).show();
                } else {
                    String entrynamet = ed1.getText().toString();
                    String websiteurlt = ed2.getText().toString();
                    String usernamet = ed3.getText().toString();
                    String passwordt = ed4.getText().toString();
                    String notet = ed5.getText().toString();
                    String timet = Long.toString(System.currentTimeMillis());

                    try {
                        db.execSQL("INSERT INTO password values('" + entrynamet + "','" + websiteurlt + "','" + usernamet + "','" + passwordt + "','" + notet + "','" + timet + "')");
                        ed1.setText("");
                        ed2.setText("");
                        ed3.setText("");
                        ed4.setText("");
                        ed5.setText("");
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Duplicate Data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        edit_entry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed1.setEnabled(true);
                ed2.setEnabled(true);
                ed4.setEnabled(true);
                save_changes_button.setVisibility(View.VISIBLE);
                edit_entry_button.setVisibility(View.INVISIBLE);
                add_entry_button.setVisibility(View.INVISIBLE);
            }
        });
        if (activityType.equals("add")) {
            ed1.setText("");
            ed2.setText("");
            ed3.setText("");
            ed4.setText("");
            ed5.setText("");
            add_entry_button.setVisibility(View.VISIBLE);
            save_changes_button.setVisibility(View.INVISIBLE);
            edit_entry_button.setVisibility(View.INVISIBLE);

        } else if (activityType.equals("edit")) {

            edit_entry_button.setVisibility(View.INVISIBLE);
            save_changes_button.setVisibility(View.VISIBLE);
            add_entry_button.setVisibility(View.INVISIBLE);
            Cursor c = db.rawQuery("SELECT * FROM password where entryname='" + typeIntent.getStringExtra("entry") + "'", null);
            if (c.moveToFirst()) {
                String entrynamet = c.getString(0);
                String websiteurlt = c.getString(1);
                String usernamet = c.getString(2);
                String passwordt = c.getString(3);
                String notet = c.getString(4);
                ed1.setText(entrynamet);
                ed2.setText(websiteurlt);
                ed3.setText(usernamet);
                ed4.setText(passwordt);
                ed5.setText(notet);
            } else {
                ed1.setText("Unavailable");
            }
        } else if (activityType.equals("view")) {
            Cursor c = db.rawQuery("SELECT * FROM password where entryname='" + typeIntent.getStringExtra("entry") + "'", null);
            if (c.moveToFirst()) {
                String entrynamet = c.getString(0);
                String websiteurlt = c.getString(1);
                String usernamet = c.getString(2);
                String passwordt = c.getString(3);
                String notet = c.getString(4);
                ed1.setText(entrynamet);
                ed2.setText(websiteurlt);
                ed3.setText(usernamet);
                ed4.setText(passwordt);
                ed5.setText(notet);
            } else {
                ed1.setText("Unavailable");
            }
            ed1.setEnabled(false);
            ed2.setEnabled(false);
            ed3.setEnabled(false);
            ed4.setEnabled(false);
            ed5.setEnabled(false);
            edit_entry_button.setVisibility(View.VISIBLE);
            add_entry_button.setVisibility(View.INVISIBLE);
            save_changes_button.setVisibility(View.INVISIBLE);
        }
        return view;

    }


}
