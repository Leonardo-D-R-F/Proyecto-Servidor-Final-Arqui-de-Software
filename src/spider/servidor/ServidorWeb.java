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
    private String ip;
    private String puerto;
    private final List<File> archivos;
    String nombreServidor;
    public ServidorWeb(String nombreServidor){
        this.ubicacionServidores = "./servidores/";
        this.archivos = new ArrayList<>();
        // crea un spider.servidor verificando que haya un directorio
        // con ese nombre y opcionalmente carga los archivos
        // que encuentra
        if (existeServidor(nombreServidor)){
            this.nombreServidor = nombreServidor;
            cargarArchivos(nombreServidor);
        }
    }
    public String obtenerRespuesta(String pedido){
        //nombre del servidore
        //respueta tiene que contener, el html y codigo del resultado
        String respuesta = 500+";<h1>Server error</h1>";
        String metodo,url,nombreServidor = null;
        String archivoDeBusqueda = null;
        if(formatoValido(pedido)){
            String[] pedidoInformacion = pedido.split(";",-1);
            metodo = pedidoInformacion[0];
            nombreServidor = pedidoInformacion[1];
            archivoDeBusqueda = pedidoInformacion[2];
            if(Objects.equals(metodo, "GET")){
                if (Objects.equals(archivoDeBusqueda, "") || Objects.equals(archivoDeBusqueda, "/")){
                    archivoDeBusqueda = "index.html";
                }
                if(existeServidor(nombreServidor)){
                    respuesta = buscarContenido(nombreServidor,archivoDeBusqueda);
                }
            }
        }else{
            respuesta= 400+";<h1>Bad request</h1>";
        }
        return respuesta;
    }
    private String buscarContenido(String nombreServidor,String contenidoDeBusqueda){
        String resultado = null;
        if(existeContenido(nombreServidor,contenidoDeBusqueda)){
            int codigo = 200;
            String contenido = traerInformacionDePagina(nombreServidor,contenidoDeBusqueda);
            if (contenido == ""){
                resultado = 501+";<h1>Not implemented<h1>";
            }else{
                resultado = codigo+";"+contenido;
            }
        }
        else{
            resultado = 404+";<h1>Not found<h1>";
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
        String cadena= "";
        InputStream ins = null;
        try {
            ins = new FileInputStream(this.ubicacionServidores +nombreServidor+"/"+contenidoDeBusqueda);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner obj = new Scanner(ins);
        while (obj.hasNextLine())
        cadena = cadena +obj.nextLine();
        return cadena;
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
        Pattern patron = Pattern.compile("[A-Za-z]{1,}[;]{1}[A-Za-z0-9./]{1,}[;]{1}[A-Za-z./]{0,}");
        Matcher mat = patron.matcher(pedido);
        if(mat.matches()){
            respuesta = true;
        }
        return respuesta;
    }

    public void iniciar() throws IOException{
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

            String respuesta = this.obtenerRespuesta(pedido);

            System.out.println(respuesta);
            flujoSalida.writeUTF(respuesta);

            misocket.close();
        }
    }
}
