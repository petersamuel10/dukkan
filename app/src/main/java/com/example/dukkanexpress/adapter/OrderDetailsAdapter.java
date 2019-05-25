package com.example.dukkanexpress.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.model.OrderDetailsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    ArrayList<OrderDetailsModel> productList;
    Context context;

    public OrderDetailsAdapter(ArrayList<OrderDetailsModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        context = parent.getContext();
        return new OrderDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {

        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image)
        ImageView product_image;
        @BindView(R.id.product_name)
        TextView product_name;
        @BindView(R.id.quantity)
        TextView quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(OrderDetailsModel productModel) {
            Glide.with(context).load(context.getString(R.string.image_link)+productModel.getProduct_img()).into(product_image);

            if (Common.isArabic)
                product_name.setText(productModel.getProduct_title_ar());
            else
                product_name.setText(productModel.getProduct_title_en());

            quantity.setText(productModel.getQuantity());

        }
    }
}
