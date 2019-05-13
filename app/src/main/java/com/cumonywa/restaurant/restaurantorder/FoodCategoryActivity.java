package com.cumonywa.restaurant.restaurantorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cumonywa.restaurant.restaurantorder.adapter.FoodCategoryAdapter;
import com.cumonywa.restaurant.restaurantorder.model.FoodCatogoryModel;
import com.cumonywa.restaurant.restaurantorder.model.FoodMenuModel;
import com.cumonywa.restaurant.restaurantorder.model.TableModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class FoodCategoryActivity extends AppCompatActivity {

    RecyclerView recycleFoodCategoryView;
    GridLayoutManager tGrid;
    FoodCatogoryModel foodCategory;
    private String ip,dbname,user,password;

    List<FoodCatogoryModel> foodCategoryList=new ArrayList<FoodCatogoryModel>();


    String tableName;
    String waiter="";
    CardView cardInfo;
    FoodCategoryAdapter foodAdapter;


    SharedPreferences.Editor editor;
    SharedPreferences mPreference;
    String conn;
    Connection con;
    CheckConnection checkConnection;
    SwipeRefreshLayout swipeRefreshLayout;
    String name;
    final DatabaseHandler db = new DatabaseHandler(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_cotegory_activity);



        recycleFoodCategoryView=findViewById(R.id.foodCategoryRecucle);
        tGrid=new GridLayoutManager(FoodCategoryActivity.this,1);
        recycleFoodCategoryView.setLayoutManager(tGrid);



        mPreference= PreferenceManager.getDefaultSharedPreferences(this);
        editor=mPreference.edit();

        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                bindData();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        bindData();
    }

    private void bindData() {
        checkConnection=new CheckConnection();
        con=checkConnection.shareclass(this);

        foodCategoryList.clear();

        Intent intent =getIntent();
        name= (String) intent.getSerializableExtra("Name");
        waiter=(String) intent.getSerializableExtra("Waitername1");
        String existwaitername="";
        if (con==null){
            Toast.makeText(getApplicationContext(),"Check IP address",Toast.LENGTH_LONG).show();
        }
        else {
            try {
                String data = "Select * from OrderTable where TableName=N'" + name + "'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(data);
                while (rs.next()) {
                    existwaitername = rs.getString("WaiterName");
                }
            } catch (Exception e) {
                Log.e("error$$$$ : ", e.getMessage());
            }
        }
        this.setTitle(name+"  "+existwaitername);
        if (con==null){
            foodCategoryList.clear();
            Toast.makeText(getApplicationContext(),"Check IP address",Toast.LENGTH_LONG).show();
        }
        else {
            foodCategoryList.clear();
            try{
                String data="Select DISTINCT categoryname from items";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(data);
                int counter=1;
                while (rs.next())
                {
                    String category_name=rs.getString("categoryname");
                    foodCategory=new FoodCatogoryModel(counter,category_name ,name,waiter);
                    foodCategoryList.add(foodCategory);

                    counter++;
                    //Log.i("TableName" ,rs.getString("tablename"));
                }

                con.close();
            }catch (Exception e){
                Log.e("error$$$$ : ", e.getMessage());
            }

            foodAdapter=new FoodCategoryAdapter(FoodCategoryActivity.this,foodCategoryList,name);
            foodAdapter.notifyDataSetChanged();
            recycleFoodCategoryView.setAdapter(foodAdapter);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch (id){
            case R.id.preview:
                //List<FoodMenuModel> foodMenuModel1=db.getAllContacts(name);
                Intent intent=new Intent(getApplicationContext(),PreviewActivity.class);
                intent.putExtra("tablename",name);
                startActivity(intent);
                break;
            case R.id.order:
                Intent order=new Intent(getApplicationContext(),OrderActivity.class);
                order.putExtra("tablename",name);
                startActivity(order);
        }
        return true;
    }
}
