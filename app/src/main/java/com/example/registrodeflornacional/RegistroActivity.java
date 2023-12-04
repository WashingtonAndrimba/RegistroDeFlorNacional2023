package com.example.registrodeflornacional;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistroActivity extends AppCompatActivity {

    private DatePicker etdFecha;
    private EditText etCantidadRegistro;
    private Spinner spFinca, spArea, spBloque, spVariedad, spEnfermedad;
    private Button btnGuardar;
    private RequestQueue requestQueue;

    private static final String URL_INSERTAR_DATOS = "http://192.168.1.7/registroFlorNacional/agregarinformacion.php";
    private static final String URL_OBTENER_VARIEDADES = "http://192.168.1.7/registroFlorNacional/recuperar_informacion.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        etdFecha = findViewById(R.id.etdFecha);
        spFinca = findViewById(R.id.spFinca);
        spArea = findViewById(R.id.spArea);
        spBloque = findViewById(R.id.spBloque);
        spVariedad = findViewById(R.id.spVariedad);
        spEnfermedad = findViewById(R.id.spEnfermedad);
        etCantidadRegistro = findViewById(R.id.etCantidadRegistro);
        btnGuardar = findViewById(R.id.btnGuardar);
        requestQueue = Volley.newRequestQueue(this);

        ArrayAdapter<String> variedadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        spVariedad.setAdapter(variedadAdapter);

        obtenerVariedadesDesdeServidor();

        String[] opciones = {"Ecuador", "Colombia", "Mexico", "Chile"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);

        spFinca.setAdapter(adapter);
        spArea.setAdapter(adapter);
        spBloque.setAdapter(adapter);
        spEnfermedad.setAdapter(adapter);



        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoConfirmacion();

            }
        });
    }

    private void obtenerVariedadesDesdeServidor() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL_OBTENER_VARIEDADES, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> variedades = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject variedad = response.getJSONObject(i);
                                String codigoVariedad = variedad.getString("VARIEDADCODIGO");
                                variedades.add(codigoVariedad);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        ArrayAdapter<String> variedadAdapter = (ArrayAdapter<String>) spVariedad.getAdapter();
                        variedadAdapter.clear();
                        variedadAdapter.addAll(variedades);
                        variedadAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void enviarDatos() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERTAR_DATOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Informacion almacenada con exito", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fecha", obtenerFechaFormateada());
                params.put("cantidad", etCantidadRegistro.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void mostrarDialogoConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro de guardar la información?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviarDatos();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private String obtenerFechaFormateada() {
        int day = etdFecha.getDayOfMonth();
        int month = etdFecha.getMonth() + 1;
        int year = etdFecha.getYear();
        return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }
}

