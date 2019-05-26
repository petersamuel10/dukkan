package com.app.dukkanexpress.register_login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.Constant;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;
import com.app.dukkanexpress.mainFragments.User;
import com.app.dukkanexpress.model.LoginData;
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
public class Login extends Fragment {

    @BindView(R.id.lang)
    TextView lang_;
    @BindView(R.id.email_ed)
    EditText email_ed;
    @BindView(R.id.password_ed)
    EditText password_ed;

    @OnClick(R.id.lang)
    public void lang() {
        if (lang_.getText().equals("ع"))
            ((MainActivity) getActivity()).setNewLocale("ar");
        else if (lang_.getText().equals("E"))
            ((MainActivity) getActivity()).setNewLocale("en");
    }

    @OnClick(R.id.create_new_user)
    public void register() {
        ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, new Register(), true);
    }

    String email, password;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login, container, false);
        ButterKnife.bind(this, view);
        alertDialog = Common.alert(getActivity());
        Paper.init(getContext());
        if (Paper.book("dukkan").contains("current_user")) {
            ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, new User(), true);
        }

        return view;
    }

    @OnClick(R.id.loginBtn)
    public void login() {

        email = email_ed.getText().toString();
        password = password_ed.getText().toString();
        if (Common.isConnectToTheInternet(getContext())) {
            if (validate(email, password))
                calApi();
        } else
            Common.showErrorAlert(getActivity(), getString(R.string.error_no_internet_connection));


    }

    private void calApi() {

        alertDialog.show();
        final Gson gson = new Gson();

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("user_email", email);
            postparams.put("user_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String signin_url = getString(R.string.api) + "LoginUser.php";
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, signin_url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                alertDialog.dismiss();
                requestQueue.stop();
                String r = response.toString();

                try {

                    LoginData current_user = gson.fromJson(r, LoginData.class);
                    Common.currentUser = current_user;
                    Paper.book("dukkan").write("current_user", current_user);

                   // Fragment user_fragment = new User();
                    ((MainActivity) getActivity()).onBackPressed();//pushFragments(Constant.TAB_USER, user_fragment, true);

                } catch (Exception e) {
                    Common.showErrorAlert(getActivity(), getString(R.string.error_please_try_again_later));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                alertDialog.dismiss();
                Common.showErrorAlert(getActivity(), getString(R.string.login_faild));
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

    private boolean validate(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            Common.showErrorAlert(getActivity(), getString(R.string.please_enter_email));
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Common.showErrorAlert(getActivity(), getString(R.string.please_enter_password));
            return false;
        }

        return true;
    }
}
