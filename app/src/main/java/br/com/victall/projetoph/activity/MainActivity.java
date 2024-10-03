package br.com.victall.projetoph.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.lang.UCharacterEnums;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import br.com.victall.projetoph.R;
import br.com.victall.projetoph.model.Livro;

public class MainActivity extends AppCompatActivity {
    private EditText editEmail;
    private EditText editNome;
    private EditText editsenha;
    private Button btnCadastro;
    private CheckBox cbTermos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNome = findViewById(R.id.editTextText);
        editEmail = findViewById(R.id.editTextTextEmailAddress2);
        editsenha = findViewById(R.id.editTextTextPassword2);
        btnCadastro = findViewById(R.id.button);
        cbTermos = findViewById(R.id.checkBox2);

    }
    private boolean verificaCampos(){
        if(!editNome.getText().toString().isEmpty())
            if(!editEmail.getText().toString().isEmpty())
                if(!editsenha.getText().toString().isEmpty())
                    return true;
        return false;
    }
    private boolean verificaSenha(){
        if(editsenha.getText().toString().length()<6)
            return false;
        return true;
    }
    private boolean verificarEmail(){
        return editEmail.getText().toString().contains("@");
    }
    public void cadastrar(View view){
        if (verificaCampos())
            if(verificaSenha())
                if(verificarEmail())
                    Toast.makeText(this, "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
    }
}
