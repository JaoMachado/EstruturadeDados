package projetoesdlinear;

import aesd.ds.exceptions.EmptyStackException;
import aesd.ds.implementations.linear.LinkedQueue;
import aesd.ds.interfaces.Queue;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import projetoesdlinear.engine.Engine;

/**
 * Simulador de fila:
 *     Simula as operações de enfileirar e desenfileirar de uma fila
 *     encadeada/ligada/dinâmica.
 * 
 * @author Prof. Dr. David Buzatto
 * Modificado por: João Pedro Machado Silva
 */
public class SimuladorFila extends Engine {
    // fila que passará pelas operações de Enfileirar e Desenfileirar
    private Queue<String> fila;
    
    // valor desenfileirado na última operação de Desenfileirar
    private String valorDesenfileirado;
    private String valorEnfileirado;
    private int teclaPressionada;
    private int resposta;
    
    private int raio;
    private int distanciaEntreElementos;
    private int tamanhoFonte;
    private int margemEsquerda;
    
    public SimuladorFila() {

        // cria a janela do jogo ou simulação
        super( 
            1200,                  // 800 pixels de largura
            800,                  // 600 pixels de largura
            "Simulador de Fila",  // título da janela
            true,                 // ativa a suavização (antialiasing)
            60 );                 // 60 quadros por segundo

    }

    /**
     * Processa a entrada inicial fornecida pelo usuário e cria
     * e/ou inicializa os objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void criar() {
        
        fila = new LinkedQueue<>();
        
        raio = 30;
        distanciaEntreElementos = 20;
        tamanhoFonte = 20;
        margemEsquerda = 10;

    }

    /**
     * Atualiza os objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void atualizar() {
    }

    /**a
     * Desenha o estado dos objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void desenhar() {
        desenharOpcoesEstadoFila();
        desenharFila();
    }

    private void desenharOpcoesEstadoFila() {
        
        int yInicial = 60;
        
        drawLine( 0, yInicial - 30, getScreenWidth(), yInicial - 30, BLACK );
        drawText( "1) Enfileirar", 10, yInicial, tamanhoFonte, BLACK );
        drawText( "2) Desenfileirar", 10, yInicial += 30, tamanhoFonte, BLACK );
        drawText( "Número de Elementos na Fila: " + fila.getSize(), getScreenWidth() - measureText( "Número de Elementos na Fila:    ", tamanhoFonte ), yInicial, tamanhoFonte, BLACK );
        drawText( "3) Limpar a Fila", 10, yInicial += 30, tamanhoFonte, BLACK );
        drawLine( 0, yInicial + 30, getScreenWidth(), yInicial + 30, BLACK );
        
        switch(teclaPressionada){
            case 1: 
                drawText( "Enfileirou: " + ( valorEnfileirado == null ? "nenhum" : valorEnfileirado ), 
                10, yInicial += 80, tamanhoFonte, BLUE );
                break;
               
            case 2:
               drawText( "Desenfileirou: " + ( valorDesenfileirado == null ? "nenhum" : valorDesenfileirado ), 
                10, yInicial += 80, tamanhoFonte, BLUE ); 
               break;
               
            case 3: 
               if(resposta == JOptionPane.YES_OPTION){
                   drawText(( "Limpou a Fila! "), 
                    10, yInicial += 80, tamanhoFonte, BLUE ); 
               }
               break;
        }
        
        
        
        if ( fila.isEmpty() ) {
            drawText( "Fila vazia!", 10, yInicial += 60, tamanhoFonte, RED );
        }
        
    }
    
    private void desenharFila() {
        
        int elementoAtual = 0;
        int xCentroAnterior = margemEsquerda;
        int xCentro = xCentroAnterior;
        int tamanho = fila.getSize();
        int xInicialFila = margemEsquerda;
                
        for ( String valor : fila ) {
            
            int yCentro = getScreenHeight() / 2;
            int yCentroAnterior = getScreenHeight() / 2;
            xCentro += raio * 2 + distanciaEntreElementos;

            drawCircleLines( xCentro, yCentro, raio, BLACK );
            
            drawText( valor, 
                    xCentro - measureText( valor, tamanhoFonte ) / 2, 
                    yCentro + 5,
                    tamanhoFonte,
                    BLACK );
            
                // ponto do início (arco de 45 graus)
                int xInicial = xCentroAnterior + 4 * distanciaEntreElementos + (int) ( Math.cos( Math.toRadians( 315 ) ) * raio );
                int yInicial = yCentroAnterior + (int) ( Math.sin( Math.toRadians( 315 ) ) * raio );

                // ponto do fim (arco de 315 graus)
                int xFinal = xCentro + 4 * distanciaEntreElementos + (int) ( Math.cos( Math.toRadians( 225 ) ) * raio );
                int yFinal = yCentro + (int) ( Math.sin( Math.toRadians( 225 ) ) * raio );

                // Calcula o ponto de controle para o arco
                int xControle = ( xInicial + xFinal ) / 2;
                int yControle = ( yInicial + yFinal ) / 2 - 20; // ajusta a altura para curvatura
                
                int yNext = yControle - 10;
                int larguraNext =  measureText( "next", tamanhoFonte );
                if(fila.getSize() == 1){
                    yNext += 15;
                    larguraNext -= 6;
                }

                drawSplineSegmentBezierQuadratic( 
                    xInicial, yInicial, 
                    xControle, yControle,
                    xFinal, yFinal, 
                    1, BLUE );
                
                if ( elementoAtual == tamanho - 1 ) {
                    drawLine( xFinal - 5, yFinal, xFinal + 5, yFinal, BLUE );
                    drawLine( xFinal - 10, yFinal + 5, xFinal + 10, yFinal + 5, BLUE );
                } else {
                    desenharSeta( xFinal, yFinal, 8, 45, BLUE );
                }

                // Desenha o texto "next" no ponto de controle do arco
                drawText( "next", 
                          xControle - larguraNext / 2,
                          yNext,
                          tamanhoFonte, 
                          BLUE );

            xCentroAnterior = xCentro;
            elementoAtual++;
            
        }
        
        if ( elementoAtual == fila.getSize() || fila.isEmpty() ) {
            int comp = 50;
            int xLinha = margemEsquerda + raio + 50;
            int xLinhaStart = xLinha;
            int xLinhaEnd = xCentro;
            int yLinha = (getScreenHeight() / 2 - ( raio * 2  + distanciaEntreElementos )) + comp;
            int grausSetaStart = 90;
            int grausSetaEnd = 90;          
            
            if ( fila.getSize() == 1) {
                xCentro += 30;
                xLinha -= 30;
                xLinhaStart -= 10;
                xLinhaEnd += 10;
                grausSetaStart = 60;
                grausSetaEnd = 120;
            }
            
            if(fila.isEmpty()){
                xCentro += 110;
                xLinha -= 30;
                xLinhaStart = xLinha;
                xLinhaEnd = xCentro;
            }
            
            drawLine( xLinha, yLinha - 35, xLinhaStart, yLinha, BLACK );
            
            if ( fila.isEmpty() ) {
                drawLine( xLinha - 5, yLinha, xLinha + 5, yLinha, BLACK );
                drawLine( xLinha - 10, yLinha + 5, xLinha + 10, yLinha + 5, BLACK );
            } else {
                desenharSeta( xLinhaStart, yLinha, 8, grausSetaStart, BLACK );
            }
            
            drawText( 
                    "start", 
                    xLinha - 30, 
                    yLinha - 45, 
                    tamanhoFonte, 
                    BLACK );
            
            drawLine( xCentro, yLinha - 35, xLinhaEnd, yLinha, BLACK );
            
            if ( fila.isEmpty() ) {
                drawLine( xCentro - 5, yLinha, xCentro + 5, yLinha, BLACK );
                drawLine( xCentro - 10, yLinha + 5, xCentro + 10, yLinha + 5, BLACK );
            } else {
                desenharSeta( xLinhaEnd, yLinha, 8, grausSetaEnd, BLACK );
            }
            
            drawText( 
                    "end", 
                    xCentro - 21, 
                    yLinha - 45, 
                    tamanhoFonte, 
                    BLACK );
        }
    }
        
    
    @Override
    public void tratarTeclado( KeyEvent e, KeyboardEventType ket ) {
        
        if ( ket == KeyboardEventType.PRESSED ) {
            
            switch ( e.getKeyCode() ) {
                
                case KeyEvent.VK_1:
                case KeyEvent.VK_NUMPAD1:
                    SimularEnfileirar();
                    teclaPressionada = 1;
                    break;
                    
                case KeyEvent.VK_2:
                case KeyEvent.VK_NUMPAD2:
                    simularDesenfileirar();
                    teclaPressionada = 2;
                    break;
                    
                case KeyEvent.VK_3:
                case KeyEvent.VK_NUMPAD3:
                    simularLimparFila();
                    teclaPressionada = 3;
                    break;
                    
            }
            
        }
        
    }

    private void SimularEnfileirar() {
        
        String valor = JOptionPane.showInputDialog( "Valor a Enfileirar:" );
        
        if ( valor != null && !valor.isBlank() ) {
            fila.enqueue( valor );
            valorEnfileirado = valor;
        }
                    
    }
    
    private void simularDesenfileirar() {
        
        try {
            valorDesenfileirado = fila.dequeue();
        } catch ( EmptyStackException exc ) {
            // fila vazia, o retorno será visual
        }
                    
    }
    
    private void simularLimparFila() {
        
        resposta = JOptionPane.showConfirmDialog(null,
                "Você realmente deseja limpar a Fila?",
                "Confirmação de Limpeza",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                fila.clear();
            } catch (EmptyStackException exc) {
                // Fila vazia, o retorno será visual
            }
        }
                    
    }
    
    private void desenharSeta( double x, double y, int tamanho, double graus, Color cor ) {
        
        drawLine( 
                x, y, 
                x + Math.cos( Math.toRadians( graus - 135 ) ) * tamanho,
                y + Math.sin( Math.toRadians( graus - 135 ) ) * tamanho,
                cor
        );
        
        drawLine( 
                x, y, 
                x + Math.cos( Math.toRadians( graus + 135 ) ) * tamanho,
                y + Math.sin( Math.toRadians( graus + 135 ) ) * tamanho,
                cor
        );
        
    }
    
    public static void main( String[] args ) {
        new SimuladorFila();
    }

}
