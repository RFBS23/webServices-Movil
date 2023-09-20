package com.fabridev.webservices;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class editar extends AppCompatActivity {

    EditText apellidosR, nombresR, telefonoR, emailR, direccionR;
    Button btneditar, btncancelar;
    String apellidos, nombres, telefono, email, direccion;
    final String URL = "http://192.168.1.39/webservices/controllers/colaborador.php"; //añadimos el ip del url

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        Bundle parametros = this.getIntent().getExtras();
        if(parametros != null) {
            obtenerdatos(parametros.getInt("idcolaborador"));
        }

        loadUI();
        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadUI(){
        apellidosR = findViewById(R.id.apellidosE);
        nombresR = findViewById(R.id.nombresE);
        telefonoR = findViewById(R.id.telefonoE);
        emailR = findViewById(R.id.emailE);
        direccionR = findViewById(R.id.direccionE);
        btneditar = findViewById(R.id.btneditar);
        btncancelar = findViewById(R.id.btncancelar);
    }

    private void obtenerdatos(int idcolaborador) {
        //añadir parametros a la URL base
        Uri.Builder URLFull = Uri.parse(URL).buildUpon();
        URLFull.appendQueryParameter("operacion", "obtenerDatos");
        URLFull.appendQueryParameter("idcolaborador", String.valueOf(idcolaborador));
        String URLActualizar = URLFull.build().toString();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLActualizar, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //crearemos un objeto donde almacenamos el valor (JSON)
                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    apellidosR.setText(jsonObject.getString("apellidos"));
                    nombresR.setText(jsonObject.getString("nombres"));
                    telefonoR.setText(jsonObject.getString("telefono"));

                    //estos campos puede retornar "null"
                    emailR.setText(jsonObject.getString("email").equals("null") ? "" : jsonObject.getString("email"));
                    direccionR.setText(jsonObject.getString("direccion").equals("null") ? "" : jsonObject.getString("direccion"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void mostrarActualizacion() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Colaboradores");
        dialog.setMessage("¿Esta seguro de Actualiza los Datos?");
        dialog.setCancelable(false);

        //definamos aceptar / cancelar
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //enviar los datos actualizados voller
                //actualizarDatos();
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        //mostramos el dialogo
        dialog.show();
    }

    private void actualizarDatos() {
        apellidos = apellidosR.getText().toString().trim();
        nombres = nombresR.getText().toString().trim();
        telefono = telefonoR.getText().toString().trim();
        email = emailR.getText().toString().trim();
        direccion = direccionR.getText().toString().trim();

        if (apellidos.isEmpty()) {
            apellidosR.setError("Completa este campo");
        } else if (nombres.isEmpty()) {
            nombresR.setError("Completa este campo");
        } else if (telefono.isEmpty()) {
            telefonoR.setError("Completa este campo");
        } else {
            mostrarActualizacion();
        }
    }

    private String isNull(String value) {
        if (value.equals("null"))
            return "";
        else
            return value;
    }

}