package com.dc.ufscar.compiladores.semantico1;

import com.dc.ufscar.compiladores.semantico1.TabelaDeSimbolos.TipoJander;

public class JanderSemantico extends JanderBaseVisitor<Void> {
    TabelaDeSimbolos tabela;

    // aqui temos uma tabela, a global, precisamos ver
    // como faremos em relação a tabela de escopos
    @Override
    public Void visitPrograma(JanderParser.ProgramaContext ctx) {
        tabela = new TabelaDeSimbolos();
        return super.visitPrograma(ctx);
    }

    @Override
    public Void visitDecl_local_global(JanderParser.Decl_local_globalContext ctx) {

        if (ctx.declaracao_local() != null) {
            visitDeclaracao_local(ctx.declaracao_local());
        } else if (ctx.declaracao_global() != null) {
            visitDeclaracao_global(ctx.declaracao_global());
        }
        return super.visitDecl_local_global(ctx);
    }

    @Override
    public Void visitDeclaracao_local(JanderParser.Declaracao_localContext ctx) {

        if (ctx.variavel() != null) {
            return visitVariavel(ctx.variavel());
        }
        if (ctx.IDENT() != null) {

            String nomeVar = ctx.IDENT().getText();
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
            if (tabela.existe(nomeVar)) {
                JanderSemanticoUtils.adicionarErroSemantico(ctx.IDENT().getSymbol(),
                        "Variável " + nomeVar + " já existe");
            } else {
                tabela.adicionar(nomeVar, tipoVar);
            }
        }
        return super.visitDeclaracao_local(ctx);
    }

    @Override
    public Void visitVariavel(JanderParser.VariavelContext ctx) {

        String strTipoVar = ctx.tipo().getText();
        TipoJander tipoVar = TipoJander.INVALIDO;
        switch (strTipoVar.toUpperCase()) {
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

        for (JanderParser.IdentificadorContext ident : ctx.identificador()) {
            String nomeVar = ident.getText();

            if (tabela.existe(nomeVar)) {

                JanderSemanticoUtils.adicionarErroSemantico(ident.start, "identificador " +
                        nomeVar + " ja declarado anteriormente");
            } else {

                tabela.adicionar(nomeVar, tipoVar);
                if (tipoVar == TipoJander.INVALIDO) {
                    JanderSemanticoUtils.adicionarErroSemantico(ident.start, "tipo " + strTipoVar + " nao declarado");
                }
            }
        }
        // na duvida tira (:
        return super.visitVariavel(ctx);
    }

    @Override
    public Void visitCmdAtribuicao(JanderParser.CmdAtribuicaoContext ctx) {

        JanderSemanticoUtils.setNomeVarAtrib(ctx.identificador().getText());
        TipoJander tipoExpressao = JanderSemanticoUtils.verificarTipo(tabela, ctx.expressao());

        if (tipoExpressao != TipoJander.INVALIDO) {
            String nomeVar = ctx.identificador().getText();
            if (!tabela.existe(nomeVar)) {
                JanderSemanticoUtils.adicionarErroSemantico(ctx.identificador().start,
                        "identificador " + nomeVar + " nao declarado");
            } else {
                TipoJander tipoVar = tabela.verificar(nomeVar);
                if (JanderSemanticoUtils.verificarTipoCompativeL(tabela, tipoVar, tipoExpressao)) {
                    JanderSemanticoUtils.adicionarErroSemantico(ctx.identificador().start,
                            "atribuicao nao compativel para " + nomeVar);
                }
            }
        }

        return super.visitCmdAtribuicao(ctx);
    }

    @Override
    public Void visitCmdLeia(JanderParser.CmdLeiaContext ctx) {
        for (JanderParser.IdentificadorContext ident : ctx.identificador()) {
            String nomeVar = ident.getText();
            if (!tabela.existe(nomeVar)) {
                JanderSemanticoUtils.adicionarErroSemantico(ident.start,
                        "identificador " + nomeVar + " nao declarado");
            }
        }
        return super.visitCmdLeia(ctx);
    }

    // @Override
    // public Void visitExp_aritmetica(JanderParser.Exp_aritmeticaContext ctx) {

    // for (JanderParser.TermoContext termo : ctx.termo()) {
    // String nomeVar = termo.getText();
    // if (!tabela.existe(nomeVar)) {
    // JanderSemanticoUtils.adicionarErroSemantico(termo.start,
    // "identificador " + nomeVar + " nao declarado");
    // }
    // }

    // return super.visitExp_aritmetica(ctx);
    // }

    @Override
    public Void visitParcela_nao_unario(JanderParser.Parcela_nao_unarioContext ctx) {
        if (ctx.identificador() != null) {

            JanderSemanticoUtils.verificarTipo(tabela, ctx);
        } else if (ctx.CADEIA() != null) {

        }

        return super.visitParcela_nao_unario(ctx);
    }

    @Override
    public Void visitParcela_unario(JanderParser.Parcela_unarioContext ctx) {
        if (ctx.identificador() != null) {
            JanderSemanticoUtils.verificarTipo(tabela, ctx);
        } else if (ctx.NUM_INT() != null) {
        } else if (ctx.NUM_REAL() != null) {
        }

        return super.visitParcela_unario(ctx);
    }

}
