package quedadas.moisesmolinalancharro.backend.excepciones;

public class ContrasenaInvalidaException extends Exception {
    public ContrasenaInvalidaException() {
        super("Contraseña invalida, debe cumplir los requisitos");
    }
}
