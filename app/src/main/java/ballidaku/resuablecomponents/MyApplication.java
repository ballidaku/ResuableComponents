package ballidaku.resuablecomponents;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by brst-pc93 on 4/26/17.
 */

public class MyApplication extends Application
{

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "LcOqrONkCy7IeiShwpuhOnY5O";
    private static final String TWITTER_SECRET = "eI9dVJHqczk5GHiWvfgIIqgjocABUtV3jYquvTTZt46cMjvfAX";

    @Override
    public void onCreate()
    {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("myrealm.realm")
                //.encryptionKey(getKey())
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                //.modules(new MySchemaModule())
                //.migration(new MyMigration())
                .build();


        Realm.setDefaultConfiguration(config);
    }
}
