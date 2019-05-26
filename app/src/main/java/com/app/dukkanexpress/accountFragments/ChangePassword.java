package com.app.dukkanexpress.accountFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.dukkanexpress.Common;
import com.app.dukkanexpress.MainActivity;
import com.app.dukkanexpress.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassword extends Fragment {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.new_password_ed)
    EditText new_password_ed;
    @BindView(R.id.current_pass_ed)
    EditText current_password_rd;
    @BindView(R.id.confirm_new_password_ed)
    EditText confirm_password_ed;
    @BindView(R.id.changePassBtn)
    Button change_pass_btn;

    String current_password,new_password,confirm_password;

    @OnClick(R.id.back)
    public void back() {
        ((MainActivity) getActivity()).onBackPressed();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_change_password, container, false);
        ButterKnife.bind(this,view);

        if(Common.isArabic)
            back.setRotation(180);

        return view;
    }



    @OnClick(R.id.changePassBtn)
    public void change_pass(){

        current_password = current_password_rd.getText().toString();
        new_password = new_password_ed.getText().toString();
        confirm_password = confirm_password_ed.getText().toString();

        if(validate(current_password,new_password,confirm_password)){

            if(Common.isConnectToTheInternet(getActivity())) {

            /*compositeDisposable.add(Common.getAPI().register()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LoginData>() {
                        @Override
                        public void accept(LoginData loginData) throws Exception {

                        }
                    }));*/

            }else
                Common.showErrorAlert(getActivity(),getString(R.string.error_no_internet_connection));
        }
    }

    private boolean validate(String current_password, String new_password, String confirm_password) {

        if(TextUtils.isEmpty(current_password)) {
            Common.showErrorAlert(getActivity(),getString(R.string.please_enter_current_password));
            return false;
        }else if (TextUtils.isEmpty(new_password)){
            Common.showErrorAlert(getActivity(),getString(R.string.please_enter_new_password));
            return false;
        }else if (TextUtils.isEmpty(confirm_password)){
            Common.showErrorAlert(getActivity(),getString(R.string.please_enter_confirm_password));
            return false;
        }else if (!new_password.equals(this.confirm_password)){
            Common.showErrorAlert(getActivity(),getString(R.string.error_confirm_password_not_match_new_password));
            return false;
        }

        return true;
    }

}
