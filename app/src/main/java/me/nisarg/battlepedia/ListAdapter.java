package me.nisarg.battlepedia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

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
            Ion.with(holder.pokeImg).load("android.resource://" + mContext.getPackageName() + "/drawable/m"+kmon.ndex);
            Picasso.with(mContext).load("android.resource://" + mContext.getPackageName() + "/drawable/"+kmon.type1.toLowerCase()).into(holder.bgImg);
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

    public Pokemon removeItem(int position) {
        final Pokemon model = PokeList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Pokemon model) {
        PokeList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Pokemon model = PokeList.remove(fromPosition);
        PokeList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<Pokemon> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateMovedItems(ArrayList<Pokemon> models) {
        for (int toPosition = models.size() - 1; toPosition >= 0; toPosition--) {
            final Pokemon model = models.get(toPosition);
            final int fromPosition = PokeList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }


    private void applyAndAnimateAdditions(ArrayList<Pokemon> models) {
        for (int i = 0, count = models.size(); i < count; i++) {
            final Pokemon model = models.get(i);
            if (!PokeList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateRemovals(ArrayList<Pokemon> models) {
        for (int i = PokeList.size() - 1; i >= 0; i--) {
            final Pokemon model = PokeList.get(i);
            if (!models.contains(model)) {
                removeItem(i);
            }
        }
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
                    result.add(item);
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
                    result.add(item);
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
