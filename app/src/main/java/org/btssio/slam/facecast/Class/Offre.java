package org.btssio.slam.facecast.Class;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.btssio.slam.facecast.Adapter.OffreAdapter;
import org.btssio.slam.facecast.Detail;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by SATTIA Dhinesh on 12/12/2017.
 */

public class Offre  implements  Serializable {

    private String idOffre;
    private String nomEvenement;
    private String typeEvenement;
    private String dateDebut;
    private int nbJour;
    private String role;
    private int nbFigurant;
    private String statut;
    private ArrayAdapter<Offre> myAdapter;
    private ArrayList<Offre> lesOffres;


    public Offre() {
        super();
    }


    //Constructor avec statut pour mes candidatures
    public Offre(String idOffre, String nomEvenement, String typeEvenement, String dateDebut, int nbJour, String role, int nbFigurant, String statut) {
        this.idOffre = idOffre;
        this.nomEvenement = nomEvenement;
        this.typeEvenement = typeEvenement;
        this.dateDebut = dateDebut;
        this.nbJour = nbJour;
        this.role = role;
        this.nbFigurant = nbFigurant;
        this.statut = statut;
    }

    public String getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(String idOffre) {
        this.idOffre = idOffre;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    public String getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(String typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public int getNbJour() {
        return nbJour;
    }

    public void setNbJour(int nbJour) {
        this.nbJour = nbJour;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getNbFigurant() {
        return nbFigurant;
    }

    public void setNbFigurant(int nbFigurant) {
        this.nbFigurant = nbFigurant;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    /*
    Requête pour récuperer les données pour afficher dans la listview
    @params URL de l'api REST
    @params context de l'application
    @params listview qui correspondant
    @params choixParseJson : le choix du parsing car json variante //  mes offres ||  les offres
    @params postulation : choix du postulation a afficher pour les different listview
     */
    public void sendRequestOffre(String URL, final Context context, final ListView listView, final int choixParseJson , final  boolean postulation) {
        StringRequest stringRequest = new StringRequest(URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(final String response) {
                        if (choixParseJson == 0) {
                            // les offres en générale
                            lesOffres = parseJsonOffres(response, context);
                        } else {
                            //Mes offres
                            lesOffres = parseJsonMesOffres(response, context);
                        }
                        myAdapter = new OffreAdapter(context, lesOffres);
                        listView.setAdapter(myAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(context,Detail.class);
                                intent.putExtra("offre", lesOffres.get(i));
                                intent.putExtra("postulation",postulation);
                                context.startActivity(intent);
                            }
                        });
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }



    /*
    Récupération mes offres
    @params leJson : la response du volley
    @params context : le context de l'application
    @return un arraylist de mes offres
     */
    public ArrayList<Offre> parseJsonMesOffres(String leJson, Context context) {
        ArrayList<Offre> offres = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(leJson);
            if (!jsonArray.isNull(0)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonOffre = jsonArray.getJSONObject(i);
                    String statut = jsonOffre.getString("statut");
                    JSONArray arrayOffre = jsonOffre.getJSONArray("offre");
                    JSONObject objectOffre = arrayOffre.getJSONObject(0);
                    String idOffre = objectOffre.getString("_id");
                    String nomEvenement = objectOffre.getString("nomEvenement");
                    String typeEvenement = objectOffre.getString("typeEvenement");
                    String dateDebut = objectOffre.getString("dateDebut");
                    int nbJours = objectOffre.getInt("nbJours");
                    String role = objectOffre.getString("role");
                    int figurant = objectOffre.getInt("nbFigurant");
                    Offre offre = new Offre(idOffre, nomEvenement, typeEvenement, dateDebut, nbJours, role, figurant, statut);
                    offres.add(offre);
                }
            } else {
                alertNullResultat(context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return offres;
    }

    /*
        Récupération mes offres
        @params leJson : la response du volley
        @params context : le context de l'application
        @return un arraylist de offre
         */

    public ArrayList<Offre> parseJsonOffres(String leJson , Context context) {
        ArrayList<Offre> offres = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(leJson);
            if (!jsonArray.isNull(0)) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonOffre = jsonArray.getJSONObject(i);
                    String idOffre = jsonOffre.getString("_id");
                    String nomEvenement = jsonOffre.getString("nomEvenement");
                    String typeEvenement = jsonOffre.getString("typeEvenement");
                    String dateDebut = jsonOffre.getString("dateDebut");
                    int nbJours = jsonOffre.getInt("nbJours");
                    String role = jsonOffre.getString("role");
                    int figurant = jsonOffre.getInt("nbFigurant");
                   JSONArray jsonCandidature = jsonOffre.getJSONArray("candidature");
                   String statut;
                   if(jsonCandidature.length() >= 1) {
                        statut = jsonCandidature.getJSONObject(0).getString("statut");
                   }
                   else{
                       statut = "null";
                   }
                    Offre offre = new Offre(idOffre, nomEvenement, typeEvenement, dateDebut, nbJours, role, figurant,statut);
                    offres.add(offre);
                }
            } else {
               alertNullResultat(context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return offres;
    }

    /*
    les résultats sont nuls
    @params context : le context de l'application
     */

    public void alertNullResultat(Context context){
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle("Aucun résultat");

        // Setting Dialog Message
        alertDialog.setMessage("Nous avons trouvé aucun résultat correspondant à votre recherche.");

        // Setting Icon to Dialog
        alertDialog.setIconAttribute(android.R.attr.alertDialogIcon);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
