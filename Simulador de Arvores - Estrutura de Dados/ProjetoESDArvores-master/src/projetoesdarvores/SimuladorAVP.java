package projetoesdarvores;

import aesd.ds.implementations.linear.LinkedQueue;
import aesd.ds.interfaces.List;
import br.com.davidbuzatto.jsge.collision.CollisionUtils;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.BLACK;
import br.com.davidbuzatto.jsge.math.Vector2;
import com.sun.jdi.Value;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.RenderingHints.Key;
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
import projetoesdarvores.esd.ArvoreVermelhoPreto;
import static projetoesdarvores.utils.Utils.desenharSeta;

/**
 * Simulador de árvores vermelho e preto:
 *     Simula as operações de inserir e remover chaves;
 *     Simula os percursos (pré-ordem, em ordem, pós-ordem e em nível).
 * 
 * @author Prof. Dr. David Buzatto
 */
public class SimuladorAVP extends EngineFrame {
    private ArvoreVermelhoPreto<Integer, String> arvore;
    private List<ArvoreVermelhoPreto.Node<Integer, String>> nos;
    private int margemCima;
    private int margemEsquerda;
    private int raio;
    private int espacamento;

    public SimuladorAVP() {
        super( 1200, 800, "Simulador de Árvores Vermelho e Preto", 60, true );
    }

    @Override
    public void create() {
        arvore = new ArvoreVermelhoPreto<>();
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

    @Override
    public void update( double delta ) {
        Vector2 mousePos = getMousePositionPoint();

        if (isMouseButtonPressed(MOUSE_BUTTON_LEFT)) {

            for (ArvoreVermelhoPreto.Node<Integer, String> no : nos) {

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
                            resetaCores(arvore);
                        }
                    });
                }
            }
        }            
    }

    @Override
    public void draw() {
        desenharConexoes();

        for (ArvoreVermelhoPreto.Node<Integer, String> no : nos) {
            desenharNo(no, espacamento, espacamento);
        }

        desenharRaiz();
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

    private void desenharNo(ArvoreVermelhoPreto.Node<Integer, String> no, int espHorizontal, int espVertical) {
        int x = espHorizontal * no.ranque + margemEsquerda;
        int y = espVertical * no.nivel + margemCima;

        if (no.color == ArvoreVermelhoPreto.NodeColor.RED) {
            ArvoreVermelhoPreto.Node<Integer, String> pai = encontrarPai(no);
            if (pai != null) {
                x = (pai.left == no) ? (x - espHorizontal / 2  + 10) : (x + espHorizontal / 2);
                y = espacamento * pai.nivel + margemCima;
            }
           
        }

        fillCircle(x, y, raio, no.cor);
        drawCircle(x, y, raio, BLACK);
        drawText(Integer.toString(no.key), x - 5, y - 5, 18, BLACK);

    }
    
    private ArvoreVermelhoPreto.Node<Integer, String> encontrarPai(ArvoreVermelhoPreto.Node<Integer, String> no) {
        for (ArvoreVermelhoPreto.Node<Integer, String> potencialPai : nos) {
            if (potencialPai.left == no || potencialPai.right == no) {
                return potencialPai;
            }
        }
        return null;
    }

    private void desenharConexoes() {
        for (ArvoreVermelhoPreto.Node<Integer, String> no : nos) {
            if (no.left != null) {
                desenharLinhaEntreNos(no, no.left);
            }
            if (no.right != null) {
                desenharLinhaEntreNos(no, no.right);
            }
        }
    }

    private void desenharLinhaEntreNos(ArvoreVermelhoPreto.Node<Integer, String> pai, ArvoreVermelhoPreto.Node<Integer, String> filho) {
        
        int xPai = espacamento * pai.ranque + margemEsquerda;
        int yPai = espacamento * pai.nivel + margemCima;
        int xFilho = espacamento * filho.ranque + margemEsquerda;
        int yFilho = espacamento * filho.nivel + margemCima;
        
        if (filho.color == ArvoreVermelhoPreto.NodeColor.RED) { 
            yFilho = yPai;
        }
        
        if (pai.color == ArvoreVermelhoPreto.NodeColor.RED) { 
            yPai -= 70;
            xPai -= 25;
        }
        
        if( filho.color == ArvoreVermelhoPreto.NodeColor.RED){
            drawLine(xPai, yPai, xFilho, yFilho,RED);
            drawLine(xPai-1, yPai, xFilho-1, yFilho,RED);
            drawLine(xPai-2, yPai, xFilho-2, yFilho,RED); 
        }else{
            drawLine(xPai, yPai, xFilho, yFilho, BLACK);
        }
        
    }

    public void resetaCores(ArvoreVermelhoPreto a) {
        for (ArvoreVermelhoPreto.Node<Integer, String> no : nos) {
            no.cor= WHITE;
        }
        repaint();
    }

    private void desenharRaiz() {
        if (arvore.getRoot() != null) {
            ArvoreVermelhoPreto.Node<Integer, String> raiz = arvore.getRoot();
            

            Vector2 centroRaiz = new Vector2(
                    espacamento * raiz.ranque+ margemEsquerda,
                    espacamento * raiz.nivel + margemCima
            );

            drawText("Raiz", centroRaiz.x - 20, centroRaiz.y - 60, 18, BLACK);
            drawLine(centroRaiz.x, centroRaiz.y - 45, centroRaiz.x, centroRaiz.y - raio, BLACK);
            
            desenharSeta(this, centroRaiz.x, centroRaiz.y - raio, 10, 90, BLACK);
        }
    }

    private void buscarPreOrdem(ArvoreVermelhoPreto.Node<Integer, String> no) {
        if (no != null) {
            System.out.println(no.key + " -> " + no.value);
            no.cor = Color.RED;
            repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorAVL.class.getName()).log(Level.SEVERE, null, ex);
            }
            buscarPreOrdem(no.left);
            buscarPreOrdem(no.right);
        }
    }

    private void buscarEmOrdem(ArvoreVermelhoPreto.Node<Integer, String> no) {
        if (no != null) {
            buscarEmOrdem(no.left);
            no.cor = Color.RED;
            repaint();

            System.out.println(no.key + " -> " + no.value);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorAVP.class.getName()).log(Level.SEVERE, null, ex);
            }

            buscarEmOrdem(no.right);
        }
    }

    private void buscarPosOrdem(ArvoreVermelhoPreto.Node<Integer, String> no) {
        if (no != null) {
            buscarPosOrdem(no.left);
            buscarPosOrdem(no.right);
            System.out.println(no.key + " -> " + no.value);
            no.cor = Color.RED;
            repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorAVP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void buscarEmNivel(ArvoreVermelhoPreto.Node<Integer, String> no) {
        if (no == null) {
            return;
        }

        Queue<ArvoreVermelhoPreto.Node<Integer, String>> fila = new LinkedList<>();
        fila.add(no);

        while (!fila.isEmpty()) {
            ArvoreVermelhoPreto.Node<Integer, String> atual = fila.poll();
            System.out.println(atual.key + " -> " + atual.value);
            atual.cor = Color.RED;
            repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimuladorAVP.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (atual.left != null) {
                fila.add(atual.left);
            }
            if (atual.right != null) {
                fila.add(atual.right);
            }
        }
    }
    
    
    public static void main( String[] args ) {
        new SimuladorAVP();
    }
    
}
