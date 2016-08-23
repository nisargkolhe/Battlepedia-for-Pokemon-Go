package me.nisarg.battlepedia;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nisarg on 22/7/16.
 */

public class MovesAdapter extends BaseAdapter {

    List<String> result;
    Context context;
    private static LayoutInflater inflater=null;
    public HashMap movesList = new HashMap();
    private HashMap<String,String> dpsList = new HashMap<String,String> ();


    public MovesAdapter(DetailActivity mainActivity, List<String> moves) {
        result=moves;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder
    {
        TextView move;
        CardView card;
        ImageView type;
        TextView dps;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.move_single, null);
        holder.move=(TextView) rowView.findViewById(R.id.move);
        holder.dps=(TextView) rowView.findViewById(R.id.dps);
        holder.type=(ImageView) rowView.findViewById(R.id.moveType);
        holder.card = (CardView) rowView.findViewById(R.id.moveCard);

        holder.move.setText(result.get(i));
        holder.dps.setText(dpsList.get(result.get(i)));
        loadMoves();
        try {
            int moveColor = context.getResources().getIdentifier(movesList.get(result.get(i)).toString().toLowerCase(), "color", context.getPackageName());
            holder.card.setCardBackgroundColor(context.getResources().getColor(moveColor));
        } catch (Exception e){
            Log.e("me.nisarg.battlepedia","bg color didn't load "+e.toString());
        }

        try{
            Picasso.with(context).load(context.getResources().getIdentifier(movesList.get(result.get(i)).toString().toLowerCase()+"icon", "drawable", context.getPackageName())).into(holder.type);
        } catch (Exception e){
            Log.e("me.nisarg.battlepedia","type icon didn't load "+e.toString());
        }

        return rowView;

    }

    public void loadMoves(){
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.getAssets().open(
                    "movesets.json")));
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
            JSONArray jsonArray = jsonObjMain.getJSONArray("Movesets");

            // JSONArray has x JSONObject
            for (int i = 0; i < jsonArray.length(); i++) {

                // Creating JSONObject from JSONArray
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                movesList.put(jsonObj.getString("attack"),jsonObj.getString("type"));
                dpsList.put(jsonObj.getString("attack"),jsonObj.getString("dps"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e("me.nisarg.battlepedia",e.toString());
            e.printStackTrace();
        }
    }
}
