package com.app.dukkanexpress.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;
import com.app.dukkanexpress.interFace.Add_to_cart;
import com.app.dukkanexpress.model.ProductModel;
import com.app.dukkanexpress.productFragmnet.ProductDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    ArrayList<ProductModel> productList;
    Context context;
    String TAB_TYPE;
    Add_to_cart add_to_cart;

    public ProductAdapter(ArrayList<ProductModel> productList, Context context, String TAB_TYPE) {
        this.productList = productList;
        this.context = context;
        this.TAB_TYPE = TAB_TYPE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);

        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.bind(productList.get(position));

        holder.product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment productFrag = new ProductDetails();
                Bundle bundle = new Bundle();
                bundle.putString("productId", productList.get(position).getProduct_id());
                productFrag.setArguments(bundle);

                ((MainActivity) Common.mActivity).pushFragments(TAB_TYPE, productFrag, true);
            }
        });

       holder.buttonCart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ProductModel productModel = productList.get(position);
               productModel.setProduct_quantity("1");
               add_to_cart.addCart(productModel);
           }
       });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_name)
        TextView product_name;
        @BindView(R.id.product_image)
        ImageView product_image;
        @BindView(R.id.product_price)
        TextView product_price;
        @BindView(R.id.buttonCart)
        ImageView buttonCart;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(ProductModel productModel) {

            if(Common.isArabic)
                product_name.setText(productModel.getProduct_title_ar());
            else
                product_name.setText(productModel.getProduct_title_en());

                product_price.setText(productModel.getProduct_price()+" "+context.getResources().getString(R.string.kd));

            Glide.with(context).load(context.getResources().getString(R.string.image_link)+productModel.getProduct_img())
                    .placeholder(R.drawable.placeholder).into(product_image);
        }
    }

    public void setListener(Add_to_cart listener) {
        this.add_to_cart = listener;
    }
}
