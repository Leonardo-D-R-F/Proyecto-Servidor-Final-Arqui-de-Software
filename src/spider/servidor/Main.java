package spider.servidor;

import spider.servidor.Internet;
import spider.servidor.ServidorWeb;

import java.io.IOException;
//import spider.navegador.NavegadorWeb;

public class Main {
    public static void main(String[] args) throws IOException {
       ServidorWeb google = new ServidorWeb("www.google.com");
       ServidorWeb fcyt = new ServidorWeb("www.fcyt.umss.edu.bo");
       ServidorWeb csumss = new ServidorWeb("www.cs.umss.edu.bo");
       google.iniciar();
    }
}
