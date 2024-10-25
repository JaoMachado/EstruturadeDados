package projetoesdlinear;

import aesd.ds.exceptions.EmptyStackException;
import aesd.ds.implementations.linear.LinkedStack;
import aesd.ds.interfaces.Stack;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import projetoesdlinear.engine.Engine;

/**
 * Simulador de pilha:
 *     Simula as operações de empilhar e desempilhar de uma pilha
 *     encadeada/ligada/dinâmica.
 * 
 * @author Prof. Dr. David Buzatto
 * Modificado por: João Pedro Machado Silva
 */
public class SimuladorPilha extends Engine {

    // pilha que passará pelas operações de empilhar e desempilhar
    private Stack<String> pilha;
    
    // valor desempilhado na última operação de desempilhar
    private String valorDesempilhado;
    private String valorEmpilhado;
    private int teclaPressionada;
    private int resposta;
    
    private int raio;
    private int distanciaEntreElementos;
    private int tamanhoFonte;
    private int margemBaixo;

    public SimuladorPilha() {

        // cria a janela do jogo ou simulação
        super( 
            1200,                  // 800 pixels de largura
            800,                  // 600 pixels de largura
            "Simulador de Pilha", // título da janela
            true,                 // ativa a suavização (antialiasing)
            60 );                 // 60 quadros por segundo

    }

    /**
     * Processa a entrada inicial fornecida pelo usuário e cria
     * e/ou inicializa os objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void criar() {
        
        pilha = new LinkedStack<>();
        
        raio = 30;
        distanciaEntreElementos = 20;
        tamanhoFonte = 20;
        margemBaixo = 80;
        
    }

    /**
     * Atualiza os objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void atualizar() {
    }

    /**
     * Desenha o estado dos objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void desenhar() {
        desenharOpcoesEstadoPilha();
        desenharPilha();
    }

    private void desenharOpcoesEstadoPilha() {
        
        int yInicial = 60;
        
        drawLine( 0, yInicial - 30, getScreenWidth(), yInicial - 30, BLACK );
        drawText( "1) Empilhar", 10, yInicial, tamanhoFonte, BLACK );
        drawText( "2) Desempilhar", 10, yInicial += 30, tamanhoFonte, BLACK );
        drawText( "Número de Elementos na Pilha: " + pilha.getSize(), getScreenWidth() - measureText( "Número de Elementos na Pilha:    ", tamanhoFonte ), yInicial, tamanhoFonte, BLACK );
        drawText( "3) Limpar a Pilha", 10, yInicial += 30, tamanhoFonte, BLACK );
        drawLine( 0, yInicial + 30, getScreenWidth(), yInicial + 30, BLACK );
        
        switch(teclaPressionada){
            case 1:
                drawText( "Empilhou: " + ( valorEmpilhado == null ? "nenhum" : valorEmpilhado ), 
                10, yInicial += 80, tamanhoFonte, BLUE );
                break;
                
            case 2:
                drawText( "Desempilhou: " + ( valorDesempilhado == null ? "nenhum" : valorDesempilhado ), 
                10, yInicial += 80, tamanhoFonte, BLUE );
                break;
                
            case 3:
                if(resposta == JOptionPane.YES_OPTION){
                    drawText(( "Limpou a Pilha! "), 
                        10, yInicial += 80, tamanhoFonte, BLUE ); 
                }
               break;
        }
        
        
        
        if ( pilha.isEmpty() ) {
            drawText( "Pilha vazia!", 10, yInicial += 60, tamanhoFonte, RED );
        }
        
    }
    
    private void desenharPilha() {
        
        int elementoAtual = 0;
        int xCentroAnterior = getScreenWidth() / 2;
        int xCentro = xCentroAnterior;
        int tamanho = pilha.getSize();
        int yInicialPilha = getScreenHeight() - margemBaixo - ( raio * 2  + distanciaEntreElementos ) * tamanho;
        
        for ( String valor : pilha ) {
            
            int yCentro = yInicialPilha + ( raio * 2  + distanciaEntreElementos ) * (elementoAtual+1);
            int yCentroAnterior = yInicialPilha + ( raio * 2  + distanciaEntreElementos ) * (elementoAtual+2);
            
            drawCircleLines( xCentro, yCentro, raio, BLACK );
            drawText( valor, 
                    xCentro - measureText( valor, tamanhoFonte ) / 2, 
                    yCentro + 5,
                    tamanhoFonte,
                    BLACK );
            
            // arcos    
            // ponto do início (arco de 45 graus)
            int xInicial = xCentro + (int) ( Math.cos( Math.toRadians( 45 ) ) * raio );
            int yInicial = yCentro + (int) ( Math.sin( Math.toRadians( 45 ) ) * raio );
            
            // ponto do fim (arco de 315 graus)
            int xFinal = xCentroAnterior + (int) ( Math.cos( Math.toRadians( 315 ) ) * raio );
            int yFinal = yCentroAnterior + (int) ( Math.sin( Math.toRadians( 315 ) ) * raio );

            int xControle = ( xInicial + xFinal ) / 2 + 20;
            int yControle = ( yInicial + yFinal ) / 2;
            
            drawSplineSegmentBezierQuadratic( 
                    xInicial, yInicial, 
                    xControle, yControle,
                    xFinal, yFinal, 
                    1, BLUE );
            
            if ( elementoAtual == tamanho - 1 ) {
                drawLine( xFinal - 5, yFinal, xFinal + 5, yFinal, BLUE );
                drawLine( xFinal - 10, yFinal + 5, xFinal + 10, yFinal + 5, BLUE );
            } else {
                desenharSeta( xFinal, yFinal, 8, 135, BLUE );
            }
            
            drawText( "anterior", xControle, yControle, tamanhoFonte, BLUE );
            
            elementoAtual++;
            
        }
        
        if ( elementoAtual == pilha.getSize() || pilha.isEmpty() ) {
                
            int comp = 20;
            int xFinal = xCentro - raio;
            int xInicial = xFinal - comp;
            int yCentro = yInicialPilha + ( raio * 2  + distanciaEntreElementos );
            
            if ( pilha.isEmpty() ) {
                yCentro = yInicialPilha;
            }

            drawLine( xInicial, yCentro, xFinal, yCentro, BLACK );
            
            if ( pilha.isEmpty() ) {
                drawLine( xFinal, yCentro - 5, xFinal, yCentro + 5, BLACK );
                drawLine( xFinal + 5, yCentro - 10, xFinal + 5, yCentro + 10, BLACK );
            } else {
                desenharSeta( xFinal, yCentro, 8, 0, BLACK );
            }
            
            String tLabel = "topo";
            drawText( 
                    tLabel, 
                    xInicial - measureText( tLabel, tamanhoFonte ) - 10, 
                    yCentro + 5, 
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
                    simularEmpilhar();
                    teclaPressionada = 1;
                    break;
                    
                case KeyEvent.VK_2:
                case KeyEvent.VK_NUMPAD2:
                    simularDesempilhar();
                    teclaPressionada = 2;
                    break;
                    
                case KeyEvent.VK_3:
                case KeyEvent.VK_NUMPAD3:
                    simularLimparPilha();
                    teclaPressionada = 3;
                    break;
                    
            }
            
        }
        
    }

    private void simularEmpilhar() {
        
        String valor = JOptionPane.showInputDialog( "Valor a empilhar:" );
        
        if ( valor != null && !valor.isBlank() ) {
            pilha.push( valor );
            valorEmpilhado = valor;
        }
                    
    }
    
    private void simularDesempilhar() {
        
        try {
            valorDesempilhado = pilha.pop();
        } catch ( EmptyStackException exc ) {
            // pilha vazia, o retorno será visual
        }
                    
    }
    
    private void simularLimparPilha() {
        resposta = JOptionPane.showConfirmDialog(null,
                "Você realmente deseja limpar a Pilha?",
                "Confirmação de Limpeza",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                pilha.clear();
            } catch (EmptyStackException exc) {
                // Pilha vazia, o retorno será visual
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
        new SimuladorPilha();
    }
    
}
