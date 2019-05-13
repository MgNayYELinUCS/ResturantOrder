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

public class PreviewAdapter extends RecyclerView.Adapter<Preview> {

    Context context;
    List<FoodMenuModel> orderPreview=new ArrayList<FoodMenuModel>();

    public PreviewAdapter(Context context, List<FoodMenuModel> orderPreview) {
        this.context = context;
        this.orderPreview = orderPreview;
    }

    @NonNull
    @Override
    public Preview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_activity,parent,false);
        return new Preview(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Preview holder, int position) {
        holder.txtFName.setText(orderPreview.get(position).getFood_name()+" - "+orderPreview.get(position).getDescription()+"");
        holder.txtFQty.setText(Integer.toString(orderPreview.get(position).getFood_qty()));
        holder.txtFPrice.setText((orderPreview.get(position).getPrice()*orderPreview.get(position).getFood_qty())+" Ks");

        final int pos = position;

        /*holder.item_name.setText(orderPreview.get(position).getFood_name());*/

        holder.chkSelected.setChecked(orderPreview.get(position).isSelected());

        holder.chkSelected.setTag(orderPreview.get(position));

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                FoodMenuModel model = (FoodMenuModel) cb.getTag();

                model.setSelected(cb.isChecked());
                orderPreview.get(pos).setSelected(cb.isChecked());

            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteItemFromList(v, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderPreview.size();
    }

    private void deleteItemFromList(View v, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        //builder.setTitle("Dlete ");
        builder.setMessage("Delete Item ?")
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseHandler db=new DatabaseHandler(context);

                                String fname=orderPreview.get(position).getFood_name();
                                String des=orderPreview.get(position).getDescription();
                                db.deleteEachContact(fname,des);

                                orderPreview.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });

        builder.show();

    }
}
class Preview extends RecyclerView.ViewHolder {

    TextView txtFName;
    TextView txtFQty;
    TextView txtFPrice;
  /* public TextView item_name;*/
    public ImageButton btn_delete;
    public CheckBox chkSelected;



    public Preview(View itemView) {
        super(itemView);
        txtFName=itemView.findViewById(R.id.txtFname);
        txtFQty=itemView.findViewById(R.id.txtFqty);
        txtFPrice = itemView.findViewById(R.id.txtFprice);
       /* item_name = (TextView) itemView.findViewById(R.id.txt_Name);*/
        btn_delete = (ImageButton) itemView.findViewById(R.id.btn_delete_unit);
        chkSelected = (CheckBox) itemView.findViewById(R.id.chk_selected);

    }
}


