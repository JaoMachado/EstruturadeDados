package projetoesdgrafos.grafo;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import static java.awt.Color.YELLOW;
import java.awt.geom.Point2D;

/**
 * Uma aresta de um grafo.
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Aresta {
    
    public Vertice origem;
    public Vertice destino;
    public Color cor;

    public Aresta( Vertice origem, Vertice destino, Color cor ) {
        this.origem = origem;
        this.destino = destino;
        this.cor = cor;
    }
    
    public void draw( EngineFrame e ) {
        e.drawLine( origem.pos, destino.pos, cor );
        
        Point2D origemPoint = new Point2D.Double(origem.pos.x, origem.pos.y);
        Point2D destinoPoint = new Point2D.Double(destino.pos.x, destino.pos.y);

        drawArrow(e, origemPoint, destinoPoint, cor);
    }
    
    private void drawArrow(EngineFrame e, Point2D origem, Point2D destino, Color cor) {
        // Vetor direção
        double dx = destino.getX() - origem.getX();
        double dy = destino.getY() - origem.getY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Normalizar o vetor direção
        dx /= length;
        dy /= length;

        // Posição do final da linha (um pouco antes do vértice destino)
        double arrowBaseX = destino.getX() - dx * 10; // Encosta antes no destino
        double arrowBaseY = destino.getY() - dy * 10;

        // Tamanho e ângulo da seta
        double arrowSize = 10;
        double angle = Math.toRadians(30);

        // Coordenadas dos dois lados da seta
        double leftX = arrowBaseX - arrowSize * Math.cos(angle) * dx + arrowSize * Math.sin(angle) * dy;
        double leftY = arrowBaseY - arrowSize * Math.cos(angle) * dy - arrowSize * Math.sin(angle) * dx;

        double rightX = arrowBaseX - arrowSize * Math.cos(angle) * dx - arrowSize * Math.sin(angle) * dy;
        double rightY = arrowBaseY - arrowSize * Math.cos(angle) * dy + arrowSize * Math.sin(angle) * dx;

        // Desenhar a linha da seta usando coordenadas explícitas
        e.drawLine(arrowBaseX, arrowBaseY, leftX, leftY, cor);
        e.drawLine(arrowBaseX, arrowBaseY, rightX, rightY, cor);
    }

}
