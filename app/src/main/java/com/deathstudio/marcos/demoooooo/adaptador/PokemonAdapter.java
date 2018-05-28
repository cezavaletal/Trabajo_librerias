package com.deathstudio.marcos.demoooooo.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.deathstudio.marcos.demoooooo.R;
import com.deathstudio.marcos.demoooooo.pojo.Pokemon;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>{

    private List<Pokemon> pokemonList;
    private Context context;

    public PokemonAdapter(Context context){
        this.context = context;
        pokemonList = new ArrayList<>();
    }

    @Override
    public PokemonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent,false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonViewHolder holder, int position) {
        Pokemon p = pokemonList.get(position);
        holder.nombre.setText(p.getName());

        Glide.with(context)
                .load("http://pokeapi.co/media/sprites/pokemon/" + p.getNumber() + ".png")
                .thumbnail(Glide.with(context).load(R.drawable.placeholder_pokebola))
                //.placeholder(R.drawable.placeholder_pokebola)
                .centerCrop()
                .crossFade()
                .dontTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.foto);

    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void agregarPokemon(ArrayList<Pokemon> listaPokemon) {
        pokemonList.addAll(listaPokemon);
        notifyDataSetChanged();
    }

    public class PokemonViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.foto) ImageView foto;
        @BindView(R.id.nombre) TextView nombre;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
