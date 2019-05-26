package com.app.dukkanexpress.productFragmnet;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.app.dukkanexpress.Cart;
import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.Constant;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetails extends Fragment {

    String product_id, quantity = "1";
    AlertDialog alertDialog;
    ArrayList<String> imageList = new ArrayList<>();
    ArrayList<ProductModel> productFavList = new ArrayList<>();
    ArrayList<CartModel> cartList;
    ProductModel productModel;
    int index_fav;

    float allTotal = 0.0f, total = 0.0f;

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rootView)
    FrameLayout rootView;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.product_name)
    TextView product_name;
    @BindView(R.id.sliderLayout)
    SliderLayout sliderLayout;
    @BindView(R.id.product_price)
    TextView product_price;
    @BindView(R.id.product_discount)
    TextView product_discount;
    @BindView(R.id.count_ed)
    TextView count_ed;
    @BindView(R.id.ic_fav)
    ImageView ic_fav;
    @BindView(R.id.ic_share)
    ImageView ic_share;
    @BindView(R.id.product_desc)
    TextView product_desc;
    @BindView(R.id.cart_button)
    ConstraintLayout cart_button;

    @OnClick(R.id.back)
    public void back() {
        ((MainActivity) getActivity()).onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        ButterKnife.bind(this, view);
        alertDialog = Common.alert(getActivity());
        if (Common.isArabic)
            back.setRotation(180);

        product_id = getArguments().getString("productId");
        if (Paper.book("dukkan").contains("fav"))
            productFavList = Paper.book("dukkan").read("fav");

        if (Common.isConnectToTheInternet(getContext())) {
            new GetProductDetails(getActivity()).execute();
            new GetGalleryProduct(getActivity()).execute();
        } else
            Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));

        return view;
    }

    private class GetProductDetails extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;

        GetProductDetails(Activity activity) {
        }

        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getResources().getString(R.string.api) + "GetProductWhereProductID.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);


                String str = "{\"product_id\":" + product_id + "}";
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
                // String status = jsonObject.getString("message");
                JSONArray productDetails = jsonObject.getJSONArray("product_details");
                JSONObject jsonObject = productDetails.getJSONObject(0);

                String product_id = jsonObject.getString("product_id");
                String product_title_ar = jsonObject.getString("product_title_ar");
                String product_title_en = jsonObject.getString("product_title_en");
                String product_price_ = jsonObject.getString("product_price");
                String product_discount_ = jsonObject.getString("product_discount");
                String product_code = jsonObject.getString("product_code");
                String product_description_en = jsonObject.getString("product_description_en");
                String product_description_ar = jsonObject.getString("product_description_ar");
                String product_specifications_en = jsonObject.getString("product_specifications_en");
                String product_specifications_ar = jsonObject.getString("product_specifications_ar");
                String product_path = jsonObject.getString("product_path");

                product_name.setText((Common.isArabic) ? product_title_ar : product_title_en);
                product_price.setText(product_price_ + " " + getContext().getResources().getString(R.string.kd));

                if (!product_discount_.equals("")) {
                    product_discount.setVisibility(View.VISIBLE);
                    product_price.setPaintFlags(product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    product_discount.setText(product_discount_ + " " + getContext().getResources().getString(R.string.kd));

                } else {
                    product_discount.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(product_description_en)) {
                    product_desc.setText((Common.isArabic) ? product_description_ar : product_description_en);
                } else
                    product_desc.setVisibility(View.GONE);

                imageList.add(getString(R.string.image_link) + product_path);

                // for favorite
                productModel = new ProductModel(product_id, product_title_ar, product_title_en, product_price_,
                        product_discount_, product_path);


                for (int i = 0; i < productFavList.size(); i++) {
                    ProductModel model = productFavList.get(i);

                    if (model.getProduct_id().equals(productModel.getProduct_id())) {
                        ic_fav.setImageResource(R.drawable.ic_favorite);
                        ic_fav.setTag("true");
                        index_fav = i;
                    } else
                        ic_fav.setTag("false");
                }

                scroll.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
            }

        }
    }

    private class GetGalleryProduct extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;
        ArrayList<String> imageList;

        GetGalleryProduct(Activity activity) {
            imageList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getResources().getString(R.string.api) + "GetGalleryWhereProductID.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);


                String str = "{\"product_id\":" + product_id + "}";
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

            if(!result.contains("message")) {

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

                            imageList.add(photos_path);

                            for (String photo : imageList) {
                                TextSliderView textSliderView = new TextSliderView(getContext());
                                textSliderView.image(photo).empty(R.drawable.placeholder).setScaleType(BaseSliderView.ScaleType.CenterCrop);
                                sliderLayout.addSlider(textSliderView);
                            }

                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

                        }
                    }

                } catch (JSONException e) {
                    Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
                }
            }
        }
    }

    @OnClick(R.id.ic_share)
    public void share() {
        Intent sentIntent = new Intent();
        sentIntent.setAction(Intent.ACTION_SEND);
        sentIntent.putExtra(Intent.EXTRA_TEXT, "Dukkan Express");
        sentIntent.putExtra(Intent.EXTRA_TEXT, productModel.getProduct_title_en());
        sentIntent.setType("text/plain");
        startActivity(sentIntent);
    }

    @OnClick(R.id.ic_fav)
    public void fav() {


        if (ic_fav.getTag().equals("true")) {
            ic_fav.setTag("false");
            productFavList.remove(index_fav);
            ic_fav.setImageResource(R.drawable.ic_favorite_border);
        } else {
            ic_fav.setTag("true");
            productFavList.add(productModel);
            ic_fav.setImageResource(R.drawable.ic_favorite);
        }
        Paper.book("dukkan").write("fav", productFavList);

    }

    @OnClick({R.id.count_ed})
    public void count_() {

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle(getString(R.string.select_count));
        final String[] count_items = new String[]{"1", "2", "3", "4", "5", "6"};

        alert.setSingleChoiceItems(count_items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                dialog.dismiss();
                quantity = count_items[position];
                count_ed.setText(quantity);
            }
        });

        alert.create().show();
    }

    @OnClick(R.id.cart_button)
    public void addToCart() {

        cartList = new ArrayList<>();

        if (Paper.book("dukkan").contains("cart")) {
            cartList = Paper.book("dukkan").read("cart");
            allTotal = Paper.book("dukkan").read("total");
        }

        if (isItemExistInCart()) {
            Common.showErrorAlert(getActivity(), getString(R.string.this_item_already_exist_in_cart));

        } else {

            if (productModel.getProduct_discount().equals("")) {
                total = Float.parseFloat(productModel.getProduct_price()) * Integer.parseInt(quantity);
            } else {
                total = Float.parseFloat(productModel.getProduct_discount()) * Integer.parseInt(quantity);
                productModel.setProduct_price(productModel.getProduct_discount());
            }

            allTotal += total;

            Paper.book("dukkan").write("total", allTotal);
            productModel.setProduct_quantity(quantity);
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

    @OnClick(R.id.ic_cart)
    public void cart() {
        ((MainActivity) getActivity()).pushFragments(Constant.TAB_HOME, new Cart(), true);
    }
}
