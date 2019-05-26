package com.app.dukkanexpress.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.Constant;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;
import com.app.dukkanexpress.model.TopCategoryModel;
import com.app.dukkanexpress.productFragmnet.Products;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.ViewHolder> {
    ArrayList<TopCategoryModel> categoryList;
    Context context;

    public TopCategoryAdapter(ArrayList<TopCategoryModel> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public TopCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_category, parent, false);

        return new TopCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopCategoryAdapter.ViewHolder holder, final int position) {

        holder.bind(categoryList.get(position));

        holder.watch_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment products = new Products();
                Bundle bundle = new Bundle();
                bundle.putString("categoryId", categoryList.get(position).getCategory_id());
                bundle.putString("tab_type", Constant.TAB_HOME);
                products.setArguments(bundle);

                ((MainActivity) Common.mActivity).pushFragments(Constant.TAB_HOME, products, true);

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.category_name)
        TextView category_name;
        @BindView(R.id.watch_all)
        TextView watch_all;
        @BindView(R.id.top_product_rec)
        RecyclerView top_product_rec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(TopCategoryModel categoryModel) {
            if (Common.isArabic)
                category_name.setText(categoryModel.getCategory_title_ar());
            else
                category_name.setText(categoryModel.getCategory_title_en());

            ProductHomeAdapter productHomeAdapter = new ProductHomeAdapter(categoryModel.getProduct_item(),context,false,0);
            top_product_rec.setAdapter(productHomeAdapter);
        }
    }
}
