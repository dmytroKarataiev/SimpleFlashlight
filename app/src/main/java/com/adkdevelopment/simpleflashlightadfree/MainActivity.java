package com.adkdevelopment.simpleflashlightadfree;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int status;

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            status = savedInstanceState.getInt("status");

            if (status != 0) {
                Intent intent = new Intent(getApplication(), FlashlightService.class);
                intent.putExtra("status", status);
                getApplication().startService(intent);
            }
        } else {
            status = 0;
        }

        final ImageView button = (ImageView) findViewById(R.id.button_image);
        final TextView statusText = (TextView) findViewById(R.id.flashlight_mode);

        // check status and use correct image
        setSwitchColor(statusText, button, status);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start service on click
                status = (status + 1) % 3;

                Intent intent = new Intent(getApplication(), FlashlightService.class);

                // Set button drawable
                setSwitchColor(statusText, button, status);

                intent.putExtra("status", status);
                getApplication().startService(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop service on application exit
        Intent intent = new Intent(getApplication(), FlashlightService.class);
        getApplication().stopService(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save status on rotate, possibly will remove rotation in the future
        outState.putInt("status", status);
    }

    /**
     * Method to set flashlight button drawable
     * @param button to switch flashlight
     * @param status flashlight mode
     */
    private void setSwitchColor(TextView mode, ImageView button, int status) {
        switch (status) {
            case 0:
                button.setImageResource(R.drawable.switch_on);
                mode.setText(R.string.flashlight_status_on);
                break;
            case 1:
                button.setImageResource(R.drawable.switch_blink);
                mode.setText(R.string.flashlight_status_blink);
                break;
            case 2:
                button.setImageResource(R.drawable.switch_off);
                mode.setText(R.string.flashlight_status_off);
                break;
        }
    }
}
