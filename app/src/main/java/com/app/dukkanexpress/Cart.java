package com.app.dukkanexpress;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.dukkanexpress.adapter.CartAdapter;
import com.app.dukkanexpress.interFace.Cart_action_interface;
import com.app.dukkanexpress.model.CartModel;
import com.app.dukkanexpress.register_login.Login;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class Cart extends Fragment implements Cart_action_interface {

    @BindView(R.id.no_data_cart)
    RelativeLayout no_data_cart;
    @BindView(R.id.cartLn)
    RelativeLayout cart_ln;
    @BindView(R.id.cart_rec)
    RecyclerView cart_rec;

    @OnClick(R.id.back)
    public void back() {
        getActivity().onBackPressed();
    }

    private ArrayList<CartModel> cartModel;

    private CartAdapter cartAdapter;
    public static TextView total_txt;


    public static float total = 0.0f;
    public String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        Paper.init(getContext());
        total_txt = view.findViewById(R.id.total);

        cart_rec.setLayoutManager(new LinearLayoutManager(getContext()));

        if ((Paper.book("dukkan").contains("cart"))) {
            cart_ln.setVisibility(View.VISIBLE);
            getCartData();
        } else
            no_data_cart.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        user_id = (Paper.book("dukkan").contains("current_user")) ? Common.currentUser.getUser().getUser_id() : "0";

    }

    private void getCartData() {

        cartModel = Paper.book("dukkan").read("cart");
        cartAdapter = new CartAdapter(cartModel);
        cartAdapter.setListener(this);
        cart_rec.setAdapter(cartAdapter);

        total = Paper.book("dukkan").read("total");
        total_txt.setText(String.format("%.3f", total) + " " + getString(R.string.kd));
    }


    @OnClick(R.id.payBtn)
    public void payBtn() {
        if (Paper.book("dukkan").contains("current_user"))
            ((MainActivity) getActivity()).pushFragments(Constant.TAB_HOME, new PaymentMethod(), true);
        else
            ((MainActivity) getActivity()).pushFragments(Constant.TAB_HOME, new Login(), true);
    }

    @Override
    public void deleteClick(int position, int flag, String quantity) {
        total -= cartModel.get(position).getTotal();
        total_txt.setText(String.format("%.3f", total) + " " + getString(R.string.kd));
        Paper.book("dukkan").write("total", total);

        cartModel.remove(position);
        cartAdapter.notifyItemRemoved(position);
        Paper.book("dukkan").write("cart", cartModel);

        if (cartModel.size() == 0) {
            cart_ln.setVisibility(View.GONE);
            no_data_cart.setVisibility(View.VISIBLE);
            Paper.book("dukkan").delete("cart");
            Paper.book("dukkan").delete("total");
        }
    }

}
