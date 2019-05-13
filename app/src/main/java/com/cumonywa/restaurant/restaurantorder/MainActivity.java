package com.cumonywa.restaurant.restaurantorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.sql.SQLException;

import com.cumonywa.restaurant.restaurantorder.adapter.TableAdapter;
import com.cumonywa.restaurant.restaurantorder.model.TableModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {
    RecyclerView recycleTableView;
    List<TableModel> tableListView;
    TableModel tableData;

    public String waitername;
    private String wname;
    String conn;

    Menu item;
    MenuItem nav_con;
    NavigationView navigationView;
    SharedPreferences.Editor editor;
    SharedPreferences mPreference;
    private String ip,dbname,user,password;
    Connection con;
    CheckConnection checkConnection;
    GridLayoutManager tGrid;
    TableAdapter myAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    List<String> tablefromlist = new ArrayList<String>();
    List<String> tabletolist = new ArrayList<String>();
    String tablefromstring="";
    String tabletostring="";
    Spinner tablefrom;
    Spinner tableto;
    private TextView ipadd;

    String fromtb="",totb="";
    ArrayAdapter<String> adapterfrom,adapterto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreference= PreferenceManager.getDefaultSharedPreferences(this);
        editor=mPreference.edit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


         navigationView = (NavigationView) findViewById(R.id.nav_view);
         View headerview=navigationView.getHeaderView(0);
         ipadd=headerview.findViewById(R.id.ipadd);
        ip=mPreference.getString("urlkey","");
        if (ip.equals(""))
        {
            ipadd.setText("Connection String");
        }
        else {
            ipadd.setText(ip.toString());
        }
        item= (Menu) navigationView.getMenu();
         nav_con=item.findItem(R.id.nav_connection);

        navigationView.setNavigationItemSelectedListener(this);

        recycleTableView=findViewById(R.id.recycle_tableList);
        tGrid = new GridLayoutManager(MainActivity.this, 3);
        recycleTableView.setLayoutManager(tGrid);

        Intent intent =getIntent();
        waitername= (String) intent.getSerializableExtra("waitername");
        wname=mPreference.getString("waiter","");

        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tableListView.clear();
                if(wname.equals("")) {
                    Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG).show();
                }else {
                    bindData();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if(wname.equals("")){
            this.setTitle("Waiter Name");
            Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_LONG).show();

        }else {
            bindData();
            this.setTitle(wname);
            Thread t=new Thread(){
                @Override
                public void run() {
                    super.run();
                    try{
                        while (!isInterrupted()){
                            Thread.sleep(5000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    bindData();
                                }
                            });
                        }
                    }catch (InterruptedException e){

                    }
                }
            };t.start();
        }
    }


    private void bindData() {
        checkConnection=new CheckConnection();
        String ipaddress="";
            con=checkConnection.shareclass(this);
            Log.i("connection$$",con+"");
            tableListView = new ArrayList<TableModel>();
            tableListView.clear();

            if (con==null){
                tableListView.clear();
                Toast.makeText(getApplicationContext(),"Check IP address",Toast.LENGTH_LONG).show();

            }
            else {
                try{
                    String data="Select * from TBNames";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(data);
                    while (rs.next())
                    {
                        String tablename=rs.getString("Name");
                        String ordertable="Select * from ordertable where TableName=N\'"+tablename+"\'";
                        Statement st = con.createStatement();
                        ResultSet r= st.executeQuery(ordertable);

                        if (r.next()){
                            tableData = new TableModel(tablename, R.mipmap.nofreetable1,wname);
                            tableListView.add(tableData);
                        }else {
                            tableData = new TableModel(tablename, R.mipmap.freetable1,wname);
                            tableListView.add(tableData);
                        }
                        //Log.i("TableName" ,rs.getString("name"));
                    }

                    //con.close();
                }catch (Exception e){
                    Log.e("error$$$$ : ", e.getMessage());
                }
                recycleTableView.setLayoutManager(tGrid);
                myAdapter = new TableAdapter(MainActivity.this, tableListView,wname);
                myAdapter.notifyDataSetChanged();
                recycleTableView.setAdapter(myAdapter);
            }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            ipadd.setText("Connection String");
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_connection) {
            Intent intent=new Intent(MainActivity.this,CheckconActivity.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_login) {
            Intent login=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(login);

        }
        else if (id == R.id.nav_changetable) {

            showchangetable();
    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showchangetable() {

        LayoutInflater layoutInflater=LayoutInflater.from(this);
        View view=layoutInflater.inflate(R.layout.change_table_activity,null);
        final AlertDialog.Builder alBuilder=new AlertDialog.Builder(this);
        alBuilder.setView(view);
        tablefrom=view.findViewById(R.id.spinner_from);
        tableto=view.findViewById(R.id.spinner_to);

        selectTable();

        alBuilder.setCancelable(false).setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               String edfromtablename =fromtb.toString();
               String edtotablename =totb.toString();
                    try{
                        Log.i("tablefromname",edfromtablename+edtotablename);
                        String query="Update OrderTable set TableName='"+edtotablename+"' where " +
                                "TableName=N'"+edfromtablename+"'";
                        Statement stmt = con.createStatement();
                        stmt.executeQuery(query);
                       con.close();
                    }catch (Exception e){
                        Log.e("error$$$$ : ", e.getMessage());
                    }
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog=alBuilder.create();
        alertDialog.show();

    }

    public void selectTable(){
        if (con == null) {
            Toast.makeText(getApplicationContext(), "Check IP address", Toast.LENGTH_LONG).show();
        } else {

            try {
                String query1 = "Select DISTINCT TableName from OrderTable";
                Statement stmt1 = con.createStatement();
                ResultSet rs1 = stmt1.executeQuery(query1);
                tablefromlist.clear();
                while (rs1.next()) {
                    tablefromstring = rs1.getString("TableName");
                    tablefromlist.add(tablefromstring);
                    Log.d("waiter name",tablefromstring);

                }
                adapterfrom = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tablefromlist);
                adapterfrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tablefrom.setAdapter(adapterfrom);

                tablefrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        fromtb = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                String query2 = "Select Name from TBNames";
                Statement stmt2 = con.createStatement();
                ResultSet rs2 = stmt2.executeQuery(query2);
                tabletolist.clear();
                while (rs2.next()) {
                    tabletostring = rs2.getString("Name");
                    tabletolist.add(tabletostring);
                    Log.d("waiter name",tablefromstring);
                }
                adapterto = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tabletolist);
                adapterto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tableto.setAdapter(adapterto);

                tableto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        totb = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            } catch (Exception e) {
                Log.e("error$$$$ : ", e.getMessage());
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
