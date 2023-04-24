import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LogCentralizadoCron {
    private LogCentralizadoCliente cliente;
    private String logLocal;
    private int ultimaLineaEnviada;

    public LogCentralizadoCron(String host, String logLocal) {
        this.logLocal = logLocal;
        this.cliente = new LogCentralizadoCliente(logLocal);
        this.ultimaLineaEnviada = 0;
        try {
            cliente.conectar(host, 1099);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciar(int intervalo) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                enviarLogs();
            }
        }, 0, intervalo * 1000);
    }

    private void enviarLogs() {
        List<String> logsNuevos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(logLocal))) {
            String linea;
            int contadorLineas = 0;

            while ((linea = br.readLine()) != null) {
                contadorLineas++;

                if (contadorLineas > ultimaLineaEnviada) {
                    logsNuevos.add(linea);
                }
            }

            ultimaLineaEnviada = contadorLineas;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!logsNuevos.isEmpty()) {
            try {
                cliente.enviarLogs(logsNuevos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java LogCentralizadoCron <host> <archivo_log_local>");
            System.exit(1);
        }

        String host = args[0];
        String logLocal = args[1];

        LogCentralizadoCron cron = new LogCentralizadoCron(host, logLocal);
        cron.iniciar(10);
    }
}