package com.example.dukkanexpress.accountFragments;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.MainActivity;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.adapter.OrderAdapter;
import com.example.dukkanexpress.model.OrderModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousOrder extends Fragment {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.no_data_order)
    RelativeLayout no_data_order;
    @BindView(R.id.order_rec)
    RecyclerView order_rec;
    @BindView(R.id.rec_ln)
    LinearLayout rec_ln;

    @OnClick(R.id.back)
    public void back() {
        ((MainActivity) getActivity()).onBackPressed();
    }

    ArrayList<OrderModel> orderModelList;
    OrderAdapter adapter;

    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_previous_order, container, false);
        ButterKnife.bind(this, view);

        alertDialog = Common.alert(getActivity());

        orderModelList = new ArrayList<>();
        order_rec.setLayoutManager(new LinearLayoutManager(getContext()));

        if (Common.isConnectToTheInternet(getContext())) {
            new GetOrders(getActivity()).execute();
        } else
            Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));

        if(Common.isArabic)
           back.setRotation(180);

        return view;
    }


    private class GetOrders extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;
        public ArrayList<ProductModel> productList;

        GetOrders(Activity activity) {
            productList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getString(R.string.api) + "GetOrderByCustomerID.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);


                String str = "{\"user_id\":\"" + Common.currentUser.getUser().getUser_id() + "\"}";
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

            if (result.contains("No Orders was Found")) {

                no_data_order.setVisibility(View.VISIBLE);

            } else {
                rec_ln.setVisibility(View.VISIBLE);
                try {
                    jsonObject = new JSONObject(new String(result));
                    // String status = jsonObject.getString("message");
                    JSONArray orders = jsonObject.getJSONArray("order_items");

                    if (orders.length() != 0) {

                        for (int i = 0; i < orders.length(); i++) {

                            JSONObject jsonObject = orders.getJSONObject(i);

                            String order_id = jsonObject.getString("order_id");
                            String order_number = jsonObject.getString("order_number");
                            String order_created_at = jsonObject.getString("order_created_at");
                            String order_status_id = jsonObject.getString("order_status_id");
                            String order_status_name = jsonObject.getString("order_status_name");
                            String order_status_en_name = jsonObject.getString("order_status_en_name");
                            String order_time = jsonObject.getString("order_time");

                            OrderModel order = new OrderModel(order_id, order_number, order_created_at,
                                    order_status_id, order_status_name, order_status_en_name, order_time);

                            orderModelList.add(order);
                        }
                        adapter = new OrderAdapter(orderModelList,getActivity());
                        order_rec.setAdapter(adapter);

                    } else {

                    }
                } catch (JSONException e) {
                    Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
                }
            }

        }
    }
}
