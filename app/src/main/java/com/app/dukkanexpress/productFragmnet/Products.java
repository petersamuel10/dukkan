package com.app.dukkanexpress.productFragmnet;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.dukkanexpress.Cart;
import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;
import com.app.dukkanexpress.adapter.ProductAdapter;
import com.app.dukkanexpress.interFace.Add_to_cart;
import com.app.dukkanexpress.model.CartModel;
import com.app.dukkanexpress.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class Products extends Fragment implements Add_to_cart {

    String TAB_TYPE;
    ArrayList<CartModel> cartList;
    ProductModel productModel;
    Add_to_cart add_to_cart;

    float allTotal = 0.0f, total = 0.0f;

    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.product_rec)
    RecyclerView product_rec;
    String categoryId;
    AlertDialog alertDialog;
    @OnClick(R.id.back)
    public void back() {
        ((MainActivity) getActivity()).onBackPressed();
    }
    @OnClick(R.id.ic_cart)
    public void cart() {
        ((MainActivity) getActivity()).pushFragments(TAB_TYPE, new Cart(), true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this,view);
        alertDialog = Common.alert(getActivity());
        add_to_cart = this;

        categoryId = getArguments().getString("categoryId");
        TAB_TYPE = getArguments().getString("tab_type");

        if (Common.isConnectToTheInternet(getContext())) {
            new GetProducts(getActivity()).execute();
        } else
            Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));

        if(Common.isArabic)
            back.setRotation(180);

        return view;
    }

    @Override
    public void addCart(ProductModel productModel) {

        cartList = new ArrayList<>();
        this.productModel = productModel;
        if (Paper.book("dukkan").contains("cart")) {
            cartList = Paper.book("dukkan").read("cart");
            allTotal = Paper.book("dukkan").read("total");
        }

        if (isItemExistInCart()) {
            Common.showErrorAlert(getActivity(), getString(R.string.this_item_already_exist_in_cart));
        } else {

            if (productModel.getProduct_discount().equals("")) {
                total = Float.parseFloat(productModel.getProduct_price());
            } else {
                total = Float.parseFloat(productModel.getProduct_discount());
                productModel.setProduct_price(productModel.getProduct_discount());
            }

            allTotal += total;

            Paper.book("dukkan").write("total", allTotal);
            cartList.add(new CartModel(productModel, total));
            Paper.book("dukkan").write("cart", cartList);

            Snackbar.make(rootView, getString(R.string.product_add_success), Snackbar.LENGTH_LONG).show();
        }
    }

    public boolean isItemExistInCart() {

        for (CartModel cart_item : cartList) {
            if (cart_item.getProduct().getProduct_id().equals(productModel.getProduct_id()))
                return true;
        }
        return false;
    }

    private class GetProducts extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;
        public ArrayList<ProductModel> productList;

        GetProducts(Activity activity) {
            productList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getResources().getString(R.string.api)+"GetProductWhereCategoryID.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);


                String str = "{\"limit\":0," + "\"category_id\": " + categoryId + "}";
                Log.i("ccc", str);

                byte[] outputInBytes = str.getBytes("UTF-8");

                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream OS = httpURLConnection.getOutputStream();
                OS.write(outputInBytes);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));

                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }

                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return response;

            } catch (IOException e) {
                Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
                return "";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            alertDialog.dismiss();

            try {
                jsonObject = new JSONObject(new String(result));
                Log.d("rrrr",result);
                JSONArray productItems = jsonObject.getJSONArray("product_item");

                if (productItems.length() != 0) {

                    for (int i = 0; i < productItems.length(); i++) {
                        JSONObject jsonObject = productItems.getJSONObject(i);

                        String product_id = jsonObject.getString("product_id");
                        String product_title_ar = jsonObject.getString("product_title_ar");
                        String product_title_en = jsonObject.getString("product_title_en");
                        String product_price = jsonObject.getString("product_price");
                        String product_discount = jsonObject.getString("product_discount");
                        String product_img = jsonObject.getString("product_img");

                        ProductModel productItem = new ProductModel(product_id, product_title_ar, product_title_en
                                , product_price, product_discount, product_img);
                        productList.add(productItem);
                    }

                    ProductAdapter adapter = new ProductAdapter(productList, getContext(),TAB_TYPE);
                    adapter.setListener(add_to_cart);
                    product_rec.setAdapter(adapter);
                } else {
                    Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
                }
            } catch (JSONException e) {
                Common.showErrorAlert(getActivity(), e.getMessage());
            }

        }
    }

}
