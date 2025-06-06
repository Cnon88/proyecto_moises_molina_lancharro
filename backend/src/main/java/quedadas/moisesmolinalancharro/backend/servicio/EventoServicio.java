package quedadas.moisesmolinalancharro.backend.servicio;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quedadas.moisesmolinalancharro.backend.entidad.Evento;
import quedadas.moisesmolinalancharro.backend.entidad.Usuario;
import quedadas.moisesmolinalancharro.backend.repositorio.EventoRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static quedadas.moisesmolinalancharro.backend.entidad.Evento.EstadoEvento.PENDIENTE;

@Service
@Transactional
public class EventoServicio {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioServicio usuarioServicio;

    public Optional<Evento> obtenerPorId(int id) {
        return this.eventoRepository.findById(id);
    }

    public List<Evento> obtenerTodos() {
        return this.eventoRepository.findAll();
    }

    public Evento crearEvento(Evento eventoACrear, String email) {
        Optional<Usuario> usuarioOpt = this.usuarioServicio.obtenerUsuarioPorEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        eventoACrear.setEstado(PENDIENTE);
        Usuario creador = usuarioOpt.get();
        eventoACrear.setCreador(creador);
        return this.eventoRepository.save(eventoACrear);
    }

    public boolean inscribirseEnEvento(int idUsuario, int idEvento) {
        Optional<Usuario> usuarioOpt = usuarioServicio.obtenerUsuarioPorId(idUsuario);
        Optional<Evento> eventoOpt = obtenerPorId(idEvento);

        if (usuarioOpt.isEmpty() || eventoOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        Evento evento = eventoOpt.get();

        // Tiene que estar en pendiente para que deje inscribirse
        if (!evento.getEstado().equals(PENDIENTE)) {
            return false;
        }

        // Evita doble inscripción
        if (usuario.getEventosComoParticipante().contains(evento)) {
            return false;
        }

        usuario.getEventosComoParticipante().add(evento);
        usuarioServicio.guardarUsuario(usuario); // Método que guarda el usuario

        return true;
    }

    public boolean desinscribirseDelEvento(int idUsuario, int idEvento) {
        Optional<Usuario> usuarioOpt = usuarioServicio.obtenerUsuarioPorId(idUsuario);
        Optional<Evento> eventoOpt = obtenerPorId(idEvento);

        if (usuarioOpt.isEmpty() || eventoOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        Evento evento = eventoOpt.get();

        if (!evento.getEstado().equals(PENDIENTE)) {
            System.out.println("Return false");
            System.out.println(evento.getEstado());
            return false;
        }

        if (!usuario.getEventosComoParticipante().contains(evento)) {
            System.out.println("Return false");
            System.out.println("no contenido");
            return false;
        }

        // Eliminar relación de ambos lados (para evitar inconsistencias en memoria)
        usuario.getEventosComoParticipante().remove(evento);
        evento.getParticipantes().remove(usuario);

        usuarioServicio.guardarUsuario(usuario);
        return true;
    }


    public boolean eliminarEventoSiEsCreador(int idUsuario, int idEvento) {
        Optional<Evento> eventoOpt = obtenerPorId(idEvento);

        if (eventoOpt.isEmpty()) {
            return false;
        }

        Evento evento = eventoOpt.get();

        if (evento.getCreador().getId() != idUsuario) {
            return false;
        }

        // Desvincular participantes
        for (Usuario participante : evento.getParticipantes()) {
            participante.getEventosComoParticipante().remove(evento);
        }
        evento.getParticipantes().clear();

        eventoRepository.delete(evento);
        return true;
    }

}
