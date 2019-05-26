package com.app.dukkanexpress;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;

public class PaymentResult extends AppCompatActivity {

    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.order_num)
    TextView order_num;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.note)
    TextView note;

    String order_number_str, price_str, valid_str;

    @OnClick(R.id.done)
    public void done() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);
        ButterKnife.bind(this);

        Paper.init(this);
        Paper.book("dukkan").delete("cart");

        message.setText(getIntent().getExtras().getString("message"));
        order_number_str = getIntent().getExtras().getString("order_num");
        price_str = getIntent().getExtras().getString("price") + " " + getString(R.string.kd);

        valid_str = getIntent().getExtras().getString("vaild");

        order_num.setText(order_number_str);
        price.setText(price_str);

        if (valid_str.equals(""))
            note.setText(getString(R.string.thanks_you_for_your_order) + " " +
                    order_number_str + " " + getString(R.string.your_price) + " " + price_str);
        else if (valid_str.equals("vaild"))
            note.setText(getString(R.string.thanks_you_for_your_order) + " " +
                    order_number_str + " " + getString(R.string.your_price_after_discount) + " " + price_str);
        else if (valid_str.equals("non"))
            note.setText(getString(R.string.thanks_you_for_your_order) + " " +
                    order_number_str + " " + getString(R.string.your_gift_code_was_invalid));
    }

    @Override
    public void onBackPressed() {
    }
}
