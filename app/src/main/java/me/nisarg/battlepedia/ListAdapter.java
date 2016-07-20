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

/**
 * Created by nisarg on 20/7/16.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context mContext;
    OnItemClickListener mItemClickListener;


    // 2
    public ListAdapter(Context context) {
        this.mContext = context;
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


        Picasso.with(mContext).load(kmon.imageName).into(holder.pokeImg);
        Picasso.with(mContext).load(mContext.getResources().getIdentifier(kmon.type1.toLowerCase(), "drawable", mContext.getPackageName())).into(holder.bgImg);
        /*Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(),mContext.getResources().getIdentifier(kmon.type1.toLowerCase(), "drawable", mContext.getPackageName()));

        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int bgColor = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                holder.pokemonNameHolder.setBackgroundColor(bgColor);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return MainActivity.PokeList.size();
    }

    // 3
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
}
