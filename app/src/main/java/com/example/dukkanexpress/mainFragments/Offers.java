package com.example.dukkanexpress.mainFragments;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.MainActivity;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.adapter.ProductHomeAdapter;
import com.example.dukkanexpress.model.ProductModel;
import com.google.gson.Gson;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class Offers extends Fragment {

    @BindView(R.id.no_data_cart)
    RelativeLayout no_data_list;
    @BindView(R.id.offers_rec)
    RecyclerView offers_rec;
    @BindView(R.id.lang)
    TextView lang_;

    @OnClick(R.id.lang)
    public void lang() {
        if (lang_.getText().equals("ع"))
            ((MainActivity) getActivity()).setNewLocale("ar");
        else if (lang_.getText().equals("E"))
            ((MainActivity) getActivity()).setNewLocale("en");
    }

    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_offers, container, false);
        ButterKnife.bind(this, view);

        alertDialog = Common.alert(getActivity());

        if (Common.isConnectToTheInternet(getContext())) {
            new GetProducts(getActivity()).execute();
        } else
            Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));

        return view;
    }

    private class GetProducts extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;
        public ArrayList<ProductModel> productList;
        Gson gson = new Gson();

        GetProducts(Activity activity) {
            productList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getResources().getString(R.string.api) + "GetOffers.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);


                String str = "{\"level\": 0}";
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
                JSONArray products_json = jsonObject.getJSONArray("product_item");

                if(products_json.length()<1)
                    no_data_list.setVisibility(View.VISIBLE);
                else {
                    for (int x = 0; x < products_json.length(); x++) {

                        ProductModel productItem;
                        JSONObject product = products_json.getJSONObject(x);

                        String result1 = product.toString();
                        productItem = gson.fromJson(result1, ProductModel.class);

                        productList.add(productItem);
                    }

                    ProductHomeAdapter adapter = new ProductHomeAdapter(productList, getContext(), true, 1);
                    offers_rec.setAdapter(adapter);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
