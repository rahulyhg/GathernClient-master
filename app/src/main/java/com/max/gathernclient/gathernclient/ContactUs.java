package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactUs extends AppCompatActivity {
Globals g ;
TextView textCall ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);
        g = new Globals(this);
        textCall = findViewById(R.id.textCall);
        String s = "920007858" +"\n" + "  9:30 am: 12:30 am";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2.5f), 0,10 ,0);
        textCall.setText(ss1);

    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;
            case R.id.textCall :
                Intent openTransActivity = new Intent(getBaseContext(), TransparentActivity.class);
                openTransActivity.putExtra("ID", "bottomDialog");
                startActivity(openTransActivity);

                break;
            case R.id.email :
                Uri uri=Uri.parse("mailto:"+"hello@gathern.co");
                Intent send = new Intent(Intent.ACTION_SENDTO , uri);
                send.putExtra(Intent.EXTRA_SUBJECT ,"Hey Gathern !");
                send.putExtra(Intent.EXTRA_TEXT ,"How are You? Nice greetings from gathern");
                startActivity(Intent.createChooser(send ,"open with"));
                break;
        }
    }

}
