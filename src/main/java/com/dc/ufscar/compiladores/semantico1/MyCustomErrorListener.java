package com.dc.ufscar.compiladores.semantico1;

import java.io.PrintWriter;
import org.antlr.v4.runtime.*;

public class MyCustomErrorListener extends BaseErrorListener {
    PrintWriter pw;
    boolean flag = false;

    public MyCustomErrorListener(PrintWriter pw) {
        this.pw = pw;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
            String msg, RecognitionException e) {
        // Aqui vamos colocar o tratamento de erro customizado
        if (flag)
            return;

        Token t = (Token) offendingSymbol;
        String tempName = JanderLexer.VOCABULARY.getDisplayName(t.getType());

        if (tempName.equals("ERRO")) {
            pw.println("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado");
        } else if (tempName.equals("COMENTARIO_NAO_FECHADO")) {
            pw.println("Linha " + t.getLine() + ": comentario nao fechado");
        } else if (tempName.equals("CADEIA_NAO_FECHADA")) {
            pw.println("Linha " + t.getLine() + ": cadeia literal nao fechada");
        } else if (t.getText().equals("<EOF>")) {
            pw.println("Linha " + line + ": erro sintatico proximo a EOF");
        } else {
            pw.println("Linha " + line + ": erro sintatico proximo a " + t.getText());
        }

        flag = true;
        pw.println("Fim da compilacao");
    }
}
