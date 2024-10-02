/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esdc4aula02;
import static esdc4aula02.Exercicioi2p2.getPrecedence;
import java.util.Stack;

/**
 * Resolução do Exercício i2.3
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Exercicioi2p3 {
    
    /**
     * Avalia uma expressão aritmética fornecida em qualquer forma (pré-fixada,
     * infixa ou pós-fixada), gerando o resultado. As operações de adição,
     * subtração, multiplicação e divisão devem ser suportadas.
     * 
     * @param expression Expressão a ser avaliada.
     * @return O valor obtido após o cômputo da expressão.
     * @throws IllegalArgumentException Caso a expressão fornecida seja inválida,
     * do ponto de vista estrutural, como ter um valor não numérico onde um
     * é esperado, bem como o uso de caracteres ou operações não suportadas.
     */
    public static double evaluate( String expression ) throws IllegalArgumentException {
        
        // implementação
        Stack<Double> stack = new Stack();
        
        String[] tokens = expression.split(" ");
        
        if(isOperator(tokens[0])){
            expression = prefixToPostfix(expression);
        } else if(isOperand(tokens[0]) && isOperator(tokens[1]) || isLeftParenthesis(tokens[0])){
            expression = infixToPostfix(expression);
        }
        
        tokens = expression.split(" ");
        
        for(String token: tokens){
            if(isOperator(token)){
                Double op1 = stack.pop();
                Double op2 = stack.pop();
                
                Double resultado = applyOperator(token, op2, op1);
                
                stack.push(resultado);
            } else {
                stack.push(Double.parseDouble(token));
            }
        }
        
        return stack.pop();
        
    }
    
    private static double applyOperator(String operator, double operand1, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 == 0) {
                    throw new ArithmeticException("Divisão por zero");
                }
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Operador inválido");
        }
    }
    
    /**
     * Converte uma expressão aritmética infixa para pós-fixada.
     * 
     * Algoritmo:
     *     - Processe a expressão infixa da esquerda para a direita;
     *     - Se o token for um operando, concatene-o à expressão pós-fixada
     *       que está sendo gerada, mais um espaço;
     *     - Se o token for um parêntese esquerdo, empilhe-o;
     *     - Se o token for um parêntese direito:
     *         - Enquanto a pilha não estiver vazia e o topo da pilha não for um
     *           parêntese esquerdo, desempilhe o topo e concatene-o à expressão
     *           pós-fixada que está sendo gerada, mais um espaço;
     *         - Após o enquanto, desempilhe o topo da pilha, descartando-o;
     *     - Se o token for um operador:
     *         - Enquanto a pilha não estiver vazia e a precedência do token for
     *           menor ou igual à precedência do topo da pilha, desempilhe o
     *           topo e se o que foi desempilhado for um operador, concatene-o à
     *           expressão pós-fixada que está sendo gerada, mais um espaço;
     *         - Após o enquanto, empilhe o token.
     *
     * Após o processamento de todos os tokens é preciso lidar com o que sobrou
     * na pilha. Sendo assim:
     * 
     *     - Enquanto a pilha não estiver vazia:
     *         - Desempilhe o topo da pilha e, se ele for um parênteses
     *           esquerdo, a expressão é inválida, então lance uma exceção do
     *           tipo IllegalArgumentException. Caso contrário, verifique se o
     *           que foi desempilhado é um operador. Se for, concatene-o à
     *           expressão pós-fixada que está sendo gerada, mais um espaço;
     *     
     * A expressão pós-fixada estará pronta na estrutura em que você está
     * armazenando a geração.
     * 
     * @param infix A expressão infixa.
     * @return A expressão pós-fixada correspondente.
     * @throws IllegalArgumentException Caso a expressão seja inválida, ou seja,
     * quando no processo de análise alguma operação peek ou pop lançar uma 
     * EmptyStackException ou se sobrar algum parêntese esquerdo na pilha
     * no final do processo de análise.
     */
    public static String infixToPostfix(String infix) throws IllegalArgumentException {
        Stack<String> stack = new Stack<>();
        
        StringBuilder postFix = new StringBuilder();
        
        String[] tokens = infix.split(" ");

        for (String token : tokens) {
            
            if (isOperand(token)) {
                postFix.append(token).append(" ");
            } else if (isLeftParenthesis(token)) {
                stack.push(token);
            } else if (isRightParenthesis(token)) {
                
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postFix.append(stack.pop()).append(" ");
                }
                
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Parêntese direito não corresponde a um parênteses esquerdo!");
                }
                
                stack.pop();
                
            } else if (isOperator(token)) {
                
                while (!stack.isEmpty() && getPrecedence(token) <= getPrecedence(stack.peek())) {
                    postFix.append(stack.pop()).append(" ");
                }
                
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            String top = stack.pop();
            if (top.equals("(")) {
                throw new IllegalArgumentException("Caractere de abertura no topo da pilha!!");
            }
            postFix.append(top).append(" ");
        }

        return postFix.toString().trim();
    }
    
    /**
     * Converte uma expressão aritmética pré-fixada para pós-fixada.
     * 
     * Algoritmo:
     *     - Processe a expressão pré-fixada em ordem inversa (direita para
     *       esquerda);
     *         - Se o token for um operando, empilhe-o;
     *         - Se o token for um operador, desempilhe dois operandos da pilha
     *           e crie uma string concatenando-os na forma:
     *               postfix = operando1 + operando2 + operador 
     *           e empilhe essa string.
     *     - Repita até o fim da expressão pré-fixada.
     *     - A expressão pós-fixada estará no topo da pilha ao fim do processo.
     * 
     * @param prefix A expressão pré-fixada.
     * @return A expressão pós-fixada correspondente.
     * @throws IllegalArgumentException Caso a expressão seja inválida, ou seja,
     * quando no processo de análise alguma operação peek ou pop lançar uma 
     * EmptyStackException.
     */
    public static String prefixToPostfix( String prefix ) throws IllegalArgumentException {
        
        // implementação
        Stack<String> stack = new Stack();
        
        String[] tokens = prefix.split(" ");
        
        for(int i = tokens.length - 1; i >= 0; i--){
            String ch = tokens[i];
            
            if(isOperand(ch)){
                stack.push(String.valueOf(ch));
                
            } else if(isOperator(ch)){
                
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Expressão inválida: operandos insuficientes para o operador '" + ch + "'");
                }
                
                String operando1 = stack.pop();
                String operando2 = stack.pop();
                
                String postfix = operando1 + " " + operando2 + " " + ch;
                
                stack.push(postfix);
            } else {
                throw new IllegalArgumentException("Caractere inválido na expressão: '" + ch + "'");
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Expressão inválida: operadores insuficientes para os operandos fornecidos!");
        }
        
        return stack.pop();
        
    }
    
    /**
     * Verifica se um token é um parênteses esquerdo.
     * 
     * @param token Token a ser verificado.
     * @return Verdadeiro caso o token seja um parênteses esquerdo, falso caso
     * contrário.
     */
    public static boolean isLeftParenthesis( char token ) {
        return token == '(';
    }
    
    /**
     * Verifica se um token é um parênteses esquerdo.
     * 
     * @param token Token a ser verificado.
     * @return Verdadeiro caso o token seja um parênteses esquerdo, falso caso
     * contrário.
     */
    public static boolean isLeftParenthesis( String token ) {
        return isLeftParenthesis( token.charAt( 0 ) );
    }
    
    /**
     * Verifica se um token é um parênteses direito.
     * 
     * @param token Token a ser verificado.
     * @return Verdadeiro caso o token seja um parênteses direito, falso caso
     * contrário.
     */
    public static boolean isRightParenthesis( char token ) {
        return token == ')';
    }
    
    /**
     * Verifica se um token é um parênteses direito.
     * 
     * @param token Token a ser verificado.
     * @return Verdadeiro caso o token seja um parênteses direito, falso caso
     * contrário.
     */
    public static boolean isRightParenthesis( String token ) {
        return isRightParenthesis( token.charAt( 0 ) );
    }
    
    /**
     * Verifica se um token é um operando.
     * 
     * @param token Token a ser verificado.
     * @return Verdadeiro caso o token seja um operando, falso caso
     * contrário.
     */
    public static boolean isOperand( char token ) {
        return token >= '0' && token <= '9'/* || 
               token >= 'A' && token <= 'Z' ||
               token >= 'a' && token <= 'z'*/;
    }
    
    /**
     * Verifica se um token é um operando.
     * 
     * @param token Token a ser verificado.
     * @return Verdadeiro caso o token seja um operando, falso caso
     * contrário.
     */
    public static boolean isOperand( String token ) {
        return isOperand( token.charAt( 0 ) );
    }
    
    /**
     * Verifica se um token é um operador.
     * 
     * @param token Token a ser verificado.
     * @return Verdadeiro caso o token seja um operador, falso caso
     * contrário.
     */
    public static boolean isOperator( char token ) {
        switch ( token ) {
            case '+':
            case '-':
            case '*':
            case '/':
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Verifica se um token é um operador.
     * 
     * @param token Token a ser verificado.
     * @return Verdadeiro caso o token seja um operador, falso caso
     * contrário.
     */
    public static boolean isOperator( String token ) {
        return isOperator( token.charAt( 0 ) );
    }
    
}
