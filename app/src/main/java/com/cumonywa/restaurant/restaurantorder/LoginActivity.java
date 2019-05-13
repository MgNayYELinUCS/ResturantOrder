package com.cumonywa.restaurant.restaurantorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cumonywa.restaurant.restaurantorder.adapter.FoodCategoryAdapter;
import com.cumonywa.restaurant.restaurantorder.model.FoodCatogoryModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner sp_waiterlist;
    EditText ed_password;
    Button btnCancel, btnOk;
    TextView tx;
    String name;
    Context context;

    ProgressBar progressBar;

    SharedPreferences.Editor editor;
    SharedPreferences mPreference;
    public static final String WaiterName="waiter";

    String conn;
    private String ip,dbname,user,password;
    Connection con;
    CheckConnection checkConnection;
    String waitername;

    TextView tvMynamar;
    Typeface typeface;
    List<String> waiterlist = new ArrayList<String>();
    final DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        context = this;
        this.setTitle("Login");
        //tvMynamar=findViewById(R.id.tvmyanmar);
        //typeface=Typeface.createFromAsset(getAssets(),"abc.ttf");
       // tvMynamar.setTypeface(typeface);

        sp_waiterlist = findViewById(R.id.sp_waiter_list);
        ed_password = findViewById(R.id.ed_password);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);


        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mPreference.edit();


        bindData();

        sp_waiterlist.setOnItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void bindData() {
        checkConnection = new CheckConnection();
        /*ip=mPreference.getString("urlkey","");
        dbname=mPreference.getString("dbkey","");
        user=mPreference.getString("userkey","");
        password=mPreference.getString("passwordkey","");

        con=checkConnection.connectionclass(ip.toString(),dbname,user,password);*/
        con=checkConnection.shareclass(this);


        if (con == null) {

            Toast.makeText(getApplicationContext(), "Check IP address", Toast.LENGTH_LONG).show();
        } else {

            try {
                String data = "Select * from accounts";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(data);

                while (rs.next()) {
                     waitername = rs.getString("username");
                     waiterlist.add(waitername);
                     Log.d("waiter name",waitername);

                }


            } catch (Exception e) {
                Log.e("error$$$$ : ", e.getMessage());
            }



            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waiterlist);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_waiterlist.setAdapter(adapter);


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        waitername = parent.getItemAtPosition(i).toString();
        Log.d("waiter name", waitername);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(LoginActivity.this , "Login Successfull" , Toast.LENGTH_LONG).show();
                //finish();
            }
        }
        @Override
        protected String doInBackground(String... params)
        {
            String waiter = waitername;
            String passwordd = ed_password.getText().toString();
            if(waiter.trim().equals("")|| passwordd.trim().equals(""))
                z = "Please enter Username and Password";
            else
            {
                try
                {
                    if (con == null)
                    {
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        Log.i("name and password =>",waiter+passwordd);
                        // Change below query according to your own database.
                        String query = "Select * from Accounts where UserName=N'" + waiter.toString() + "'and Password='"+ passwordd.toString() +"'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            z = "Login successful";
                            isSuccess=true;

                            Intent i=new Intent(LoginActivity.this,MainActivity.class);
                            i.putExtra("waitername",waitername);

                            String wname=waiter.toString();
                            editor.putString(WaiterName,wname);
                            editor.apply();

                            startActivity(i);
                            finish();

                            con.close();
                        }
                        else
                        {
                            z = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
    }

}