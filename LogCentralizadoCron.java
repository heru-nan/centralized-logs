import java.io.*;
import java.util.*;

public class LogCentralizadoCron {
    private LogCentralizadoCliente cliente;
    private String logLocal;
    private int ultimaLineaEnviada;
    private String estadoArchivo;

    public LogCentralizadoCron(String host, String logLocal) {
        this.logLocal = logLocal;
        this.cliente = new LogCentralizadoCliente(logLocal);
        this.estadoArchivo = logLocal + ".estado";

        try {
            cliente.conectar(host, 1099);
            cargarEstado();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarEstado() {
        try (BufferedReader br = new BufferedReader(new FileReader(estadoArchivo))) {
            String linea = br.readLine();
            ultimaLineaEnviada = linea != null ? Integer.parseInt(linea) : 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarEstado() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(estadoArchivo))) {
            pw.println(ultimaLineaEnviada);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iniciar(int intervalo) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                enviarLogs();
                guardarEstado();
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