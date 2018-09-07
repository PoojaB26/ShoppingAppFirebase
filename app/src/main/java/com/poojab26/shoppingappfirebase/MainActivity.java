package com.poojab26.shoppingappfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Remote Config keys
    private static final String SPECIAL_BOOL = "special";
    private static final String SPECIAL_MESSAGE = "special_message";

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private TextView mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseSetup();
        mMessage = findViewById ( R.id.msg );


        fetchMessage();
    }

    private void firebaseSetup() {
        // Get Remote Config instance.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        // Set default Remote Config parameter values. An app uses the in-app default values, and
        // when you need to adjust those defaults, you set an updated value for only the values you
        // want to change in the Firebase console.
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    }

    /**
     * Checks if special is true, then displays the special message
     * Otherwise displays local message.
     */
    private void fetchMessage() {

        long cacheExpiration = 3600; // 1 hour in seconds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void> () {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            mFirebaseRemoteConfig.activateFetched();
                            if(mFirebaseRemoteConfig.getBoolean ( SPECIAL_BOOL ))
                                mMessage.setText(mFirebaseRemoteConfig.getString(SPECIAL_MESSAGE));
                            else
                                mMessage.setText ( "Today is an usual day" );

                        } else {
                            Toast.makeText(MainActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                       // displayWelcomeMessage();
                    }
                });
    }

    /**
     * Display a welcome message in all caps if welcome_message_caps is set to true. Otherwise,
     * display a welcome message as fetched from welcome_message.
     */
    // [START display_welcome_message]
//    private void displayWelcomeMessage() {
//        // [START get_config_values]
//        String welcomeMessage = mFirebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY);
//        // [END get_config_values]
//        if (mFirebaseRemoteConfig.getBoolean(WELCOME_MESSAGE_CAPS_KEY)) {
//            mWelcomeTextView.setAllCaps(true);
//        } else {
//            mWelcomeTextView.setAllCaps(false);
//        }
//        mWelcomeTextView.setText(welcomeMessage);
//    }
}
