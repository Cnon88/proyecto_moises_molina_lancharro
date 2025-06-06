package quedadas.moisesmolinalancharro.backend.excepciones;

public class UsuarioExistentePorEmailException extends Exception {
    public UsuarioExistentePorEmailException(String email) {
        super("Usuario ya existe con ese nickname (" + email + ")");
    }
}
