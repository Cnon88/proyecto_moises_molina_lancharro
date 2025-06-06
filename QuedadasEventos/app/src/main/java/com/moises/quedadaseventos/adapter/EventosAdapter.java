package com.moises.quedadaseventos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.activitiy.DetalleEventoActivity;
import com.moises.quedadaseventos.dto.EventoDto;

import java.util.ArrayList;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<EventoDto> eventos;
    private AccionBotonEventoItem accionBotonEventoItem;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemTituloEvento;
        private final TextView tvItemFechaEvento;
        private final ImageView ivItemEventoImagen;
        private final Button btnAccion;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            tvItemTituloEvento = (TextView) view.findViewById(R.id.tvItemTituloEvento);
            tvItemFechaEvento = (TextView) view.findViewById(R.id.tvItemFechaEvento);
            ivItemEventoImagen = (ImageView) view.findViewById(R.id.ivItemEventoImagen);
            btnAccion = (Button) view.findViewById(R.id.btnAccionItemEvento);
        }

        public TextView getTvItemTituloEvento() {
            return tvItemTituloEvento;
        }

        public TextView getTvItemFechaEvento() {
            return tvItemFechaEvento;
        }

        public ImageView getIvItemEventoImagen() {
            return ivItemEventoImagen;
        }

        public Button getBtnAccion() {
            return btnAccion;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     */
    public EventosAdapter(Context context, ArrayList<EventoDto> dataset, AccionBotonEventoItem accion) {
        ctx = context;
        eventos = dataset;
        accionBotonEventoItem = accion;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_evento, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        EventoDto evento = eventos.get(position);

        viewHolder.getTvItemTituloEvento().setText(evento.getTitulo());
        viewHolder.getTvItemFechaEvento().setText(evento.getFechaEvento().replace("T", " "));

        String nombreJuego = evento.getJuego().getNombre();
        String imgName = null;
        switch (nombreJuego) {
            case "Paleo":
                imgName = "paleo.jpeg";
                break;
            case "After the virus":
                imgName = "after_the_virus.jpeg"; // Add your image names
                break;
            case "Guerra del Anillo":
                imgName = "guerra_del_anillo.jpeg";
                break;
            case "Pequeñas grandes mazmorras":
                imgName = "pequenas_grandes_mazmorras.jpeg";
                break;
        }

        // Get resource ID from drawable name
        int resourceId = ctx.getResources().getIdentifier(
                imgName.replace(".jpeg", ""), // Remove extension
                "drawable",
                ctx.getPackageName()
        );

        if (resourceId != 0) {
            viewHolder.getIvItemEventoImagen().setImageResource(resourceId);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(ctx, DetalleEventoActivity.class);
            intent.putExtra("eventoId", evento.getId());
            ctx.startActivity(intent);
        });

        switch (accionBotonEventoItem.getTipoAccion()) {
            case INSCRIBIRSE:
                viewHolder.getBtnAccion().setVisibility(View.VISIBLE);
                viewHolder.getBtnAccion().setText(R.string.inscribirse);
                viewHolder.getBtnAccion().setBackgroundColor(ContextCompat.getColor(ctx, R.color.primary_color));
                viewHolder.getBtnAccion().setText("+");
                break;

            case DESINSCRIBIRSE:
                viewHolder.getBtnAccion().setVisibility(View.VISIBLE);
                viewHolder.getBtnAccion().setText(R.string.desinscribirse);
                viewHolder.getBtnAccion().setBackgroundColor(ContextCompat.getColor(ctx, R.color.accent_color));
                viewHolder.getBtnAccion().setText("-");
                break;

            case ELIMINAR:
                viewHolder.getBtnAccion().setVisibility(View.VISIBLE);
                viewHolder.getBtnAccion().setText(R.string.eliminar);
                viewHolder.getBtnAccion().setBackgroundColor(ContextCompat.getColor(ctx, R.color.status_inactive));
                viewHolder.getBtnAccion().setText("X");
                break;
        }

        viewHolder.getBtnAccion().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonEventoItem.realizarAccion(evento.getId());
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return eventos.size();
    }
}