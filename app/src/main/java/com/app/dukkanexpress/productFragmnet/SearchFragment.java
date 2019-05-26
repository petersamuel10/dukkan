package com.app.dukkanexpress.productFragmnet;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.R;
import com.app.dukkanexpress.adapter.ProductHomeAdapter;
import com.app.dukkanexpress.model.ProductModel;
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

import static android.view.View.GONE;

public class SearchFragment extends Fragment {

    String search_str = "";
    public ArrayList<ProductModel> productList;
    ProductHomeAdapter adapter = new ProductHomeAdapter();

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.search_ed)
    EditText search_ed;
    @BindView(R.id.ic_clear)
    ImageView ic_clear;
    @BindView(R.id.no_data_cart)
    RelativeLayout no_data_list;
    @BindView(R.id.search_rec)
    RecyclerView search_rec;

    @OnClick(R.id.ic_clear)
    public void clear() {
        search_ed.setText("");
        search_str = "";
        adapter.clear();
    }

    @OnClick(R.id.ic_cancel)
    public void setBack() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.back)
    public void back_() {
        getActivity().onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.product_search, container, false);
        ButterKnife.bind(this, view);

        if (Common.isArabic)
            back.setRotation(180);

   /*     search_ed.requestFocus();

        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);*/

        search_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    ic_clear.setVisibility(View.VISIBLE);

                    search_str = s.toString();

                    if (Common.isConnectToTheInternet(getContext())) {
                        new GetProducts(getActivity()).execute();
                    } else
                        Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));


                } else {
                    ic_clear.setVisibility(GONE);
                    adapter.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private class GetProducts extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;
        Gson gson = new Gson();

        GetProducts(Activity activity) {
            productList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getResources().getString(R.string.api) + "SearchProduct.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);


                String str = "{\"limit\": 0,\"product_name\":\"" + search_str + "\"}";
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

            productList.clear();
            try {
                if (result.contains("message")) {
                    adapter.clear();
                } else {
                    jsonObject = new JSONObject(new String(result));
                    JSONArray products_json = jsonObject.getJSONArray("product_item");
                    if (products_json.length() < 1)
                        no_data_list.setVisibility(View.VISIBLE);
                    else {
                        for (int x = 0; x < products_json.length(); x++) {

                            ProductModel productItem;
                            JSONObject product = products_json.getJSONObject(x);

                            String result1 = product.toString();
                            productItem = gson.fromJson(result1, ProductModel.class);
                            productItem.setProduct_img(productItem.getProduct_path());

                            productList.add(productItem);
                        }


                        adapter = new ProductHomeAdapter(productList, getContext(), true, 0);
                        adapter.notifyDataSetChanged();
                        search_rec.setAdapter(adapter);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
