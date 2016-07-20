package me.nisarg.battlepedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static me.nisarg.battlepedia.R.id.bgImg;

/**
 * Created by nisarg on 20/7/16.
 */

public class DetailActivity extends AppCompatActivity {
    public static String EXTRA_PARAM_ID;

    private Pokemon pokedet;

    private TextView txtName;
    private ImageView bgImage;
    private ImageView pokeImage;
    private TextView txtNdex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        pokedet = MainActivity.PokeList.get(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

        bgImage = (ImageView) findViewById(bgImg);
        txtName = (TextView) findViewById(R.id.textView);
        txtNdex = (TextView) findViewById(R.id.ndex);
        pokeImage = (ImageView) findViewById(R.id.pokeImg);


        loadDetails();
    }

    private void loadDetails(){
        txtName.setText(pokedet.name);
        txtNdex.setText("#"+pokedet.ndex);
        Picasso.with(getApplicationContext()).load(pokedet.imageName).into(pokeImage);
        Picasso.with(getApplicationContext()).load(getApplicationContext().getResources().getIdentifier(pokedet.type1.toLowerCase(), "drawable", getApplicationContext().getPackageName())).into(bgImage);
    }
}
