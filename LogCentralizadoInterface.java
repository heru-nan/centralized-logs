import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LogCentralizadoInterface extends Remote {
    void agregarLog(String clienteID, List<String> logs) throws RemoteException;
}
