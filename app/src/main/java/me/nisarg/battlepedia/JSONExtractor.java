package me.nisarg.battlepedia;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by nisarg on 20/7/16.
 */

public class JSONExtractor extends Activity{
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("pokelist.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
