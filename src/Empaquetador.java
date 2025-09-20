public class Empaquetador extends Thread{
    private CadenaMontaje cinta;
    private int tipo;

    public Empaquetador(CadenaMontaje cinta, int tipo) {
        this.cinta = cinta;
        this.tipo = tipo;
    }
}
