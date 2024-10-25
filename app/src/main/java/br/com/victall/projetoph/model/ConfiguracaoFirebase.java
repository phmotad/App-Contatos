package br.com.victall.projetoph.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.StartupTime;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import br.com.victall.projetoph.activity.MainActivity;

public class ConfiguracaoFirebase {

    public static void salvar(Usuario usuario, Context context) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getUser() != null) {
                                String id = task.getResult().getUser().getUid();
                                databaseReference.child("usuarios").child(id).setValue(usuario)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    context.startActivity(new Intent(context, LoginActivity.class));
                                                }
                                            }
                                        });
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void logar(String email, String senha, Context context){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    context.startActivity(new Intent(context, MainActivity.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Erro: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(context,"Erro inesperado. Tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void deslogar(Context context){
        FirebaseAuth.getInstance().signOut();
        context.startActivity(new Intent(context,LoginActivity.class));
    }

    public static boolean estaLogado(){
        return FirebaseAuth.getInstance().getCurrentUser()!=null;
    }

    public static void salvarContato(Contato contato, Context context, Uri uri){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String id = databaseReference.child("contatos").push().getKey();
        contato.setId(id);

        if(id==null)
            return;

        final StorageReference fotoPerfilRef = FirebaseStorage.getInstance().getReference().child("fotos");

        //Fazer Upload do Arquivo
        UploadTask uploadTask = fotoPerfilRef.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fotoPerfilRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String urlConvertida = uri.toString();
                        contato.setFotoPath(urlConvertida);


                        databaseReference.child("contatos").child(id).setValue(contato)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(context, "Contato salvo com sucesso!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

}
