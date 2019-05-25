package com.example.dukkanexpress.mainFragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.dukkanexpress.Cart;
import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.Constant;
import com.example.dukkanexpress.MainActivity;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.adapter.CategoryAdapter;
import com.example.dukkanexpress.adapter.TopCategoryAdapter;
import com.example.dukkanexpress.model.CategoryModel;
import com.example.dukkanexpress.model.ProductModel;
import com.example.dukkanexpress.model.TopCategoryModel;
import com.example.dukkanexpress.productFragmnet.SearchFragment;
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


public class Home extends Fragment {


    @BindView(R.id.sliderLayout)
    SliderLayout sliderLayout;
    @BindView(R.id.category_rec)
    RecyclerView category_rec;
    @BindView(R.id.top_product_rec)
    RecyclerView top_product_rec;
    @BindView(R.id.lang)
    TextView lang_;

    @OnClick(R.id.lang)
    public void lang() {
        if (lang_.getText().equals("ع"))
            ((MainActivity) getActivity()).setNewLocale("ar");
        else if (lang_.getText().equals("E"))
            ((MainActivity) getActivity()).setNewLocale("en");
    }

    @OnClick(R.id.ic_cart)
    public void cart() {
        ((MainActivity) getActivity()).pushFragments(Constant.TAB_HOME, new Cart(), true);
    }

    @OnClick(R.id.ic_search)
    public void search() {
        ((MainActivity)getActivity()).pushFragments(Constant.TAB_HOME,new SearchFragment(),true);
    }

    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_home, container, false);
        ButterKnife.bind(this, view);

        alertDialog = Common.alert(getActivity());

        if (Common.isConnectToTheInternet(getContext())) {
            new GetCategory(getActivity()).execute();
            new GetSlider(getActivity()).execute();
            new GetTopCategory(getActivity()).execute();
        } else
            Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));

        return view;
    }


    private class GetCategory extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;
        public ArrayList<CategoryModel> categoryList;

        GetCategory(Activity activity) {
            categoryList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getResources().getString(R.string.api) + "GetCategory.php";
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
            //  alertDialog.dismiss();

            try {
                jsonObject = new JSONObject(new String(result));
                // String status = jsonObject.getString("message");
                JSONArray categoryItems = jsonObject.getJSONArray("category_item");

                if (categoryItems.length() != 0) {

                    for (int i = 0; i < categoryItems.length(); i++) {
                        JSONObject jsonObject = categoryItems.getJSONObject(i);

                        String category_id = jsonObject.getString("category_id");
                        String category_icon = jsonObject.getString("category_icon");
                        String category_title_ar = jsonObject.getString("category_title_ar");
                        String category_title_en = jsonObject.getString("category_title_en");

                        CategoryModel categoryItem = new CategoryModel(category_id, category_icon, category_title_ar, category_title_en);
                        categoryList.add(categoryItem);
                    }
                    Log.d("xxx111", String.valueOf(categoryList.size()));
                    CategoryAdapter adapter = new CategoryAdapter(categoryList, getContext());
                    category_rec.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {


                }
            } catch (JSONException e) {
                Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
            }

        }
    }

    private class GetTopCategory extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;
        public ArrayList<TopCategoryModel> categoryList_;

        GetTopCategory(Activity activity) {
            categoryList_ = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getResources().getString(R.string.api) + "GetTopCategory.php";
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
            Log.i("cccxxx", result);
            Gson gson = new Gson();

            try {
                jsonObject = new JSONObject(new String(result));
                // String status = jsonObject.getString("message");
                JSONArray categoryItems = jsonObject.getJSONArray("category");

                if (categoryItems.length() != 0) {

                    for (int i = 0; i < categoryItems.length(); i++) {
                        JSONObject jsonObject = categoryItems.getJSONObject(i);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("category_item");

                        String category_id = jsonObject1.getString("category_id");
                        String category_icon = jsonObject1.getString("category_icon");
                        String category_title_ar = jsonObject1.getString("category_title_ar");
                        String category_title_en = jsonObject1.getString("category_title_en");


                        JSONArray products_json = jsonObject1.getJSONArray("product_item");
                        ArrayList<ProductModel> productList = productList = new ArrayList<>();
                        for (int x = 0; x < products_json.length(); x++) {

                            ProductModel productItem = new ProductModel();
                            try {

                                JSONObject product = products_json.getJSONObject(x);

                                String result1 = product.toString();
                                productItem = gson.fromJson(result1, ProductModel.class);

                                productList.add(productItem);
                            }catch (JSONException e){

                            }

                        }


                        TopCategoryModel categoryItem = new TopCategoryModel(category_id, category_title_ar, category_title_en, category_icon, productList);
                        categoryList_.add(categoryItem);
                        Log.d("xxx111222", String.valueOf(categoryList_.size()));
                    }
                    TopCategoryAdapter adapter = new TopCategoryAdapter(categoryList_, getContext());
                    top_product_rec.setAdapter(adapter);
                    alertDialog.dismiss();
                } else {

                    Common.showErrorAlert(getActivity(), getString(R.string.empty_my_list));
                }
            } catch (JSONException e) {
                Log.d("error", e.getMessage());
                Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
            }

        }
    }

    private class GetSlider extends AsyncTask<String, Void, String> {

        public ProgressDialog dialog;
        public JSONObject jsonObject = null;
        ArrayList<String> mylist;

        GetSlider(Activity activity) {
            this.dialog = new ProgressDialog(activity);
            mylist = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getResources().getString(R.string.api) + "GetSlider.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);


                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);


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
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();

            Log.d("ststst", "result login: " + result);

            // Toast.makeText(Login.this, "//////////////", Toast.LENGTH_SHORT).show();
            try {
                jsonObject = new JSONObject(new String(result));
                //String status = jsonObject.getString("message");
                JSONArray sliderList = jsonObject.getJSONArray("slider");


                if (sliderList.length() != 0) {

                    for (int i = 0; i < sliderList.length(); i++) {
                        JSONObject jsonObject = sliderList.getJSONObject(i);

                        String photos_id = jsonObject.getString("photos_id");
                        String photos_path = getResources().getString(R.string.image_link) + jsonObject.getString("photos_path");
                        Log.i("link1", getResources().getString(R.string.image_link) + photos_path);

                        mylist.add(photos_path);

                        for (String photo : mylist) {
                            TextSliderView textSliderView = new TextSliderView(getContext());
                            textSliderView.image(photo).empty(R.drawable.placeholder).setScaleType(BaseSliderView.ScaleType.Fit);
                            sliderLayout.addSlider(textSliderView);
                        }

                        // slider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
                        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
