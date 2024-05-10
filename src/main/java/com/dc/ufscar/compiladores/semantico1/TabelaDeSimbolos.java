package com.dc.ufscar.compiladores.semantico1;

import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {
    public enum TipoJander {
        LITERAL, 
        INTEIRO, 
        REAL, 
        LOGICO,
        INVALIDO
    }

    class EntradaTabelaDeSimbolos {
        String nome;
        TipoJander tipo;

        private EntradaTabelaDeSimbolos(String nome, TipoJander tipo) {
            this.nome = nome;
            this.tipo = tipo;
        }
    }

    private final Map<String, EntradaTabelaDeSimbolos> tabela;
    
    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }

    public void adicionar(String nome, TipoJander tipo) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public TipoJander verificar(String nome) {
        return tabela.get(nome).tipo;
    }
}
