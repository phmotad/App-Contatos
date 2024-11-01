package br.com.victall.projetoph.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.victall.projetoph.R;
import br.com.victall.projetoph.helper.ConfiguracaoFirebase;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editsenha;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editTextTextEmailAddress2);
        editsenha = findViewById(R.id.editTextTextPassword2);
        btnLogin = findViewById(R.id.btnEntrar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(ConfiguracaoFirebase.estaLogado()){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private boolean verificaCampos(){
        if(!editEmail.getText().toString().isEmpty())
            if(!editsenha.getText().toString().isEmpty())
                return true;
        return false;
    }

    public void entrar(View view){
        if(verificaCampos())
            ConfiguracaoFirebase.logar(editEmail.getText().toString(),editsenha.getText().toString(),this);
        else
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
    }

    public void abreTelaCadastro(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }
}