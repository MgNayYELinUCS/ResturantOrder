package com.cumonywa.restaurant.restaurantorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cumonywa.restaurant.restaurantorder.adapter.OrderAdapter;
import com.cumonywa.restaurant.restaurantorder.adapter.PreviewAdapter;
import com.cumonywa.restaurant.restaurantorder.model.FoodMenuModel;
import com.cumonywa.restaurant.restaurantorder.model.TableModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    final DatabaseHandler db = new DatabaseHandler(this);
    GridLayoutManager orderGrid;
    OrderAdapter orderAdapter;
    RecyclerView orderview;
    TextView totalPrice;
    Button btnBill;
   // Button btnBill;
    String tablename;

    private String ip,dbname,user,password;
    Connection con;
    CheckConnection checkConnection;
    SharedPreferences.Editor editor;
    SharedPreferences mPreference;
    List<FoodMenuModel> foodMenuModel1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyle_order_activty);
        this.setTitle("Order");
        totalPrice=findViewById(R.id.txttotalPrice);
        //btnBill=findViewById(R.id.btn_bill);

        foodMenuModel1=new ArrayList<FoodMenuModel>();
        mPreference= PreferenceManager.getDefaultSharedPreferences(this);
        editor=mPreference.edit();
        orderview = findViewById(R.id.orderPreview1);
        orderGrid = new GridLayoutManager(OrderActivity.this, 1);
        orderview.setLayoutManager(orderGrid);

        checkConnection=new CheckConnection();
        con=checkConnection.shareclass(this);
        Intent intent =getIntent();
        tablename= (String) intent.getSerializableExtra("tablename");

        if (con == null) {
            Toast.makeText(getApplicationContext(), "Check IP address", Toast.LENGTH_LONG).show();
        } else {

            try {
                String data = "Select ItemName,SUM(ItemCount) AS ItemSum,ItemPrice,WaiterName from OrderTable where TableName=N'"+tablename+"' GROUP BY ItemName,ItemPrice,WaiterName";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(data);
                int totalprice=0;
                while (rs.next()) {
                    FoodMenuModel foodMenuModel = new FoodMenuModel();
                    foodMenuModel.setFood_name(rs.getString("ItemName"));
                    foodMenuModel.setFood_qty(Integer.parseInt(rs.getString("ItemSum")));
                    foodMenuModel.setPrice(Integer.parseInt(rs.getString("ItemPrice")));
                    foodMenuModel.setWaitername(rs.getString("WaiterName"));
                    totalprice+=Integer.parseInt(rs.getString("ItemPrice"))*Integer.parseInt(rs.getString("ItemSum"));

                    foodMenuModel1.add(foodMenuModel);
                }
                rs.close();
                totalPrice.setText(totalprice+" Ks");
            } catch (Exception e) {
                Log.e("error$$$$ : ", e.getMessage());
            }
            orderAdapter = new OrderAdapter(OrderActivity.this, foodMenuModel1);
            orderAdapter.notifyDataSetChanged();
            orderview.setAdapter(orderAdapter);
        }

        /*btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog showD=showDialog();
                showD.show();
            }
        });*/

    }
    /*private AlertDialog showDialog(){
        AlertDialog dialog=new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("Bill")
                .setMessage("Are you sure you want to obill?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (con == null) {
                            Toast.makeText(getApplicationContext(), "Check IP address", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                //delete oder do
                                String ordertable="Select * from ordertable where TableName=N\'"+tablename+"\'";
                                Statement stmt = con.createStatement();
                                ResultSet rs = stmt.executeQuery(ordertable);
                                while (rs.next())
                                {
                                    String itemName=rs.getString("ItemName");
                                    String price=rs.getString("ItemPrice");
                                    String qty=rs.getString("ItemCount");
                                    String tablename1=rs.getString("TableName");
                                    int totalAmount=Integer.parseInt(price)*Integer.parseInt(qty);
                                    String data = "INSERT INTO Bill(ItemName,Price,Quantity,TableName,Amount,TotalAmount) VALUES(N'"+itemName+"','"+price+"','"+qty+"',N'"+tablename1+"','"+totalAmount+"','"+totalAmount+"')";
                                    Statement stmt1 = con.createStatement();
                                    stmt1.executeUpdate(data);
                                }
                                rs.close();

                                String deleteordertable="Delete from ordertable where TableName=N\'"+tablename+"\'";
                                stmt = con.createStatement();
                                stmt.executeUpdate(deleteordertable);
                                con.close();
                                Toast.makeText(getApplicationContext(), "Bill pay", Toast.LENGTH_LONG).show();
                                finish();
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
    }*/
}
