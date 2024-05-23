package com.dc.ufscar.compiladores.semantico1;

import java.io.IOException;
import java.io.PrintWriter;

import org.antlr.v4.runtime.CommonTokenStream;

import com.dc.ufscar.compiladores.semantico1.JanderParser.ProgramaContext;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

public class Principal {
    public static void main(String[] args) {
        try {
            // CharStream cs = CharStreams.fromFileName(args[0]);
            CharStream cs = CharStreams.fromFileName("entrada/2.algoritmo_2-4_apostila_LA.txt");
            JanderLexer lex = new JanderLexer(cs);
            // PrintWriter pw = new PrintWriter(args[1]);
            PrintWriter pw = new PrintWriter("2.out");

            CommonTokenStream tokens = new CommonTokenStream(lex);
            JanderParser parser = new JanderParser(tokens);
            MyCustomErrorListener mcel = new MyCustomErrorListener(pw);
            parser.addErrorListener(mcel);
            ProgramaContext arvore = parser.programa();
            JanderSemantico js = new JanderSemantico();
            js.visitPrograma(arvore);
            JanderSemanticoUtils.errosSemanticos.forEach(pw::println);
            pw.println("Fim da compilacao");
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}