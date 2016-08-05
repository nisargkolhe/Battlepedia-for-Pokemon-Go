package me.nisarg.battlepedia;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static me.nisarg.battlepedia.MainActivity.PokeList;

/**
 * Created by nisarg on 20/7/16.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    private ArrayList<Pokemon> itemsCopy = new ArrayList<Pokemon>();

    public ListAdapter(Context context) {
        this.mContext = context;
        itemsCopy.addAll(PokeList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Pokemon kmon;

        kmon = PokeList.get(position);


        holder.pokemonName.setText(kmon.name);
        holder.ndex.setText("#"+kmon.ndex);
        String color = kmon.type1.toLowerCase();
        int img = mContext.getResources().getIdentifier("n"+kmon.ndex+".gif", "drawable", mContext.getPackageName());
        holder.pokemonNameHolder.setBackgroundColor(mContext.getResources().getIdentifier(color, "color", mContext.getPackageName()));


        if(kmon.getThumb(mContext) != null){
            Ion.with(holder.pokeImg).resize(200,200).load("android.resource://" + mContext.getPackageName() + "/drawable/m"+kmon.ndex);
            //Picasso.with(mContext).load("android.resource://" + mContext.getPackageName() + "/drawable/"+kmon.type1.toLowerCase()).resize(200,200).into(holder.bgImg);
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[] {mContext.getResources().getColor(mContext.getResources().getIdentifier(kmon.type1.toLowerCase(), "color", mContext.getPackageName())),mContext.getResources().getIdentifier(kmon.type1.toLowerCase(), "color", mContext.getPackageName())});
            gd.setCornerRadius(0f);
            holder.cardFrame.setBackground(gd);
            holder.pokeImg.setVisibility(View.VISIBLE);
            holder.bgImg.setVisibility(View.VISIBLE);
        } else{
            holder.pokeImg.setVisibility(View.GONE);
            holder.bgImg.setVisibility(View.GONE);
        }
       /* Ion.with(holder.pokeImg).load("android.resource://" + mContext.getPackageName() + "/drawable/m"+kmon.ndex);
        Picasso.with(mContext).load("android.resource://" + mContext.getPackageName() + "/drawable/"+kmon.type1.toLowerCase()).into(holder.bgImg);*/


    }

    @Override
    public int getItemCount() {
        return PokeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout pokemonHolder;
        public LinearLayout pokemonNameHolder;
        public FrameLayout cardFrame;
        public TextView pokemonName;
        public TextView ndex;
        public ImageView bgImg;
        public ImageView pokeImg;

        public ViewHolder(View itemView) {
            super(itemView);
            pokemonHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            pokemonName = (TextView) itemView.findViewById(R.id.placeName);
            pokemonNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            bgImg = (ImageView) itemView.findViewById(R.id.bgImg);
            pokeImg = (ImageView) itemView.findViewById(R.id.pokeImg);
            ndex = (TextView) itemView.findViewById(R.id.ndex);
            cardFrame = (FrameLayout) itemView.findViewById(R.id.frame);
            pokemonHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView,getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public void reset(){
        PokeList.clear();
        PokeList.addAll(itemsCopy);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        if(text.isEmpty()){
            PokeList.clear();
            PokeList.addAll(itemsCopy);
        } else{
            ArrayList<Pokemon> result = new ArrayList<>();
            text = text.toLowerCase();
            for(Pokemon item: itemsCopy){
                if(item.name.toLowerCase().contains(text)){
                    if(!result.contains(item)) {
                        result.add(item);
                    }
                }
            }
            PokeList.clear();
            PokeList.addAll(result);
        }
        notifyDataSetChanged();
    }

    public void filterByType(String text) {
        if(text.isEmpty()){
            PokeList.clear();
            PokeList.addAll(itemsCopy);
        } else{
            ArrayList<Pokemon> result = new ArrayList<>();
            text = text.toLowerCase();
            for(Pokemon item: itemsCopy){
                if(item.type1.toLowerCase().contains(text) || item.type2.toLowerCase().contains(text)){
                    if(!result.contains(item)) {
                        result.add(item);
                    }
                }
            }
            PokeList.clear();
            PokeList.addAll(result);
        }
        notifyDataSetChanged();
    }

    public void sortByNdex(){
        ArrayList<Pokemon> result = new ArrayList<>(151);
        result.addAll(itemsCopy);
        Collections.sort(result, new Comparator<Pokemon>() {
            @Override
            public int compare(Pokemon pokemon, Pokemon t1) {
                return pokemon.getNdex() - t1.getNdex();
            }
        });
        MainActivity.PokeList.clear();
        MainActivity.PokeList.addAll(result);
        notifyDataSetChanged();
    }
}
