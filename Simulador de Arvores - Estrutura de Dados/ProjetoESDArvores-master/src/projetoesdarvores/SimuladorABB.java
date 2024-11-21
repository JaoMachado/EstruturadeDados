package projetoesdarvores;

import aesd.ds.interfaces.List;
import br.com.davidbuzatto.jsge.collision.CollisionUtils;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.BLACK;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import projetoesdarvores.esd.ArvoreBinariaBusca;
import static projetoesdarvores.utils.Utils.desenharSeta;

/**
 * Simulador de árvores binárias de busca: Simula as operações de inserir e
 * remover chaves; Simula os percursos (pré-ordem, em ordem, pós-ordem e em
 * nível).
 *
 * @author Prof. Dr. David Buzatto
 */
public class SimuladorABB extends EngineFrame {

    private ArvoreBinariaBusca<Integer, String> arvore;
    private List<ArvoreBinariaBusca.Node<Integer, String>> nos;
    private int margemCima;
    private int margemEsquerda;
    private int raio;
    private int espacamento;

    public SimuladorABB() {
        super(1200, 800, "Simulador de Árvores Binárias de Busca", 60, true);
    }

    @Override
    public void create() {
        arvore = new ArvoreBinariaBusca<>();
        arvore.put(5, "cinco");
        arvore.put(2, "dois");
        arvore.put(10, "dez");
        arvore.put(15, "quinze");
        arvore.put(12, "doze");
        arvore.put(1, "um");
        arvore.put(3, "três");
        nos = arvore.coletarParaDesenho();
        margemCima = 180;
        margemEsquerda = 50;
        raio = 30;
        espacamento = 60;
        resetaCores(arvore);
        
        SwingUtilities.invokeLater(() -> {
            desenharOpcoesArvore();
        });
    }

    private void desenharOpcoesArvore() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        Border roundedBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Comandos");
        controlPanel.setBorder(new CompoundBorder(roundedBorder, new EmptyBorder(10, 10, 10, 10)));

        JButton insertButton = new JButton("Inserir (put)");
        insertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        insertButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                String entrada = JOptionPane.showInputDialog(
                        this,
                        "Insira um número inteiro:",
                        "Inserir",
                        JOptionPane.QUESTION_MESSAGE);
                try {
                    int valor = Integer.parseInt(entrada);
                    arvore.put(valor, String.valueOf(valor));
                    nos = arvore.coletarParaDesenho();
                    resetaCores(arvore);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Valor inválido! Por favor, insira um número inteiro.");
                }
            });
        });

        JButton clearButton = new JButton("Limpar");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearButton.addActionListener(e -> {
            arvore.clear();
            nos = arvore.coletarParaDesenho();
            repaint();
        });

        JPanel traversalPanel = new JPanel();
        traversalPanel.setLayout(new BoxLayout(traversalPanel, BoxLayout.Y_AXIS));
        Border traversalBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Percursos");
        traversalPanel.setBorder(new CompoundBorder(traversalBorder, new EmptyBorder(5, 5, 5, 5)));

        JButton preOrderButton = new JButton("Pré-Ordem");
        preOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        preOrderButton.addActionListener(e -> {
            new Thread(() -> {
                System.out.println("Buscando em Pré-Ordem:");
                buscarPreOrdem(arvore.getRoot());
                resetaCores(arvore);
            }).start();
        });

        JButton inOrderButton = new JButton("Em Ordem");
        inOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        inOrderButton.addActionListener(e -> {
            new Thread(() -> {
                System.out.println("Buscando em Ordem:");
                buscarEmOrdem(arvore.getRoot());
                resetaCores(arvore);
            }).start();
        });

        JButton postOrderButton = new JButton("Pós-Ordem");
        postOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        postOrderButton.addActionListener(e -> {
            new Thread(() -> {
                System.out.println("Buscando em Pós-Ordem:");
                buscarPosOrdem(arvore.getRoot());
                resetaCores(arvore);
            }).start();
        });

        JButton levelOrderButton = new JButton("Em Nível");
        levelOrderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelOrderButton.addActionListener(e -> {
            new Thread(() -> {
                System.out.println("Buscando em Nível:");
                buscarEmNivel(arvore.getRoot());
                resetaCores(arvore);
            }).start();
        });

        traversalPanel.add(preOrderButton);
        traversalPanel.add(Box.createVerticalStrut(5));
        traversalPanel.add(inOrderButton);
        traversalPanel.add(Box.createVerticalStrut(5));
        traversalPanel.add(postOrderButton);
        traversalPanel.add(Box.createVerticalStrut(5));
        traversalPanel.add(levelOrderButton);

        controlPanel.add(insertButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(clearButton);
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(traversalPanel);

        controlPanel.setPreferredSize(new Dimension(160, getHeight() / 2));

        add(controlPanel, BorderLayout.WEST);
    }


    @Override
    public void update(double delta) {
        Vector2 mousePos = getMousePositionPoint();


        if (isMouseButtonPressed(MOUSE_BUTTON_LEFT)) {

            for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {

                Vector2 centro = new Vector2(
                        espacamento * no.ranque + margemEsquerda,
                        espacamento * no.nivel + margemCima
                );

                if (CollisionUtils.checkCollisionPointCircle(mousePos, centro, raio)) {
                    SwingUtilities.invokeLater(() -> {
                        int opcao = JOptionPane.showConfirmDialog(
                                this,
                                "Remover o nó " + no.key + "?",
                                "Confirmação",
                                JOptionPane.YES_NO_OPTION);
                        if (opcao == JOptionPane.YES_OPTION) {
                            arvore.delete(no.key);
                            nos = arvore.coletarParaDesenho();
                        }
                    });
                }
            }
        }

    }

    @Override
    public void draw() {
        desenharConexoes();

        for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
            desenharNo(no, espacamento, espacamento);
        }

        desenharRaiz();
    }

     public void desenharOpcoes(int x, int y) {
        drawText("(N) Inserir", x, y, 18, BLUE);
        drawText("(C) Limpar", x, y+=20, 18, BLUE);
        drawText("(1) Pré-Ordem", x, y += 20, 18, BLACK);
        drawText("(2) Em Ordem", x, y += 20, 18, BLACK);
        drawText("(3) Pós-Ordem", x, y += 20, 18, BLACK);
        drawText("(4) Em Nível", x, y += 20, 18, BLACK);
    }

    private void desenharNo(ArvoreBinariaBusca.Node<Integer, String> no, int espHorizontal, int espVertical) {
        fillCircle(espHorizontal * no.ranque + margemEsquerda, espVertical * no.nivel + margemCima, raio, no.cor);
        drawCircle(espHorizontal * no.ranque + margemEsquerda, espVertical * no.nivel + margemCima, raio, BLACK);
        drawText(Integer.toString(no.key), espHorizontal * no.ranque + margemEsquerda - 5, espVertical * no.nivel + margemCima - 5, 18, BLACK);
    }

    private void desenharConexoes() {
        for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
            if (no.left != null) {
                desenharLinhaEntreNos(no, no.left);
            }
            if (no.right != null) {
                desenharLinhaEntreNos(no, no.right);
            }
        }
    }

    private void desenharLinhaEntreNos(ArvoreBinariaBusca.Node<Integer, String> pai, ArvoreBinariaBusca.Node<Integer, String> filho) {
        int xPai = espacamento * pai.ranque + margemEsquerda;
        int yPai = espacamento * pai.nivel + margemCima;
        int xFilho = espacamento * filho.ranque + margemEsquerda;
        int yFilho = espacamento * filho.nivel + margemCima;

        drawLine(xPai, yPai, xFilho, yFilho, BLACK);
    }

    private void buscarPreOrdem(ArvoreBinariaBusca.Node<Integer, String> no) {
        if (no == null) {
            return;
        }

        no.cor = Color.RED;
        repaint();
        System.out.println(no.key + " - " + no.value);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimuladorABB.class.getName()).log(Level.SEVERE, null, ex);
        }

        buscarPreOrdem(no.left);
        buscarPreOrdem(no.right);
    }

    private void buscarPosOrdem(ArvoreBinariaBusca.Node<Integer, String> no) {
        if (no == null) {
            return;
        }

        buscarPosOrdem(no.left);

        buscarPosOrdem(no.right);

        no.cor = Color.RED;
        repaint();

        System.out.println(no.key + " - " + no.value);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimuladorABB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscarEmOrdem(ArvoreBinariaBusca.Node<Integer, String> no) {
        if (no == null) {
            return;
        }

        buscarEmOrdem(no.left);

        no.cor = Color.RED;
        repaint();

        System.out.println(no.key + " - " + no.value);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SimuladorABB.class.getName()).log(Level.SEVERE, null, ex);
        }

        buscarEmOrdem(no.right);
    }

    private void buscarEmNivel(ArvoreBinariaBusca.Node<Integer, String> raiz) {
        if (raiz == null) {
            return;
        }

        Queue<ArvoreBinariaBusca.Node<Integer, String>> fila = new LinkedList<>();
        fila.add(raiz);

        while (!fila.isEmpty()) {
            ArvoreBinariaBusca.Node<Integer, String> no = fila.poll();

            no.cor = Color.RED;
            repaint();

            System.out.println(no.key + " - " + no.value);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorABB.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (no.left != null) {
                fila.add(no.left);
            }
            if (no.right != null) {
                fila.add(no.right);
            }
        }
    }

    public void resetaCores(ArvoreBinariaBusca a) {
        for (ArvoreBinariaBusca.Node<Integer, String> no : nos) {
            no.cor = WHITE;
        }
        repaint();
    }

    private void desenharRaiz() {
        if (arvore.getRoot() != null) {
            ArvoreBinariaBusca.Node<Integer, String> raiz = arvore.getRoot();

            Vector2 centroRaiz = new Vector2(
                    espacamento * raiz.ranque + margemEsquerda,
                    espacamento * raiz.nivel + margemCima
            );

            drawText("Raiz", centroRaiz.x - 20, centroRaiz.y - 60, 18, BLACK);
            drawLine(centroRaiz.x, centroRaiz.y - 45, centroRaiz.x, centroRaiz.y - raio, BLACK);
            
            desenharSeta(this, centroRaiz.x, centroRaiz.y - raio, 10, 90, BLACK);
        }
    }

    public static void main(String[] args) {
        new SimuladorABB();
    }

}
