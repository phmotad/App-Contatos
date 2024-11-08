package br.com.victall.projetoph.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import br.com.victall.projetoph.R;
import br.com.victall.projetoph.helper.ConfiguracaoFirebase;
import br.com.victall.projetoph.model.Contato;
import br.com.victall.projetoph.adapter.ContatoAdapter;

public class MainActivity extends AppCompatActivity implements ContatoAdapter.OnClickListener {

    private ImageView btnPagPrincipal;
    private ImageView imgSelected;
    private ArrayList<Contato> listaTarefas;
    private RecyclerView recyclerView;
    private ContatoAdapter adapter;
    private int SELECT_PICTURE = 200;
    private String pathTemp;
    private Uri uriTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPagPrincipal = findViewById(R.id.btnPagPrincipal);
        recyclerView = findViewById(R.id.lstTarefas);
        listaTarefas = new ArrayList<>();
        adapter = new ContatoAdapter(listaTarefas,this, this);
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


        //Criando Máscara do Campo de Telefone
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");  // cria o formato desejado
        MaskTextWatcher mtw = new MaskTextWatcher(edtTelefoneDialog, smf);  // insere o formato no campo a ser preenchido
        edtTelefoneDialog.addTextChangedListener(mtw);  // seta o formato no campo texto

        //Criando um Alertadialog com base na view criada
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar contato");
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        imgSelecionaFoto.setOnClickListener(v->escolherImagem(imgSelecionaFoto));

        btnSalvarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Ação do clique do botão salvar
                Contato contato = new Contato();
                contato.setNome(edtNomeDialog.getText().toString());
                contato.setEmail(edtEmailDialog.getText().toString());
                contato.setTelefone(edtTelefoneDialog.getText().toString());
                alertDialog.dismiss(); //Comando para fechar o Dialog
                ConfiguracaoFirebase.salvarContato(contato, MainActivity.this,uriTemp,false);
            }
        });
        }

    private void escolherImagem(ImageView view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
        imgSelected = view;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        imgSelected.setImageBitmap(bitmap);
                        pathTemp = saveToInternalStorage(bitmap);
                        uriTemp = selectedImageUri;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
    }

    public static String saveToInternalStorage(Bitmap bitmapImage){


        File mypath=new File("//data//data//br.com.victall.projetoph//","foto.jpg");


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private void recuperaContatos(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseDatabase.getReference().child("contatos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listaTarefas.clear();

                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Contato contato = dataSnapshot.getValue(Contato.class);
                        listaTarefas.add(contato);
                    }

                    adapter = new ContatoAdapter(listaTarefas,MainActivity.this, MainActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        recuperaContatos();
    }
    public void deslogar(View view){
        ConfiguracaoFirebase.deslogar(this);
    }

    @Override
    public void OnClick(int posicao, boolean isFavorite, int type) {
        if(type == 1) {
            listaTarefas.get(posicao).setFavorito(isFavorite);
            String contatoId = listaTarefas.get(posicao).getId();
            ConfiguracaoFirebase.atualizaContato(isFavorite,this,contatoId);}
        else{

            Intent intent = new Intent(this,ContatoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("contato",listaTarefas.get(posicao));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
