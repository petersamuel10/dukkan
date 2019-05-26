package com.app.dukkanexpress.mainFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;
import com.app.dukkanexpress.adapter.ProductHomeAdapter;
import com.app.dukkanexpress.model.ProductModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class MyList extends Fragment {

    @BindView(R.id.muList_rec)
    RecyclerView muList_rec;
    @BindView(R.id.lang)
    TextView lang_;
    @BindView(R.id.no_data_cart)
    RelativeLayout no_data_list;
    @BindView(R.id.list_ln)
    LinearLayout list_ln;

    @OnClick(R.id.lang)
    public void lang() {
        if (lang_.getText().equals("ع"))
            ((MainActivity) getActivity()).setNewLocale("ar");
        else if (lang_.getText().equals("E"))
            ((MainActivity) getActivity()).setNewLocale("en");
    }

    ArrayList<ProductModel> productFavList = new ArrayList<>();
    AlertDialog alertDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_my_list, container, false);
        ButterKnife.bind(this, view);
        Paper.init(getContext());
        alertDialog = Common.alert(getActivity());

        if (Paper.book("dukkan").contains("fav"))
            productFavList = Paper.book("dukkan").read("fav");
        else
            no_data_list.setVisibility(View.GONE);

        if (productFavList.size() < 1){
            no_data_list.setVisibility(View.VISIBLE);
            list_ln.setVisibility(View.GONE);
    }
        else {
            ProductHomeAdapter adapter = new ProductHomeAdapter(productFavList, getContext(), true, 2);
            muList_rec.setAdapter(adapter);
            list_ln.setVisibility(View.VISIBLE);
        }

        return view;
    }

}
