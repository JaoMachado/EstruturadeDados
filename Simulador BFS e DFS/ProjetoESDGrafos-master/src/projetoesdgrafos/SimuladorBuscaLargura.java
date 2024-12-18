package projetoesdgrafos;

import br.com.davidbuzatto.jsge.collision.CollisionUtils;
import projetoesdgrafos.utils.Utils;
import projetoesdgrafos.grafo.Grafo;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.MOUSE_BUTTON_LEFT;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import projetoesdgrafos.grafo.Aresta;
import projetoesdgrafos.grafo.Vertice;
import queue.LinkedQueue;

/**
 * Simulador de Busca em Largura.
 *
 * @author Prof. Dr. David Buzatto
 */
public class SimuladorBuscaLargura extends EngineFrame {

    private Grafo grafo;
    private boolean[] marked;
    private int[] distTo;
    private int[] edgeTo;
    private int fonte;
    private List<Aresta> caminhoPercorrido;

    public SimuladorBuscaLargura() {
        super(650, 500, "Busca em Largura", 60, true);
    }

    @Override
    public void create() {
        caminhoPercorrido = new ArrayList<>();
        grafo = Utils.criarGrafoTeste();
        marked = new boolean[grafo.getQuantidadeVertices()];
        fonte = -1;
        System.out.println(grafo);
        setDefaultFontSize(20);
        setDefaultStrokeLineWidth(2);
        setDefaultStrokeEndCap(STROKE_CAP_ROUND);
    }

    @Override
    public void update(double delta) {
        Vector2 mousePos = getMousePositionPoint();
        if (isMouseButtonPressed(MOUSE_BUTTON_LEFT)) {
            try {
                caminhoPercorrido.clear();
                draw();
                repaint();
                
                String entrada = JOptionPane.showInputDialog(
                        null,
                        "Digite o índice do vértice raiz (0 a " + (grafo.getQuantidadeVertices() - 1) + "):",
                        "Seleção de Vértice",
                        JOptionPane.QUESTION_MESSAGE
                );

                if (entrada != null) {
                    fonte = Integer.parseInt(entrada.trim());

                    if (fonte < 0 || fonte >= grafo.getQuantidadeVertices()) {
                        throw new NumberFormatException("Vértice inválido!");
                    }

                    marked = new boolean[grafo.getQuantidadeVertices()];
                    bfs(grafo, fonte);
                }
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Entrada inválida! Por favor, digite um número inteiro válido.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else if(isMouseButtonPressed(MOUSE_BUTTON_RIGHT)) {
            for (Map.Entry<Integer, Vertice> entry : grafo.vertices.entrySet()) {
                if (CollisionUtils.checkCollisionPointCircle(mousePos, entry.getValue().pos, 30)) {
                    try {
                        caminhoPercorrido.clear();
                        draw();
                        repaint();

                        fonte = entry.getValue().id;


                        if (fonte < 0 || fonte >= grafo.getQuantidadeVertices()) {
                            throw new NumberFormatException("Vértice inválido!");
                        }

                        marked = new boolean[grafo.getQuantidadeVertices()];
                        bfs(grafo, fonte);    

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Entrada inválida! Por favor, digite um número inteiro válido.",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        
            }
        }
    }
    
    private void bfs(Grafo grafo, int source) {
        LinkedQueue<Integer> q = new LinkedQueue<>();
        distTo = new int[grafo.getQuantidadeVertices()];
        edgeTo = new int[grafo.getQuantidadeVertices()];
        marked[source] = true;
        q.enqueue(source);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            List<Aresta> adjacentes = grafo.adjacentes(v);
            
            for (Aresta aresta : adjacentes) {
                Vertice destino = aresta.destino;

                if (!marked[destino.id]) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SimuladorBuscaProfundidade.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    caminhoPercorrido.add(aresta);
                    System.out.println("Indo de " + v + " para " + destino.id);
                    
                    edgeTo[destino.id] = v;
                    distTo[destino.id] = distTo[v] + 1;
                    marked[destino.id] = true;
                    q.enqueue(destino.id);

                    draw();
                    repaint();
                }
            }
        }
    }

    @Override
    public void draw() {
        clearBackground(WHITE);
        drawText("Clique com o botão esquerdo para digitar a fonte e executar o algoritmo.", 10, 10, 15, BLACK);
        drawText("Clique com o botão direito em cima da fonte para executar o algoritmo.", 10, 30, 15, BLACK);

        for (Aresta aresta : grafo.getArestas()) {
            aresta.cor = BLACK;
            aresta.draw(this);
        }

        for (Aresta aresta : caminhoPercorrido) {
            aresta.cor = GREEN;
            aresta.draw(this);
            
            drawArrow(this, aresta.origem.pos, aresta.destino.pos, GREEN);
        }

        for (Vertice vertice : grafo.vertices.values()) {
            vertice.draw(this);
            
            if (marked[vertice.id]) {
                drawText("d=" + distTo[vertice.id], vertice.pos.x, vertice.pos.y - 20, 10, BLACK);
            }
        }
        
    }
    
    private void drawArrow(EngineFrame e, Vector2 origem, Vector2 destino, Color cor) {
        double dx = destino.x - origem.x;
        double dy = destino.y - origem.y;

        double angle = Math.toDegrees(Math.atan2(dy, dx));

        double deslocamento = 30;
        double arrowX = destino.x - dx * deslocamento / Math.sqrt(dx * dx + dy * dy);
        double arrowY = destino.y - dy * deslocamento / Math.sqrt(dx * dx + dy * dy);

        int tamanhoSeta = 12;

        desenharSeta(e, arrowX, arrowY, tamanhoSeta, angle, cor);
    }

    
    public static void desenharSeta(EngineFrame engine, double x, double y, int tamanho, double graus, Color cor) {
        engine.drawLine(
            x, y,
            x + Math.cos(Math.toRadians(graus - 135)) * tamanho,
            y + Math.sin(Math.toRadians(graus - 135)) * tamanho,
            cor
        );

        engine.drawLine(
            x, y,
            x + Math.cos(Math.toRadians(graus + 135)) * tamanho,
            y + Math.sin(Math.toRadians(graus + 135)) * tamanho,
            cor
        );
    }

    public static void main(String[] args) {
        new SimuladorBuscaLargura();
    }

}
