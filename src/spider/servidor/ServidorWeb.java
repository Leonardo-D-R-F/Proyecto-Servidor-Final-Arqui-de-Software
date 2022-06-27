package spider.servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ServidorWeb {
    private String ubicacionServidores;
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

        String metodo,url,nombreServidor = null,archivoDeBusqueda;
        if(pedido.contains(";")){
           // String[] pedidoMetodoUrl = pedido.toString().split(",",-1);
            url = pedido;
            String[] pedidoUrl = url.split(";",-1);
            nombreServidor = pedidoUrl[0];
            archivoDeBusqueda = pedidoUrl[1];
        }else{
            //String[] pedidoMetodoUrl = pedido.getUrl().split(";",-1);
            nombreServidor= pedido;
            archivoDeBusqueda = "index.html";
        }
        if (Objects.equals(archivoDeBusqueda, "") || Objects.equals(archivoDeBusqueda, "/")){
            archivoDeBusqueda = "index.html";
        }
        String respuesta = 500+";<h1>Server error<h1>";
        if(existeServidor(nombreServidor)){
           respuesta = buscarContenido(nombreServidor,archivoDeBusqueda);
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
}

//un ejercicio para llevar la orientada a objetos a la forma mas extrema
//ayuda a pensar en los principios mas simples en el desarrollo
//- Calentamiento
//- implementar 