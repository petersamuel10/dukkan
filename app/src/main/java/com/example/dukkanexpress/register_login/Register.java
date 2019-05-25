package com.example.dukkanexpress.register_login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dukkanexpress.Common;
import com.example.dukkanexpress.Constant;
import com.example.dukkanexpress.MainActivity;
import com.example.dukkanexpress.R;
import com.example.dukkanexpress.mainFragments.User;
import com.example.dukkanexpress.model.LoginData;
import com.example.dukkanexpress.model.RegisterModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends Fragment {

    @BindView(R.id.email_ed)
    EditText email_ed;
    @BindView(R.id.user_name_ed)
    EditText user_name_rd;
    @BindView(R.id.password_ed)
    EditText password_ed;
    @BindView(R.id.confirm_password_ed)
    EditText confirm_password_ed;
    @BindView(R.id.phone_ed)
    EditText phone_ed;
    @BindView(R.id.address_ed)
    EditText address_ed;

    @OnClick(R.id.ic_back_arrow)
    public void back() {
        ((MainActivity) getActivity()).onBackPressed();
    }

    String email, name, password, confirm_password, phone, address;
    RegisterModel registerModel;

    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register, container, false);
        ButterKnife.bind(this, view);
        alertDialog = Common.alert(getActivity());
        Paper.init(getContext());
        if (Paper.book("dukkan").contains("current_user")) {
            ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, new User(), true);
        }

        return view;
    }

    @OnClick(R.id.registerBtn)
    public void register() {

        name = user_name_rd.getText().toString();
        email = email_ed.getText().toString();
        password = password_ed.getText().toString();
        phone = phone_ed.getText().toString();
        address = address_ed.getText().toString();
        confirm_password = confirm_password_ed.getText().toString();

        if (validate(name, email, password, confirm_password, phone, address)) {
            registerModel = new RegisterModel(name, email, password, phone, address);
            if (Common.isConnectToTheInternet(getContext())) {
                calApi();
            } else
                Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));
        }

    }

    private boolean validate(String name, String email, String password, String confirm_password, String phone, String address) {

        if (TextUtils.isEmpty(name)) {
            Common.showErrorAlert(getActivity(), getString(R.string.please_enter_user_name));
            return false;
        } else if (TextUtils.isEmpty(email)) {
            Common.showErrorAlert(getActivity(), getString(R.string.please_enter_email));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Common.showErrorAlert(getActivity(), getString(R.string.please_enter_password));
            return false;
        } else if (TextUtils.isEmpty(confirm_password)) {
            Common.showErrorAlert(getActivity(), getString(R.string.please_enter_confirm_password));
            return false;
        } else if (TextUtils.isEmpty(phone)) {
            Common.showErrorAlert(getActivity(), getString(R.string.please_enter_your_phone));
            return false;
        } else if (TextUtils.isEmpty(address)) {
            Common.showErrorAlert(getActivity(), getString(R.string.please_enter_your_address));
            return false;
        } else if (!password.equals(confirm_password)) {
            Common.showErrorAlert(getActivity(), getString(R.string.error_confirm_password_not_match_new_password));
            return false;
        }

        return true;
    }


    private void calApi() {

        final Gson gson = new Gson();
        alertDialog.show();
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("user_name", name);
            postparams.put("user_email", email);
            postparams.put("user_password", password);
            postparams.put("user_telep", phone);
            postparams.put("user_address", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String signUp_url = getString(R.string.api) + "/CreateUser.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, signUp_url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String r = response.toString();
                alertDialog.dismiss();

                try {

                    LoginData current_user = gson.fromJson(r, LoginData.class);
                    Common.currentUser = current_user;
                    Paper.book("dukkan").write("current_user", current_user);

                    Fragment user_fragment = new User();
                    ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, user_fragment, true);

                } catch (Exception e) {
                    Common.showErrorAlert(getActivity(), "rrrrr" + e.getMessage());
                }
                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Common.showErrorAlert(getActivity(), getString(R.string.email_register_before));
                requestQueue.stop();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cache-control", "application/json");
                return params;
            }
        };

        requestQueue.add(stringRequest);

    }

}
