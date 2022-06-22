package servidor;

import servidor.PedidoHTTP;
import servidor.RespuestaHTTP;
import servidor.ServidorWeb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Internet{
    private List<ServidorWeb> servidores;
    public Internet (){
        servidores = new ArrayList<>();
//        Thread mihilo = new Thread(this);
//        mihilo.start();
    }
    public void registrar(ServidorWeb servidorWeb){
        // debe verifica que el servidor no este registrado
        if(!servidorExiste(servidorWeb)){
            servidores.add(servidorWeb);
        }
    }
    public RespuestaHTTP ejecutarPedido(PedidoHTTP pedido){
        String[] urlDireccion = pedido.getUrl().split(";",-1);
        String nombreServidor = urlDireccion[0];

        RespuestaHTTP respuesta = null;
        if(formatoValido(pedido.getUrl())){
            if(!Objects.equals(servidores.size(), "0")){
                if(Objects.equals(pedido.getMetodo(), "GET")){
                    ServidorWeb servidor =  this.obtenerServidor(nombreServidor);
                    if(servidor!= null){
                        respuesta = servidor.obtenerRespuesta(pedido);
                    }else{
                        respuesta = new RespuestaHTTP(500,"<h1>Server error<h1>");
                    }
                }
            }else{
                respuesta = new RespuestaHTTP(400,"<h1>Bad request<h1>");
            }
        }else{
            respuesta= new RespuestaHTTP(400,"<h1>Bad request<h1>");
        }

        return respuesta;
    }
    private ServidorWeb obtenerServidor(String nombreServidor){
        ServidorWeb respuesta = null;
        for (int i=0;i<servidores.size();i++) {
            ServidorWeb servidor = servidores.get(i);
            if(Objects.equals(servidor.toString(), nombreServidor)){
                respuesta = servidor;
            }

        }
        return respuesta;
    }
    private boolean servidorExiste(ServidorWeb servidorWeb){
        boolean respuesta = false;
        for (int i=0;i<servidores.size();i++) {
            ServidorWeb servidor = servidores.get(i);
            if(Objects.equals(servidor, servidorWeb)){
                respuesta = true;
            }
        }
        return  respuesta;
    }
    private boolean formatoValido(String pedido){
        boolean respuesta = false;
        Pattern patron = Pattern.compile("[A-Za-z0-9./]{1,}[;]{0,}[A-Za-z./]{0,}");
        Matcher mat = patron.matcher(pedido);
        if(mat.matches()){
            respuesta = true;
        }
        return respuesta;
    }
    //@Override
    public void run() throws IOException {
        ServerSocket servidor =  null;
        Socket misocket = null;

        DataInputStream flujoEntrada;
        DataOutputStream flujoSalida;
        final int puerto = 9999;

        System.out.println("Estoy a la escucha");
        servidor = new ServerSocket(puerto);

            while (true) {

                misocket = servidor.accept();

                flujoEntrada = new DataInputStream(misocket.getInputStream());
                flujoSalida = new DataOutputStream(misocket.getOutputStream());

                String tipoPeticion = "";
                String url = "";

                tipoPeticion = flujoEntrada.readUTF();
                url = flujoEntrada.readUTF();

                PedidoHTTP pedidoHTTP = new PedidoHTTP(tipoPeticion, url);

                RespuestaHTTP respuestaHTTP = this.ejecutarPedido(pedidoHTTP);

                String codigo = respuestaHTTP.getCodigoRespuesta() + "";
                String mensaje = respuestaHTTP.getRecurso();

                System.out.println(codigo + mensaje);
                flujoSalida.writeUTF(codigo);
                flujoSalida.writeUTF(mensaje);

                misocket.close();
            }


    }
}
