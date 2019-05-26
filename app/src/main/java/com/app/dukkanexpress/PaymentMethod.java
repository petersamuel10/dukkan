package com.app.dukkanexpress;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dukkanexpress.model.AddOrder;
import com.app.dukkanexpress.model.CartModel;
import com.app.dukkanexpress.model.ProductInfo;
import com.app.dukkanexpress.register_login.Login;
import com.google.gson.Gson;

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

public class PaymentMethod extends Fragment {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.total)
    TextView total_txt;
    @BindView(R.id.code_ed)
    EditText code_ed;
    @BindView(R.id.arrow1)
    ImageView arrow1;
    @BindView(R.id.arrow2)
    ImageView arrow2;

    @OnClick(R.id.back)
    public void back() {
        getActivity().onBackPressed();
    }

    Float total = 0.0f;
    private AddOrder addOrderModel;
    public String user_id, code = "";
    private AlertDialog alertDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_method, container, false);
        ButterKnife.bind(this, view);
        Paper.init(getContext());
        if (Common.isArabic) {
            back.setRotation(180);
            arrow1.setRotation(180);
            arrow2.setRotation(180);
        }

        alertDialog = Common.alert(getActivity());

        total = Paper.book("dukkan").read("total");
        total_txt.setText(String.format("%.3f", total) + " " + getString(R.string.kd));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Paper.book("dukkan").contains("current_user"))
            user_id = (Paper.book("dukkan").contains("current_user")) ? Common.currentUser.getUser().getUser_id() : "0";
        else
            ((MainActivity) getActivity()).pushFragments(Constant.TAB_HOME, new Login(), true);
    }

    @OnClick(R.id.cash)
    public void cashOnDelivery() {

        if (Paper.book("dukkan").contains("current_user"))
            user_id = (Paper.book("dukkan").contains("current_user")) ? Common.currentUser.getUser().getUser_id() : "0";
        else
            ((MainActivity) getActivity()).pushFragments(Constant.TAB_HOME, new Login(), true);

        if (user_id != "0") {

            if (!TextUtils.isEmpty(code_ed.getText().toString()))
                code = code_ed.getText().toString();


            ArrayList<CartModel> cartModel = Paper.book("dukkan").read("cart");

            ArrayList<ProductInfo> productList = new ArrayList<>();

            for (CartModel product : cartModel) {

                ProductInfo productInfo = new ProductInfo(product.getProduct().getProduct_id(),
                        product.getProduct().getProduct_price(),
                        product.getProduct().getProduct_quantity());

                productList.add(productInfo);
            }

            addOrderModel = new AddOrder("1", user_id, code, productList);

            Gson gson = new Gson();
            String add_order_model_str = gson.toJson(addOrderModel);

            if (Common.isConnectToTheInternet(getContext())) {
                new AddOrderAPI(getActivity(), add_order_model_str).execute();
            } else
                Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));
        }
    }


    private class AddOrderAPI extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;
        public String add_order_model_str;

        AddOrderAPI(Activity activity, String add_order_model_str) {
            this.add_order_model_str = add_order_model_str;
        }

        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getString(R.string.api) + "AddOrder.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);

                byte[] outputInBytes = add_order_model_str.getBytes("UTF-8");

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

                String message = jsonObject.getString("message");
                String order_num = jsonObject.getString("order_num");
                String price = jsonObject.getString("price");
                String vaild = jsonObject.getString("vaild");

                Intent i = new Intent(getContext(), PaymentResult.class);
                i.putExtra("message", message);
                i.putExtra("order_num", order_num);
                i.putExtra("price", price);
                i.putExtra("vaild", vaild);
                getActivity().startActivity(i);

            } catch (JSONException e) {
                Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
            }

        }
    }

}
