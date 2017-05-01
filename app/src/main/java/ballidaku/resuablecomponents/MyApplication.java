package ballidaku.resuablecomponents;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by brst-pc93 on 4/26/17.
 */

public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

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
