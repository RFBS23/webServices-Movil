package com.fabridev.webservices;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class registrar extends AppCompatActivity {

    EditText apellidosR, nombresR, telefonoR, emailR, direccionR;
    Button btnregistrar;

    String apellidos, nombres, telefono, email, direccion;

    //constante
    final String URL = "http://192.168.1.39/webservices/controllers/colaborador.php"; //añadimos el ip del url
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //referencia
        loadUI();
        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarControles();
            }
        });
    }

    private void loadUI(){
        apellidosR = findViewById(R.id.apellidosR);
        nombresR = findViewById(R.id.nombresR);
        telefonoR = findViewById(R.id.telefonoR);
        emailR = findViewById(R.id.emailR);
        direccionR = findViewById(R.id.direccionR);
        btnregistrar = findViewById(R.id.btnregistrar);
    }

    private void  resetUI(){
        apellidosR.setText("");
        nombresR.setText("");
        telefonoR.setText("");
        emailR.setText("");
        direccionR.setText("");
        //btnregistrar.setText("");
    }

    private void validarControles() {
        //asignamos un valor a la variable trim (eliminar los espacios)
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
            telefonoR.setError("Completar este campo");
        } else {
            mostrarDialogoRegistro();
        }
    }

    private void mostrarDialogoRegistro() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Colaboradores");
        dialog.setMessage("¿Esta seguro de registrar");
        dialog.setCancelable(false);

        //definimos aceptar / cancelar
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                registrarColaborador();
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

    private void registrarColaborador() {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("")) {
                    resetUI();
                    apellidosR.requestFocus();
                    Toast.makeText(getApplicationContext(), "Colaborador registrado correctamente", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //no funciono la comunicacion con ws
                error.printStackTrace();
                //Log.e("Error", error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("operacion", "agregar");
                parametros.put("apellidos", apellidos);
                parametros.put("nombres", nombres);
                parametros.put("telefono", telefono);
                parametros.put("email", email);
                parametros.put("direccion", direccion);
                return parametros;
            }
        };

        //enviamos la soli
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}