package com.cumonywa.restaurant.restaurantorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckconActivity extends AppCompatActivity {
    EditText ipaddress,dbname,username,password;
    Button btnCheck,btnSave;
    String ipAddrString,dbnameString,usernameString,passString;

    public static final String MyPerferences="MyPrefs";
    public static final String UrlKey="urlkey";
    public static final String dbKey="dbkey";
    public static final String userKey="userkey";
    public static final String passwordKey="passwordKey";

    SharedPreferences.Editor editor;
    SharedPreferences mPreference;
    Connection conn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_connection);

        ipaddress=findViewById(R.id.serverip);
        dbname=findViewById(R.id.dbname);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        btnCheck=findViewById(R.id.btn_check);
        btnSave=findViewById(R.id.btn_save);


        mPreference= PreferenceManager.getDefaultSharedPreferences(this);
        editor=mPreference.edit();


        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAddrString=ipaddress.getText().toString();
                dbnameString=dbname.getText().toString();
                usernameString=username.getText().toString();
                passString=password.getText().toString();

                if(ipAddrString.equals("")|| dbnameString.equals("")||usernameString.equals("")||passString.equals("")){
                    Toast.makeText(getApplication(),"Fill Data",Toast.LENGTH_SHORT).show();
                }
                else {
                    CheckConnection connection=new CheckConnection();
                    conn=connection.connectionclass(ipAddrString,dbnameString,usernameString,passString);
                    if(conn==null){
                        Toast.makeText(getApplication(),"Connection Fail",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplication(),"Connection successful",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAddrString=ipaddress.getText().toString();
                dbnameString=dbname.getText().toString();
                usernameString=username.getText().toString();
                passString=password.getText().toString();

                if(ipAddrString.equals("")||dbnameString.equals("")||usernameString.equals("")||passString.equals("")){
                    Toast.makeText(getApplication(),"Fill Data",Toast.LENGTH_SHORT).show();
                }
                else {
                    CheckConnection connection=new CheckConnection();
                    conn=connection.connectionclass(ipAddrString,dbnameString,usernameString,passString);
                    if(conn==null){
                        Toast.makeText(getApplication(),"Connection Fail",Toast.LENGTH_SHORT).show();
                    }else {
                        editor.putString(UrlKey,ipAddrString);
                        editor.putString(dbKey,dbnameString);
                        editor.putString(userKey,usernameString);
                        editor.putString(passwordKey,passString);
                        editor.commit();
                        finish();
                    }
                }
            }
        });
        ipaddress.setText(mPreference.getString("urlkey",""));
        dbname.setText(mPreference.getString("dbkey",""));
        username.setText(mPreference.getString("userkey",""));
        password.setText(mPreference.getString("passwordKey",""));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conout, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch (id){
            case R.id.conout:
                AlertDialog show=showDialog();
                show.show();
        }
        return true;
    }
    private AlertDialog showDialog(){
        AlertDialog dialog=new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("LOG OUT")
                .setMessage("Are you sure you want to Log Out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPreference.edit().clear().commit();
                        ipaddress.setText("");
                        dbname.setText("");
                        username.setText("");
                        password.setText("");

                        }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create();
        return dialog;
    }
}
