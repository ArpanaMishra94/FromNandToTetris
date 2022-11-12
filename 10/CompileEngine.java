import java.io.*;
import java.util.*;

public class CompileEngine {
    public static void main(String args[]) throws Exception {
        File input = new File(args[0]+".xml");
        File output = new File(args[0]+"Compile.xml");
        CompilationEngine compl = new CompilationEngine(new Scanner(input), new PrintWriter(output));
        compl.compiler();
    }
}

class CompilationEngine {
    Scanner in;
    PrintWriter writ;
    String cur;
    String[] op = {"+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "="};
    HashSet<String> operator = new HashSet<String>(Arrays.asList(op));

    CompilationEngine(Scanner a, PrintWriter p) {
        in = a;
        writ = p;
    }

    static String[] type(String cu) {
        cu = cu.trim();
        //System.out.println(cu);
        String[] toRet = new String[2];
        if (cu.split(" ").length == 3) {
            toRet[0] = cu.substring(1, cu.split(" ")[0].length() - 1);
            toRet[1] = cu.split(" ")[1];
        } else {
            String temp = "";
            for (int i = 1; i < cu.split(" ").length - 1; i++)
                temp += cu.split(" ")[i] + " ";
            toRet[0] = cu.substring(1, cu.split(" ")[0].length() - 1);
            toRet[1] = temp;
        }
        return toRet;
    }

    void compiler() {
        cur = in.nextLine();
        cur = in.nextLine().trim();
        while (!cur.equals("</tokens>") && in.hasNext()) {
            String[] line = type((String) cur);
            if (line[0].equals("keyword")) {
                if (line[1].equals("class"))
                    compileClass();
                //writ.flush();
            }
        }
        writ.flush();
        writ.close();
    }

    void compileClass() {
        writ.println("<class>\n<keyword> class </keyword>");
        System.out.println("<class>\n<keyword> class </keyword>");

        //class name
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol {
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //static or field variable
        cur = in.nextLine();
        while (type(cur)[1].equals("static") || type(cur)[1].equals("field"))
            compileClassVarDec();

        //subroutine
        while (type(cur)[1].equals("constructor") || type(cur)[1].equals("method") || type(cur)[1].equals("function"))
            compileSubroutine();

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        writ.println("</class>");
        System.out.println("</class>");
    }

    void compileClassVarDec() {
        writ.println("<classVarDec>");
        System.out.println("<classVarDec>");

        while (!type(cur)[1].equals(";")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine();
        }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        writ.println("</classVarDec>");
        System.out.println("</classVarDec>");
    }

    void compileSubroutine() {
        writ.println("<subroutineDec>");
        System.out.println("<subroutineDec>");

        //method, function, constructor
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //type
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // subroutine name
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // symbol (
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // parameter List
        cur = in.nextLine();
        writ.println("<parameterList>");
        System.out.println("<parameterList>");

        while (!type(cur)[1].equals(")"))
            compileParameterList();
        writ.println("</parameterList>");
        System.out.println("</parameterList>");

        // symbol )
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        writ.println("<subroutineBody>");
        System.out.println("<subroutineBody>");

        // symbol {
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // varDec
        cur = in.nextLine();
        while (type(cur)[1].equals("var"))
            compileVarDec();

        //statements
        writ.println("<statements>");
        System.out.println("<statements>");
        while (!type(cur)[1].equals("}"))
            compileStatements();
        writ.println("</statements>");
        System.out.println("</statements>");

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        writ.println("</subroutineBody>");
        System.out.println("</subroutineBody>");
        writ.println("</subroutineDec>");
        System.out.println("</subroutineDec>");
        cur = in.nextLine();
    }

    void compileParameterList() {
        //type
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // varName
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        if (type(cur)[1].equals(",")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine();
        }
    }

    void compileVarDec() {
        writ.println("<varDec>");
        System.out.println("<varDec>");

        while (!type(cur)[1].equals(";")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine();
        }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        cur = in.nextLine();
        writ.println("</varDec>");
        System.out.println("</varDec>");
    }

    void compileStatements() {

        if (type(cur)[1].equals("let"))
            compileLet();

        else if (type(cur)[1].equals("if"))
            compileIf();

        else if (type(cur)[1].equals("while"))
            compileWhile();

        else if (type(cur)[1].equals("do"))
            compileDo();

        else if (type(cur)[1].equals("return"))
            compileReturn();
    }

    void compileCall(){
        while (!type(cur)[1].equals("(")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine();
        }

        // symbol (
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // expressionList
        cur = in.nextLine();
        compileExpressionList();

        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        cur = in.nextLine();

    }

    void compileDo() {
        writ.println("<doStatement>");
        System.out.println("<doStatement>");

        // subroutine name
        while (!type(cur)[1].equals("(")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine();
        }

        // symbol (
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // expressionList
        cur = in.nextLine();
        compileExpressionList();

        while (!type(cur)[1].equals(";")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine();
        }

        //symbol ;
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        writ.println("</doStatement>");
        System.out.println("</doStatement>");
    }

    void compileLet() {
        writ.println("<letStatement>");
        System.out.println("<letStatement>");

        // keyword let
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // varName
        cur = in.nextLine();
        while (!(type(cur)[1].equals("[") || type(cur)[1].equals("="))) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine();
        }

        if (type(cur)[1].equals("[")) {
            // symbol [
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

            cur = in.nextLine();
            compileExpression();

            // symbol ]
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine();
        }

        // symbol =
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //expression
        cur = in.nextLine();
        compileExpression();

        //symbol ;
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        writ.println("</letStatement>");
        System.out.println("</letStatement>");
    }

    void compileExpressionList() {
        writ.println("<expressionList>");
        System.out.println("<expressionList>");

        while (!type(cur)[1].equals(")")) {
            if (type(cur)[1].equals(",")) {
                writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                System.out.printf("<%s> %s </%s>", type(cur)[0], type(cur)[1], type(cur)[0]);
                cur = in.nextLine();
            }
            compileExpression();
        }

        writ.println("</expressionList>");
        System.out.println("</expressionList>");

    }

    void compileTerm() {
        writ.println("<term>");
        System.out.println("<term>");

        if (type(cur)[0].equals("identifier") || type(cur)[1].equals("[") || type(cur)[1].equals("(") || type(cur)[0].equals("integerConstant") ||
                type(cur)[0].equals("stringConstant") || type(cur)[1].equals("true") || type(cur)[1].equals("false") || type(cur)[1].equals("null") ||
                type(cur)[1].equals("this") || type(cur)[1].equals("-") || type(cur)[1].equals("~") || type(cur)[1].equals(".")) {

            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

            if (type(cur)[0].equals("identifier")) {
                cur = in.nextLine();
                if (type(cur)[1].equals("(")) {
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                    System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine();
                    compileExpressionList();
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                    System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine();
                } else if (type(cur)[1].equals("[")) {
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                    System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine();
                    compileExpression();
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                    System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine();
                }
                else if(type(cur)[1].equals(".")){
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                    System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine();
                    compileCall();
                }
            } else if (type(cur)[1].equals("(")) {
                cur = in.nextLine();
                compileExpression();
                writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                cur = in.nextLine();
            } else if (type(cur)[1].equals("~") || type(cur)[1].equals("-")) {
                cur = in.nextLine();
                compileTerm();
            } else
                cur = in.nextLine();
        }
        writ.println("</term>");
        System.out.println("</term>");
    }

    void compileExpression() {
        writ.println("<expression>");
        System.out.println("<expression>");

        compileTerm();

        //cur = in.nextLine();
        while (operator.contains(type(cur)[1])) {

            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

            cur = in.nextLine();
            //System.out.println("aaaaaaaaaaaaaaaaaaaaaaa" + cur);
            if (!type(cur)[1].equals(";"))
                compileTerm();
            //System.out.println("s");
        }
        //System.out.println(cur);
        writ.println("</expression>");
        System.out.println("</expression>");
    }

    void compileWhile() {
        writ.println("<whileStatement>");
        System.out.println("<whileStatement>");

        // keyword while
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol (
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //expression
        cur = in.nextLine();
        compileExpression();

        //symbol )
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol {
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //statements
        cur = in.nextLine();
        writ.println("<statements>");
        System.out.println("<statements>");
        while (!type(cur)[1].equals("}"))
            compileStatements();
        writ.println("</statements>");
        System.out.println("</statements>");

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        writ.println("</whileStatement>");
        System.out.println("</whileStatement>");
    }

    void compileReturn() {
        writ.println("<returnStatement>");
        System.out.println("<returnStatement>");

        //return keyword
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        if(!type(cur)[1].equals(";"))
        compileExpression();

        //symbol ;
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        writ.println("</returnStatement>");
        System.out.println("</returnStatement>");
    }

    void compileIf() {
        writ.println("<ifStatement>");
        System.out.println("<ifStatement>");
        // keyword if
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol (
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // expression in if
        cur = in.nextLine();
        compileExpression();

        //symbol )
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol {
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //statements
        cur = in.nextLine();
        writ.println("<statements>");
        System.out.println("<statements>");
        while (!type(cur)[1].equals("}"))
            compileStatements();
        writ.println("</statements>");
        System.out.println("</statements>");

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
        if (type(cur)[1].equals("else"))
            compileElse();
        writ.println("</ifStatement>");
        System.out.println("</ifStatement>");
    }

    void compileElse() {
        // keyword else
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol {
        cur = in.nextLine();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //statements
        cur = in.nextLine();
        writ.println("<statements>");
        System.out.println("<statements>");
        while (!type(cur)[1].equals("}"))
            compileStatements();
        writ.println("</statements>");
        System.out.println("</statements>");

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine();
    }
}