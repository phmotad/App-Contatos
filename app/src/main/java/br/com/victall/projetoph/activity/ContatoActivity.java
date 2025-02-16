package br.com.victall.projetoph.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.victall.projetoph.IGetContato;
import br.com.victall.projetoph.R;
import br.com.victall.projetoph.databinding.ActivityContatoBinding;
import br.com.victall.projetoph.helper.ConfiguracaoFirebase;
import br.com.victall.projetoph.model.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContatoActivity extends AppCompatActivity {

    private TextInputEditText edtNome, edtEmail, edtTelefone;
    private Button btnSalvar;
    private ImageView imgEditar;
    private int SELECT_PICTURE = 200;
    private String pathTemp;
    private Uri uriTemp;
    private ImageView imgSelected;
    private CircleImageView imgFoto;
    private Contato contato;
    private ActivityContatoBinding binding;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContatoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        edtNome = findViewById(R.id.edtNomeEdicao);
        edtEmail = findViewById(R.id.edtEmailEdicao);
        edtTelefone = findViewById(R.id.telefoneEdicao);
        btnSalvar = findViewById(R.id.btnSalvarEdicao);
        imgEditar = findViewById(R.id.imgEdicao2);
        imgFoto = findViewById(R.id.imgEdicao);

        //Criando Máscara do Campo de Telefone
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)NNNNN-NNNN");  // cria o formato desejado
        MaskTextWatcher mtw = new MaskTextWatcher(edtTelefone, smf);  // insere o formato no campo a ser preenchido
        edtTelefone.addTextChangedListener(mtw);  // seta o formato no campo texto

        if(getIntent().getExtras()!=null){
            contato = (Contato) getIntent().getExtras().get("contato");
            edtNome.setText(contato.getNome());
            edtEmail.setText(contato.getEmail());
            edtTelefone.setText(contato.getTelefone());
        }

        imgFoto.setOnClickListener(v->escolherImagem());
        imgEditar.setOnClickListener(v->escolherImagem());

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //acao do clique do botao salvar
                Contato contato = new Contato();
                contato.setNome(edtNome.getText().toString());
                contato.setEmail(edtEmail.getText().toString());
                contato.setTelefone(edtTelefone.getText().toString());
                contato.setFotoPath(pathTemp);
                ConfiguracaoFirebase.salvarContato(contato, ContatoActivity.this, uriTemp, true, new IGetContato() {
                    @Override
                    public void sucess(String idUser) {
                        finish();
                    }
                });
            }
        });
        recuperarImagem();
        binding.toolbar.include.ibVoltar.setOnClickListener(view -> finish());
    }

    private void recuperarImagem(){
        if(contato!=null){
            if(!contato.getFotoPath().isEmpty()){
                File file = new File("//data//data//br.com.victall.projetoph//fotos//"+contato.getId()+".jpg");
                if(file.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    imgFoto.setImageBitmap(myBitmap);
                }
            }
        }
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
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        imgFoto.setImageBitmap(bitmap);
                        pathTemp = saveToInternalStorage(contato.getId());
                        uriTemp = selectedImageUri;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }
    }

    public String saveToInternalStorage(String idUser){

        File fotosDir = new File("//data//data//br.com.victall.projetoph//fotos//");
        if(!fotosDir.exists())
            fotosDir.mkdir();

        File mypath = new File("//data//data//br.com.victall.projetoph//fotos//",idUser+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
}