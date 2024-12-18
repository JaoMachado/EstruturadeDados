package projetoesdgrafos.utils;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import static br.com.davidbuzatto.jsge.core.engine.EngineFrame.BLACK;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import static java.awt.Color.green;
import projetoesdgrafos.grafo.Vertice;
import projetoesdgrafos.grafo.Grafo;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Prof. Dr. David Buzatto
 */
public interface Utils {
    
    public static Grafo criarGrafoTeste() {
        
        Grafo grafo = new Grafo();
        Map<Integer, Vertice> st = new TreeMap<>();
        
        grafo.addVertice( 100, 100 );
        grafo.addVertice( 150, 200 );
        grafo.addVertice( 250, 200 );
        grafo.addVertice( 200, 300 );
        grafo.addVertice( 300, 300 );
        grafo.addVertice( 100, 400 );
        grafo.addVertice( 350, 200 );
        grafo.addVertice( 450, 150 );
        grafo.addVertice( 550, 150 );
        grafo.addVertice( 450, 250 );
        grafo.addVertice( 550, 250 );
        grafo.addVertice( 450, 350 );
        grafo.addVertice( 550, 350 );
        
        grafo.addAresta( 0, 5, BLACK );
        grafo.addAresta( 4, 3, BLACK );
        grafo.addAresta( 0, 1, BLACK );
        grafo.addAresta( 9, 12, BLACK );
        grafo.addAresta( 6, 4, BLACK );
        grafo.addAresta( 5, 4, BLACK );
        grafo.addAresta( 0, 2, BLACK );
        grafo.addAresta( 11, 12, BLACK );
        grafo.addAresta( 9, 10, BLACK );
        grafo.addAresta( 0, 6, BLACK );
        grafo.addAresta( 7, 8, BLACK );
        grafo.addAresta( 9, 11, BLACK );
        grafo.addAresta( 5, 3, BLACK );
        
        return grafo;
        
    }
    
    public static void desenharSeta( EngineFrame engine, double x, double y, int tamanho, double graus, Color cor ) {
        
        engine.drawLine( 
                x, y, 
                x + Math.cos( Math.toRadians( graus - 135 ) ) * tamanho,
                y + Math.sin( Math.toRadians( graus - 135 ) ) * tamanho,
                cor
        );
        
        engine.drawLine( 
                x, y, 
                x + Math.cos( Math.toRadians( graus + 135 ) ) * tamanho,
                y + Math.sin( Math.toRadians( graus + 135 ) ) * tamanho,
                cor
        );
        
    }
    
}
