import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.time.Instant;


public class LogCentralizadoServidor extends UnicastRemoteObject implements LogCentralizadoInterface {

    private int correlativo = 0;
    private String logCentralizado = "log_centralizado.txt";

    public LogCentralizadoServidor() throws RemoteException {
    }

   @Override
    public synchronized void agregarLog(String clienteID, List<String> logs) throws RemoteException {
        try (FileWriter fileWriter = new FileWriter(logCentralizado, true)) {
            for (String log : logs) {
                long timestamp = Instant.now().getEpochSecond();

                String entradaLog = String.format("%s;%d;%s%n", log, timestamp, clienteID);
                fileWriter.write(entradaLog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    try {
        LogCentralizadoServidor servidor = new LogCentralizadoServidor();
        Naming.rebind("LogCentralizado", servidor);
        System.out.println("Servidor de log centralizado iniciado.");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
