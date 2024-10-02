/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esdc4aula02;

import java.util.Stack;

/**
 * Resolução do Exercício i2.1
 * 
 * @author Prof. Dr. David Buzatto
 */
public class Exercicioi2p1 {
    
    /**
     * Verifica se uma expressão composta por pares de símbolos arbitrários está
     * balanceada.
     * 
     * @param expression A expressão a ser verificada.
     * @param pairs Um conjunto de caracteres em que cada dois representam um
     * par de caracteres usados na verificação.
     * @return Se a expressão está balanceada.
     * @throws IllegalArgumentException Caso os pares forem fornecidos de forma
     * errada, ou seja, se houver uma quantidade ímpar de elementos, faltando 
     * assim a dupla de fechamento de um par.
     */
    public static boolean isBalanced( String expression, char... pairs ) 
            throws IllegalArgumentException {
        
        // implementação
        
        if(pairs.length % 2 != 0){
            throw new IllegalArgumentException("Número de pares inválido!");
        }
        
        Stack<Character> stack = new Stack();
        
        for(char ch: expression.toCharArray()){
            
            for(int i = 0; i < pairs.length; i += 2){
                
                if (ch == pairs[i]) {
                    stack.push(ch);
                    break;
                    
                } else if (ch == pairs[i + 1]) { 
                    
                    if (stack.isEmpty() || stack.peek() != pairs[i]) {
                        return false;
                    }
                    
                    stack.pop();
                    break;
                }
            }
        }

        return stack.isEmpty();
    }
    
}
