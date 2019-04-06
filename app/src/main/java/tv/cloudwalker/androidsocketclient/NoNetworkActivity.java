package tv.cloudwalker.androidsocketclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import services.CloudwalkerNSDService;

public class NoNetworkActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
    }

    public void gotoNsd(View view) {
        startService(new Intent(getApplicationContext(), CloudwalkerNSDService.class));
        startActivity(new Intent(view.getContext(), NsdActivity.class));
        onBackPressed();
    }
}
