package com.moises.quedadaseventos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.dto.UsuarioDto;
import com.moises.quedadaseventos.retrofit.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder> {

    private final List<UsuarioDto> usuarios;
    private AccionBotonUserItem callback;
    private final ApiService apiService;

    public UsuariosAdapter(List<UsuarioDto> usuarios, ApiService apiService) {
        this.usuarios = usuarios;
        this.apiService = apiService;
    }

    public void setCallback(AccionBotonUserItem callback) {
        this.callback = callback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNickname;
        private final Button btnAccion;

        public ViewHolder(View view) {
            super(view);
            tvNickname = view.findViewById(R.id.tvNickname);
            btnAccion = view.findViewById(R.id.btnAccionItemUsuario);
        }

        public void bind(UsuarioDto usuario, AccionBotonUserItem accionBtn, ApiService apiService) {

            int usuarioIdItem = usuario.getId();
            tvNickname.setText(usuario.getNickname());

            switch (accionBtn.getTipoAccion()) {
                case SIN_ACCION:
                    btnAccion.setVisibility(View.GONE);
                    break;
                case SEGUIR:
                    btnAccion.setVisibility(View.VISIBLE);
                    btnAccion.setText(R.string.seguir);
                    break;
                case DEJAR_DE_SEGUIR:
                    btnAccion.setVisibility(View.VISIBLE);
                    btnAccion.setText(R.string.dejar_de_seguir);
                    break;
            }


            btnAccion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (accionBtn.getTipoAccion()) {
                        case SEGUIR:
                            apiService.seguirUsuario(usuarioIdItem).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(v.getContext(), "Usuario seguido correctamente", Toast.LENGTH_LONG).show();
                                        accionBtn.onAccionRealizada();
                                    }
                                    else {
                                        Toast.makeText(v.getContext(), "Ocurrio un problema al seguir al usuario", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(v.getContext(), "Error inesperado", Toast.LENGTH_LONG).show();
                                }
                            });
                            break;
                        case DEJAR_DE_SEGUIR:
                            apiService.dejarDeSeguirUsuario(usuarioIdItem).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(v.getContext(), "Unfollow procesado correctamente", Toast.LENGTH_LONG).show();
                                        accionBtn.onAccionRealizada();
                                    }
                                    else {
                                        Toast.makeText(v.getContext(), "Ocurrio un problema al hacer unfollow al usuario", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(v.getContext(), "Error inesperado", Toast.LENGTH_LONG).show();
                                }
                            });
                            break;
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public UsuariosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosAdapter.ViewHolder holder, int position) {
        holder.bind(usuarios.get(position), callback, apiService);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public void actualizarUsuarios(List<UsuarioDto> nuevosUsuarios) {
        usuarios.clear();
        usuarios.addAll(nuevosUsuarios);
        notifyDataSetChanged();
    }

    public List<UsuarioDto> getUsuarios() {
        return usuarios;
    }
}
