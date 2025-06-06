package quedadas.moisesmolinalancharro.backend.excepciones;

public class UsuarioExistentePorNombreException extends Exception {
    public UsuarioExistentePorNombreException(String username) {
        super("Usuario ya existe con ese nickname (" + username + ")");
    }
}
