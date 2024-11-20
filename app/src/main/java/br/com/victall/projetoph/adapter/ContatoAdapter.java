package br.com.victall.projetoph.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import br.com.victall.projetoph.R;
import br.com.victall.projetoph.model.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ViewHolder> {

    private ArrayList<Contato> contatoList;
    private Context context;
    private OnClickListener listener;
    private OnLongClickListener longClickListener;

    public ContatoAdapter(ArrayList<Contato> contatoList, Context context, OnClickListener listener, OnLongClickListener longClickListener){
        this.contatoList = contatoList;
        this.context = context;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ContatoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_contato,parent,false);
        return new ViewHolder(view);
    }

    public interface OnClickListener{
        void OnClick( int posicao,boolean isFavorite,int type);
    }

    public interface OnLongClickListener{
        boolean OnLongClick(int posicao);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatoAdapter.ViewHolder holder, int position) {

        Contato contato = contatoList.get(holder.getAdapterPosition());

        holder.txtEmail.setText(contato.getEmail());
        holder.txtTelefone.setText(contato.getTelefone());
        holder.txtNome.setText(contato.getNome());
        holder.itemView.setOnClickListener(v->listener.OnClick(holder.getAdapterPosition(),true,2));
        holder.itemView.setOnLongClickListener(v->longClickListener.OnLongClick(holder.getAdapterPosition()));

        File file = new File("//data//data//br.com.victall.projetoph//fotos//"+contato.getId()+".jpg");


        StorageReference fotoPerfilRef = FirebaseStorage.getInstance().getReference().child("fotos");
        String fotoRef150 = contato.getId();
        StorageReference userImageRef150 = fotoPerfilRef.child(fotoRef150);

        if(file.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            holder.imgPerfil.setImageBitmap(myBitmap);
        }
        else if(!contato.getFotoPath().isEmpty()){

            userImageRef150.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    if(file.exists()&&file.isFile()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        holder.imgPerfil.setImageBitmap(myBitmap);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.imgPerfil.setImageDrawable(context.getDrawable(R.drawable.baseline_image_24));
                }
            });
        }
        holder.imgFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contato.setFavorito(!contato.isFavorito());
                listener.OnClick(holder.getAdapterPosition(), contato.isFavorito(),1);
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        if(contato.isFavorito())
            holder.imgFavorito.setImageResource(R.drawable.baseline_favorite_24);
        else
            holder.imgFavorito.setImageResource(R.drawable.baseline_favorite_border_24);

    }

    @Override
    public int getItemCount() {
        return contatoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNome;
        TextView txtTelefone;
        TextView txtEmail;
        ImageView imgFavorito;
        CircleImageView imgPerfil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNome = itemView.findViewById(R.id.txtNomeContato);
            txtTelefone = itemView.findViewById(R.id.txtFoneContato);
            txtEmail = itemView.findViewById(R.id.txtEmailContato);
            imgPerfil = itemView.findViewById(R.id.imgPerfil);
            imgFavorito = itemView.findViewById(R.id.imgFavoriteContato);
        }
    }
}
