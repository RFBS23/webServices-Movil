package com.fabridev.webservices;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class listar extends AppCompatActivity {
    ListView listacolaboradores;

    //objeto requeridos para transferir dato(WS > listview)
    private List<String> dataList = new ArrayList<>(); // guardamos apellidos y nombres
    private List<Integer> dataID = new ArrayList<>(); //guardamos unicamente las pk y utilizamos el integer para que sea un entero
    private customAdapter adapter;

    //constante
    final String URL = "http://192.168.1.39/webservices/controllers/colaborador.php"; //añadimos el ip del url

    private String[] opciones = {"Editar", "Eliminar", "Cancelar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        loadUI();
        //al iniciar la actividad
        obtenerDatos();

        //evento al seleccionar un elemento del ListView
        listacolaboradores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Editar / Eliminar / Cancelar
                //Ahora el metodo recibe la PK y la ubicacion del elemento
                VerAlertaOpciones(dataID.get(position), position);
            }
        });
    }

    private void loadUI(){
        listacolaboradores = findViewById(R.id.listacolaboradores);
    }

    private void obtenerDatos() {
        dataID.clear();
        dataList.clear();
        adapter = new customAdapter(this, dataList);
        listacolaboradores.setAdapter(adapter);

        //construir una nueva URL (Capa Nvalores)
        Uri.Builder URLFull = Uri.parse(URL).buildUpon();
        URLFull.appendQueryParameter("operacion", "listar");
        String URLUpdate = URLFull.build().toString();

        //instancia de JSONArray(clase Volley)
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URLUpdate, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = new JSONObject(response.getString(i));
                        dataList.add(jsonObject.getInt("idcolaborador") + " | " + jsonObject.getString("nombres") + " " + jsonObject.getString("apellidos"));
                        dataID.add(jsonObject.getInt("idcolaborador"));
                    }
                    //alertamos al adaptador de que hay cambios
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.d("error", error.tostring());
            }
        });
        //enviamos el request(requerimientos)
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void eliminarregistro (int pkDelete) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                obtenerDatos();
                Toast.makeText(getApplicationContext(), "eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("operacion", "eliminar");
                parametros.put("idcolaborador", String.valueOf(pkDelete));
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void verConfirmacionEliminar(int pkDelete){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Eliminar Datos");
        dialogo.setMessage("¿Esta Segur@ de eliminar el registro?");
        dialogo.setCancelable(false);

        //definir botones
        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarregistro(pkDelete);
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogo.show();
    }

    // opciones de alertas
    private void VerAlertaOpciones(int primarykey, int positionIndex) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(dataList.get(positionIndex));
        dialogo.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int itemIndex) {
                //3 opciones => 0,1,2
                //Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
                switch (itemIndex) {
                    case 0:
                        //editar
                        Intent intent = new Intent(getApplicationContext(), editar.class);
                        intent.putExtra("idcolaborador", primarykey);
                        startActivity(intent);
                        break;
                    case 1:
                        //eliminar
                        verConfirmacionEliminar(primarykey);
                    case 2:
                        //cancelar
                        dialogInterface.dismiss();
                        break;
                }
            }
        });
        dialogo.show();
    }
}