package com.cumonywa.restaurant.restaurantorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.cumonywa.restaurant.restaurantorder.adapter.PreviewAdapter;
import com.cumonywa.restaurant.restaurantorder.model.FoodMenuModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckBox chk_select_all;
    private Button btn_delete_all;
    String tablename;
    Button btnOrder;
    final DatabaseHandler db = new DatabaseHandler(this);

    private String ip,dbname,user,password;
    Connection con;
    CheckConnection checkConnection;
    SharedPreferences.Editor editor;
    SharedPreferences mPreference;

    private List<FoodMenuModel> item_list = new ArrayList<FoodMenuModel>();
    private PreviewAdapter previewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_preview_activity);
        this.setTitle("Preview");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chk_select_all = (CheckBox) findViewById(R.id.chk_select_all);
        btn_delete_all = (Button) findViewById(R.id.btn_delete_all);
        btnOrder=findViewById(R.id.btnOrder);

        mPreference= PreferenceManager.getDefaultSharedPreferences(this);
        editor=mPreference.edit();

        Intent intent =getIntent();
        tablename= (String) intent.getSerializableExtra("tablename");

        item_list= db.getAllContacts(tablename);


        previewAdapter = new PreviewAdapter(PreviewActivity.this,item_list);
        previewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(previewAdapter);

        checkConnection=new CheckConnection();
        /*ip=mPreference.getString("urlkey","");
        dbname=mPreference.getString("dbkey","");
        user=mPreference.getString("userkey","");
        password=mPreference.getString("passwordkey","");

        con=checkConnection.connectionclass(ip.toString(),dbname,user,password);*/
        con=checkConnection.shareclass(this);

        chk_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chk_select_all.isChecked()) {

                    for (FoodMenuModel model : item_list) {
                        model.setSelected(true);
                    }
                } else {

                    for (FoodMenuModel model : item_list) {
                        model.setSelected(false);
                    }
                }

                previewAdapter.notifyDataSetChanged();
            }
        });

        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chk_select_all.isChecked()) {

                    AlertDialog show=showdeleteAll();
                    show.show();
                } else {
                    Snackbar.make(v, "Please click on select all check box, to delete all items.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (con == null) {
                    Toast.makeText(getApplicationContext(), "Check IP address", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        List<FoodMenuModel> foodMenuModel1=db.getAllContacts(tablename);
                        for (int i = 0; i < foodMenuModel1.size(); i++) {

                            String linecode="";
                            String kitchenname="";
                            String categoryname="";

                            try {
                                String data="select CategoryName,LineCode,KitchenName from Items where itemname=N'"+foodMenuModel1.get(i).getFood_name()+"'";
                                Statement stmt = con.createStatement();
                                ResultSet rs = stmt.executeQuery(data);
                                while (rs.next()) {
                                    linecode=rs.getString("LineCode");
                                    kitchenname=rs.getString("KitchenName");
                                    categoryname=rs.getString("CategoryName");
                                }

                                //con.close();
                            }catch (Exception e){

                            }
                            int amount=foodMenuModel1.get(i).getFood_qty()*foodMenuModel1.get(i).getPrice();

                            int no=0;
                            String query="select COUNT(*) AS NO from OrderTable where TableName='"+tablename+"'";
                            Statement stmt1 = con.createStatement();
                            ResultSet rs = stmt1.executeQuery(query);
                            while (rs.next()) {
                                no=rs.getInt("NO");
                            }
                            no++;
                            Log.i("no",no+"");
                            String data = "INSERT INTO OrderTable(ItemName,ItemCount,TableName,WaiterName,LineCode,Amount,ItemPrice,ShouldPrint,Categories,Description,KitchenName,No,Finish) VALUES(N'"+foodMenuModel1.get(i).getFood_name()+
                                    "',N'"+foodMenuModel1.get(i).getFood_qty()+ "',N'" + tablename + "',N'"+foodMenuModel1.get(i).getWaitername()+ "',N'"+linecode+ "',N'"+amount+"',N'"+foodMenuModel1.get(i).getPrice()+"',0,N'"+categoryname+"',N'"+foodMenuModel1.get(i).getDescription()+"',N'"+kitchenname+"',N'"+no+"',0)";
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(data);
                            Log.i("des",foodMenuModel1.get(i).getDescription());
                            //Log.i("Select Database #####  ", foodMenuModel1.get(i).getFood_name() + "   " + foodMenuModel1.get(i).getFood_qty() + "  " + foodMenuModel1.get(i).getPrice());
                        }

                        db.deleteAllContact(tablename);
                        finish();
                        con.close();
                    } catch (Exception e) {
                        Log.e("error$$$$ : ", e.getMessage());
                    }
                }
            }
        });
    }
   /* private AlertDialog showDialog(){
        AlertDialog dialog=new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("Order")
                .setMessage("Are you sure you want to order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (con == null) {
                            Toast.makeText(getApplicationContext(), "Check IP address", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                 List<FoodMenuModel> foodMenuModel1=db.getAllContacts(tablename);
                                for (int i = 0; i < foodMenuModel1.size(); i++) {

                                    String linecode="";
                                    String kitchenname="";
                                    String categoryname="";

                                    try {
                                        String data="select CategoryName,LineCode,KitchenName from Items where itemname=N'"+foodMenuModel1.get(i).getFood_name()+"'";
                                        Statement stmt = con.createStatement();
                                        ResultSet rs = stmt.executeQuery(data);
                                        while (rs.next()) {
                                            linecode=rs.getString("LineCode");
                                            kitchenname=rs.getString("KitchenName");
                                            categoryname=rs.getString("CategoryName");
                                        }

                                        //con.close();
                                    }catch (Exception e){

                                    }
                                    int amount=foodMenuModel1.get(i).getFood_qty()*foodMenuModel1.get(i).getPrice();

                                    int no=0;
                                    String query="select * from OrderTable where TableName='"+tablename+"'";
                                    Statement stmt1 = con.createStatement();
                                    ResultSet rs = stmt1.executeQuery(query);
                                    while (rs.next()) {
                                        no=rs.getInt("No");
                                    }
                                    no++;
                                    String data = "INSERT INTO OrderTable(ItemName,ItemCount,TableName,WaiterName,LineCode,Amount,ItemPrice,ShouldPrint,Categories,Description,KitchenName,No,Finish) VALUES(N'"+foodMenuModel1.get(i).getFood_name()+
                                            "',N'"+foodMenuModel1.get(i).getFood_qty()+ "',N'" + tablename + "',N'"+foodMenuModel1.get(i).getWaitername()+ "',N'"+linecode+ "',N'"+amount+"',N'"+foodMenuModel1.get(i).getPrice()+"',0,N'"+categoryname+"',N'"+foodMenuModel1.get(i).getDescription()+"',N'"+kitchenname+"',N'"+no+"',0)";
                                    Statement stmt = con.createStatement();
                                    stmt.executeUpdate(data);
                                    Log.i("des",foodMenuModel1.get(i).getDescription());
                                    //Log.i("Select Database #####  ", foodMenuModel1.get(i).getFood_name() + "   " + foodMenuModel1.get(i).getFood_qty() + "  " + foodMenuModel1.get(i).getPrice());
                                }

                                db.deleteAllContact(tablename);
                                finish();
                                con.close();
                            } catch (Exception e) {
                                Log.e("error$$$$ : ", e.getMessage());
                            }

                        }

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
*/
    private AlertDialog showdeleteAll(){
        AlertDialog dialog=new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("AddOrder")
                .setMessage("Are you sure you want to delete all order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        item_list.clear();

                        db.deleteAllContact(tablename);
                        previewAdapter.notifyDataSetChanged();
                        chk_select_all.setChecked(false);
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

