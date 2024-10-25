package projetoesdlinear;

import aesd.ds.exceptions.EmptyStackException;
import aesd.ds.implementations.linear.LinkedDeque;
import aesd.ds.interfaces.Deque;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import projetoesdlinear.engine.Engine;

/**
 * Simulador de deque: Simula as operações de inserir no início e no fim e
 * remover do início e do fim de uma deque encadeada/ligada/dinâmica.
 *
 * @author Prof. Dr. David Buzatto
 * Modificado por: João Pedro Machado Silva
 */
public class SimuladorDeque extends Engine {

    // deque que passará pelas operações de Enfileirar e Desenfileirar
    private Deque<String> deque;

    private String valorRemovido;
    private String valorInserido;
    private int teclaPressionada;
    private int resposta;

    private int raio;
    private int distanciaEntreElementos;
    private int tamanhoFonte;
    private int margemEsquerda;

    public SimuladorDeque() {

        // cria a janela do jogo ou simulação
        super(
                1200, // 800 pixels de largura
                800, // 600 pixels de largura
                "Simulador de Deque", // título da janela
                true, // ativa a suavização (antialiasing)
                60);                 // 60 quadros por segundo

    }

    /**
     * Processa a entrada inicial fornecida pelo usuário e cria e/ou inicializa
     * os objetos/contextos/variáveis do jogo ou simulação.
     */
    @Override
    public void criar() {

        deque = new LinkedDeque<>();

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
        desenharOpcoesEstadoDeque();
        desenharDeque();
    }

    private void desenharOpcoesEstadoDeque() {

        int yInicial = 60;

        drawLine( 0, yInicial - 30, getScreenWidth(), yInicial - 30, BLACK );
        drawText("1) Inserir no início                                        4) Remover do Fim", 10, yInicial, tamanhoFonte, BLACK);
        drawText("2) Inserir no fim                                           5) Limpar o Deque", 10, yInicial += 30, tamanhoFonte, BLACK);
        drawText("3) Remover do Início", 10, yInicial += 30, tamanhoFonte, BLACK);
        drawText( "Número de Elementos no Deque: " + deque.getSize(), getScreenWidth() - measureText( "Número de Elementos no Deque:          ", tamanhoFonte ), yInicial + 80, tamanhoFonte, BLACK );
        drawLine( 0, yInicial + 30, getScreenWidth(), yInicial + 30, BLACK );
        
        switch (teclaPressionada) {
            case 1:
                if (valorInserido != null) {
                    drawText(("Valor [ " + valorInserido + " ] Inserido no Início"), 10, yInicial += 80, tamanhoFonte, BLUE);
                }
                break;

            case 2:
                if (valorInserido != null) {
                    drawText(("Valor [ " + valorInserido + " ] Inserido no Fim"), 10, yInicial += 80, tamanhoFonte, BLUE);
                }
                break;

            case 3:
                if (valorRemovido != null) {
                    drawText(("Valor [ " + valorRemovido + " ] Removido do Inicio"), 10, yInicial += 80, tamanhoFonte, BLUE);
                }
                break;

            case 4:
                if (valorRemovido != null) {
                    drawText(("Valor [ " + valorRemovido + " ] Removido do Fim"), 10, yInicial += 80, tamanhoFonte, BLUE);
                }
                break;

            case 5:
                if(resposta == JOptionPane.YES_OPTION && deque.isEmpty()){
                    drawText(("Limpou o Deque"), 10, yInicial += 80, tamanhoFonte, BLUE);
                }
                break;
        }

        if (deque.isEmpty()) {
            drawText("Deque vazio!", 10, yInicial += 60, tamanhoFonte, RED);
        }

    }

    private void desenharDeque() {

        int elementoAtual = 0;
        int xCentroAnterior = margemEsquerda;
        int xCentro = xCentroAnterior;
        int tamanho = deque.getSize();
        int xInicialDeque = margemEsquerda;

        for (String valor : deque) {

            int yCentro = getScreenHeight() / 2;
            int yCentroAnterior = getScreenHeight() / 2;
            xCentro += raio * 2 + distanciaEntreElementos;

            drawCircleLines(xCentro, yCentro, raio, BLACK);

            drawText(valor,
                    xCentro - measureText(valor, tamanhoFonte) / 2,
                    yCentro + 5,
                    tamanhoFonte,
                    BLACK);

            // ponto do início (arco de 45 graus)
            int xInicialNext = xCentroAnterior + 4 * distanciaEntreElementos + (int) (Math.cos(Math.toRadians(315)) * raio);
            int yInicialNext = yCentroAnterior + (int) (Math.sin(Math.toRadians(315)) * raio);

            // ponto do fim (arco de 315 graus)
            int xFinalNext = xCentro + 4 * distanciaEntreElementos + (int) (Math.cos(Math.toRadians(225)) * raio);
            int yFinalNext = yCentro + (int) (Math.sin(Math.toRadians(225)) * raio);

            // Calcula o ponto de controle para o arco
            int xControleNext = (xInicialNext + xFinalNext) / 2;
            int yControleNext = (yInicialNext + yFinalNext) / 2 - 20; // ajusta a altura para curvatura

            int yNext = yControleNext - 10;
            int larguraNext = measureText("next", tamanhoFonte);
            if (deque.getSize() == 1) {
                yNext += 15;
                larguraNext -= 6;
            }

            drawSplineSegmentBezierQuadratic(
                    xInicialNext, yInicialNext,
                    xControleNext, yControleNext,
                    xFinalNext, yFinalNext,
                    1, BLUE);

            if (elementoAtual == tamanho - 1) {
                drawLine(xFinalNext - 5, yFinalNext, xFinalNext + 5, yFinalNext, BLUE);
                drawLine(xFinalNext - 10, yFinalNext + 5, xFinalNext + 10, yFinalNext + 5, BLUE);
            } else {
                desenharSeta(xFinalNext, yFinalNext, 8, 45, BLUE);
            }

            // Desenha o texto "next" no ponto de controle do arco
            drawText("next",
                    xControleNext - larguraNext / 2,
                    yNext,
                    tamanhoFonte,
                    BLUE);

            // ponto do início (arco de 45 graus)
            int xInicialPrevious = xCentroAnterior + (int) (Math.cos(Math.toRadians(45)) * raio);
            int yInicialPrevious = yCentroAnterior + (int) (Math.sin(Math.toRadians(45)) * raio);

            // ponto do fim (arco de 315 graus)
            int xFinalPrevious = xCentro + (int) (Math.cos(Math.toRadians(135)) * raio);
            int yFinalPrevious = yCentro + (int) (Math.sin(Math.toRadians(135)) * raio);

            // Calcula o ponto de controle para o arco
            int xControlePrevious = (xInicialPrevious + xFinalPrevious) / 2;
            int yControlePrevious = (yInicialPrevious + yFinalPrevious) / 2 + 20; // ajusta a altura para curvatura

            int yPrevious = yControlePrevious + 10;
            int larguraPrevious = measureText("previous", tamanhoFonte - 5);
            if (deque.getSize() == 1) {
                yPrevious += 15;
                larguraPrevious -= 6;
            }

            drawSplineSegmentBezierQuadratic(
                    xInicialPrevious, yInicialPrevious,
                    xControlePrevious, yControlePrevious,
                    xFinalPrevious, yFinalPrevious,
                    1, BLUE);

            if (elementoAtual == 0) {
                drawLine(xInicialPrevious + 5, yInicialPrevious, xInicialPrevious - 5, yInicialPrevious, BLUE);
                drawLine(xInicialPrevious + 10, yInicialPrevious - 5, xInicialPrevious - 10, yInicialPrevious - 5, BLUE);
            } else {
                desenharSeta(xInicialPrevious, yInicialPrevious, 8, 225, BLUE);
            }

            // Desenha o texto "next" no ponto de controle do arco
            drawText("previous",
                    xControlePrevious - larguraPrevious / 2,
                    yPrevious,
                    tamanhoFonte - 5,
                    BLUE);

            xCentroAnterior = xCentro;
            elementoAtual++;

        }

        if (elementoAtual == deque.getSize() || deque.isEmpty()) {
            int comp = 50;
            int xLinha = margemEsquerda + raio + 50;
            int xLinhaStart = xLinha;
            int xLinhaEnd = xCentro;
            int yLinha = (getScreenHeight() / 2 - (raio * 2 + distanciaEntreElementos)) + comp;
            int grausSetaStart = 90;
            int grausSetaEnd = 90;

            if (deque.getSize() == 1) {
                xCentro += 30;
                xLinha -= 30;
                xLinhaStart -= 10;
                xLinhaEnd += 10;
                grausSetaStart = 60;
                grausSetaEnd = 120;
            }

            if (deque.isEmpty()) {
                xCentro += 110;
                xLinha -= 30;
                xLinhaStart = xLinha;
                xLinhaEnd = xCentro;
            }

            drawLine(xLinha, yLinha - 35, xLinhaStart, yLinha, BLACK);

            if (deque.isEmpty()) {
                drawLine(xLinha - 5, yLinha, xLinha + 5, yLinha, BLACK);
                drawLine(xLinha - 10, yLinha + 5, xLinha + 10, yLinha + 5, BLACK);
            } else {
                desenharSeta(xLinhaStart, yLinha, 8, grausSetaStart, BLACK);
            }

            drawText(
                    "first",
                    xLinha - 30,
                    yLinha - 45,
                    tamanhoFonte,
                    BLACK);

            drawLine(xCentro, yLinha - 35, xLinhaEnd, yLinha, BLACK);

            if (deque.isEmpty()) {
                drawLine(xCentro - 5, yLinha, xCentro + 5, yLinha, BLACK);
                drawLine(xCentro - 10, yLinha + 5, xCentro + 10, yLinha + 5, BLACK);
            } else {
                desenharSeta(xLinhaEnd, yLinha, 8, grausSetaEnd, BLACK);
            }

            drawText(
                    "last",
                    xCentro - 21,
                    yLinha - 45,
                    tamanhoFonte,
                    BLACK);
        }
    }

    @Override
    public void tratarTeclado(KeyEvent e, KeyboardEventType ket) {

        if (ket == KeyboardEventType.PRESSED) {

            String valor;

            switch (e.getKeyCode()) {

                case KeyEvent.VK_1:
                case KeyEvent.VK_NUMPAD1:
                    simularInserirInicio();
                    teclaPressionada = 1;
                    break;

                case KeyEvent.VK_2:
                case KeyEvent.VK_NUMPAD2:
                    simularInserirFim();
                    teclaPressionada = 2;
                    break;

                case KeyEvent.VK_3:
                case KeyEvent.VK_NUMPAD3:
                    simularRemoverInicio();
                    teclaPressionada = 3;
                    break;

                case KeyEvent.VK_4:
                case KeyEvent.VK_NUMPAD4:
                    simularRemoverFim();
                    teclaPressionada = 4;
                    break;

                case KeyEvent.VK_5:
                case KeyEvent.VK_NUMPAD5:
                    simularLimparDeque();
                    teclaPressionada = 5;
                    break;

            }

        }

    }

    private void simularInserirInicio() {

        valorInserido = JOptionPane.showInputDialog("Valor a Inserir no Início:");

        if (valorInserido != null && !valorInserido.isBlank()) {
            deque.addFirst(valorInserido);
        }

    }

    private void simularInserirFim() {

        valorInserido = JOptionPane.showInputDialog("Valor a Inserir no Fim:");

        if (valorInserido != null && !valorInserido.isBlank()) {
            deque.addLast(valorInserido);
        }

    }

    private void simularRemoverInicio() {

        try {
            valorRemovido = deque.removeFirst();
        } catch (EmptyStackException exc) {
            // deque vazio, o retorno será visual
        }

    }

    private void simularRemoverFim() {

        try {
            valorRemovido = deque.removeLast();
        } catch (EmptyStackException exc) {
            // deque vazio, o retorno será visual
        }

    }

    private void simularLimparDeque() {

        resposta = JOptionPane.showConfirmDialog(null,
                "Você realmente deseja limpar o Deque?",
                "Confirmação de Limpeza",
                JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            try {
                deque.clear();
            } catch (EmptyStackException exc) {
                // Deque vazio, o retorno será visual
            }
        }

    }

    private void desenharSeta(double x, double y, int tamanho, double graus, Color cor) {

        drawLine(
                x, y,
                x + Math.cos(Math.toRadians(graus - 135)) * tamanho,
                y + Math.sin(Math.toRadians(graus - 135)) * tamanho,
                cor
        );

        drawLine(
                x, y,
                x + Math.cos(Math.toRadians(graus + 135)) * tamanho,
                y + Math.sin(Math.toRadians(graus + 135)) * tamanho,
                cor
        );

    }

    public static void main(String[] args) {
        new SimuladorDeque();
    }

}
