package com.cumonywa.restaurant.restaurantorder.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cumonywa.restaurant.restaurantorder.DatabaseHandler;
import com.cumonywa.restaurant.restaurantorder.R;
import com.cumonywa.restaurant.restaurantorder.model.FoodMenuModel;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<Order> {

    Context context;
    List<FoodMenuModel> orderPreview=new ArrayList<FoodMenuModel>();

    public OrderAdapter(Context context, List<FoodMenuModel> orderPreview) {
        this.context = context;
        this.orderPreview = orderPreview;
    }

    @NonNull
    @Override
    public Order onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_activity,parent,false);
        return new Order(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Order holder, int position) {
        holder.txtFName.setText(orderPreview.get(position).getFood_name());
        holder.txtFQty.setText(Integer.toString(orderPreview.get(position).getFood_qty()));
        holder.txtFPrice.setText(Integer.toString(orderPreview.get(position).getPrice()));
        holder.txtTFPrice.setText(Integer.toString(orderPreview.get(position).getPrice()*orderPreview.get(position).getFood_qty()));

    }

    @Override
    public int getItemCount() {
        return orderPreview.size();
    }

}
class Order extends RecyclerView.ViewHolder {

    TextView txtFName;
    TextView txtFQty;
    TextView txtFPrice;
    TextView txtTFPrice;
    public ImageButton btn_delete;
    public CheckBox chkSelected;


    public Order(View itemView) {
        super(itemView);
        txtFName=itemView.findViewById(R.id.txtFname);
        txtFQty=itemView.findViewById(R.id.txtFqty);
        txtFPrice = itemView.findViewById(R.id.txtFprice);
        txtTFPrice = itemView.findViewById(R.id.txtTFprice);
        btn_delete = (ImageButton) itemView.findViewById(R.id.btn_delete_unit);
        chkSelected = (CheckBox) itemView.findViewById(R.id.chk_selected);

    }
}
