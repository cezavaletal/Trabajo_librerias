package com.deathstudio.marcos.demoooooo;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deathstudio.marcos.demoooooo.adaptador.PokemonAdapter;
import com.deathstudio.marcos.demoooooo.configuracion.Configuracion;
import com.deathstudio.marcos.demoooooo.pojo.Pokemon;
import com.deathstudio.marcos.demoooooo.pojo.PokemonRespuesta;
import com.deathstudio.marcos.demoooooo.retrofitservice.PokemonService;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class  MainActivity extends AppCompatActivity{

    private Retrofit retrofit;
    private PokemonAdapter pokemonAdapter;
    private int offset;//a partir de que numero para adelante lo vamoss a obtener
    private boolean listoParaCargar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pokemonAdapter = new PokemonAdapter(this);
        recyclerView.setAdapter(pokemonAdapter);
        recyclerView.setHasFixedSize(true);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();//vistas secundarias al recycler view
                    int totalItemCount = layoutManager.getItemCount();//total de la lista
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();//Encuentra la primera posicion del item visible

                    if (listoParaCargar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(Configuracion.TAG, "Es el momento de pedir mas :v");
                            listoParaCargar = false;
                            offset += 20;//le sumamos 20
                            obtenerDatos(offset);//hacemos una nueva consulta
                        }
                    }
                }
            }
        });


        retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())//convertirmos el json a objecto
                .build();

        listoParaCargar = true;
        offset = 0;
        obtenerDatos(offset);
    }

    private void obtenerDatos(int offset){
        PokemonService service = retrofit.create(PokemonService.class);
        Call<PokemonRespuesta> pokemonRespuestaCall = service.obtenerListaPokemon(20, offset);
        pokemonRespuestaCall.enqueue(new Callback<PokemonRespuesta>() {
            @Override
            public void onResponse(Call<PokemonRespuesta> call, Response<PokemonRespuesta> response) {
                listoParaCargar = true;
                progressBar.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    PokemonRespuesta pokemonRespuesta = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonRespuesta.getResults();
                    pokemonAdapter.agregarPokemon(listaPokemon);

                    /*******************************************************************************
                     * Probando Logger
                     *******************************************************************************/

                        FormatStrategy formatStrategy =  PrettyFormatStrategy.newBuilder().tag(Configuracion.MENSAJE_JSON).build();
                        Logger.addLogAdapter ( new AndroidLogAdapter(formatStrategy));

                        for(int i=0; i<listaPokemon.size();i++){
                            Pokemon pokemon = listaPokemon.get(i);
                            Logger.i(pokemon.getNumber() +"-)"+" Nombre: "+pokemon.getName()+" url: "+pokemon.getUrl());
                        }

                        Logger.i(new Gson().toJson(pokemonRespuesta));//objeto a json

                } else {
                    Log.e(Configuracion.TAG, " onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRespuesta> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                listoParaCargar = true;
                Toast.makeText(getApplicationContext(),"Revisa tu conexión a internet >:v",Toast.LENGTH_LONG).show();
                Log.e(Configuracion.TAG, " onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
