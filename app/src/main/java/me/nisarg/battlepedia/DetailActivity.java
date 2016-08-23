package me.nisarg.battlepedia;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Transition;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.AnimateGifMode;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static me.nisarg.battlepedia.MainActivity.PokeList;
import static me.nisarg.battlepedia.R.id.bgImg;

/**
 * Created by nisarg on 20/7/16.
 */

public class DetailActivity extends Activity {
    public static String EXTRA_PARAM_ID;
    private Context mContext;

    private Pokemon pokedet;

    private TextView txtName;
    private ImageView bgImage;
    private ImageView pokeImage;
    private TextView txtNdex;
    private TextView type1;
    private LinearLayout type1box;
    private TextView type2;
    private LinearLayout type2box;
    private LinearLayout pokeNameHolder;
    private MovesGridView quickMovesGrid;
    private MovesGridView chargeMovesGrid;
    private EditText calcCP;
    private TextView cpResult;
    private TextView txtCp;
    private TextView baseHP;
    private TextView maxTotalHP;
    private TextView maxTotalCP;
    private TextView attack;
    private TextView stamina;
    private TextView defence;
    private TextView weaknesses;
    private TextView advantages;
    private FrameLayout cardFrame;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setMinimumHeight(0);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        int pos  = Integer.parseInt(bundle.getString(EXTRA_PARAM_ID));
        pokedet = PokeList.get(pos);


        cardFrame = (FrameLayout) findViewById(R.id.frame);
        bgImage = (ImageView) findViewById(bgImg);
        txtName = (TextView) findViewById(R.id.textView);
        txtNdex = (TextView) findViewById(R.id.ndex);
        pokeNameHolder = (LinearLayout) findViewById(R.id.placeNameHolder);
        pokeImage = (ImageView) findViewById(R.id.pokeImg);

        type1 = (TextView) findViewById(R.id.type1);
        type1box = (LinearLayout) findViewById(R.id.type1box);
        type2 = (TextView) findViewById(R.id.type2);
        type2box = (LinearLayout) findViewById(R.id.type2box);

        baseHP = (TextView) findViewById(R.id.baseHP);
        maxTotalCP = (TextView) findViewById(R.id.maxTotalCP);
        maxTotalHP = (TextView) findViewById(R.id.maxTotalHP);
        attack = (TextView) findViewById(R.id.attack);
        defence = (TextView) findViewById(R.id.defence);
        stamina = (TextView) findViewById(R.id.stamina);

        quickMovesGrid = (MovesGridView) findViewById(R.id.quickGrid);
        chargeMovesGrid = (MovesGridView) findViewById(R.id.chargeGrid);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;

        if (density >= DisplayMetrics.DENSITY_XHIGH) {
            quickMovesGrid.setNumColumns(2);
            chargeMovesGrid.setNumColumns(2);
        }



        weaknesses = (TextView) findViewById(R.id.weakAgainst);
        advantages = (TextView) findViewById(R.id.advOver);

        List<String> quickMoves = Arrays.asList(pokedet.basicAttack.split("\\s*,\\s*"));
        List<String> chargeMoves = Arrays.asList(pokedet.specialAttack.split("\\s*,\\s*"));

        quickMovesGrid.setAdapter(new MovesAdapter(this, quickMoves));
        chargeMovesGrid.setAdapter(new MovesAdapter(this, chargeMoves));
        quickMovesGrid.setExpanded(true);
        chargeMovesGrid.setExpanded(true);

        txtCp = (TextView) findViewById(R.id.txtCp);
        LinearLayout multiplierBox = (LinearLayout) findViewById(R.id.multiplier);
        TextView labelCP = (TextView) findViewById(R.id.cpLabel);
        cpResult = (TextView) findViewById(R.id.txtResult);
        calcCP = (EditText) findViewById(R.id.calc);
        if (pokedet.cpMultiplier.length() < 1) {
            calcCP.setEnabled(false);
            multiplierBox.setVisibility(View.GONE);
            labelCP.setVisibility(View.GONE);
        }
        if (pokedet.ndex == 133) {
            TextView msg = (TextView) findViewById(R.id.eeveeMsg);
            msg.setVisibility(View.VISIBLE);
        }
        calcCP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!calcCP.getText().toString().equals("")) {
                    cpResult.setText("" + Math.round(Integer.parseInt(calcCP.getText().toString()) * Double.parseDouble(pokedet.cpMultiplier)));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadDetails();
        windowTransition();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void loadDetails() {
        txtName.setText(pokedet.name);
        txtNdex.setText("#" + pokedet.ndex);
        type1.setText(pokedet.type1);
        baseHP.setText("" + pokedet.hpBase);
        maxTotalHP.setText("" + pokedet.maxTotalHP);
        maxTotalCP.setText("" + pokedet.maxTotalCP);
        attack.setText("" + pokedet.attack);
        stamina.setText("" + pokedet.stamina);
        defence.setText("" + pokedet.defence);

        HashMap<String, String> type1stats = getTypeStats(pokedet.type1.toUpperCase());
        HashMap<String, String> type2stats = getTypeStats(pokedet.type2.toUpperCase());
        ArrayList<String> adv = new ArrayList<String>();
        ArrayList<String> weak = new ArrayList<String>();


        for (String key : type1stats.keySet()) {
            if (Double.parseDouble(type1stats.get(key)) < 1) {
                if (!adv.contains(key))
                    adv.add(key);
            } else if (Double.parseDouble(type1stats.get(key)) > 1) {
                if (!weak.contains(key))
                    weak.add(key);
            }
        }

        for (String key : type2stats.keySet()) {
            if (Double.parseDouble(type2stats.get(key)) < 1) {
                if (!adv.contains(key))
                    adv.add(key);
            } else if (Double.parseDouble(type2stats.get(key)) > 1) {
                if (!weak.contains(key))
                    weak.add(key);
            }
        }

        advantages.setText(TextUtils.join("\n", adv));
        weaknesses.setText(TextUtils.join("\n", weak));

        if (!pokedet.cpMultiplier.equals("")) {
            txtCp.setText(pokedet.cpMultiplier);
        }
        int type1color = getResources().getIdentifier(pokedet.type1.toLowerCase(), "color", getPackageName());
        int type2color = getResources().getIdentifier(pokedet.type2.toLowerCase(), "color", getPackageName());
        pokeNameHolder.setBackgroundColor(getResources().getIdentifier(pokedet.type1.toLowerCase(), "color", getPackageName()));

        type1box.setBackgroundColor(getResources().getColor(type1color));
        type2box.setBackgroundColor(getResources().getColor(type2color));
        //type2box.setBackgroundColor(mContext.getResources().getIdentifier(pokedet.type2.toLowerCase(), "color", mContext.getPackageName()));


        if (pokedet.type1.equals(pokedet.type2)) {
            type2box.setVisibility(View.GONE);
        } else {
            type2.setText(pokedet.type2);
        }

        //Picasso.with(getApplicationContext()).load(pokedet.imageName).into(pokeImage);
        //Glide.with(getApplicationContext()).load("android.resource://" + mContext.getPackageName() + "/drawable/n"+pokedet.ndex).asBitmap().into(pokeImage);
        Ion.with(pokeImage).resize(200, 200).load("android.resource://" + mContext.getPackageName() + "/drawable/m" + pokedet.ndex);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Ion.with(pokeImage).animateGif(AnimateGifMode.ANIMATE).load("android.resource://" + mContext.getPackageName() + "/drawable/n" + pokedet.ndex);
            }
        }, 1500);
        //Ion.with(bgImage).load("android.resource://" + mContext.getPackageName() + "/drawable/"+pokedet.type1.toLowerCase());
        Picasso.with(getApplicationContext())
                .load("android.resource://" + mContext.getPackageName() + "/drawable/" + pokedet.type1.toLowerCase())
                .fit()
                .into(bgImage);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {mContext.getResources().getColor(mContext.getResources().getIdentifier(pokedet.type1.toLowerCase(), "color", mContext.getPackageName())),mContext.getResources().getIdentifier(pokedet.type1.toLowerCase(), "color", mContext.getPackageName())});
        gd.setCornerRadius(0f);
        cardFrame.setBackground(gd);
    }

    private HashMap getTypeStats(String type) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        HashMap typeData = new HashMap();
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
                    "typeadv.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            Log.e("me.nisarg.battlepedia", e.toString());
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                Log.e("me.nisarg.battlepedia", e.toString());
            }
        }
        String myjsonstring = sb.toString();

        try {
            JSONObject jsonObjMain = new JSONObject(myjsonstring);
            JSONArray jsonArray = jsonObjMain.getJSONArray(type);
            //for (int i = 0; i < jsonArray.length(); i++) {
            //}
            typeData = new Gson().fromJson(jsonArray.get(0).toString(), new TypeToken<HashMap<String, String>>() {
            }.getType());

        } catch (JSONException e) {
            Log.e("me.nisarg.battlepedia", e.toString());
            e.printStackTrace();
        }
        return typeData;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(makeEnterTransition());

            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    getWindow().getEnterTransition().removeListener(this);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
    }

    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    public static Transition makeExitTransition() {
        Transition fade = new Fade();
        fade.addTarget(bgImg);
        return fade;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Detail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }

    @Override
    public void onBackPressed(){
        bgImage.animate().alpha(0.0f);
        super.onBackPressed();
    }
}
