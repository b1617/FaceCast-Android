package org.btssio.slam.facecast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.btssio.slam.facecast.Class.Offre;
import org.btssio.slam.facecast.R;
import org.btssio.slam.facecast.Repositories.AccountRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    private TextView tvDetailNom;
    private TextView tvDetailType;
    private TextView tvDetailDate;
    private TextView tvDetailRole;
    private TextView tvDetailNbJours;
    private TextView tvDetailNbFigurant;
    private TextView tvDetailStatut;

    private Button btnPostuler;
    private String urlApiKey ;
    private boolean postulation;
    private Offre offre;
    private final String URL = "http://10.0.2.2:3000/rest/candidature/ajout/";
    private final String URL_MES_CANDIDATURES="http://10.0.2.2:3000/rest/candidature/enCour/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AccountRepository arApiKey = new AccountRepository(getApplicationContext());
        tvDetailNom = (TextView) findViewById(R.id.tvDetailNom);
        tvDetailType = (TextView) findViewById(R.id.tvDetailType);
        tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
        tvDetailRole = (TextView) findViewById(R.id.tvDetailRole);
        tvDetailNbJours =(TextView) findViewById(R.id.tvDetailNbJours);
        tvDetailNbFigurant = (TextView) findViewById(R.id.tvDetailNbFigurant);
        tvDetailStatut = (TextView)findViewById(R.id.tvDetailStatut);
        btnPostuler = (Button)findViewById(R.id.btnPostuler);


        Intent intentOffre = getIntent();
          offre = (Offre) intentOffre.getSerializableExtra("offre");
        setTitle(offre.getNomEvenement());
      //  Log.i("offreStatut" , offre.getStatut());
        postulation = intentOffre.getBooleanExtra("postulation", false);


        tvDetailNom.setText(offre.getNomEvenement());
        tvDetailType.setText(offre.getTypeEvenement());
        tvDetailDate.setText(offre.getDateDebut().substring(0,10));
        tvDetailRole.setText(offre.getRole());
        tvDetailStatut.setText(offre.getStatut());
        tvDetailNbJours.setText(String.valueOf(offre.getNbJour()) + " Jours");
    tvDetailNbFigurant.setText(String.valueOf(offre.getNbFigurant()) + " Figurants");

// vérification si déja postuler
        if (postulation && offre.getStatut().equals("null")) {
            btnPostuler.setVisibility(View.VISIBLE);
            urlApiKey = URL + arApiKey.getApikey();
            btnPostuler.setOnClickListener(this);
        }
        else if (postulation) {
            btnPostuler.setVisibility(View.VISIBLE);
            btnPostuler.setEnabled(false);
            btnPostuler.setBackgroundColor(Color.GRAY);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /*
    Envois de la requête avec l'url et l'objet offres
     */

    @Override
    public void onClick(View view) {
        sendJsonRequest(urlApiKey, offre);
    }


/*
requête pour candidater à l'offre
@params Url de l'api rest
@params offre : L'objet offre
 */
    private void sendJsonRequest(String URL , final Offre offre) {
        final AccountRepository accountRepository = new AccountRepository(getApplicationContext());
        JSONObject params = new JSONObject();
        try {
            params.put("idOffre", offre.getIdOffre());
            params.put("apiKey", accountRepository.getApikey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // récup l'apiKey de la candidature et on compare avec l'apiKey enregistrer dans les shared preferences
                            if (response.getString("apiKey").equals(accountRepository.getApikey())){
                                Toast.makeText(getApplicationContext(),"Reussi" , Toast.LENGTH_LONG).show();
                                btnPostuler.setBackgroundColor(Color.GRAY);
                                btnPostuler.setEnabled(false);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                } );
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
