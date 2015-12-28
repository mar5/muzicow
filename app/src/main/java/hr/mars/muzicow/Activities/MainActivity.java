package hr.mars.muzicow.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import hr.mars.muzicow.Activities.adapters.FragmentAdapter;

import hr.mars.muzicow.Models.DJ;
import hr.mars.muzicow.Models.LoginAtributes;
import hr.mars.muzicow.R;
import hr.mars.muzicow.Services.TwitterLoginListener;
import hr.mars.muzicow.Utils.SNetworkChooser;
import hr.mars.muzicow.Utils.TwitterRetData;
import io.fabric.sdk.android.Fabric;

/**
 * Created by mars on 14/11/15.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TWITTER_KEY = "f6FNdst2ZaoQWZYvYOu2a5QCy";
    private static final String TWITTER_SECRET = "deHaJ2nBf5Lj5luPg2Avu7w0JOxbb61GUNZavlb4SELDyK0WUV ";
    private TwitterLoginButton loginButton;
    DJ djObject;
    public static LoginAtributes at = new LoginAtributes();
    TwitterRetData tw = new TwitterRetData();
    SNetworkChooser ch = new SNetworkChooser();
    String role;
    Intent intent;
    Bundle bundle;

    public static LoginAtributes getAt() {
        return at;
    }
    public static void setAt(LoginAtributes at) {
        MainActivity.at = at;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_network_login);
        final Spinner mySpinner=(Spinner) findViewById(R.id.spinner);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = mySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try {
            intent = getIntent();
            bundle = intent.getExtras();
        }
        catch (Exception e){

        }
        if(bundle != null){
            role = bundle.getString("userRole");
            at.setRole(bundle.getString(role));
        }

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter);

        loginButton.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                at.setSession(Twitter.getInstance().core.getSessionManager().getActiveSession());
                ch.setSNetwork(tw);
                ch.loginChoice();

                tw.setListener(new TwitterLoginListener() {
                    @Override
                    public void success(Result<User> userOb) {
                        djObject = new DJ();
                        Log.d("pokazi", "ovo je id prosljeden" + userOb.data.idStr);
                        djObject.set_ID(userOb.data.idStr);
                        djObject.setName(userOb.data.name);
                        djObject.setDescription(userOb.data.description);
                        djObject.setLocation(userOb.data.location);
                        djObject.setProfile_url(userOb.data.profileImageUrl);
                        djObject.setNickname(userOb.data.screenName);
                        djObject.setWebsite(userOb.data.url);
                        showUserData(djObject);

                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.d("pokazi", e.getMessage());

                    }
                });

            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginButton.onActivityResult(requestCode, resultCode, data);

    }
    public void showUserData(DJ djObject){
        Intent myIntent = new Intent(MainActivity.this, FragmentAdapter.class);
        if(role.equals("Participant")) {
            myIntent.putExtra("userRole", "Participant");
            myIntent.putExtra("sesija", role);
            myIntent.putExtra("hr.mars.muzicow.RESTful.model.DJ",  djObject);
            MainActivity.this.startActivity(myIntent);
        }
        else{
            myIntent.putExtra("userRole", "Artist");
            myIntent.putExtra("sesija", role);
            myIntent.putExtra("hr.mars.muzicow.RESTful.model.DJ",  djObject);
            MainActivity.this.startActivity(myIntent);
        }
    }

    


}