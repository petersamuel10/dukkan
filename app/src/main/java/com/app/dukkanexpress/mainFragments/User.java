package com.app.dukkanexpress.mainFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.Constant;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;
import com.app.dukkanexpress.accountFragments.AccountInfo;
import com.app.dukkanexpress.accountFragments.ChangePassword;
import com.app.dukkanexpress.accountFragments.PreviousOrder;
import com.app.dukkanexpress.register_login.Login;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class User extends Fragment {
    @BindView(R.id.lang)
    TextView lang_;
    @BindView(R.id.account_information)
    TextView account_information;
    @BindView(R.id.previous_order)
    TextView previous_order;
    @BindView(R.id.change_password)
    TextView change_password;
    @BindView(R.id.logout)
    TextView logout;


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
        View view = inflater.inflate(R.layout.main_user, container, false);
        ButterKnife.bind(this, view);
        Paper.init(getContext());

        if (!Paper.book("dukkan").contains("current_user"))
            ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, new Login(), true);
        else
            Common.currentUser = Paper.book("dukkan").read("current_user");

        if (Common.isArabic) {
            account_information.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            previous_order.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            change_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
            logout.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_left, 0, 0, 0);
        } else {
            account_information.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
            previous_order.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
            change_password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
            logout.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_right, 0);
        }

        return view;
    }

    @OnClick(R.id.account_information)
    public void info() {
        Fragment user_fragment = new AccountInfo();
        ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, user_fragment, true);

    }

    @OnClick(R.id.previous_order)
    public void previous_order() {
        Fragment user_fragment = new PreviousOrder();
        ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, user_fragment, true);
    }

    @OnClick(R.id.change_password)
    public void change_pass() {
        Fragment user_fragment = new ChangePassword();
        ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, user_fragment, true);
    }


    @OnClick(R.id.logout)
    public void logout() {

        Paper.book("dukkan").delete("current_user");
        Fragment user_fragment = new User();
        ((MainActivity) getActivity()).pushFragments(Constant.TAB_USER, user_fragment, true);
    }
}
