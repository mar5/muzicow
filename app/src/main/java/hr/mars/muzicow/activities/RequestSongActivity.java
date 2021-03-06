package hr.mars.muzicow.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;

import hr.mars.muzicow.R;
import hr.mars.muzicow.models.Event;
import hr.mars.muzicow.requests.SongAPI;
import hr.mars.muzicow.services.ServiceGenerator;
import hr.mars.muzicow.utils.Registry;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Emil on 13.1.2016..
 */
public class RequestSongActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    EditText song;
    EditText artist;
    EditText description;
    EditText youtube;
    Button send;
    Event eventObj;
    String sng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.song_request);
        context = getApplicationContext();
        eventObj = (Event) Registry.getInstance().get("Event");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**
         * Listener for click in menu items
         * @param item clicked item
         */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                TwitterCore.getInstance().logOut();
                LoginManager.getInstance().logOut();
                logout();
                return true;
            }
        });

        send = (Button) findViewById(R.id.sendBtn);
        send.setOnClickListener(this);
        song = (EditText) findViewById(R.id.songTxt);
        artist = (EditText) findViewById(R.id.artistTxt);
        description = (EditText) findViewById(R.id.descriptionTxt);
        youtube = (EditText) findViewById(R.id.ytbTxt);
    }
    /**
     * Method for logout
     */
    public void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Method listening onClick event for buttons
     * It's starting new activity based on which
     * button is clicked
     * @param v    View object
     */
    public void onClick(View v) {
        final SongAPI eventRetrofit = ServiceGenerator.createService(SongAPI.class);
        switch (v.getId()) {
            case R.id.sendBtn:
                /**
                 * Checking if all values are entered
                 */
                if (song.getText().toString().matches("") || artist.getText().toString().matches("") ||
                        description.getText().toString().matches("") || youtube.getText().toString().matches("")) {
                    Toast.makeText(RequestSongActivity.this, "You must enter all values", Toast.LENGTH_LONG).show();
                } else {
                    /**
                     * Sending data to api based on query
                     * @param artist artist name
                     * @param eventObj event ID
                     * @param description song description
                     * @param youtube youtube link
                     * @param song song name
                     */
                    eventRetrofit.createSong(artist.getText().toString(), eventObj.get_ID(),
                            description.getText().toString(), "0", "0", "", youtube.getText().toString(),
                            song.getText().toString(),
                            new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {
                                    Toast.makeText(RequestSongActivity.this, "You have successfully requested song", Toast.LENGTH_LONG).show();
                                    send.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(RequestSongActivity.this, "Song wasnt succesfully requested", Toast.LENGTH_LONG).show();
                                }
                            });
                }
        }
    }
}