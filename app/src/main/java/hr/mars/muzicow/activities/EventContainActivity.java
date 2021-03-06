package hr.mars.muzicow.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterCore;

import hr.mars.muzicow.R;
import hr.mars.muzicow.adapter.FragmentAdapter;
import hr.mars.muzicow.fragments.user.EventInfoFragment;
import hr.mars.muzicow.fragments.user.PlaylistUserFragment;
import hr.mars.muzicow.models.Event;
import hr.mars.muzicow.utils.Registry;
import hr.mars.muzicow.utils.SocialAuth;

/**
 * Created by Emil on 27.12.2015..
 */
public class EventContainActivity extends AppCompatActivity {
    Context context;

    Event eventObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /**
         * Listener for click in menu items
         * @param item clicked item
         */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SocialAuth at=(SocialAuth)Registry.getInstance().get("authManager");
                at.logout();
                logout();
                return true;
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Event eve = new Event();
        /**
         * Setting Event object to hashmap
         * @param eve    Event object
         */
        Registry.getInstance().set("Event", eve);

        eventObj = (Event) Registry.getInstance().get("Event");

        if (bundle != null) {
            eventObj.set_ID(bundle.getString("EventId"));
            eventObj.setName(bundle.getString("EventName"));
            eventObj.setGenre(bundle.getString("EventGenre"));
            eventObj.setLatitude(bundle.getString("EventLatitude"));
            eventObj.setLongitude(bundle.getString("EventLongitude"));
            eventObj.setDj_ID(bundle.getString("EventDjId"));
        }


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /**
     * Method for adding fragments to viewPager
     * based on role
     * @param viewPager    ViewPager object
     */
    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventInfoFragment(), "Event info");
        adapter.addFragment(new PlaylistUserFragment(), "Review Playlist");
        viewPager.setAdapter(adapter);

    }
    /**
     * Method for logout
     */
    public void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
