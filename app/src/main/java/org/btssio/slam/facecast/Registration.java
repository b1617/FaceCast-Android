package org.btssio.slam.facecast;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.btssio.slam.facecast.Class.Figurant;
import org.btssio.slam.facecast.Class.Offre;
import org.btssio.slam.facecast.Repositories.AccountRepository;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Registration extends AppCompatActivity {
    private EditText etNom;
    private EditText etPrenom;
    private EditText etAge;
    private EditText etRole;
    private RadioGroup rgSexe;
    private EditText etEmail;
    private Button btnInscription;
    private int age;
    private final String URL = "http://10.0.2.2:3000/rest/figurant";
    private int ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Inscription");
        btnInscription = (Button) findViewById(R.id.btnInscription);
        etNom = (EditText) findViewById(R.id.etNom);
        etPrenom = (EditText) findViewById(R.id.etPrenom);
        etAge = (EditText) findViewById(R.id.etAge);
        etRole = (EditText) findViewById(R.id.etRole);
        rgSexe = (RadioGroup) findViewById(R.id.rgSexe);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (rgSexe.getCheckedRadioButtonId() != -1) {

                    ct = 0;
                    int idSelect = rgSexe.getCheckedRadioButtonId();
                    RadioButton rbSelect = (RadioButton) findViewById(idSelect);


                    String nom = etNom.getText().toString();
                    checkEmptyEdittext(etNom, nom);


                    String prenom = etPrenom.getText().toString();
                    checkEmptyEdittext(etPrenom, prenom);

                    try {
                        age = Integer.parseInt(etAge.getText().toString());
                        ct++;
                    } catch (NumberFormatException e) {
                        etAge.setError("empty");
                    }

                    String role = etRole.getText().toString();
                    checkEmptyEdittext(etRole, role);

                    String email = etEmail.getText().toString();
                    checkEmptyEdittext(etEmail, email);

                    char sexe = rbSelect.getText().charAt(0);
                    String apiKey = apiKeyGenerator();
                    if (ct == 5) {
                        Figurant figurant = new Figurant(apiKey, nom, prenom, email, age, sexe, role);
                        sendJsonRequest(URL, figurant);
                    }
                } else {
                    Toast.makeText(Registration.this,
                            "Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    /*
    Vérification d'un editext vide
    @params editext :  a signaler si vide
     @params text : les données de l'editext
     */

    private void checkEmptyEdittext(EditText value, String text) {
        if (text.length() == 0) {
            value.setError("Empty");
        } else {
            ct++;
        }
    }

    /*
    Inscription d'un figurant
    @params Url : url d'api rest
    @params figurant : l'objet figurant construit avec le formulaire

     */
    private void sendJsonRequest(String URL, final Figurant figurant) {
        JSONObject params = new JSONObject();
        try {
            params.put("nom", figurant.getNom());
            params.put("prenom", figurant.getPrenom());
            params.put("age", figurant.getAge());
            params.put("sexe", String.valueOf(figurant.getSexe()));
            params.put("email", figurant.getEmail());
            params.put("role", figurant.getRole());
            params.put("apiKey", figurant.getApikey());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, URL, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // L'inscription valide on enregistre le figurant dans les shared preferences
                        if (response.length() >= 1) {
                            AccountRepository accountRepository = new AccountRepository(getApplicationContext());
                            try {
                                String jsNom = response.getString("nom");
                                String jsPrenom = response.getString("prenom");
                                String jsEmail = response.getString("email");
                                String jsApikey = response.getString("apiKey");
                                int jsAge = response.getInt("age");
                                char jsSexe = response.getString("sexe").charAt(0);
                                String jsRole = response.getString("role");
                                Figurant jsFigurant = new Figurant(jsApikey, jsNom, jsPrenom, jsEmail, jsAge, jsSexe, jsRole);
                                Gson gson = new Gson();
                                String figurantToJson = gson.toJson(jsFigurant);
                                accountRepository.setAccount(jsApikey, figurantToJson);
                                startActivity(new Intent(Registration.this, MainActivity.class));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest);
    }

    /*
    Générer une apiKey
    @return l'apiKey
     */
    public static String apiKeyGenerator() {
        final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int N = alphabet.length();
        Random r = new Random();
        String apiKey = new String();
        for (int i = 0; i < 20; i++) {
            apiKey += alphabet.charAt(r.nextInt(N));
        }
        return apiKey;
    }
}
