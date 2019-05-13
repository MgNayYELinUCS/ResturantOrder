package com.cumonywa.restaurant.restaurantorder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cumonywa.restaurant.restaurantorder.FoodCategoryActivity;
import com.cumonywa.restaurant.restaurantorder.R;
import com.cumonywa.restaurant.restaurantorder.model.TableModel;

import java.util.ArrayList;
import java.util.List;

public  class TableAdapter extends RecyclerView.Adapter<TableData> {

    Context context;
    List<TableModel> TableList=new ArrayList<TableModel>();
    String waiterName;

    @Override
    public TableData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_table,parent,false);
        return new TableData(view);
    }

    @Override
    public void onBindViewHolder(final TableData holder, final int position) {
         holder.imgTable.setImageResource(TableList.get(position).getTableImage());
         holder.txtTableName.setText(TableList.get(position).getTableName());
         waiterName=TableList.get(position).getWaitername();
        holder.cview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,FoodCategoryActivity.class);
                intent.putExtra("Name",TableList.get(holder.getAdapterPosition()).getTableName());

                intent.putExtra("Waitername1",waiterName);
              //Log.i("@@@@@@@waiter$$$$$ name",waiterName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return TableList.size();
    }

    public TableAdapter(Context context, List tableList,String waitername) {
        this.context = context;
         TableList = tableList;
         this.waiterName=waitername;
    }
}

class TableData extends RecyclerView.ViewHolder{
    ImageView imgTable;
    TextView txtTableName;
    CardView cview;

    public TableData(View itemView) {
        super(itemView);
        imgTable=itemView.findViewById(R.id.imgTable);
        txtTableName=itemView.findViewById(R.id.txttableName);
        cview=itemView.findViewById(R.id.table_cardview);
    }
}



