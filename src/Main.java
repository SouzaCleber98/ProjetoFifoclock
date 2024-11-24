import models.Fifo;
import view.FifoView;

public class Main {
    public static void main(String[] args) {
        // Define a capacidade da memória
        int memoryCapacity = 3;

        // Cria a instância de Fifo
        Fifo fifo = new Fifo(memoryCapacity);

        // Cria a interface gráfica passando o objeto Fifo
        new FifoView(fifo);
    }
}
