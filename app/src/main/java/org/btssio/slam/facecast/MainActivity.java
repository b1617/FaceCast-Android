package org.btssio.slam.facecast;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.btssio.slam.facecast.Class.ConnexionInternet;
import org.btssio.slam.facecast.Class.Figurant;
import org.btssio.slam.facecast.Class.Offre;
import org.btssio.slam.facecast.Fragment.HistoriqueCandidatures;
import org.btssio.slam.facecast.Fragment.Home;
import org.btssio.slam.facecast.Fragment.MesCandidatures;
import org.btssio.slam.facecast.Fragment.OffresEnCours;
import org.btssio.slam.facecast.Fragment.OffresPassees;
import org.btssio.slam.facecast.Fragment.OffresPostules;
import org.btssio.slam.facecast.Repositories.AccountRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AccountRepository accRepo;
    private String leEmail;
    private Button connexion;
    private Button quitter;
    private String URL_REST = "http://10.0.2.2:3000/rest";
    private AlertDialog ad;
    private Figurant figurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// Creation du compte pour la sauvegarde des informations de l'application
        accRepo = new AccountRepository(getApplicationContext());
// Verification de l'existence d'un compte
        if (!accRepo.isAccountConfigured()) {
            //Toast.makeText(this,"non configuré ", Toast.LENGTH_LONG).show();

            // Lance la fenetre pour la recherche d'un compte
            LayoutInflater factory = LayoutInflater.from(this);
            final View CompteView = factory.inflate(R.layout.alertdialog_compte, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

            alertDialogBuilder.setView(CompteView);
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
            alertDialogBuilder.setTitle("Vérification d'email");
            alertDialogBuilder
                    .setCancelable(false);
            ad = alertDialogBuilder.create();


            final EditText email = (EditText) CompteView.findViewById(R.id.etEmail);
            connexion = (Button) CompteView.findViewById(R.id.btnConnexion);
            quitter = (Button) CompteView.findViewById(R.id.btnQuitter);

            final ConnexionInternet connexionInternet = new ConnexionInternet(getApplicationContext());

            connexion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    leEmail = email.getText().toString();
                    if (leEmail.isEmpty()) {
                        email.setError("Veuillez indiquer votre email");
                    } else if (!leEmail.matches("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$")) {

                        email.setError("Email non valide");

                    } else if (connexionInternet.YaConnexion() == false) {
                        InputMethodManager clavier = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        clavier.hideSoftInputFromWindow(connexion.getWindowToken(), 0);
                        onTermine(getApplicationContext());

                    } else {
                        InputMethodManager clavier = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        clavier.hideSoftInputFromWindow(connexion.getWindowToken(), 0);
                        onRequest();
                    }

                }
            });
            quitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    ad.dismiss();
                }
            });

            ad.show();

        }


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


// Premiere page par defaut : Fragment Home
        Home home = new Home();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, home).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView txtIdentite = (TextView) header.findViewById(R.id.tvHeaderIdentite);
        TextView txtEmail = (TextView) header.findViewById(R.id.tvHeaderEmail);
        if (accRepo.getApikey().isEmpty()) {
            txtIdentite.setText("Félicitation");
            txtEmail.setText("Bienvenue sur Facecast");

        } else {
            Gson figurantGson = new Gson();
            Figurant figurantJsonToObject = figurantGson.fromJson(accRepo.getFigurant(), Figurant.class);
            txtIdentite.setText(figurantJsonToObject.getNom() + " " + figurantJsonToObject.getPrenom());
            txtEmail.setText(figurantJsonToObject.getEmail());
        }
    }


    /*
    Envois requête pour vérifier l'email
     */
    public void onRequest() {
        super.onStart();
        String urlFigurant = URL_REST + "/figurant/email/" + leEmail;
        sendRequest(urlFigurant);
    }


    /*
    Vérification email du figurant
    @params  l'url figurant avec l'email
     */
    private void sendRequest(String url) {
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // s'il y a un figurant existant avec l'email
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            if (!jsonArray.isNull(0)) {
                                JSONObject jsonFigurant = jsonArray.getJSONObject(0);
                                String idFigurant = jsonFigurant.getString("_id");
                                String apiKey = jsonFigurant.getString("apiKey");
                                String nom = jsonFigurant.getString("nom");
                                String prenom = jsonFigurant.getString("prenom");
                                String email = jsonFigurant.getString("email");
                                int age = jsonFigurant.getInt("age");
                                char sexe = jsonFigurant.getString("sexe").charAt(0);
                                String role = jsonFigurant.getString("role");
                                figurant = new Figurant(idFigurant, apiKey, nom, prenom, email, age, sexe, role);
                                Gson gson = new Gson();
                                String figurantToJson = gson.toJson(figurant);

                                // enregistrement du figurant et apiKey
                                accRepo.setAccount(apiKey, figurantToJson);
                                ad.dismiss();
                            } else {
                                final AlertDialog alertDialog = new AlertDialog.Builder(
                                        MainActivity.this).create();

                                alertDialog.setTitle("Inconnu");
                                alertDialog.setMessage("Vous n'êtes pas inscrit.");
                                alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);

                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*
    Vérification internet
    @params context de l'application
     */
    public static void onTermine(Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Pas de connexion internet");
        alertDialog.setMessage("Veuillez verifier votre connexion d'internet.");
        alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {alertDialog.dismiss();}});
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_mes_candidatures) {
            setTitle("Mes candidatures");
            fragment = new MesCandidatures();
        } else if (id == R.id.nav_historique) {
            setTitle("Historique Candidatures");
            fragment = new HistoriqueCandidatures();
        } else if (id == R.id.nav_offres) {
            setTitle("Offres en cours");
            fragment = new OffresEnCours();
        } else if (id == R.id.nav_offres_passees) {
            setTitle("Anciennes offres");
            fragment = new OffresPassees();
        } else if (id == R.id.nav_offres_postules) {
            setTitle("Mes offres");
            fragment = new OffresPostules();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
