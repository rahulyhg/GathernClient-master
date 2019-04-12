package com.max.gathernclient.gathernclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class TermsAndCancelation extends AppCompatActivity {
String cancelPolicy ,reserveConditions ;
MyTextView cancel_Policy , reserve_Conditions ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_cancelation);
        getStrings();
        cancel_Policy = findViewById(R.id.cancel_Policy);
        cancel_Policy.setText(cancelPolicy);
        reserve_Conditions = findViewById(R.id.reserve_Conditions);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;
        }
    }
    private void getStrings() {
        cancelPolicy = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("cancelPolicy")).toString();
        reserveConditions = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("reserveConditions")).toString();
    }
}
