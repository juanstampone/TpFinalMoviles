package com.example.tpfinalmoviles.Model;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinalmoviles.R;
import com.example.tpfinalmoviles.Utils.CustomExpandableListAdapter;
import com.example.tpfinalmoviles.io.Response.Vaca;
import com.example.tpfinalmoviles.io.CowApiAdapter;
import com.example.tpfinalmoviles.io.Response.Rodeo;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultarRodeo extends AppCompatActivity {
    private Button bConsultarRodeo;
    private Button bRegresar;
    private ScrollView scrollView;
    private EditText idRodeo;
    private TextView idLocation,promedioBCS;
   // private Tarea tarea;
    private String sms;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListNombres;
    private HashMap<String, Vaca> listaVacas;
    private int lastExpandedPosition = -1;
    private JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_rodeo);
        bConsultarRodeo = (Button) findViewById(R.id.bConsultar);
        bRegresar = (Button) findViewById(R.id.bBack);
        scrollView = findViewById(R.id.idScrollView);
        idRodeo = findViewById(R.id.idRodeoB);
        idLocation = findViewById(R.id.etIdLocation);
        promedioBCS = findViewById(R.id.etBCSPromedio);

        bConsultarRodeo.setEnabled(false);
        //Hay que controlar que cargue algo en idRodeo, para activar boton consultar.
        idRodeo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int before, int count) {
                if (count>0){ //count es cantidad de caracteres que tiene
                    bConsultarRodeo.setEnabled(true);
                }else{
                    bConsultarRodeo.setEnabled(false);
                }
            }
        });

        bConsultarRodeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.setVisibility(View.VISIBLE);
                iniciar();
            }
        });
        bRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void iniciar() {
        final int idR = Integer.parseInt(idRodeo.getText().toString());
        Call<Rodeo> call = CowApiAdapter.getApiService().getRodeo(idR);
        call.enqueue(new Callback<Rodeo>() {
            @Override
            public void onResponse(Call<Rodeo> call, Response<Rodeo> response) {
                if (response.isSuccessful()) {
                    Rodeo rodeo = response.body();
                    System.out.println("IDE RODEO: " + rodeo.getId());
                    System.out.println("IDE LOCATION: " + rodeo.getLocation());
                    System.out.println("IDE COWS: " + rodeo.getCows().size());
                    idLocation.setText(String.valueOf(rodeo.getId()));
                    promedioBCS.setText(String.valueOf(rodeo.getBcsPromedio()));
                    Type tipoListaVaca = new TypeToken<List<Vaca>>(){}.getType();
                    init(rodeo.getCows());
                    expandableListView.setAdapter(expandableListAdapter);
                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                            System.out.println("ENTROOO");
                            if(lastExpandedPosition != -1 && groupPosition != lastExpandedPosition){
                                expandableListView.collapseGroup(lastExpandedPosition);
                            }
                            lastExpandedPosition = groupPosition;
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<Rodeo> call, Throwable t) {
                System.out.println("FALAA");
            }
        });
    }

    private void init(List<Vaca> vacas) {
        this.expandableListView = findViewById(R.id.elvList);
        this.listaVacas = getVacas(vacas);
        this.expandableListNombres = new ArrayList<>(listaVacas.keySet());
        this.expandableListAdapter = new CustomExpandableListAdapter(this,
                expandableListNombres, listaVacas);

    }

    private HashMap<String, Vaca> getVacas(List<Vaca> vacas) {
        HashMap<String, Vaca> listaV = new HashMap<>();
        System.out.println("SIZE " + vacas.size());
        for (Vaca v:vacas){
            listaV.put("Vaca "+ v.getId(), v);
        }
        System.out.println("SIZE2 " + listaV.size());
        return listaV;
    }


}
