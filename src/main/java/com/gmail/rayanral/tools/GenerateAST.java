package com.gmail.rayanral.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAST {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"
        ));
    }

    private static void defineAst(String outputDir, String baseClass, List<String> types) throws IOException {
        String path = outputDir + "/" + baseClass + ".java";

        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println("package com.gmail.rayanral.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseClass + " {");

        for(String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseClass, className, fields);
        }

        writer.println("}");
        writer.close();
    }

    private static void defineType(
            PrintWriter writer,
            String baseName,
            String className,
            String fieldList
    ) {

        writer.println("  static class " + className + " extends " +
                baseName + " {");
        String[] fields = fieldList.split(", ");

        // Declare fields.
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        // Constructor.
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }
        writer.println("    }");
        writer.println();

        writer.println("  }");

    }

}
