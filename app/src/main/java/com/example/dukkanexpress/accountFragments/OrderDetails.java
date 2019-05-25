package com.example.dukkanexpress.accountFragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.adapter.OrderDetailsAdapter;
import com.example.dukkanexpress.model.OrderDetailsModel;
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

public class OrderDetails extends Fragment {

    @BindView(R.id.back)
    ImageView back_arrow;
    @BindView(R.id.rootView)
    ConstraintLayout rootView;
    @BindView(R.id.order_number)
    TextView order_number;
    @BindView(R.id.order_date)
    TextView order_date;
    @BindView(R.id.order_status)
    TextView order_status;
    @BindView(R.id.rec_order_products)
    RecyclerView rec_order_products;

    @OnClick(R.id.back)
    public void back() {
        getActivity().onBackPressed();
    }

    AlertDialog alertDialog;
    OrderDetailsAdapter adapter;
    ArrayList<OrderDetailsModel> orderDetailsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_order_details, container, false);
        ButterKnife.bind(this, view);

        alertDialog = Common.alert(getActivity());

        if (Common.isArabic)
            back_arrow.setRotation(180);


        if (Common.isConnectToTheInternet(getContext())) {
            new GetOrderDetails(getActivity()).execute();
        } else
            Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));

        return view;
    }

    private class GetOrderDetails extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;

        GetOrderDetails(Activity activity) {
            orderDetailsList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getString(R.string.api) + "GetOrderByOrderID.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);

                String str = "{\"order_id\":\"" + getArguments().getString("orderId") + "\"}";
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
            rootView.setVisibility(View.VISIBLE);

            try {
                jsonObject = new JSONObject(new String(result));

                    JSONArray order_list = jsonObject.getJSONArray("order_list");
                    JSONObject object = order_list.getJSONObject(0);

                    JSONObject orders = object.getJSONObject("orders");

                    order_number.setText(orders.getString("order_number"));
                    order_date.setText(orders.getString("order_created_at"));
                    order_status.setText(orders.getString("order_status_en_name"));

                    JSONArray product_List = object.getJSONArray("product_List");
                    for (int i = 0; i < product_List.length(); i++) {
                        JSONObject object1 = product_List.getJSONObject(i);

                        String product = (object1.getJSONObject("product")).toString();

                        OrderDetailsModel orderDetailsModel =new OrderDetailsModel();
                        Gson gson = new Gson();
                        orderDetailsModel = gson.fromJson(product,OrderDetailsModel.class);

                        orderDetailsList.add(orderDetailsModel);

                    }

                    rec_order_products.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new OrderDetailsAdapter(orderDetailsList);
                    rec_order_products.setAdapter(adapter);

            } catch (JSONException e) {
                Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
            }

        }
    }

}
