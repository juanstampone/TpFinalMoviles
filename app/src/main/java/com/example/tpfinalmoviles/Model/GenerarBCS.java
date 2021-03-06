package com.example.tpfinalmoviles.Model;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinalmoviles.R;
import com.example.tpfinalmoviles.io.CowApiAdapter;
import com.example.tpfinalmoviles.io.Response.Sesion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerarBCS extends AppCompatActivity {

    private final String ESTADO_SWITCH = "Estado de switch";

    private Switch simpleSwitch;
    private Button bRegrasar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_bcs);
        simpleSwitch = (Switch) findViewById(R.id.simpleSwitch);
        bRegrasar = (Button) findViewById(R.id.bBack);

        SharedPreferences sharedPreferences = getSharedPreferences(ESTADO_SWITCH,MODE_PRIVATE);
        boolean estado = sharedPreferences.getBoolean(ESTADO_SWITCH,false);
        if (estado){
            simpleSwitch.setChecked(estado);
        }
        bRegrasar.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               saveSharedPreference();
               finish();
            }
        });

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                generarBCS(simpleSwitch.isChecked());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSharedPreference();
        finish();
    }

    private void saveSharedPreference(){
        SharedPreferences sharedPreferences = getSharedPreferences(ESTADO_SWITCH,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ESTADO_SWITCH,simpleSwitch.isChecked());
        editor.commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("sesion", simpleSwitch.isChecked());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        generarBCS(savedInstanceState.getBoolean("sesion"));
    }

    private void generarBCS(boolean checked) {
        Sesion sesion = new Sesion(Boolean.toString(checked));
        Call<Sesion> call = CowApiAdapter.getApiService().generarSesion(sesion);
        call.enqueue(new Callback<Sesion>() {
            @Override
            public void onResponse(Call<Sesion> call, Response<Sesion> response) {
                if (!response.isSuccessful()) {
                    return;
                }
            }

            @Override
            public void onFailure(Call<Sesion> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
