package amazon.jy.com.amazon.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import amazon.jy.com.amazon.R;

public class PayResultActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        findView();
    }
    private void findView() {
        Button mToOrderDetail = findViewById(R.id.to_order_detail);
        mToOrderDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetail = new Intent(PayResultActivity.this, OrdersActivity.class);
                startActivity(orderDetail);
                finish();
            }
        });
    }
}
