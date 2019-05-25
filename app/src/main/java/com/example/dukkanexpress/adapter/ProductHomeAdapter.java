package com.example.dukkanexpress.adapter;

import android.content.Context;
import android.graphics.Paint;
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
import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.Constant;
import com.example.dukkanexpress.MainActivity;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.model.ProductModel;
import com.example.dukkanexpress.productFragmnet.ProductDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductHomeAdapter extends RecyclerView.Adapter<ProductHomeAdapter.ViewHolder> {

    ArrayList<ProductModel> productList;
    Context context;
    boolean isOffer;
    int tab_position;

    public ProductHomeAdapter() {
    }

    public ProductHomeAdapter(ArrayList<ProductModel> productList, Context context, boolean isOffer, int tab_position) {
        this.productList = productList;
        this.context = context;
        this.isOffer = isOffer;
        this.tab_position = tab_position;
    }

    @NonNull
    @Override
    public ProductHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view;
        if(isOffer)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_offer,parent,false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_home,parent,false);

        return new ProductHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHomeAdapter.ViewHolder holder, final int position) {

        holder.bind(productList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment productFrag = new ProductDetails();
                Bundle bundle = new Bundle();
                bundle.putString("productId", productList.get(position).getProduct_id());
                productFrag.setArguments(bundle);

                switch (tab_position){
                    case 0:
                        ((MainActivity) Common.mActivity).pushFragments(Constant.TAB_HOME, productFrag, true);
                        break;
                    case 1:
                        ((MainActivity) Common.mActivity).pushFragments(Constant.TAB_OFFERS, productFrag, true);
                        break;
                    case 2:
                        ((MainActivity) Common.mActivity).pushFragments(Constant.TAB_MYLIST, productFrag, true);
                        break;

                }

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
        @BindView(R.id.product_discount)
        TextView product_discount;


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

            if(!productModel.getProduct_discount().equals("")){
                product_price.setPaintFlags(product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);;
                product_discount.setVisibility(View.VISIBLE);
                product_discount.setText(productModel.getProduct_discount()+" "+context.getResources().getString(R.string.kd));
            }else
                product_discount.setVisibility(View.GONE);

            switch (tab_position){
                case 0:
                    Glide.with(context).load(context.getResources().getString(R.string.image_link)+productModel.getProduct_img())
                            .placeholder(R.drawable.placeholder).into(product_image);
                    break;
                case 1:
                    Glide.with(context).load(context.getResources().getString(R.string.image_link)+productModel.getProduct_path())
                            .placeholder(R.drawable.placeholder).into(product_image);
                    break;
                case 2:
                    Glide.with(context).load(context.getResources().getString(R.string.image_link)+productModel.getProduct_img())
                            .placeholder(R.drawable.placeholder).into(product_image);
                    break;
            }
        }
    }

    public void clear(){
        productList.clear();
        notifyDataSetChanged();
    }
}
