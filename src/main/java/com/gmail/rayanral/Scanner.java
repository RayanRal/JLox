package com.gmail.rayanral;

import java.util.ArrayList;
import java.util.List;

import static com.gmail.rayanral.TokenType.*;

public class Scanner {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch(c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ':':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case '!':
                addToken(isMatch('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(isMatch('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(isMatch('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(isMatch('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if(isMatch('/')) {
                    while (peek() != '\n' && isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case '"': string(); break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            case '\n':
                line++;
                break;
            default:
                Lox.error(line, "Unexpected character");
                break;
        }
    }

    private boolean isMatch(char expected) {
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if(peek() == '\n') {
                line++;
            }
            advance();
        }
        if(isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}
