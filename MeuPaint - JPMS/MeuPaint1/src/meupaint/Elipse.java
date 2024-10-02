/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package meupaint;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author bv110309
 */
public class Elipse extends Forma {

    public Elipse(int xIni, int yIni, int xFim, int yFim, Color corContorno, Color corPreenchimento) {
        super( xIni, yIni, xFim, yFim, corContorno, corPreenchimento );
    }
    
    @Override
    public void desenhar( Graphics2D g2d ) {
        
        int xIniD = xIni < xFim ? xIni : xFim;
        int xFimD = xIni < xFim ? xFim : xIni;
        
        int yIniD = yIni < yFim ? yIni : yFim;
        int yFimD = yIni < yFim ? yFim : yIni;
        
        g2d.setColor( corPreenchimento );
        g2d.fillOval( xIniD, yIniD, xFimD - xIniD, yFimD - yIniD );
        
        g2d.setColor( corContorno );
        g2d.drawOval( xIniD, yIniD, xFimD - xIniD, yFimD - yIniD );
        
    }
    
}