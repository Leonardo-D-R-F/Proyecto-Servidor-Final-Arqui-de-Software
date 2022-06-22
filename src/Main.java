import servidor.Internet;
import servidor.PedidoHTTP;
import servidor.RespuestaHTTP;
import servidor.ServidorWeb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import spider.navegador.NavegadorWeb;

public class Main {
    public static void main(String[] args) throws IOException {
       ServidorWeb google = new ServidorWeb("www.google.com");
       ServidorWeb fcyt = new ServidorWeb("www.fcyt.umss.edu.bo");
       ServidorWeb csumss = new ServidorWeb("www.cs.umss.edu.bo");
       Internet internet = new Internet();
       internet.registrar(google);
       internet.registrar(fcyt);
       internet.registrar(csumss);
       //NavegadorWeb aracnido = new NavegadorWeb(internet);
       //aracnido.run();
        internet.run();
    }

}
