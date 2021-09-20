import java.io.*;
import java.util.*;

public class Lexer {
    public int line = 1;
    private char peek = ' ';
    private Hashtable words = new Hashtable();

    void reserve(Word t) {
        words.put(t.lexeme, t);
    }

    public Lexer() {
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.FALSE, "false"));
    }

    public Token scan() throws IOException {
        // Leitura de espacos em branco, quebra de linha e tabulacao
        if (peek == ' ') {
            for (;; peek = (char) System.in.read()) {
                if (peek == ' ' || peek == '\t')
                    continue;
                else if (peek == '\n')
                    line++;
                else
                    break;
            }
        }

        // Leitura de numeros inteiros
        if (Character.isDigit(peek)) {
            String strHex = Character.toString(peek);
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10); // Retorna o valor numerico em decimal
                peek = (char) System.in.read();
            } while (Character.isDigit(peek));

            // Leitura de constantes char em hexadecimal
            if (peek == 'x') {
                strHex += peek;
                for (int i = 0; i < 2; i++) {
                    peek = (char) System.in.read();
                    if (checkHex(peek)) {
                        v = 16 * v + Character.digit(peek, 16);
                    } else {
                        erro(line, strHex);
                    }
                }
            }

            return new Num(v);
        }

        Token t = new Token(peek);
        peek = ' ';
        return t;
    }

    private boolean checkHex(char c) {
        if (Character.isDigit(c) || c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F') {
            return true;
        } else {
            return false;
        }
    }

    private void erro(int line, String lexeme) {
        System.out.println("Erro na linha: " + (line) + ". Lexema nao reconhecido: [" + lexeme + "]");
        System.exit(0);
    }

}
