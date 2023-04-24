import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class LogCentralizadoCliente {
    private LogCentralizadoInterface servidor;
    private String logLocal;

    public LogCentralizadoCliente(String logLocal) {
        this.logLocal = logLocal;
    }

    public void conectar(String host, int puerto) throws Exception {
        Registry registry = LocateRegistry.getRegistry(host, puerto);
        servidor = (LogCentralizadoInterface) registry.lookup("LogCentralizado");
    }

    public void enviarLogs(List<String> logs) throws Exception {
        servidor.agregarLog(logLocal, logs);
    }
}
