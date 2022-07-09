package spider.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Internet {
    private List<ServidorWeb> servidores;

    public Internet (){
        servidores = new ArrayList<>();
    //    Thread mihilo = new Thread(this);
     ///  mihilo.start();
    }
    public void registrar(ServidorWeb servidorWeb){
        // debe verifica que el spider.servidor no este registrado
        if(!servidorExiste(servidorWeb)){
            servidores.add(servidorWeb);
        }
    }
    public String ejecutarPedido(String pedido){

        String tipoPedido = pedido.substring(0,pedido.indexOf(';'));
        String direccion = pedido.substring(pedido.indexOf(';')+1);

        String[] partes = direccion.split(";",-1);
        String nombreServidor = partes[0];

        String respuesta = "";
        System.out.println(pedido);
        if(formatoValido(pedido)){
            if(!Objects.equals(servidores.size(), "0")){
                if(Objects.equals(tipoPedido, "GET")){
                    ServidorWeb servidor =  this.obtenerServidor(nombreServidor);
                    if(servidor!= null){
                        respuesta = servidor.obtenerRespuesta(pedido);
                    }else{
                        respuesta = 500+";<h1>Server error</h1>";
                    }
                }
            }else{
                respuesta = 400+";<h1>Bad request</h1>";
            }
        }else{
            respuesta= 400+";<h1>Bad request</h1>";
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
        Pattern patron = Pattern.compile("[A-Za-z]{1,}[;]{1}[A-Za-z0-9./]{1,}[;]{1,}[A-Za-z./]{0,}");
        Matcher mat = patron.matcher(pedido);
        if(mat.matches()){
            respuesta = true;
        }
        return respuesta;
    }
    //@Override
    public void run() throws IOException {

        for (int i=0;i<servidores.size();i++) {
            ServidorWeb servidor = servidores.get(i);

        }

        int puerto = 8080;
        ServerSocket servidor =  null;
        Socket misocket = null;

        DataInputStream flujoEntrada;
        DataOutputStream flujoSalida;

        System.out.println("Estoy a la escucha");
        servidor = new ServerSocket(puerto);

            while (true) {

                misocket = servidor.accept();

                flujoEntrada = new DataInputStream(misocket.getInputStream());
                flujoSalida = new DataOutputStream(misocket.getOutputStream());

                String pedido = "";

                pedido = flujoEntrada.readUTF();
                System.out.println(pedido);

                String respuesta = this.ejecutarPedido(pedido);

                System.out.println(respuesta);
                flujoSalida.writeUTF(respuesta);

                misocket.close();
            }
    }
}
