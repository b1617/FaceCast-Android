package org.btssio.slam.facecast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.btssio.slam.facecast.Repositories.AccountRepository;

public class Splash extends AppCompatActivity {
private AccountRepository accRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ImageView imgSplash = (ImageView)findViewById(R.id.imgSplash);
        Animation an = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotation);
        final Animation an2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.abc_fade_out);
        imgSplash.startAnimation(an);
        // Creation du compte pour la sauvegarde des informations de l'application
        accRepo = new AccountRepository(getApplicationContext());
// Verification de l'existence d'un compte

        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // imgSplash.startAnimation(an2);
                //  finish();
                if (!accRepo.isAccountConfigured()) {
                    startActivity(new Intent(Splash.this, Sign.class));
                    finish();
                }
                else{
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
