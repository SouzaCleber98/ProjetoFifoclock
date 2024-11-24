package models;

import java.util.ArrayList;
import java.util.List;


public class Fifo {
    private final List<Page> pages;
    private final int capacity;
    private int pointer; // Representa o ponteiro do "relógio"

    public Fifo(int capacity) {
        this.capacity = capacity;
        this.pages = new ArrayList<>();
        this.pointer = 0;
    }

    // Método para simular a adição de uma página
    public void addPage(int pageId) {
        // Verifica se a página já está presente
        // Antes de adicionar a página, iremos verificar se ela está presente
        //ai mudamos o clock
        for (Page page : pages) {
            if (page.id == pageId) {
                System.out.println("Página " + pageId + " já está na memória.");
                //ai mudamos o clock
                page.referenceBit = true; // Atualiza o bit de referência
                return;
            }
            //Se o número não está presente, então iremos tentar adicionar o número
        }
        // Caso a memória tenha espaço disponível
        // Vamos verificar se o tamanho atual do vetor (pages.size()) é menor que a capacidade total (capacity).

        if (pages.size() < capacity) {
            // Se for menor, há espaço vazio na memória.
            // Adicionamos a nova página ao vetor (lista) de páginas.
            pages.add(new Page(pageId));

            // Caso contrário, a memória está cheia.
            // Precisamos substituir uma página utilizando o algoritmo do relógio.
        } else {
            // Substituir página usando o algoritmo do relógio
            replacePage(pageId);// Chama o método de substituição

        }
    }
//método de substituição
    private void replacePage(int pageId) {
        while (true) {
            //eu pego um objeto de uma lista do mesmo objeto
            Page current = pages.get(pointer);
            // se for false
            if (!current.referenceBit) //transforma o false em true
                {
                // Substitui a página atual
                System.out.println("Página " + current.id + " substituída por página " + pageId + ".");
                pages.set(pointer, new Page(pageId));//substitue a página atual
                pointer = (pointer + 1) % capacity; // Move o ponteiro
                break;
            } else {
                // Reseta o bit de referência e avança o ponteiro
                current.referenceBit = false;
                pointer = (pointer + 1) % capacity;
            }
        }
    }

    // Método para exibir o estado atual da memória
    public void printMemoryState() {
        System.out.println("Estado atual da memória:");
        for (int i = 0; i < pages.size(); i++) {
            Page page = pages.get(i);
            System.out.println("Posição " + i + ": Página " + page.id + " (Bit: " + page.referenceBit + ")");
        }
    }
    //get
    public List<Page> getPages() {
        return pages;
    }


    public int getCapacity() {
        return capacity;
    }

    public int getPointer() {
        return pointer;
    }
}