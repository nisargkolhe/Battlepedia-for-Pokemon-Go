package me.nisarg.battlepedia;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private ListAdapter mAdapter;
    private Toolbar toolbar;


    public static ArrayList<Pokemon> PokeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getSupportActionBar().setElevation(7);
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        initPokelist();


        mAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mStaggeredLayoutManager.setSpanCount(2);
        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    ListAdapter.OnItemClickListener onItemClickListener = new ListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_PARAM_ID, position);

            ImageView bgImage = (ImageView) v.findViewById(R.id.bgImg);
            ImageView pokeImage = (ImageView) v.findViewById(R.id.pokeImg);
            FrameLayout bg = (FrameLayout) v.findViewById(R.id.frame);
            TextView ndex = (TextView) v.findViewById(R.id.ndex);

            View navigationBar = findViewById(android.R.id.navigationBarBackground);
            View statusBar = findViewById(android.R.id.statusBarBackground);

            LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.placeNameHolder);

            Pair<View, String> imagePair = Pair.create((View) pokeImage, "tPokeImage");
            Pair<View, String> bgPair = Pair.create((View) bgImage, "tBgImage");
            Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");
            Pair<View, String> ndexPair = Pair.create((View) ndex, "tNdex");


            Pair<View, String> navPair = Pair.create(navigationBar,
                    Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> toolbarPair = Pair.create((View)toolbar, "tActionBar");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                    imagePair, bgPair, holderPair, ndexPair, navPair, statusPair);
            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
        }

    };

    private void initPokelist(){
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
                    "pokelist.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            Log.e("me.nisarg.battlepedia",e.toString());
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                Log.e("me.nisarg.battlepedia",e.toString());
            }
        }
        String myjsonstring = sb.toString();
        // Try to parse JSON
        try {
            // Creating JSONObject from String
            JSONObject jsonObjMain = new JSONObject(myjsonstring);

            // Creating JSONArray from JSONObject
            JSONArray jsonArray = jsonObjMain.getJSONArray("Pokemons");

            // JSONArray has x JSONObject
            for (int i = 0; i < jsonArray.length(); i++) {

                // Creating JSONObject from JSONArray
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Pokemon Poke = new Pokemon();

                // Getting data from individual JSONObject
                Poke.name = jsonObj.getString("name");
                Poke.ndex = jsonObj.getInt("num");
                Poke.cpMultiplier = jsonObj.getInt("cpMultiplier");
                Poke.maxTotalCP = jsonObj.getInt("maxTotalCP");
                Poke.maxTotalHP = jsonObj.getInt("maxTotalHP");
                Poke.stamina = jsonObj.getInt("stamina");
                Poke.attack = jsonObj.getInt("attack");
                Poke.defence = jsonObj.getInt("defence");
                Poke.basicAttack = jsonObj.getString("basicAttack");
                Poke.specialAttack = jsonObj.getString("specialAttack");
                Poke.type1 = jsonObj.getString("type1");
                Poke.type2 = jsonObj.getString("type2");
                Poke.imageName = jsonObj.getString("img");
                PokeList.add(Poke);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    
    
}
