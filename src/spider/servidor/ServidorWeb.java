package spider.servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServidorWeb{
    private String ubicacionServidores;
    private final List<File> archivos;
    String nombreServidor;
    public ServidorWeb(String nombreServidor){
        this.ubicacionServidores = "./servidores/";
        this.archivos = new ArrayList<>();
        if (existeServidor(nombreServidor)){
            this.nombreServidor = nombreServidor;
            cargarArchivos(nombreServidor);
        }
    }
    public String obtenerRespuesta(String pedido){
        String respuesta = 500+";<HTML><BODY><H1>Server error</H1></BODY></HTML>";
        String metodo,nombreServidor;
        String archivoDeBusqueda;
        if(formatoValido(pedido)){
            String[] pedidoInformacion = pedido.split(";",-1);
            metodo = pedidoInformacion[0];
            nombreServidor = this.nombreServidor;
            archivoDeBusqueda = pedidoInformacion[1];
            if(Objects.equals(metodo, "GET")){
                if (Objects.equals(archivoDeBusqueda, "") || Objects.equals(archivoDeBusqueda, "/")){
                    archivoDeBusqueda = "index.html";
                }
                if(existeServidor(nombreServidor)){
                    respuesta = buscarContenido(nombreServidor,archivoDeBusqueda);
                }
            }
        }else{
            respuesta= 400+";<HTML><BODY><H1>Bad request</H1></BODY></HTML>";
        }
        return respuesta;
    }
    private String buscarContenido(String nombreServidor,String contenidoDeBusqueda){
        String resultado;
        if(existeContenido(nombreServidor,contenidoDeBusqueda)){
            int codigo = 200;
            String contenido = traerInformacionDePagina(nombreServidor,contenidoDeBusqueda);
            if (contenido.equals("")){
                resultado = 501+";<HTML><BODY><H1>Not implemented</H1></BODY></HTML>";
            }else{
                resultado = codigo+";"+contenido;
            }
        }
        else{
            resultado = 404+";<HTML><BODY><H1>Not found</H1></BODY></HTML>";
        }
        return resultado;
    }
    private boolean existeContenido(String nombreServidor,String contenidoDeBusqueda){
        boolean respuesta = false;
        File directorio = new File(this.ubicacionServidores+nombreServidor+"/"+contenidoDeBusqueda);
        if (directorio.exists()) {
            respuesta = true;
        }
        return respuesta;

    }
    private boolean existeServidor(String nombreServidor){
        boolean respuesta = false;
        File directorio = new File(this.ubicacionServidores+nombreServidor);
        if (directorio.exists()) {
            respuesta = true;
        }
        return respuesta;
    }
    private String traerInformacionDePagina(String nombreServidor,String contenidoDeBusqueda){
        StringBuilder cadena= new StringBuilder();
        boolean bandera = true;
        InputStream ins = null;
        try {
            ins = new FileInputStream(this.ubicacionServidores +nombreServidor+"/"+contenidoDeBusqueda);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert ins != null;
        Scanner obj = new Scanner(ins);
        while (obj.hasNextLine())
            if(bandera){
                cadena = new StringBuilder(obj.nextLine());
                bandera = false ;
            }else {
                cadena.append(obj.nextLine());
            }

        return cadena.toString();
    }
    private void cargarArchivos(String nombreServidor){
        File carpeta = new File(ubicacionServidores+nombreServidor);
        String[] listado = carpeta.list();
        if (listado == null || listado.length == 0) {
            System.out.println("No hay elementos dentro de la carpeta actual");
        }
        else {
            for (String s : listado) {
                File archivo = new File(ubicacionServidores + nombreServidor + "/" + s);
                archivos.add(archivo);
            }
        }
    }
    @Override
    public String toString() {
        return nombreServidor;
    }
    private boolean formatoValido(String pedido){
        boolean respuesta = false;
        Pattern patron = Pattern.compile("[A-Za-z0-9]+[;][A-Za-z0-9./]*");
        Matcher mat = patron.matcher(pedido);
        if(mat.matches()){
            respuesta = true;
        }
        return respuesta;
    }
    public void iniciar() throws IOException{
        int puerto = 8080;
        ServerSocket servidor;
        Socket misocket;

        DataInputStream flujoEntrada;
        DataOutputStream flujoSalida;

        System.out.println("Estoy a la escucha");
        servidor = new ServerSocket(puerto);

        while (true) {

            misocket = servidor.accept();

            flujoEntrada = new DataInputStream(misocket.getInputStream());
            flujoSalida = new DataOutputStream(misocket.getOutputStream());

            String pedido;

            pedido = flujoEntrada.readUTF();
            System.out.println(pedido);

            String respuesta = this.obtenerRespuesta(pedido);

            System.out.println(respuesta);
            flujoSalida.writeUTF(respuesta);

            misocket.close();
        }
    }
}
