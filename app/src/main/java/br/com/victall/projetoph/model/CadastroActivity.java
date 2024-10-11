package br.com.victall.projetoph.model;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import br.com.victall.projetoph.R;

public class CadastroActivity extends AppCompatActivity {
    private EditText editEmail;
    private EditText editNome;
    private EditText editsenha;
    private Button btnCadastro;
    private CheckBox cbTermos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNome = findViewById(R.id.editTextText);
        editEmail = findViewById(R.id.editTextTextEmailAddress2);
        editsenha = findViewById(R.id.editTextTextPassword2);
        btnCadastro = findViewById(R.id.btnEntrar);
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
        if(verificaCampos()){
            if(verificaSenha()){
                if (verificarEmail()) {
                    Usuario usuario = new Usuario();
                    usuario.setEmail(editEmail.getText().toString());
                    usuario.setSenha(editsenha.getText().toString());
                    usuario.setNome(editNome.getText().toString());
                    ConfiguracaoFirebase.salvar(usuario,this);
                }
                else Toast.makeText(this, "Preencha um E-mail válido!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "A senha deve conter mais de 6 caracteres!", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "Preencha um nome válido!", Toast.LENGTH_SHORT).show();
    }
}