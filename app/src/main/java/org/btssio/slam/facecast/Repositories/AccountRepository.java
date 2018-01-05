package org.btssio.slam.facecast.Repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

// Classe intermediaire le compte de l'utilisateur et l'enregistrement Android
public class AccountRepository extends Repository {

	// Constructeur
	public AccountRepository(Context context) {
		super(context);
	}

	// Enregistre le compte dans les SharedPreferences
	public void setAccount(String apikey , String figurant) {
		SharedPreferences app = PreferenceManager.getDefaultSharedPreferences(Repository.context);
		Editor prefsEditor = app.edit();
		prefsEditor.putString("APIKEY", apikey);
		prefsEditor.putString("FIGURANT", figurant);
		prefsEditor.commit();
	}
	
	// Supprime le compte
	public void unsetAccount() {
		SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(Repository.context);
		Editor prefsEditor = appSharedPrefs.edit();
		prefsEditor.clear();
		prefsEditor.commit();
	}

	// Indique si le compte est configure ou non
	public boolean isAccountConfigured() {
		AccountRepository accRepo = new AccountRepository(Repository.context);
		String apikey = accRepo.getApikey();

		if (!apikey.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	// Recupere l'apikey
	public String getApikey()	{
		SharedPreferences app = PreferenceManager.getDefaultSharedPreferences(Repository.context);
		return app.getString("APIKEY","");
	}
// Recupere le figurant
	public String getFigurant()	{
		SharedPreferences app = PreferenceManager.getDefaultSharedPreferences(Repository.context);
		return app.getString("FIGURANT","");
	}
	// Positionne l'apikey
	public void setApikey(String apikey)	{
		SharedPreferences app = PreferenceManager.getDefaultSharedPreferences(Repository.context);
		Editor prefsEditor = app.edit();
		prefsEditor.putString("APIKEY",apikey);
		prefsEditor.commit();	
	}
}

