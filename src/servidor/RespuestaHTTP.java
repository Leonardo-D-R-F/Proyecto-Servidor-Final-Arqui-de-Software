package servidor;

public class RespuestaHTTP {
    private int codigoRespuesta;
    private String recurso;

    public RespuestaHTTP(int codigoRespuesta,String recurso){
        this.codigoRespuesta = codigoRespuesta;
        this.recurso = recurso;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public String getRecurso() {
        return recurso;
    }

}
