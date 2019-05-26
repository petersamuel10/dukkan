package com.app.dukkanexpress.mainFragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;
import com.app.dukkanexpress.adapter.CompanyAdapter;
import com.app.dukkanexpress.model.CompanyModel;
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
public class AboutApp extends Fragment {

    @BindView(R.id.lang)
    TextView lang_;
    @BindView(R.id.com_rec)
    RecyclerView com_rec;

    AlertDialog alertDialog;
    ArrayList<CompanyModel> companyList;
    Gson gson = new Gson();

    @OnClick(R.id.lang)
    public void lang() {
        if (lang_.getText().equals("ع"))
            ((MainActivity) getActivity()).setNewLocale("ar");
        else if (lang_.getText().equals("E"))
            ((MainActivity) getActivity()).setNewLocale("en");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_about_app, container, false);
        ButterKnife.bind(this, view);

        alertDialog = Common.alert(getActivity());

        if (Common.isConnectToTheInternet(getContext())) {
            new GetAboutApp().execute();
        } else
            Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));

        return view;
    }

    private class GetAboutApp extends AsyncTask<String, Void, String> {

        public JSONObject jsonObject = null;


        @Override
        protected void onPreExecute() {

            alertDialog.show();
            companyList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            String signin_url = getString(R.string.api) + "GetCompany.php";
            try {
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("cache-control", "application/json");
                httpURLConnection.setConnectTimeout(7000);
                httpURLConnection.setReadTimeout(7000);

                String str = "{\"level\" : 0}";

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
                JSONArray jsonObject1 = jsonObject.getJSONArray("category_item");
                for(int i=0;i<jsonObject1.length();i++){

                    JSONObject object = jsonObject1.getJSONObject(i);
                    String company = object.toString();

                    CompanyModel companyModel = gson.fromJson(company,CompanyModel.class);

                    companyList.add(companyModel);
                }

                CompanyAdapter adapter = new CompanyAdapter(companyList);
                com_rec.setAdapter(adapter);


            } catch (JSONException e) {
                Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
            }

        }
    }
}
