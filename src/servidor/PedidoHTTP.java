package servidor;

public class PedidoHTTP {
    private String metodo;
    private String uRL;

    public PedidoHTTP(String metodo, String url) {
        this.metodo = metodo;
        this.uRL = url;
    }

    public String getMetodo() {
        return this.metodo;
    }

    public String getUrl() {
        return this.uRL;
    }
}
