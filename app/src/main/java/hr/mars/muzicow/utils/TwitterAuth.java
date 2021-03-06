package hr.mars.muzicow.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import hr.mars.muzicow.adapter.FragmentAdapterChooser;
import hr.mars.muzicow.models.DJ;
import hr.mars.muzicow.models.Login;
import io.fabric.sdk.android.Fabric;

/**
 * Created by mars on 27/01/16.
 */
public class TwitterAuth implements SocialAuth<Login, Context, TwitterLoginButton> {
    Context ctx;
    TwitterLoginButton loginButton;
    String Provider;
    String TWITTER_KEY="f6FNdst2ZaoQWZYvYOu2a5QCy";
    String TWITTER_SECRET="deHaJ2nBf5Lj5luPg2Avu7w0JOxbb61GUNZavlb4SELDyK0WUV";
    String role;
    TwitterSession session;
    DJ djObject;
    TwitterRetData twitterReadData = new TwitterRetData();

    @Override
    public Context getContext() {
        return this.ctx;
    }

    @Override
    public void setContext(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void setSocialObject(TwitterLoginButton loginButton) {
        this.loginButton = loginButton;
    }

    @Override
    public TwitterLoginButton getsocialObject() {
        return this.loginButton;
    }

    public void setup() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(this.getKey(), this.getSecret());
        Fabric.with(this.ctx, new Twitter(authConfig));
    }


    @Override
    public String getKey() {
        return TWITTER_KEY;
    }

    @Override
    public void setKey(String key) {
        this.TWITTER_KEY = key;
    }

    @Override
    public String getSecret() {
        return TWITTER_SECRET;
    }

    @Override
    public void setSecret(String key) {
        this.TWITTER_SECRET = key;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void logout() {
        TwitterCore.getInstance().logOut();
    }

    /**
     * Implemented method from SocialAth interface
     * When login button is clicked, it calls callback which
     * implements 2 methods, succes and failure.
     * <p/>
     * Succes result returns Twitter session.
     * That session is setted in Singleton class Registry,
     * so that object is alive thrue application life cycle.
     *
     * @param loginButton object of Twitter button
     *
     */
    @Override
    public void signup(TwitterLoginButton loginButton) {
        /*
            Signup implementation for Twitter login.
        */
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                ((Login) Registry.getInstance().get("login.atr")).setSession(Twitter.getInstance().core.getSessionManager().getActiveSession());
                  /*
                    method which gets user from Twitter API
                */
                twitterReadData.retSNetData();
                twitterReadData.setListener(new TwitterLoginListener() {
                    @Override
                    public void success(Result<User> userOb) {
                        djObject = new DJ();
                        djObject.set_ID(userOb.data.idStr);
                        djObject.setName(userOb.data.name);
                        djObject.setDescription(userOb.data.description);
                        djObject.setLocation(userOb.data.location);
                        djObject.setProfile_url(userOb.data.profileImageUrl);
                        djObject.setNickname(userOb.data.screenName);
                        djObject.setWebsite(userOb.data.url);
                        showUserData(djObject);
                        Log.d("Name", djObject.getName());
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.d("Tw - data get fail", e.getMessage());
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }
    /**
     * Method for sending active user data
     * @param djObject   object of logged in user
     */

    public void showUserData(DJ djObject) {
        Intent myIntent = new Intent(this.ctx, FragmentAdapterChooser.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (getRole().equals("Participant")) {
            myIntent.putExtra("userRole", "Participant");
            myIntent.putExtra("Session", getRole());
            myIntent.putExtra("Twitter object", djObject);
            this.ctx.startActivity(myIntent);
        } else {
            myIntent.putExtra("userRole", "Artist");
            myIntent.putExtra("Session", getRole());
            myIntent.putExtra("Twitter object", djObject);
            this.ctx.startActivity(myIntent);
        }
    }
}