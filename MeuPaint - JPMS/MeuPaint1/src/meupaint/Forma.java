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
public abstract class Forma {
    
    protected int xIni;
    protected int yIni;
    protected int xFim;
    protected int yFim;
    protected Color corContorno;
    protected Color corPreenchimento;

    public Forma(int xIni, int yIni, int xFim, int yFim, Color corContorno, Color corPreenchimento) {
        this.xIni = xIni;
        this.yIni = yIni;
        this.xFim = xFim;
        this.yFim = yFim;
        this.corContorno = corContorno;
        this.corPreenchimento = corPreenchimento;
    }
    
    public abstract void desenhar( Graphics2D g2d );

    public int getxIni() {
        return xIni;
    }

    public void setxIni(int xIni) {
        this.xIni = xIni;
    }

    public int getyIni() {
        return yIni;
    }

    public void setyIni(int yIni) {
        this.yIni = yIni;
    }

    public int getxFim() {
        return xFim;
    }

    public void setxFim(int xFim) {
        this.xFim = xFim;
    }

    public int getyFim() {
        return yFim;
    }

    public void setyFim(int yFim) {
        this.yFim = yFim;
    }
    
}
