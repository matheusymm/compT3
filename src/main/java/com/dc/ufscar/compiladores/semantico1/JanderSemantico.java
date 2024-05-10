package com.dc.ufscar.compiladores.semantico1;

import com.dc.ufscar.compiladores.semantico1.TabelaDeSimbolos.TipoJander;

public class JanderSemantico extends JanderBaseVisitor<Void>{
    TabelaDeSimbolos tabela;

    @Override
    public Void visitPrograma(JanderParser.ProgramaContext ctx) {
        tabela = new TabelaDeSimbolos();
        return super.visitPrograma(ctx);
    }

    @Override                               
    public Void visitDeclaracao_local(JanderParser.Declaracao_localContext ctx) {
        String nomeVar = ctx.IDENT().getText();
        System.out.println("Declarando variável " + nomeVar);
        String strTipoVar = ctx.tipo_basico().getText();
        TipoJander tipoVar = TipoJander.INVALIDO;
        switch (strTipoVar) {
            case "INTEIRO":
                tipoVar = TipoJander.INTEIRO;
                break;  
            case "REAL":
                tipoVar = TipoJander.REAL;
                break;
            case "LITERAL":
                tipoVar = TipoJander.LITERAL;
                break;
            case "LOGICO":
                tipoVar = TipoJander.LOGICO;
                break;
            default:
                break;
        }
        if(tabela.existe(nomeVar)) {
            JanderSemanticoUtils.adicionarErroSemantico(ctx.IDENT().getSymbol(), "Variável " + nomeVar + " já existe");
        } else {
            tabela.adicionar(nomeVar, tipoVar);
        }
        return super.visitDeclaracao_local(ctx);
    }               

    @Override
    public Void visitCmdAtribuicao(JanderParser.CmdAtribuicaoContext ctx) {
        TipoJander tipoExpressao = JanderSemanticoUtils.verificarTipo(tabela, ctx.expressao());
        if(tipoExpressao != TipoJander.INVALIDO) {
            String nomeVar = ctx.identificador().getText();
            if(!tabela.existe(nomeVar)) {
                JanderSemanticoUtils.adicionarErroSemantico(ctx.identificador().start, "Variável " + nomeVar + " não foi declarada antes do uso");
            } else {
                TipoJander tipoVar = tabela.verificar(nomeVar);
                if(tipoVar != tipoExpressao) {
                    JanderSemanticoUtils.adicionarErroSemantico(ctx.identificador().start, "Atribuição de tipos incompatíveis");
                }
            }
        }
        return super.visitCmdAtribuicao(ctx);
    }

    @Override
    public Void visitCmdLeia(JanderParser.CmdLeiaContext ctx) {
        for(JanderParser.IdentificadorContext ident : ctx.identificador()) {
            String nomeVar = ident.getText();
            if(!tabela.existe(nomeVar)) {
                JanderSemanticoUtils.adicionarErroSemantico(ident.start, "Variável " + nomeVar + " não foi declarada antes do uso");
            }
        }
        return super.visitCmdLeia(ctx);
    }

    @Override
    public Void visitExp_aritmetica(JanderParser.Exp_aritmeticaContext ctx) {
        JanderSemanticoUtils.verificarTipo(tabela, ctx);
        return super.visitExp_aritmetica(ctx);
    }
}
