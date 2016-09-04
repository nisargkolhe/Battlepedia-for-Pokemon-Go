package me.nisarg.battlepedia;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nisarg on 22/7/16.
 */

public class MovesAdapter extends BaseAdapter {

    private List<String> pokeMoves;
    private Context context;
    private static LayoutInflater inflater = null;
    private ArrayList<Move> movesArrayList = new ArrayList<Move>();
    private String type1;
    private String type2;


    public MovesAdapter(DetailActivity mainActivity, List<String> pokeMoves, ArrayList<Move> movesArrayList, String type1, String type2) {
        this.pokeMoves = pokeMoves;
        this.context = mainActivity;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.type1 = type1.toLowerCase();
        this.type2 = type2.toLowerCase();
        this.movesArrayList = movesArrayList;
    }

    @Override
    public int getCount() {
        return pokeMoves.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class Holder {
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
        holder.move = (TextView) rowView.findViewById(R.id.move);
        holder.dps = (TextView) rowView.findViewById(R.id.dps);
        holder.type = (ImageView) rowView.findViewById(R.id.moveType);
        holder.card = (CardView) rowView.findViewById(R.id.moveCard);
        //loadMoves();

        Move move = getMove(pokeMoves.get(i).toUpperCase(), movesArrayList);

        //Check if STAB
        if(move.getType().equals(type1) || move.getType().equals(type2)){
            holder.move.setText(pokeMoves.get(i)+"*");
            holder.move.setTypeface(holder.move.getTypeface(),Typeface.ITALIC);
            String dps = "" + Math.round((Double.parseDouble(move.getDps()) * 125))/100.0;
            holder.dps.setText(dps);
        }
        else{
            holder.move.setText(pokeMoves.get(i));
            holder.dps.setText(move.getDps());
        }


        try {
            int moveColor = context.getResources().getIdentifier(move.getType(), "color", context.getPackageName());
            holder.card.setCardBackgroundColor(context.getResources().getColor(moveColor));
        } catch (Exception e) {
           Log.e("me.nisarg.battlepedia", "bg color didn't load " + e.toString());
        }

        try {
            Picasso.with(context).load(context.getResources().getIdentifier(move.getType() + "icon", "drawable", context.getPackageName())).into(holder.type);
        } catch (Exception e) {
            Log.e("me.nisarg.battlepedia", "type icon didn't load " + e.toString());
        }

        return rowView;

    }

    public static Move getMove(String attack, ArrayList<Move> moves){
        for(Move m : moves){
            //Log.e("me.nisarg.battlepedia",m.getAttack());
            if(m.getAttack().contains(attack.toUpperCase())){
                return m;
            }
        }
        return null;
    }
}
