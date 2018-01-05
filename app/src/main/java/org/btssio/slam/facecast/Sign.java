package org.btssio.slam.facecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class Sign extends AppCompatActivity implements View.OnClickListener {
    private Button btnVerification;
    private Button btnInscriptionBis;
    private TextView tvMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        setTitle("Connexion");
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        tvMsg.setText("L’agence FaceCast est spécialisée dans la fourniture de figurants anonymes pour des événements comme des spectacles ou des films.");

        btnVerification = (Button) findViewById(R.id.btnVerification);
        btnInscriptionBis = (Button) findViewById(R.id.btnInscriptionBis);
        btnVerification.setOnClickListener(this);
        btnInscriptionBis.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnVerification:
                startActivity(new Intent(Sign.this, MainActivity.class));
                finish();
                break;
            case R.id.btnInscriptionBis:
                startActivity(new Intent(Sign.this, Registration.class));
                finish();
                break;
        }

    }




}
