public class Colocador extends Thread{
    private CadenaMontaje cinta;
    private int tipo;

    public Colocador(CadenaMontaje cinta, int tipo) {
        this.cinta = cinta;
        this.tipo = tipo;
    }
}
