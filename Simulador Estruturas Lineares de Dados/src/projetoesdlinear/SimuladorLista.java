package projetoesdlinear;

import aesd.ds.exceptions.EmptyStackException;
import aesd.ds.interfaces.List;
import aesd.ds.implementations.linear.DoublyLinkedList;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import projetoesdlinear.engine.Engine;

/**
 * Simulador de lista:
 *     Simula as seguintes operações de uma lista encadeada/ligada/dinâmica:
 *         - Inserir no fim;
 *         - Inserir em posição especificada;
 *         - Alterar em posição especificada;
 *         - Remover de posição especificada;
 * 
 * @author Prof. Dr. David Buzatto
 * Modificado por: João Pedro Machado Silva
 */
public class SimuladorLista extends Engine {

    // declaração de variáveis
    private List<String> lista;
    
    private String valorRemovido;
    private String valorAdicionado;
    private String valorAlterado;
    private String valorAlteradoAnterior;
    private int teclaPressionada;
    private int index;
    private int resposta;
    
    private int raio;
    private int distanciaEntreElementos;
    private int tamanhoFonte;
    private int margemEsquerda;

    public SimuladorLista() {

        // cria a janela do jogo ou simulação
        super( 
            1200,                  // 800 pixels de largura
            800,                  // 600 pixels de largura
            "Simulador de Lista", // título da janela
            true,                 // ativa a suavização (antialiasing)
            60 );                 // 60 quadros por segundo

    }

    /**
     * Processa a entrada inicial fornecida pelo usuário e cria
     * e/ou inicializa os objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void criar() {
        lista = new DoublyLinkedList<>();
        
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

    /**
     * Desenha o estado dos objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void desenhar() {
        desenharOpcoesEstadoLista();
        desenharLista();
    }
    
    private void desenharOpcoesEstadoLista() {
        
        int yInicial = 60;
        
        drawLine( 0, yInicial - 30, getScreenWidth(), yInicial - 30, BLACK );
        drawText( "1) Inserir no fim                                        4) Remover de posição especificada", 10, yInicial, tamanhoFonte, BLACK );
        drawText( "2) Inserir em posição especificada                       5) limpar a Lista", 10, yInicial += 30, tamanhoFonte, BLACK );
        drawText( "3) Alterar em posição especificada                       ", 10, yInicial += 30, tamanhoFonte, BLACK );
        drawText( "Número de Elementos na Lista: " + lista.getSize(), getScreenWidth() - measureText( "Número de Elementos na Lista:             ", tamanhoFonte ), yInicial + 80, tamanhoFonte, BLACK );
        drawLine( 0, yInicial + 30, getScreenWidth(), yInicial + 30, BLACK );
        
        if(index < 0 || index > lista.getSize()){
            drawText(("POSIÇÃO [ " + index + " ] NÃO EXISTE NA LISTA!!!"), 10, yInicial += 80, tamanhoFonte, RED);
        } else {
            switch(teclaPressionada){
            
                case 1:
                case 2:
                    if (valorAdicionado != null) {
                        drawText(("Valor [ " + valorAdicionado + " ] Adicionado na Posição [ " + index + " ]"), 10, yInicial += 80, tamanhoFonte, BLUE);
                    }
                    break;

                case 3:
                    if (valorAlterado != null) {
                        drawText(("Valor Alterado na Posição [ " + index + " ] de  [ " + valorAlteradoAnterior + " ] para [ "  + valorAlterado + " ]"), 10, yInicial += 80, tamanhoFonte, BLUE);
                    }
                    break;

                case 4:
                    if (valorRemovido != null) {
                        drawText(("Valor [ " + valorRemovido + " ] Removido na Posição [ " + index + " ]"), 10, yInicial += 80, tamanhoFonte, BLUE);
                    }
                    break;

                case 5:
                    if(resposta == JOptionPane.YES_OPTION && lista.isEmpty()){
                        drawText(("Limpou a Lista"), 10, yInicial += 80, tamanhoFonte, BLUE);
                    }
                    break;
            }
        }
        
        
        if ( lista.isEmpty() ) {
            drawText( "Lista vazia!", 10, yInicial += 60, tamanhoFonte, RED );
        }
        
    }
    
    private void desenharLista() {
        
        int elementoAtual = 0;
        int xCentroAnterior = margemEsquerda;
        int xCentro = xCentroAnterior;
        int tamanho = lista.getSize();
        int xIniciallista = margemEsquerda;
                
        for ( String valor : lista ) {
            
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
                int xInicialNext = xCentroAnterior + 4 * distanciaEntreElementos + (int) ( Math.cos( Math.toRadians( 315 ) ) * raio );
                int yInicialNext = yCentroAnterior + (int) ( Math.sin( Math.toRadians( 315 ) ) * raio );

                // ponto do fim (arco de 315 graus)
                int xFinalNext = xCentro + 4 * distanciaEntreElementos + (int) ( Math.cos( Math.toRadians( 225 ) ) * raio );
                int yFinalNext = yCentro + (int) ( Math.sin( Math.toRadians( 225 ) ) * raio );

                // Calcula o ponto de controle para o arco
                int xControleNext = ( xInicialNext + xFinalNext ) / 2;
                int yControleNext = ( yInicialNext + yFinalNext ) / 2 - 20; // ajusta a altura para curvatura
                
                int yNext = yControleNext - 10;
                int larguraNext =  measureText( "next", tamanhoFonte );
                if(lista.getSize() == 1){
                    yNext += 15;
                    larguraNext -= 6;
                }

                drawSplineSegmentBezierQuadratic( 
                    xInicialNext, yInicialNext, 
                    xControleNext, yControleNext,
                    xFinalNext, yFinalNext, 
                    1, BLUE );
                
                if ( elementoAtual == tamanho - 1 ) {
                    drawLine( xFinalNext - 5, yFinalNext, xFinalNext + 5, yFinalNext, BLUE );
                    drawLine( xFinalNext - 10, yFinalNext + 5, xFinalNext + 10, yFinalNext + 5, BLUE );
                } else {
                    desenharSeta( xFinalNext, yFinalNext, 8, 45, BLUE );
                }

                // Desenha o texto "next" no ponto de controle do arco
                drawText( "next", 
                          xControleNext - larguraNext / 2,
                          yNext,
                          tamanhoFonte, 
                          BLUE );
               
                // ponto do início (arco de 45 graus)
                int xInicialPrevious = xCentroAnterior + (int) ( Math.cos( Math.toRadians( 45 ) ) * raio );
                int yInicialPrevious = yCentroAnterior + (int) ( Math.sin( Math.toRadians( 45 ) ) * raio );

                // ponto do fim (arco de 315 graus)
                int xFinalPrevious = xCentro +  (int) ( Math.cos( Math.toRadians( 135 ) ) * raio );
                int yFinalPrevious = yCentro + (int) ( Math.sin( Math.toRadians( 135 ) ) * raio );

                // Calcula o ponto de controle para o arco
                int xControlePrevious = ( xInicialPrevious + xFinalPrevious ) / 2;
                int yControlePrevious = ( yInicialPrevious + yFinalPrevious ) / 2 + 20; // ajusta a altura para curvatura
                
                int yPrevious = yControlePrevious + 10;
                int larguraPrevious =  measureText( "previous", tamanhoFonte - 5 );
                if(lista.getSize() == 1){
                    yPrevious += 15;
                    larguraPrevious -= 6;
                }

                drawSplineSegmentBezierQuadratic( 
                    xInicialPrevious, yInicialPrevious, 
                    xControlePrevious, yControlePrevious,
                    xFinalPrevious, yFinalPrevious, 
                    1, BLUE );
                
                if ( elementoAtual == 0 ) {
                    drawLine( xInicialPrevious + 5, yInicialPrevious, xInicialPrevious - 5, yInicialPrevious, BLUE );
                    drawLine( xInicialPrevious + 10, yInicialPrevious - 5, xInicialPrevious - 10, yInicialPrevious - 5, BLUE );
                } else {
                    desenharSeta( xInicialPrevious, yInicialPrevious, 8, 225, BLUE );
                }

                // Desenha o texto "next" no ponto de controle do arco
                drawText( "previous", 
                          xControlePrevious - larguraPrevious / 2,
                          yPrevious,
                          tamanhoFonte - 5, 
                          BLUE );

            xCentroAnterior = xCentro;
            elementoAtual++;
            
        }
        
        if ( elementoAtual == lista.getSize() || lista.isEmpty() ) {
            int comp = 50;
            int xLinha = margemEsquerda + raio + 50;
            int xLinhaStart = xLinha;
            int xLinhaEnd = xCentro;
            int yLinha = (getScreenHeight() / 2 - ( raio * 2  + distanciaEntreElementos )) + comp;
            int grausSetaStart = 90;
            int grausSetaEnd = 90;          
            
            if ( lista.getSize() == 1) {
                xCentro += 30;
                xLinha -= 30;
                xLinhaStart -= 10;
                xLinhaEnd += 10;
                grausSetaStart = 60;
                grausSetaEnd = 120;
            }
            
            if(lista.isEmpty()){
                xCentro += 110;
                xLinha -= 30;
                xLinhaStart = xLinha;
                xLinhaEnd = xCentro;
            }
            
            drawLine( xLinha, yLinha - 35, xLinhaStart, yLinha, BLACK );
            
            if ( lista.isEmpty() ) {
                drawLine( xLinha - 5, yLinha, xLinha + 5, yLinha, BLACK );
                drawLine( xLinha - 10, yLinha + 5, xLinha + 10, yLinha + 5, BLACK );
            } else {
                desenharSeta( xLinhaStart, yLinha, 8, grausSetaStart, BLACK );
            }
            
            drawText( 
                    "first", 
                    xLinha - 30, 
                    yLinha - 45, 
                    tamanhoFonte, 
                    BLACK );
            
            drawLine( xCentro, yLinha - 35, xLinhaEnd, yLinha, BLACK );
            
            if ( lista.isEmpty() ) {
                drawLine( xCentro - 5, yLinha, xCentro + 5, yLinha, BLACK );
                drawLine( xCentro - 10, yLinha + 5, xCentro + 10, yLinha + 5, BLACK );
            } else {
                desenharSeta( xLinhaEnd, yLinha, 8, grausSetaEnd, BLACK );
            }
            
            drawText( 
                    "last", 
                    xCentro - 21, 
                    yLinha - 45, 
                    tamanhoFonte, 
                    BLACK );
        }
    }
    
    public void tratarTeclado( KeyEvent e, KeyboardEventType ket ) {
        
        if ( ket == KeyboardEventType.PRESSED ) {
            
            switch ( e.getKeyCode() ) {
                
                case KeyEvent.VK_1:
                case KeyEvent.VK_NUMPAD1:
                    simularInserirFim();
                    teclaPressionada = 1;
                    break;
                    
                case KeyEvent.VK_2:
                case KeyEvent.VK_NUMPAD2:
                    simularInserirPosicaoEspecificada();
                    teclaPressionada = 2;
                    break;
                    
                case KeyEvent.VK_3:
                case KeyEvent.VK_NUMPAD3:
                    simularAlterarPosicaoEspecificada();
                    teclaPressionada = 3;
                    break;
                
                case KeyEvent.VK_4:
                case KeyEvent.VK_NUMPAD4:
                    simularRemoverPosicaoEspecificada();
                    teclaPressionada = 4;
                    break;
                    
                case KeyEvent.VK_5:
                case KeyEvent.VK_NUMPAD5:
                    simularLimparLista();
                    teclaPressionada = 5;
                    break;
            }
            
        }
        
    }
    
    private void simularInserirFim() {
        
        valorAdicionado = JOptionPane.showInputDialog( "Valor a Inserir no Fim:" );
        index = lista.getSize();
        
        if ( valorAdicionado != null && !valorAdicionado.isBlank() ) {
            lista.add( valorAdicionado );
        }
                    
    }
    
    private void simularInserirPosicaoEspecificada() {
        
        index = Integer.valueOf(JOptionPane.showInputDialog( "Posição a Inserir o Valor:" ));
        
        try {
            if(index >= 0 && index <= lista.getSize()){
                valorAdicionado = JOptionPane.showInputDialog( "Valor a Inserir na Posição Especificada:" );
            }
            if ( valorAdicionado != null && !valorAdicionado.isBlank() ) {
                lista.add( index, valorAdicionado );
            } 
        } catch ( EmptyStackException exc ) {
            
        }
                    
    }
    
    private void simularAlterarPosicaoEspecificada() {
        
        index = Integer.valueOf(JOptionPane.showInputDialog( "Posição a Alterar o Valor:" ));
        valorAlteradoAnterior = lista.get(index);
        
        try{
            if(index >= 0 && index <= lista.getSize()){
                valorAlterado = JOptionPane.showInputDialog( "Valor a Inserir na Posição Especificada:" );
            }
            if ( valorAlterado != null && !valorAlterado.isBlank() ) {
                lista.set( index, valorAlterado );
            }
        } catch ( EmptyStackException exc ) {
            // index fora da lista, retorno visual
        }
       
                    
    }
    
    private void simularRemoverPosicaoEspecificada() {
        
        index = Integer.valueOf(JOptionPane.showInputDialog( "Posição a Remover o Valor:" ));
        
        try{
            valorRemovido = lista.remove( index );
        } catch ( EmptyStackException exc ) {
            // lista vazia, o retorno será visual
        }
                    
    }
    
    private void simularLimparLista() {
        
        resposta = JOptionPane.showConfirmDialog(null,
                "Você realmente deseja limpar a Lista?",
                "Confirmação de Limpeza",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                lista.clear();
            } catch (EmptyStackException exc) {
                // Lista vazia, o retorno será visual
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
        new SimuladorLista();
    }
    
}
