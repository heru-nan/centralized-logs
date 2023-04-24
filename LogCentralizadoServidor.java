import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogCentralizadoServidor extends UnicastRemoteObject implements LogCentralizadoInterface {

    private int correlativo = 0;
    private String logCentralizado = "log_centralizado.txt";

    public LogCentralizadoServidor() throws RemoteException {
    }

    @Override
    public synchronized void agregarLog(String clienteID, List<String> logs) throws RemoteException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        try (FileWriter fileWriter = new FileWriter(logCentralizado, true)) {
            for (String log : logs) {
                LocalDateTime now = LocalDateTime.now();
                String fecha = dateFormatter.format(now);
                String hora = timeFormatter.format(now);

                correlativo++;
                String entradaLog = String.format("%d;%s;%s;%s - %s%n", correlativo, fecha, hora, clienteID, log);
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
