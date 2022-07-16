package spider.servidor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
       ServidorWeb leonardoroldan = new ServidorWeb("www.leonardoroldan.as");
       leonardoroldan.iniciar();
    }
}
