package br.com.victall.projetoph.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import br.com.victall.projetoph.R;
import br.com.victall.projetoph.model.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ViewHolder> {

    private ArrayList<Contato> contatoList;
    private Context context;
    private OnClickListener listener;

    public ContatoAdapter(ArrayList<Contato> contatoList, Context context, OnClickListener listener) {
        this.contatoList = contatoList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContatoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_contato,parent,false);
        return new ViewHolder(view);
    }

    public interface OnClickListener{
        void OnClick( int posicao,boolean isFavorite);
    }

    @Override
    public void onBindViewHolder(@NonNull ContatoAdapter.ViewHolder holder, int position) {

        Contato contato = contatoList.get(holder.getAdapterPosition());

        holder.txtEmail.setText(contato.getEmail());
        holder.txtTelefone.setText(contato.getTelefone());
        holder.txtNome.setText(contato.getNome());

        holder.imgFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contato.setFavorito(!contato.isFavorito());
                listener.OnClick(holder.getAdapterPosition(), contato.isFavorito());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        if(!contato.getFotoPath().isEmpty()){
            Glide.with(context).load(contato.getFotoPath()).into(holder.imgPerfil).onLoadFailed(context.getResources().getDrawable(R.drawable.baseline_image_24));
        }

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
