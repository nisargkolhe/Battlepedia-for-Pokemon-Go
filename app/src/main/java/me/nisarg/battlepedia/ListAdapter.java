package me.nisarg.battlepedia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by nisarg on 20/7/16.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;
    private ArrayList<Pokemon> itemsCopy = new ArrayList<Pokemon>();

    public ListAdapter(Context context) {
        this.mContext = context;
        itemsCopy.addAll(MainActivity.PokeList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Pokemon kmon;

        kmon = MainActivity.PokeList.get(position);

        holder.pokemonName.setText(kmon.name);
        holder.ndex.setText("#"+kmon.ndex);
        String color = kmon.type1.toLowerCase();
        int img = mContext.getResources().getIdentifier("n"+kmon.ndex+".gif", "drawable", mContext.getPackageName());
        holder.pokemonNameHolder.setBackgroundColor(mContext.getResources().getIdentifier(color, "color", mContext.getPackageName()));

        Picasso.with(mContext).load("android.resource://" + mContext.getPackageName() + "/drawable/n"+kmon.ndex).into(holder.pokeImg);
        Picasso.with(mContext).load(mContext.getResources().getIdentifier(kmon.type1.toLowerCase(), "drawable", mContext.getPackageName())).into(holder.bgImg);

    }

    @Override
    public int getItemCount() {
        return MainActivity.PokeList.size();
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
            if (mItemClickListener != null) mItemClickListener.onItemClick(itemView,getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public Pokemon removeItem(int position) {
        final Pokemon model = MainActivity.PokeList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Pokemon model) {
        MainActivity.PokeList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Pokemon model = MainActivity.PokeList.remove(fromPosition);
        MainActivity.PokeList.add(toPosition, model);
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
            final int fromPosition = MainActivity.PokeList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }


    private void applyAndAnimateAdditions(ArrayList<Pokemon> models) {
        for (int i = 0, count = models.size(); i < count; i++) {
            final Pokemon model = models.get(i);
            if (!MainActivity.PokeList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateRemovals(ArrayList<Pokemon> models) {
        for (int i = MainActivity.PokeList.size() - 1; i >= 0; i--) {
            final Pokemon model = MainActivity.PokeList.get(i);
            if (!models.contains(model)) {
                removeItem(i);
            }
        }
    }


    public void filter(String text) {

        
        if(text.isEmpty()){
            MainActivity.PokeList.clear();
            MainActivity.PokeList.addAll(itemsCopy);
        } else{
            ArrayList<Pokemon> result = new ArrayList<>();
            text = text.toLowerCase();
            for(Pokemon item: itemsCopy){
                if(item.name.toLowerCase().contains(text)){
                    result.add(item);
                }
            }
            MainActivity.PokeList.clear();
            MainActivity.PokeList.addAll(result);
        }
        notifyDataSetChanged();
    }
}
