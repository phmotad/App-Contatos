package br.com.victall.projetoph.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.lang.UCharacterEnums;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.victall.projetoph.R;
import br.com.victall.projetoph.model.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private ImageView btnPagPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPagPrincipal = findViewById(R.id.btnPagPrincipal);
    }
    public void deslogar(View view){
        ConfiguracaoFirebase.deslogar(this);
    }
}
