package br.com.victall.projetoph.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import br.com.victall.projetoph.R;
import android.animation.ObjectAnimator;
import android.widget.ImageView;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.imgZap);

        // Cria o animador para diminuir a escala X e Y da imagem
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0.5f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 0.5f);

        // Define a duração da animação para 2 segundos (2000 milissegundos)
        scaleDownX.setDuration(2000);
        scaleDownY.setDuration(2000);

        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        };

        scaleDownX.addListener(listener);
        scaleDownY.addListener(listener);

        // Inicia as animações
        scaleDownX.start();
        scaleDownY.start();

    }
}