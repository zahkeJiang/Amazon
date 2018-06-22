package amazon.jy.com.amazon.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jiangy on 18-4-8.
 */

public class App extends Application {
    SharedPreferences preferences;
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static App getApp() {
        return app;
    }

    public SharedPreferences getPreferences() {
        preferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return preferences;
    }
}
