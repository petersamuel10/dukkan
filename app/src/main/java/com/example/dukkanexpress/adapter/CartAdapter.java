package com.example.dukkanexpress.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dukkanexpress.Cart;
import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.interFace.Cart_action_interface;
import com.example.dukkanexpress.model.CartModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<CartModel> cartList;
    ArrayList<String> quantityList;
    Context context;
    Cart_action_interface cart_action_interface;

    public CartAdapter(ArrayList<CartModel> cartModel) {
        this.cartList = cartModel;
        quantityList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        context = parent.getContext();
        Paper.init(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.bind(cartList.get(position));

        holder.product_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle(context.getString(R.string.select_count));
                final String[] count_items = new String[]{"1", "2", "3", "4", "5", "6"};

                alert.setSingleChoiceItems(count_items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int positionItem) {
                        dialog.dismiss();
                        holder.product_quantity.setText(count_items[positionItem]);
                        Float item_cost =  Float.valueOf(cartList.get(position).getProduct().getProduct_price())*
                                Integer.parseInt(count_items[positionItem]);
                        Float previous_item_cost = cartList.get(position).getTotal();
                        Float total = Cart.total - previous_item_cost;
                        total += item_cost;
                        Cart.total = total;
                        Cart.total_txt.setText(String.format("%.3f", total) + " " + context.getString(R.string.kd));
                        cartList.get(position).setTotal(item_cost);
                        cartList.get(position).getProduct().setProduct_quantity(count_items[positionItem]);
                        notifyDataSetChanged();
                        Paper.book("dukkan").write("cart", cartList);
                        Paper.book("dukkan").write("total",total);

                    }
                });

                alert.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image)
        ImageView product_image;
        @BindView(R.id.product_name)
        TextView product_name;
        @BindView(R.id.total)
        TextView product_price;
        @BindView(R.id.quantity)
        TextView product_quantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CartModel cart_product) {

            quantityList.add(cart_product.getProduct().getProduct_quantity());

            if (Common.isArabic) {
                product_name.setText(cart_product.getProduct().getProduct_title_ar());
            } else {
                product_name.setText(cart_product.getProduct().getProduct_title_en());
            }

            product_quantity.setText(cart_product.getProduct().getProduct_quantity());

            product_price.setText(cart_product.getProduct().getProduct_price() + " " + context.getResources().getString(R.string.kd));
            Glide.with(context).load(context.getResources().getString(R.string.image_link) + cart_product.getProduct().getProduct_img())
                    .placeholder(R.drawable.placeholder).into(product_image);

        }

        @OnClick(R.id.ic_delete)
        public void delete_cart() {
            cart_action_interface.deleteClick(getAdapterPosition(),0,null); }

    }

    public void setListener(Cart_action_interface listener) {
        this.cart_action_interface = listener;
    }
}
