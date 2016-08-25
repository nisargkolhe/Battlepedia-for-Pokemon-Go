package me.nisarg.battlepedia;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private ListAdapter mAdapter;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private Menu menu;

    public static ArrayList<Pokemon> PokeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Helper.setContext(getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setFitsSystemWindows(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "FAB", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open(
                    "pokelist.json")));
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
                Poke.setNdex(jsonObj.getInt("num"));
                Poke.setCpMultiplier(jsonObj.getString("cpMultiplier"));
                Poke.setMaxTotalCP(jsonObj.getInt("maxTotalCP"));
                Poke.setMaxTotalHP(jsonObj.getInt("maxTotalHP"));
                Poke.setHpBase(jsonObj.getInt("hpBase"));
                Poke.setStamina(jsonObj.getInt("stamina"));
                Poke.setAttack(jsonObj.getInt("attack"));
                Poke.setDefence(jsonObj.getInt("defence"));
                Poke.setBasicAttack(jsonObj.getString("basicAttack"));
                Poke.setSpecialAttack(jsonObj.getString("specialAttack"));
                Poke.setType1(jsonObj.getString("type1"));
                Poke.setType2(jsonObj.getString("type2"));
                PokeList.add(Poke);

            }

        } catch (JSONException e) {
            Log.e("me.nisarg.battlepedia", e.toString());
            e.printStackTrace();
        }

        mAdapter = new ListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mStaggeredLayoutManager.setSpanCount(2);
        mAdapter.setOnItemClickListener(onItemClickListener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        SubMenu topChannelMenu = navigationView.getMenu().addSubMenu("Types");
        final String[] types = {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fire", "Water", "Grass", "Electric", "Psychic", "Ice", "Dragon", "Dark", "Fairy"};
        for (String type : types) {
            topChannelMenu.add(type).setIcon(getApplicationContext().getResources().getIdentifier(type.toLowerCase() + "icon", "drawable", getApplicationContext().getPackageName()));
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setEnabled(true);
                mDrawerLayout.closeDrawers();
                if (menuItem.getTitle().toString().equals("Alphabetic")) {
                    mAdapter.reset();
                } else if (menuItem.getTitle().toString().equals("Ndex")) {
                    mAdapter.sortByNdex();
                } else if (Arrays.asList(types).contains(menuItem.getTitle().toString())) {
                    mAdapter.filterByType(menuItem.getTitle().toString());
                }
                return true;
            }
        });
    }

    ListAdapter.OnItemClickListener onItemClickListener = new ListAdapter.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(View v, int position) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(DetailActivity.EXTRA_PARAM_ID, "" + position);
            intent.putExtras(bundle);

            ImageView bgImage = (ImageView) v.findViewById(R.id.bgImg);
            ImageView pokeImage = (ImageView) v.findViewById(R.id.pokeImg);
            FrameLayout bg = (FrameLayout) v.findViewById(R.id.frame);
            TextView ndex = (TextView) v.findViewById(R.id.ndex);

            View navigationBar = findViewById(android.R.id.navigationBarBackground);
            View statusBar = findViewById(android.R.id.statusBarBackground);

            LinearLayout placeNameHolder = (LinearLayout) v.findViewById(R.id.placeNameHolder);

            Pair<View, String> imagePair = Pair.create((View) pokeImage, "tPokeImage");
            Pair<View, String> bgPair = Pair.create((View) bg, "tBg");
            Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");
            Pair<View, String> ndexPair = Pair.create((View) ndex, "tNdex");
            Pair<View, String> toolbarPair = Pair.create((View) toolbar, "tActionBar");


            Pair<View, String> navPair = Pair.create(navigationBar,
                    Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
            /*Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);*/
            ActivityOptionsCompat options;
            if (hasNavBar(getApplicationContext())) {
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair, navPair, ndexPair, toolbarPair, holderPair);
            } else {
                options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair, ndexPair, toolbarPair, holderPair);
            }

            ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());
        }

    };

    public boolean hasNavBar(Context context) {
        Point realSize = new Point();
        Point screenSize = new Point();
        boolean hasNavBar = false;
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        realSize.x = metrics.widthPixels;
        realSize.y = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        if (realSize.y != screenSize.y) {
            int difference = realSize.y - screenSize.y;
            int navBarHeight = 0;
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                navBarHeight = resources.getDimensionPixelSize(resourceId);
            }
            if (navBarHeight != 0) {
                if (difference == navBarHeight) {
                    hasNavBar = true;
                }
            }

        }
        return hasNavBar;

    }

    private void setUpActionBar() {
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getSupportActionBar().setElevation(7);
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.searchPokemon);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.searchPokemon).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }*/
                mAdapter.filter(query);
                myActionMenuItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.filter(s);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.toggle_view) {
            int count = mStaggeredLayoutManager.getSpanCount();
            int fCount = 2;
            switch (count) {
                case 1:
                    menu.getItem(0).setIcon(R.drawable.ic_view_module_white_24dp);
                    break;
                case 2:
                    fCount = 3;
                    menu.getItem(0).setIcon(R.drawable.ic_view_column_white_24dp);
                    break;
                case 3:
                    menu.getItem(0).setIcon(R.drawable.ic_view_stream_white_24dp);
                    fCount = 1;

            }
            mStaggeredLayoutManager.setSpanCount(fCount);
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
