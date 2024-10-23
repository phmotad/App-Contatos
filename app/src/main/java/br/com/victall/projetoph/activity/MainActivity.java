package br.com.victall.projetoph.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import br.com.victall.projetoph.R;
import br.com.victall.projetoph.model.ConfiguracaoFirebase;
import br.com.victall.projetoph.model.Contato;
import br.com.victall.projetoph.model.ContatoAdapter;

public class MainActivity extends AppCompatActivity {

    private ImageView btnPagPrincipal;
    private ArrayList<Contato> listaTarefas;
    private RecyclerView recyclerView;
    private ContatoAdapter adapter;
    private int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPagPrincipal = findViewById(R.id.btnPagPrincipal);
        recyclerView = findViewById(R.id.lstTarefas);
        listaTarefas = new ArrayList<>();
        adapter = new ContatoAdapter(listaTarefas,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
        public void adicionaTarefa(View view){

        //Definido um view para acessar os campos do diálogo
        View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog,null,false);
        EditText edtNomeDialog = dialogView.findViewById(R.id.edtNomeDialog);
        EditText edtEmailDialog = dialogView.findViewById(R.id.edtEmailDialog);
        EditText edtTelefoneDialog = dialogView.findViewById(R.id.edtTelefoneDialog);
        Button btnSalvarTarefa = dialogView.findViewById(R.id.btnSalvarTarefa);
        ImageView imgSelecionaFoto = dialogView.findViewById(R.id.imgSelecionaFoto);

        //Criando um Alertadialog com base na view criada
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar contato");
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        imgSelecionaFoto.setOnClickListener(v->escolherImagem());

        btnSalvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Ação do clique do botão salvar
                Contato contato = new Contato();
                contato.setNome(edtNomeDialog.getText().toString());
                contato.setEmail(edtEmailDialog.getText().toString());
                contato.setTelefone(edtTelefoneDialog.getText().toString());
                listaTarefas.add(contato); //Adiciona na lista
                adapter.notifyDataSetChanged(); //Comando para atualizar a lista
                alertDialog.dismiss(); //Comando para fechar o Dialog
                ConfiguracaoFirebase.salvarContato(contato, MainActivity.this);
            }
        });
        }

    private void escolherImagem() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                }
            }
        }
    }

    public void deslogar(View view){
        ConfiguracaoFirebase.deslogar(this);
    }
}
