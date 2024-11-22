package br.com.victall.projetoph.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.victall.projetoph.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.imgZap);
        TextView textView = findViewById(R.id.txtBemVindo);
        textView.setAlpha(0f); // Inicialmente invisível

        // Cria os animadores para diminuir a escala X e Y da imagem
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 0.5f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 0.5f);

        // Cria o animador para mover a imagem para baixo
        ObjectAnimator moveDown = ObjectAnimator.ofFloat(imageView, "translationY", 0f, 500f);

        // Cria os animadores para aumentar a escala X e Y do textview
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(textView, "scaleY", 1f, 1.5f);

        // Cria o animador para fazer o TextView aparecer
        ObjectAnimator fadeInText = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);

        // Define a duração das animações para 3 segundos (3000 milissegundos)
        scaleDownX.setDuration(2000);
        scaleDownY.setDuration(2000);
        scaleUpX.setDuration(2000);
        scaleUpY.setDuration(2000);
        moveDown.setDuration(2000);
        fadeInText.setDuration(3000);

        // Cria um AnimatorSet para executar as animações simultaneamente
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleDownX, scaleDownY, scaleUpX, scaleUpY, moveDown, fadeInText);

        // Adiciona um listener para detectar quando a animação termina
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Nada a fazer aqui
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Inicia a LoginActivity quando a animação termina
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Nada a fazer aqui
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Nada a fazer aqui
            }
        });

        // Inicia as animações
        animatorSet.start();
    }
}
