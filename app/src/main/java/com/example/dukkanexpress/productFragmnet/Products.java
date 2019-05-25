package com.example.dukkanexpress.productFragmnet;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dukkanexpress.Cart;
import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.Constant;
import com.example.dukkanexpress.MainActivity;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.adapter.ProductAdapter;
import com.example.dukkanexpress.model.ProductModel;

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

public class Products extends Fragment {

    String TAB_TYPE;

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
