package com.example.registrodeflornacional;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class OpcionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opciones);

        Button btnRegistro = findViewById(R.id.btnRegistro);
        Button btnConsultarActualizar = findViewById(R.id.btnConsultarActualizar);
        Button btnReporte = findViewById(R.id.btnReporte);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPantalla("RegistroActivity");
            }
        });

        btnConsultarActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPantalla("ConsultaActivity");
            }
        });

        btnReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirPantalla("ReporteActivity");
            }
        });
    }

    private void abrirPantalla(String nombrePantalla) {
        Class<?> clase;
        try {

            clase = Class.forName("com.example.registrodeflornacional." + nombrePantalla);

            Intent intent = new Intent(this, clase);

            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
