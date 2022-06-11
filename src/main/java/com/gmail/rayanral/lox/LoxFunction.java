package com.gmail.rayanral.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Stmt.Function declaration;

    public LoxFunction(Stmt.Function declaration) {
        this.declaration = declaration;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(interpreter.globals);
        for(int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return ret) {
            return ret.value;
        }
        return null;
    }

    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
