package com.cumonywa.restaurant.restaurantorder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cumonywa.restaurant.restaurantorder.CheckConnection;
import com.cumonywa.restaurant.restaurantorder.FoodCategoryActivity;
import com.cumonywa.restaurant.restaurantorder.FoodMenuItem;
import com.cumonywa.restaurant.restaurantorder.R;
import com.cumonywa.restaurant.restaurantorder.interfaces.CustomButtonListener;
import com.cumonywa.restaurant.restaurantorder.model.FoodMenuModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class FoodMenuAdapter extends RecyclerView.Adapter<FoodData>  {

    CustomButtonListener customButtonListener;
    Context context;
    public ArrayList<Integer> quantity = new ArrayList<Integer>();
    List<FoodMenuModel> foodDataList=new ArrayList<FoodMenuModel>();
    public static int Count[];
    public static String FoodItem[];
    public static int FoodPrice[];
    public static String Deslist[];
    CheckConnection checkConnection;
    Connection con;
    String categoryname;
    List<String> descriptionlist=new ArrayList<String>();
    String des="";
    ArrayAdapter<String> adapterfrom;

    public FoodMenuAdapter(FoodMenuItem foodMenuItem, List foodItem,String categoryname){
        this.context=foodMenuItem;
        this.foodDataList=foodItem;
        this.categoryname=categoryname;
        for (int i = 0; i < foodItem.size(); i++) {
            quantity.add(0);

        }
        FoodItem=new String[foodItem.size()];
        FoodPrice=new int[foodItem.size()];
        Count=new int[foodItem.size()];
        Deslist=new String[foodItem.size()];
    }

    public void setCustomButtonListener(CustomButtonListener customButtonListner)
    {
        this.customButtonListener = customButtonListner;
    }

    @Override
    public FoodData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_foodnemu,parent,false);

        return new FoodData(view);
    }

    @Override
    public void onBindViewHolder(final FoodData holder, final int position) {
        holder.txtFoodName.setText(foodDataList.get(position).getFood_name());
        holder.txtPrice.setText(Integer.toString(foodDataList.get(position).getPrice())+" Ks");

        checkConnection=new CheckConnection();
        con=checkConnection.shareclass(context);

        descriptionlist.clear();

        if (con==null){
            descriptionlist.clear();
            Toast.makeText(context,"Check IP address",Toast.LENGTH_LONG).show();
        }
        else {
            try {
                descriptionlist.clear();
                String data = "Select Description from Descriptions where Category=N\'" + categoryname + "\'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(data);
                descriptionlist.add("");
                while (rs.next())
                {
                    descriptionlist.add(rs.getString("Description"));
                }

                con.close();
            } catch (Exception e) {
                Log.e("error$$$$ : ", e.getMessage());
            }
            adapterfrom = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, descriptionlist);
            adapterfrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spinner_des.setAdapter(adapterfrom);

        }

        holder.spinner_des.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                des=adapterView.getItemAtPosition(i).toString();
                Deslist[position]=des.toString();

                /*Log.i("count",Count[position]+"");
                if(Count[position]>0) {
                    des=adapterView.getItemAtPosition(i).toString();
                    Deslist[position] = des.toString();
                    Log.i("count",Count[position]+des);
                }*/

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //Toast.makeText(context,des,Toast.LENGTH_LONG).show();
        try{

            holder.txtQty.setText(quantity.get(position)+"");

        }catch(Exception e){
            e.printStackTrace();
        }
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Count[position] = Count[position] + 1;
                if (customButtonListener != null) {
                    customButtonListener.onButtonClickListener(position, holder.txtQty, 1);
                    quantity.set(position,((quantity.get(position))+1));
                }
                /*Log.i("Dess",holder.spinner_des.getItemAtPosition(position).toString());

                if (!Deslist[position].equals(""))
                    Deslist[position]=des.toString();
                else
                    Deslist[position]=holder.spinner_des.getItemAtPosition(position).toString();
                */
                FoodItem[position]=foodDataList.get(position).getFood_name();
                FoodPrice[position]=foodDataList.get(position).getPrice();

            }
        });

        holder.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Count[position]>0){
                    Count[position] = Count[position] - 1;
                }

                if (customButtonListener != null) {
                    customButtonListener.onButtonClickListener(position,holder.txtQty, -1);
                    if(quantity.get(position)>0)
                        quantity.set(position, (quantity.get(position))-1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodDataList.size();
    }
}

class FoodData extends RecyclerView.ViewHolder{
    TextView txtFoodName;
    TextView txtQty;
    TextView txtPrice;
    Button btnAdd,btnSub;
    Spinner spinner_des;

    public FoodData(View itemView) {
        super(itemView);
        txtFoodName=itemView.findViewById(R.id.txtFoodItem);
        txtQty=itemView.findViewById(R.id.txtCount);
        txtPrice=itemView.findViewById(R.id.txtFoodItemPrice);
        btnAdd=itemView.findViewById(R.id.btnAdd);
        btnSub=itemView.findViewById(R.id.btnSub);
        spinner_des=itemView.findViewById(R.id.spinner_des);
    }
}
