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

import com.cumonywa.restaurant.restaurantorder.FoodMenuItem;
import com.cumonywa.restaurant.restaurantorder.R;
import com.cumonywa.restaurant.restaurantorder.model.FoodCatogoryModel;

import java.util.ArrayList;
import java.util.List;




public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryData> {
    Context context;
    List<FoodCatogoryModel> foodcList = new ArrayList<FoodCatogoryModel>();
    String tablename;
    int i=0;
   // int img[]={R.mipmap.a,R.mipmap.b,R.mipmap.c,R.mipmap.d,R.mipmap.e};
    int img[]={R.mipmap.a,R.mipmap.b,R.mipmap.c,R.mipmap.d,R.mipmap.e,R.mipmap.f,R.mipmap.g,R.mipmap.h,R.mipmap.i,R.mipmap.j,R.mipmap.k,R.mipmap.l,R.mipmap.m,R.mipmap.n,R.mipmap.o,R.mipmap.p,R.mipmap.q,R.mipmap.r,R.mipmap.s,R.mipmap.t,R.mipmap.u,R.mipmap.v,R.mipmap.w,R.mipmap.x,R.mipmap.y,R.mipmap.z};

    public FoodCategoryAdapter(Context context, List foodcate,String tableName) {
        this.context = context;
        this.foodcList = foodcate;
        this.tablename=tableName;
    }

    @Override
    public FoodCategoryData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_foodcategory, parent, false);
        return new FoodCategoryData(view);

    }

    @Override
    public void onBindViewHolder(FoodCategoryData holder, final int position) {
        holder.txtId.setText(Integer.toString(foodcList.get(position).getFoodId()));
        holder.txtFoodName.setText(foodcList.get(position).getFoodName());

        if(position>=img.length){
            holder.imgCategory.setImageResource(img[i]);
            i++;
            if(i>=img.length){
                i=0;
            }
        }else {
            holder.imgCategory.setImageResource(img[position]);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, FoodMenuItem.class);
                intent.putExtra("categoryname",foodcList.get(position).getFoodName());
                intent.putExtra("tablename",tablename);
                intent.putExtra("waitername",foodcList.get(position).getWaiterName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodcList.size();
    }
}

class FoodCategoryData extends RecyclerView.ViewHolder{
    TextView txtId;
    TextView txtFoodName;
    CardView cardView;
    ImageView imgCategory;
public FoodCategoryData(View itemView) {
    super(itemView);
    txtId=itemView.findViewById(R.id.txtId);
    txtFoodName=itemView.findViewById(R.id.txtFoodName);
    cardView=itemView.findViewById(R.id.card_foodCategory);
    imgCategory=itemView.findViewById(R.id.img_category);
}
}

