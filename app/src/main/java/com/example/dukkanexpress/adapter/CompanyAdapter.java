package com.example.dukkanexpress.adapter;

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

import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.Constant;
import com.example.dukkanexpress.MainActivity;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.model.CompanyModel;
import com.example.dukkanexpress.productFragmnet.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    ArrayList<CompanyModel> companyList;
    Context context;

    public CompanyAdapter(ArrayList<CompanyModel> companyList) {

        this.companyList = companyList;
    }

    @NonNull
    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company,parent,false);
        context = parent.getContext();
        return new CompanyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.ViewHolder holder, final int i) {
        holder.bind(companyList.get(i));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment products = new Products();
                Bundle bundle = new Bundle();
                bundle.putString("categoryId", companyList.get(i).getId());
                bundle.putString("tab_type", Constant.TAB_ABOUTAPP);
                products.setArguments(bundle);


                ((MainActivity) Common.mActivity).pushFragments(Constant.TAB_ABOUTAPP, products, true);

            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.com_name)
        TextView com_name;
        @BindView(R.id.com_image)
        ImageView com_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(CompanyModel companyModel) {

            if(Common.isArabic)
                com_name.setText(companyModel.getCategory_title_ar());
            else
                com_name.setText(companyModel.getCategory_title_en());

            Picasso.with(context).load(context.getString(R.string.image_link)+companyModel.getCategory_icon())
                    .placeholder(R.drawable.placeholder).into(com_image);
        }
    }
}
