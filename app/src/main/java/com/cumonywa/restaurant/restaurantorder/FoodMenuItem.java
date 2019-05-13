package com.cumonywa.restaurant.restaurantorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cumonywa.restaurant.restaurantorder.adapter.FoodMenuAdapter;
import com.cumonywa.restaurant.restaurantorder.interfaces.CustomButtonListener;
import com.cumonywa.restaurant.restaurantorder.model.FoodCatogoryModel;
import com.cumonywa.restaurant.restaurantorder.model.FoodMenuModel;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FoodMenuItem extends AppCompatActivity implements CustomButtonListener{

    RecyclerView recycle_foodItem;
    List<FoodMenuModel> foodItem=new ArrayList<FoodMenuModel>();
    List<FoodMenuModel> selectedfoodItem=new ArrayList<FoodMenuModel>();

    FoodMenuModel foodmenu;
    Button btnAddItem;
    static int quantity;
    FoodMenuAdapter foodMenuAdapter;

    SharedPreferences.Editor editor;
    SharedPreferences mPreference;
    String conn;
    private String waiter="";
    Connection con;
    CheckConnection checkConnection;
    GridLayoutManager tGrid;
    String category_name;
    String tablename;
    final DatabaseHandler db = new DatabaseHandler(this);
    SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_menu_activity);
        searchView=findViewById(R.id.search_view);
        recycle_foodItem=findViewById(R.id.recycle_foodMenuItem);
        btnAddItem=findViewById(R.id.btnAddItem);

        tGrid=new GridLayoutManager(FoodMenuItem.this,1);
        recycle_foodItem.setLayoutManager(tGrid);

        mPreference= PreferenceManager.getDefaultSharedPreferences(this);
        editor=mPreference.edit();


        Intent intent =getIntent();
        category_name= (String) intent.getSerializableExtra("categoryname");
        tablename=(String) intent.getSerializableExtra("tablename");
        waiter=(String)intent.getSerializableExtra("waitername");

        this.setTitle(category_name);
        searchView.setQueryHint("Type your keyword here");


        final String data="Select itemname,itemprice from items where categoryname=N\'"+category_name+"\'";
        bindData(data);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /* String data11="Delete from OrderTable";
                try {
                    Statement stmt = con.createStatement();
                    stmt.executeQuery(data11);
                }
                catch (Exception e){

                }*/
                for (int i = 0; i < foodMenuAdapter.FoodItem.length; i++) {
                    if (foodMenuAdapter.FoodItem[i] != null && foodMenuAdapter.Count[i] != 0) {
                        foodmenu=new FoodMenuModel( foodMenuAdapter.FoodItem[i], foodMenuAdapter.FoodPrice[i],foodMenuAdapter.Count[i],waiter ,true);
                        selectedfoodItem.add(foodmenu);
                        String foodname1=foodMenuAdapter.FoodItem[i];

                        int count=db.getItemCount(foodname1);
                        if(count>0) {
                            db.foodupdate(foodMenuAdapter.FoodItem[i], foodMenuAdapter.Count[i], foodMenuAdapter.FoodPrice[i], tablename, waiter, foodMenuAdapter.Deslist[i].toString());

                        }
                        else
                           db.addContact(foodMenuAdapter.FoodItem[i],foodMenuAdapter.Count[i],foodMenuAdapter.FoodPrice[i],tablename,waiter,foodMenuAdapter.Deslist[i]);
                    }
                }
                finish();

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String data="";
                if(s.equals("")) {
                    data="Select itemname,itemprice from items where categoryname=N\'"+category_name+"\'";
                }
                else
                    data="Select itemname,itemprice from items where categoryname=N\'"+category_name+"\' and itemname LIKE N'%"+s+"%'";
                bindData(data);
                return false;
            }
        });
    }

    private void bindData(String data) {
        checkConnection=new CheckConnection();
        con=checkConnection.shareclass(this);

        foodItem.clear();

        if (con==null){
            foodItem.clear();
            Toast.makeText(getApplicationContext(),"Check IP address",Toast.LENGTH_LONG).show();
        }
        else {
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(data);
                int counter=1;
                while (rs.next())
                {
                    foodmenu=new FoodMenuModel(rs.getString("itemname"),rs.getInt("itemprice"),2 );
                    foodItem.add(foodmenu);
                    counter++;
                }
            }catch (Exception e){
                Log.e("error$$$$ : ", e.getMessage());
            }

            foodMenuAdapter=new FoodMenuAdapter(FoodMenuItem.this,foodItem,category_name);
            foodMenuAdapter.notifyDataSetChanged();
            recycle_foodItem.setAdapter(foodMenuAdapter);
            foodMenuAdapter.setCustomButtonListener(this);


        }
    }

    @Override
    public void onButtonClickListener(int position, TextView editText, int value) {
        //View view = listView.getChildAt(position);
        quantity = Integer.parseInt(editText.getText().toString());
        quantity = quantity+(1*value);
        if(quantity<0)
            quantity=0;
        editText.setText(quantity+"");
    }
}
