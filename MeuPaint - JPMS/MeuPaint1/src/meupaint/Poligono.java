package meupaint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;


/**
 *
 * @authors Vinicius Jabara, Vinicius Pittoli, João Pedro Machado
 */
public class Poligono extends Forma {

    private int lados;

    public Poligono(int xIni, int yIni, int xFim, int yFim, int lados, Color corContorno, Color corPreenchimento) {
        super(xIni, yIni, xFim, yFim, corContorno, corPreenchimento);
        this.lados = lados;
    }
 
    @Override
    public void desenhar(Graphics2D g2d) {
    
    if(lados<3) { lados=3; }
    
    double hypot = Math.hypot(xFim-xIni, yFim-yIni);
    double angle = Math.atan2(yFim-yIni, xFim-xIni);
    double angleIncrement = 2*Math.PI/lados;
    
    Path2D.Double poligono = new Path2D.Double();
    poligono.moveTo( xFim, yFim );
    
    for(int i=1; i<lados; i++){
            
        double xAtual = Math.cos(angle+i*angleIncrement) * hypot + xIni;
        double yAtual = Math.sin(angle+i*angleIncrement) * hypot + yIni;
            
        poligono.lineTo( xAtual, yAtual); 
            
    }
    
    poligono.closePath();

    g2d.setColor( corPreenchimento );
    g2d.fill( poligono );

    g2d.setColor( corContorno );
    g2d.draw( poligono );

    }
}