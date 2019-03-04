package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class CloudwalkerFormActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloudwalker_form);

        Bundle bundle = getIntent().getBundleExtra("profile");

        ((TextView)findViewById(R.id.email)).setText(bundle.getString("email"));
        ((TextView)findViewById(R.id.name)).setText(bundle.getString("fullName"));
        ((TextView)findViewById(R.id.googleid)).setText(bundle.getString("id"));

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);


        Glide.with(this)
                .load(bundle.getString("photourl"))
                .into((CircleImageView)findViewById(R.id.profile_image));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:

                startActivity(new Intent(CloudwalkerFormActivity.this, NSDActivity.class));

                break;

            case R.id.logout:
                break;

            default:
        }
    }
}
