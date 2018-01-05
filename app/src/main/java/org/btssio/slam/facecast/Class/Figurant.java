package org.btssio.slam.facecast.Class;

/**
 * Created by SATTIA Dhinesh on 09/12/2017.
 */

public class Figurant {
    private String idFigurant;
    private String apikey;
    private String nom;
    private String prenom;
    private String email;
    private int age;
    private char sexe;
    private String role;

    public Figurant() {
    }

    public Figurant(String idFigurant, String apikey, String nom, String prenom, String email, int age, char sexe, String role) {
        this(apikey,nom,prenom,email,age,sexe,role);
        this.idFigurant = idFigurant;
    }
    //constructeur pour l'inscription d'un figuant
    public Figurant(String apikey, String nom, String prenom, String email, int age, char sexe, String role) {
        this.apikey = apikey;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.age = age;
        this.sexe = sexe;
        this.role = role;

    }
    @Override
    public String toString() {
        return "Figurant{" +
                "idFigurant='" + idFigurant + '\'' +
                ", apikey='" + apikey + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", sexe='" + sexe + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
    public String getIdFigurant() {
        return idFigurant;
    }

    public void setIdFigurant(String idFigurant) {
        this.idFigurant = idFigurant;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSexe() {
        return sexe;
    }

    public void setSexe(char sexe) {
        this.sexe = sexe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
