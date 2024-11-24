package view;

import models.Fifo;
import models.Page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FifoView extends JFrame {
    private JTable table;
    private JTextArea txtLog;
    private JTextField txtPageSequence;
    private JLabel lblCurrentPage;
    private JButton btnSetSequence, btnNext, btnRestart;
    private int currentPageIndex = 0;
    private int[] pageSequence;
    private Fifo fifo;

    public FifoView(Fifo fifo) {
        this.fifo = fifo;

        setTitle("Simulação do FIFO Clock");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Painel para entrada de sequência de páginas
        JPanel configPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        txtPageSequence = new JTextField();
        btnSetSequence = new JButton("Definir Sequência");
        btnSetSequence.addActionListener(e -> setPageSequence());
        configPanel.add(new JLabel("Sequência de Páginas:"));
        configPanel.add(txtPageSequence);
        configPanel.add(btnSetSequence);

        add(configPanel, BorderLayout.NORTH);

        // Tabela para exibir o estado da memória
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Área de log
        txtLog = new JTextArea();
        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.SOUTH);

        // Painel para exibir informações da simulação
        JPanel infoPanel = new JPanel(new GridLayout(1, 1, 10, 10));
        lblCurrentPage = new JLabel("Página Atual: Nenhuma");
        infoPanel.add(lblCurrentPage);
        add(infoPanel, BorderLayout.EAST);

        // Painel para botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnNext = new JButton("Próxima Página");
        btnNext.setEnabled(false);
        btnNext.addActionListener(e -> processNextPage());
        btnRestart = new JButton("Reiniciar");
        btnRestart.addActionListener(e -> restartSimulation());
        buttonPanel.add(btnNext);
        buttonPanel.add(btnRestart);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void setPageSequence() {
        try {
            String[] pages = txtPageSequence.getText().split(",");
            pageSequence = new int[pages.length];
            for (int i = 0; i < pages.length; i++) {
                pageSequence[i] = Integer.parseInt(pages[i].trim());
            }
            txtLog.append("Sequência de páginas definida: " + txtPageSequence.getText() + "\n");
            txtPageSequence.setEnabled(false);
            btnSetSequence.setEnabled(false);
            btnNext.setEnabled(true);
            initializeTable();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, insira uma sequência de páginas válida (ex: 1,2,3,4).");
        }
    }

    private void initializeTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Estado da Memória");
        table.setModel(model);
        updateTable();
    }

    private void processNextPage() {
        if (currentPageIndex >= pageSequence.length) {
            txtLog.append("Simulação concluída!\n");
            btnNext.setEnabled(false);
            return;
        }

        int pageId = pageSequence[currentPageIndex];
        lblCurrentPage.setText("Página Atual: " + pageId);
        txtLog.append("Acessando a página " + pageId + "...\n");

        fifo.addPage(pageId); // Adiciona ou substitui a página
        updateTable();

        currentPageIndex++;
    }

    private void updateTable() {
        List<Page> memoryState = fifo.getPages();
        DefaultTableModel model = new DefaultTableModel();

        // Configurando as colunas
        model.addColumn("Estado da Memória");

        // Preenchendo os dados
        for (Page page : memoryState) {
            model.addRow(new Object[]{"ID: " + page.id + ", Bit: " + page.referenceBit});
        }

        table.setModel(model);
    }

    private void restartSimulation() {
        txtPageSequence.setEnabled(true);
        txtPageSequence.setText("");
        btnSetSequence.setEnabled(true);
        btnNext.setEnabled(false);
        txtLog.setText("");
        lblCurrentPage.setText("Página Atual: Nenhuma");
        currentPageIndex = 0;
        fifo = new Fifo(fifo.getCapacity());
        initializeTable();
        txtLog.append("Simulação reiniciada.\n");
    }
}
