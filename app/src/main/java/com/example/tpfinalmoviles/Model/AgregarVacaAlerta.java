package com.example.tpfinalmoviles.Model;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpfinalmoviles.R;
import com.example.tpfinalmoviles.Utils.ToastHandler;
import com.example.tpfinalmoviles.io.CowApiAdapter;
import com.example.tpfinalmoviles.io.Response.VacaAlerta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AgregarVacaAlerta extends AppCompatActivity {
    private static  String ERROR_POST = "Error al cargar alerta.";
    private static  String ERROR_CONECTION = "Error de conexión.";
    private static  String CORRECT_POST = "Alerta Vaca cargada con exito.";

    private EditText etIdVaca, etBCSmax,etBCSmin;
    private TextView etInfo;
    private Button bCargar, bBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vaca_alerta);
        bCargar = (Button) findViewById(R.id.bCargar);
        bBack = (Button) findViewById(R.id.bBack);
        etInfo = findViewById(R.id.etInfo);
        etIdVaca = (EditText)findViewById(R.id.etIdVaca);
        etBCSmax = (EditText)findViewById(R.id.etBCSmax);
        etBCSmin = (EditText)findViewById(R.id.etBCSmin);

        //No es necesario controlar que cargue algo en los campos, restricciones ya sobre base de datos.
        bCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(esValido(etIdVaca) && esValido (etBCSmin) && esValido(etBCSmin)) {
                    bCargar.setText("Enviando Datos");
                    bCargar.setEnabled(false);
                    agregarVacaAlerta();
                }else
                    ToastHandler.get().showToast(getApplicationContext(), ERROR_POST, Toast.LENGTH_SHORT);
            }
        });

        bBack.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean esValido(EditText editText) {
        if (editText.getText().toString().length()>0)
            return true;
        return false;
    }

    private void agregarVacaAlerta() {
        int idVaca = Integer.parseInt(etIdVaca.getText().toString());
        double maxBCS = Double.parseDouble(etBCSmax.getText().toString());
        double minBCS = Double.parseDouble((etBCSmin.getText().toString()));

        VacaAlerta vacaAlerta = new VacaAlerta(idVaca,maxBCS,minBCS);
        Call<VacaAlerta> call = CowApiAdapter.getApiService().agregarVacaAlerta(vacaAlerta);
        call.enqueue(new Callback<VacaAlerta>() {
            @Override
            public void onResponse(Call<VacaAlerta> call, Response<VacaAlerta> response) {
                if (!response.isSuccessful()) {
                    bCargar.setText("Cargar Alerta");
                    bCargar.setEnabled(true);
                    ToastHandler.get().showToast(getApplicationContext(), ERROR_POST, Toast.LENGTH_SHORT);
                    return;
                }
                VacaAlerta vacaResponseAlerta = response.body();
                etInfo.setText("Id Vaca Alerta: " + String.valueOf(vacaResponseAlerta.getId()));
                bCargar.setText("Cargar Alerta");
                bCargar.setEnabled(true);
                ToastHandler.get().showToast(getApplicationContext(), CORRECT_POST, Toast.LENGTH_SHORT);
            }
            @Override
            public void onFailure(Call<VacaAlerta> call, Throwable t) {
                bCargar.setText("Cargar Alerta");
                bCargar.setEnabled(true);
                ToastHandler.get().showToast(getApplicationContext(), ERROR_CONECTION, Toast.LENGTH_SHORT);
            }
        });
    }
}
