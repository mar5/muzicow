package hr.mars.muzicow.Login;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

import hr.mars.muzicow.Adapters.FragmentAdapter;
import hr.mars.muzicow.Interface.SocialnetworkManager;
import hr.mars.muzicow.R;
import hr.mars.muzicow.RESTful.model.DJ;
import io.fabric.sdk.android.Fabric;



/**
 * Created by Emil on 26.11.2015..
 */

public class Login extends AppCompatActivity implements SocialnetworkManager {

    private static final String TWITTER_KEY = "f6FNdst2ZaoQWZYvYOu2a5QCy";
    private static final String TWITTER_SECRET = "deHaJ2nBf5Lj5luPg2Avu7w0JOxbb61GUNZavlb4SELDyK0WUV ";

    private TwitterLoginButton loginButton;

    String role;
    String sess;
    TwitterSession session;
    DJ dj;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
          TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
          Fabric.with(this, new Twitter(authConfig));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            role = bundle.getString("userRole");
        }

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter);
        loginButton.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {

                Log.d("prikaz", "uspjesan login");
                session = Twitter.getInstance().core.getSessionManager().getActiveSession();
                sess = session.toString();
                retTwitterData();


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
    public void retTwitterData(){

        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {


                    @Override
                    public void success(Result<User> userResult) {
                        dj = new DJ();
                        Intent myIntent = new Intent(Login.this, FragmentAdapter.class);
                        dj.set_ID(userResult.data.idStr);

                        if(role.equals("Korisnik")) {
                            myIntent.putExtra("userRole", "Korisnik");
                            myIntent.putExtra("sesija", sess);
                            myIntent.putExtra("hr.mars.muzicow.RESTful.model.DJ", dj);
                            Login.this.startActivity(myIntent);
                        }
                        else{
                            myIntent.putExtra("userRole", "DJ");
                            myIntent.putExtra("sesija", sess);
                            myIntent.putExtra("hr.mars.muzicow.RESTful.model.DJ", dj);
                            Login.this.startActivity(myIntent);

                        }


                        Log.d("prikaz", userResult.data.idStr);
                        Log.d("prikaz", userResult.data.name);
                        Log.d("prikaz", userResult.data.description);
                        Log.d("prikaz", userResult.data.location);
                        Log.d("prikaz", userResult.data.profileImageUrl);


                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.d("prikaz", e.getMessage());
                    }

                });


    }

}
