package br.com.victall.projetoph.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.lang.UCharacterEnums;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.common.value.qual.ArrayLenRange;

import java.util.ArrayList;
import java.util.List;

import br.com.victall.projetoph.R;
import br.com.victall.projetoph.model.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private ImageView btnPagPrincipal;
    private List<String> listaTarefas;
    private ListView lstTarefas;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPagPrincipal = findViewById(R.id.btnPagPrincipal);
        lstTarefas = findViewById(R.id.lstTarefas);
        listaTarefas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listaTarefas);
        lstTarefas.setAdapter(adapter);
    }
        public void adicionaTarefa(View view){

        //Definido um view para acessar os campos do diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog,null,false);
        EditText edtTarefa = dialogView.findViewById(R.id.edtTarefa);
        Button btnSalvarTarefa = dialogView.findViewById(R.id.btnSalvarTarefa);

        //Criando um Alertadialog com base na view criada
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar tarefa");
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnSalvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Ação do clique do botão salvar
                String novaTarefa = edtTarefa.getText().toString(); //Comando para pegar o texto digitado pelo usuario
                listaTarefas.add(novaTarefa); //Adiciona na lista
                adapter.notifyDataSetChanged(); //Comando para atualizar a lista
                alertDialog.dismiss(); //Comando para fechar o Dialog
            }
        });
        }
    public void deslogar(View view){
        ConfiguracaoFirebase.deslogar(this);
    }
}
