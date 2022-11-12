import java.io.*;
import java.util.*;

class VMWriter{
    PrintWriter writ;

    VMWriter(PrintWriter p){
        writ = p;
    }

    void writePush(String st, int ind){
        writ.printf("push %s %s\n", st, Integer.toString(ind));
    }

    void writePop(String st, int ind){
        writ.printf("pop %s %s\n", st, Integer.toString(ind));
    }

    void writeArithmetic(String st){
        writ.println(st);
    }

    void writeLabel(String st){
        writ.println("label "+st);
    }

    void writeIfGoto(String st){
        writ.println("if-goto "+st);
    }

    void writeGoto(String st){
        writ.println("goto "+st);
    }

    void writeCall(String name, int index){
        writ.printf("call %s %s\n", name, Integer.toString(index));
    }

    void writeFunction(String clas, String name, int index){
        writ.printf("function %s.%s %s\n",clas, name, Integer.toString(index));
    }

    void writeReturn(){
        writ.println("return");
    }

    void close(){
        writ.flush();;
        writ.close();
    }
}

class SymbolTable{
    int staticIndex=0, fieldIndex=0, localIndex=0, argumentIndex=0;
    LinkedList<SymbolTableBlock> table = new LinkedList<SymbolTableBlock>();

    int indexFinder(String s){
        if(s.equals("static")){
            staticIndex++;
            return staticIndex-1;
        }
        else if(s.equals("field")){
            fieldIndex++;
            return fieldIndex-1;
        }
        else if(s.equals("local")){
            localIndex++;
            return localIndex-1;
        }
        else{
            argumentIndex++;
            return argumentIndex-1;
        }
    }

    boolean contains(String name){
        Iterator it = table.iterator();
        while (it.hasNext()){
            SymbolTableBlock bloc = (SymbolTableBlock) it.next();
            if(bloc.name.equals(name))
                return true;
        }
        return false;
    }

    void define(String na, String ty, String kin){
        table.add(new SymbolTableBlock(na, ty, kin, indexFinder(kin)));
    }

    int varCount(String s){
        if(s.equals("static"))
            return staticIndex;
        else if(s.equals("field"))
            return fieldIndex;
        else if(s.equals("local"))
            return localIndex;
        else
            return argumentIndex;
    }

    String kindOf(String name){
        Iterator it = table.iterator();
        SymbolTableBlock temp;
        while (it.hasNext()){
            temp = (SymbolTableBlock) it.next();
            if(name.equals((temp.name)))
                return temp.kind;
        }
        return "error";
    }

    String typeOf(String name){
        Iterator it = table.iterator();
        SymbolTableBlock temp;
        while (it.hasNext()){
            temp = (SymbolTableBlock) it.next();
            if(name.equals((temp.name)))
            return temp.type;
        }
        return "error";
    }

    int indexOf(String name){
        Iterator it = table.iterator();
        SymbolTableBlock temp;
        while (it.hasNext()){
            temp = (SymbolTableBlock) it.next();
            if(name.equals((temp.name)))
            return temp.index;
        }
        System.out.println("error in indexof");
        return -1;
    }
}

class SymbolTableBlock{
    String name, type, kind;
    int index;
    SymbolTableBlock(String na, String ty, String kin, int ind){
        name = na;
        type = ty;
        kind = kin;
        index = ind;
    }
}

class Tokenizer{
    String[] keyWordArr = {"class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean", "void", "true", "false", "null", "this",
            "let", "do", "if", "else", "while", "return"};
    String[] symbolArr = {"{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~"};
    HashSet<String> keyWord = new HashSet<String>(Arrays.asList(keyWordArr));
    HashSet<String> symbol = new HashSet<String>(Arrays.asList(symbolArr));

    Tokenizer(Scanner in, PrintWriter writ){
        writ.println("<tokens>");
        String cur;
        String[] typeval;
        while (in.hasNext()) {
            cur = in.nextLine().trim();
            System.out.println("line"+ cur);
            while (true) {
                cur = cur.trim();
                if (cur == null || cur.length() == 0 || cur.startsWith("//"))
                    break;
                typeval = Finder(cur);
                System.out.println("while "+ cur+" "+typeval[0]+" "+typeval[1]);
                cur = typeval[2];
                writ.printf("%s \n","<"+typeval[0]+"> " + typeval[1] + " </"+typeval[0]+">");
            }
        }
        writ.println("</tokens>");
        writ.flush();
        writ.close();
    }

    String[] Finder(String cur){
        Iterator it = keyWord.iterator();
        String temp;
        while (it.hasNext()) {
            temp = (String) it.next();
            if (cur.startsWith(temp)) {
                cur = cur.substring(temp.length()).trim();
                //System.out.println(cur + " "+temp.length());
                String[] toRet = {"keyword", temp, cur};
                return toRet;
            }
        }
        it = symbol.iterator();
        while (it.hasNext()) {
            temp = (String) it.next();
            if (cur.startsWith(temp)) {
                cur= cur.substring(temp.length()).trim();
                temp = (temp =="<"?"&lt;":temp);
                temp = (temp ==">"?"&gt;":temp);
                temp = (temp =="\""?"&quot;":temp);
                temp = (temp =="&"?"&amp;":temp);
                String[] toRet = {"symbol", temp, cur};
                return toRet;
            }
        }
        if(Character.compare(cur.charAt(0),'0')>=0 && Character.compare(cur.charAt(0),'9')<=0)
            return integerConstant(cur);
        if(cur.charAt(0)=='"')
            return stringConstant(cur);
        return identifier(cur);
    }

    String[] integerConstant(String cur) {
        String inte = "";
        int len = cur.length(), i = 0;
        for (; i < len && Character.compare(cur.charAt(i), '0') >= 0 && Character.compare(cur.charAt(i), '9') <= 0; i++)
            inte += cur.charAt(i);
        cur = cur.substring(i).trim();
        String[] toRet = {"integerConstant", inte, cur};
        return toRet;
    }

    String[] stringConstant(String cur){
        String temp = "";
        int i=1;
        for(;i<cur.length() && cur.charAt(i)!='"'; i++)
            temp += cur.charAt(i);
        System.out.println("aa"+temp);
        cur=cur.substring(++i).trim();
        String[] toRet = {"stringConstant", temp, cur};
        return toRet;
    }

    boolean isKeyword(String cur){
        Iterator it = keyWord.iterator();
        while (it.hasNext())
            if(cur.startsWith((String)it.next()))
                return true;
        return false;
    }

    boolean isSymbol(String c){
        if(symbol.contains(c))
            return true;
        else
            return false;
    }

    String[] identifier(String cur){
        String temp = "";
        int i =0;
        for(;i<cur.length() && !isSymbol(cur.substring(i,i+1)) && cur.charAt(i)!=' ';i++)
            temp+=cur.charAt(i);
        cur= cur.substring(i).trim();
        String[] toRet = {"identifier", temp, cur};
        return toRet;
    }
}

class Analyzer {
    Scanner in;
    PrintWriter writ;
    String cur;
    String[] op = {"+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "="};
    HashSet<String> operator = new HashSet<String>(Arrays.asList(op));

    Analyzer(Scanner a, PrintWriter p) {
        in = a;
        writ = p;
    }

    static String[] type(String cu) {
        cu = cu.trim();
        System.out.println(cu);
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
        cur = in.nextLine().trim();
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
        //System.out.println("<class>\n<keyword> class </keyword>");

        //class name
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        //System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol {
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        //System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //static or field variable
        cur = in.nextLine().trim();
        while (type(cur)[1].equals("static") || type(cur)[1].equals("field"))
            compileClassVarDec();

        //subroutine
        while (type(cur)[1].equals("constructor") || type(cur)[1].equals("method") || type(cur)[1].equals("function"))
            compileSubroutine();

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        writ.println("</class>");
       // System.out.println("</class>");
    }

    void compileClassVarDec() {
        writ.println("<classVarDec>");
       // System.out.println("<classVarDec>");

        while (!type(cur)[1].equals(";")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            //System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine().trim();
        }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        //System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        writ.println("</classVarDec>");
       // System.out.println("</classVarDec>");
    }

    void compileSubroutine() {
        writ.println("<subroutineDec>");
       // System.out.println("<subroutineDec>");

        //method, function, constructor
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //type
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
     //   System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // subroutine name
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // symbol (
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // parameter List
        cur = in.nextLine().trim();
        writ.println("<parameterList>");
      //  System.out.println("<parameterList>");

        while (!type(cur)[1].equals(")"))
            compileParameterList();
        writ.println("</parameterList>");
      //  System.out.println("</parameterList>");

        // symbol )
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        writ.println("<subroutineBody>");
       // System.out.println("<subroutineBody>");

        // symbol {
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // varDec
        cur = in.nextLine().trim();
        while (type(cur)[1].equals("var"))
            compileVarDec();

        //statements
        writ.println("<statements>");
       /// System.out.println("<statements>");
        while (!type(cur)[1].equals("}"))
            compileStatements();
        writ.println("</statements>");
   //     System.out.println("</statements>");

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        //System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        writ.println("</subroutineBody>");
       // System.out.println("</subroutineBody>");
        writ.println("</subroutineDec>");
      //  System.out.println("</subroutineDec>");
        cur = in.nextLine().trim();
    }

    void compileParameterList() {
        //type
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        //System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // varName
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        if (type(cur)[1].equals(",")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        //    System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine().trim();
        }
    }

    void compileVarDec() {
        writ.println("<varDec>");
    //    System.out.println("<varDec>");

        while (!type(cur)[1].equals(";")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      ///      System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine().trim();
        }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        cur = in.nextLine().trim();
        writ.println("</varDec>");
       // System.out.println("</varDec>");
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
         //   System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine().trim();
        }

        // symbol (
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // expressionList
        cur = in.nextLine().trim();
        compileExpressionList();

        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        cur = in.nextLine().trim();

    }

    void compileDo() {
        writ.println("<doStatement>");
     //   System.out.println("<doStatement>");

        // subroutine name
        while (!type(cur)[1].equals("(")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //      System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine().trim();
        }

        // symbol (
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        //System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // expressionList
        cur = in.nextLine().trim();
        compileExpressionList();

        while (!type(cur)[1].equals(";")) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
           // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine().trim();
        }

        //symbol ;
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        writ.println("</doStatement>");
       // System.out.println("</doStatement>");
    }

    void compileLet() {
        writ.println("<letStatement>");
      //  System.out.println("<letStatement>");

        // keyword let
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // varName
        cur = in.nextLine().trim();
        while (!(type(cur)[1].equals("[") || type(cur)[1].equals("="))) {
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //      System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine().trim();
        }

        if (type(cur)[1].equals("[")) {
            // symbol [
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
         //   System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

            cur = in.nextLine().trim();
            compileExpression();

            // symbol ]
            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
          //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            cur = in.nextLine().trim();
        }

        // symbol =
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //expression
        cur = in.nextLine().trim();
        compileExpression();

        //symbol ;
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        writ.println("</letStatement>");
      //  System.out.println("</letStatement>");
    }

    void compileExpressionList() {
        writ.println("<expressionList>");
       // System.out.println("<expressionList>");

        while (!type(cur)[1].equals(")")) {
            if (type(cur)[1].equals(",")) {
                writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
            //    System.out.printf("<%s> %s </%s>", type(cur)[0], type(cur)[1], type(cur)[0]);
                cur = in.nextLine().trim();
            }
            compileExpression();
        }

        writ.println("</expressionList>");
      //  System.out.println("</expressionList>");

    }

    void compileTerm() {
        writ.println("<term>");
       // System.out.println("<term>");

        if (type(cur)[0].equals("identifier") || type(cur)[1].equals("[") || type(cur)[1].equals("(") || type(cur)[0].equals("integerConstant") ||
                type(cur)[0].equals("stringConstant") || type(cur)[1].equals("true") || type(cur)[1].equals("false") || type(cur)[1].equals("null") ||
                type(cur)[1].equals("this") || type(cur)[1].equals("-") || type(cur)[1].equals("~") || type(cur)[1].equals(".")) {

            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
         //   System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

            if (type(cur)[0].equals("identifier")) {
                cur = in.nextLine().trim();
                if (type(cur)[1].equals("(")) {
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                  //  System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine().trim();
                    compileExpressionList();
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                   // System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine().trim();
                } else if (type(cur)[1].equals("[")) {
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                 //   System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine().trim();
                    compileExpression();
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                  //  System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine().trim();
                }
                else if(type(cur)[1].equals(".")){
                    writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                   // System.out.printf("<%s> %s </%s\n>", type(cur)[0], type(cur)[1], type(cur)[0]);
                    cur = in.nextLine().trim();
                    compileCall();
                }
            } else if (type(cur)[1].equals("(")) {
                cur = in.nextLine().trim();
                compileExpression();
                writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
               // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
                cur = in.nextLine().trim();
            } else if (type(cur)[1].equals("~") || type(cur)[1].equals("-")) {
                cur = in.nextLine().trim();
                compileTerm();
            } else
                cur = in.nextLine().trim();
        }
        writ.println("</term>");
       // System.out.println("</term>");
    }

    void compileExpression() {
        writ.println("<expression>");
      //  System.out.println("<expression>");

        compileTerm();

        //cur = in.nextLine().trim();
        while (operator.contains(type(cur)[1])) {

            writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
          //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

            cur = in.nextLine().trim();
            //System.out.println("aaaaaaaaaaaaaaaaaaaaaaa" + cur);
            if (!type(cur)[1].equals(";"))
                compileTerm();
            //System.out.println("s");
        }
        //System.out.println(cur);
        writ.println("</expression>");
       // System.out.println("</expression>");
    }

    void compileWhile() {
        writ.println("<whileStatement>");
     //   System.out.println("<whileStatement>");

        // keyword while
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol (
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //expression
        cur = in.nextLine().trim();
        compileExpression();

        //symbol )
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol {
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //statements
        cur = in.nextLine().trim();
        writ.println("<statements>");
       // System.out.println("<statements>");
        while (!type(cur)[1].equals("}"))
            compileStatements();
        writ.println("</statements>");
       // System.out.println("</statements>");

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        writ.println("</whileStatement>");
       // System.out.println("</whileStatement>");
    }

    void compileReturn() {
        writ.println("<returnStatement>");
        //System.out.println("<returnStatement>");

        //return keyword
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        if(!type(cur)[1].equals(";"))
            compileExpression();

        //symbol ;
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
      //  System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        writ.println("</returnStatement>");
      //  System.out.println("</returnStatement>");
    }

    void compileIf() {
        writ.println("<ifStatement>");
        //System.out.println("<ifStatement>");
        // keyword if
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        //System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol (
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        // expression in if
        cur = in.nextLine().trim();
        compileExpression();

        //symbol )
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
     //   System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol {
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
       // System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //statements
        cur = in.nextLine().trim();
        writ.println("<statements>");
       // System.out.println("<statements>");
        while (!type(cur)[1].equals("}"))
            compileStatements();
        writ.println("</statements>");
       // System.out.println("</statements>");

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
     //   System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
        if (type(cur)[1].equals("else"))
            compileElse();
        writ.println("</ifStatement>");
     //   System.out.println("</ifStatement>");
    }

    void compileElse() {
        // keyword else
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //symbol {
        cur = in.nextLine().trim();
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        //statements
        cur = in.nextLine().trim();
        writ.println("<statements>");
        System.out.println("<statements>");
        while (!type(cur)[1].equals("}"))
            compileStatements();
        writ.println("</statements>");
        System.out.println("</statements>");

        //symbol }
        writ.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);
        System.out.printf("<%s> %s </%s>\n", type(cur)[0], type(cur)[1], type(cur)[0]);

        cur = in.nextLine().trim();
    }
}

class CompilationEngine {
    VMWriter writ;
    Scanner in;
    String cur;
    String className;
    int label_unique = 0;
    SymbolTable classTable, subroutineTable;
    CompilationEngine(Scanner a, PrintWriter b) {
        in = a;
        writ = new VMWriter(b);
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

    static void checkCurrent(String get, String exp){
        if(!get.equals(exp))
            System.out.println("Get "+get+" expected "+exp);
    }

    void CompileClass() {
        classTable = new SymbolTable();

        // <class>
        cur = in.nextLine().trim();

        // keyword class then class name then symbol {
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "class");

        // class name
        cur = in.nextLine().trim();
        className = type(cur)[1];
        // symbol {
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "{");

        cur = in.nextLine().trim();
        //checkCurrent(cur, "<classVarDec>");
        while (cur.equals("<classVarDec>")) {
            compileClassVarDec();
            cur = in.nextLine().trim();
        }

        // cur = <subroutineDec>

        checkCurrent(cur, "<subroutineDec>");
        while(cur.equals("<subroutineDec>")) {
            compileSubroutineDec();
            // next command
            cur = in.nextLine().trim();
        }

        //cur = symbol }
        checkCurrent(type(cur)[1].trim(), "}");

        // </class>
        cur = in.nextLine().trim();
        checkCurrent(cur, "</class>");
    }

    void compileClassVarDec() {
        // keyword field/ static
        cur = in.nextLine().trim();
        String kind = type(cur)[1];

        //type
        cur = in.nextLine().trim();
        String typ = type(cur)[1];

        //VarName
        cur = in.nextLine().trim();
        while (true) {
            String name = type(cur)[1];

            classTable.define(name, typ, kind);

            cur = in.nextLine().trim();
            if (type(cur)[1].equals(";"))
                break;
            if (type(cur)[1].equals(","))
                cur = in.nextLine().trim();
        }
        //</classVarDec>
        cur = in.nextLine().trim();
        checkCurrent(cur, "</classVarDec>");
    }

    void compileSubroutineDec() {

        subroutineTable = new SymbolTable();

        // keyword method, function, constructor
        cur = in.nextLine().trim();
        String subroutineType = type(cur)[1];

        // subroutine return type
        cur = in.nextLine().trim();
        String retType = type(cur)[1];

        // subroutine name
        cur = in.nextLine().trim();
        String name = type(cur)[1];
        // symbol (
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "(");

        // parameter list
        cur = in.nextLine().trim();
        checkCurrent(cur, "<parameterList>");
        compileParameterList();

        // cur = )

        // subroutine body
        cur = in.nextLine().trim();
        checkCurrent(cur, "<subroutineBody>");

        // symbol {
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "{");

        compileSubroutineBody(name, subroutineType);

        //</subroutineDec>
        cur = in.nextLine().trim();
        checkCurrent(cur, "</subroutineDec>");

    }

    void compileParameterList() {
        //keyword type
        cur = in.nextLine().trim();
        while (true) {
            if (type(cur)[1].equals(","))
                cur = in.nextLine().trim();
            else if (cur.equals("</parameterList>"))
                break;

            // keyword
            String typ = type(cur)[1];

            //name
            cur = in.nextLine().trim();
            String name = type(cur)[1];


            subroutineTable.define(name, typ, "argument");

            cur = in.nextLine().trim();
        }
        // symbol }
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], ")");
    }

    void compileSubroutineBody(String name, String subroutineType) {
        int local = 0;
        // <varDec> or <statements>
        cur = in.nextLine().trim();

        while (cur.equals("<varDec>")) {
            local += compileVarDec();;
            cur = in.nextLine().trim();
        }
        local = subroutineTable.varCount("local");

        writ.writeFunction(className, name, local);
        if(subroutineType.equals("method")){
            writ.writePush("argument", 0);
            writ.writePop("pointer", 0);
        }

        else if(subroutineType.equals("constructor")){
            writ.writePush("constant", classTable.varCount("field"));
            writ.writeCall("Memory.alloc", 1);
            writ.writePop("pointer", 0);
        }

        // type of statement
        cur = in.nextLine().trim();
        while (!cur.equals("</statements>")) {
            //System.out.println("asd "+cur);
            compileStatement();
            cur = in.nextLine().trim();
        }

        //symbol }
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "}");

        //</subroutineBody>
        cur = in.nextLine().trim();
        checkCurrent(cur, "</subroutineBody>");

    }

    int compileVarDec() {
        int locl = 0;
        // keyword var
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "var");

        //type
        cur = in.nextLine().trim();
        String typ = type(cur)[1];

        // varName
        cur = in.nextLine().trim();
        while (true) {
            if (type(cur)[1].equals(",")) {
                cur = in.nextLine().trim();
            }
            if (type(cur)[1].equals(";")) {
                break;
            }
            String name = type(cur)[1];
            subroutineTable.define(name, typ, "local");
            locl++;
            cur = in.nextLine().trim();
        }
        //</varDec>
        cur = in.nextLine().trim();
        return locl;
    }

    void compileStatement() {
        if (cur.equals("<letStatement>"))
            letStatement();
        else if (cur.equals("<doStatement>"))
            doStatement();
        else if (cur.equals("<ifStatement>"))
            ifStatement();
        else if (cur.equals("<whileStatement>"))
            whileStatement();
        else if(cur.equals("<returnStatement>"))
            compileReturn();
    }

    void compileReturn() {
        // keyword return
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "return");

        // next ; or expression
        cur = in.nextLine().trim();
        if (type(cur)[1].equals(";"))
            writ.writePush("constant", 0);
        else if (cur.equals("<expression>"))
            compileExpression();
        writ.writeReturn();

        // <returnStatement>
        cur = in.nextLine().trim();
        checkCurrent(cur, "</returnStatement>");
    }

    void letStatement() {
        // let keyword
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "let");

        // identifier name
        cur = in.nextLine().trim();
        String name = type(cur)[1];
      //  System.out.println(name);

        // symbol =,[
        cur = in.nextLine().trim();
        String sym = type(cur)[1];
        if(sym.equals("[")){
           // System.out.println(subroutineTable.contains(name));
            if(subroutineTable.contains(name))
                writ.writePush(subroutineTable.kindOf(name), subroutineTable.indexOf(name));
            else if(classTable.contains(name))
                writ.writePush("this", classTable.indexOf(name));
            //<expression>
            cur = in.nextLine().trim();
            checkCurrent(cur, "<expression>");
            compileExpression();

            writ.writeArithmetic("add");

            //symbol ]
            // symbol =
            cur = in.nextLine().trim();
            checkCurrent(type(cur)[1], "=");

            // <expression>
            cur = in.nextLine().trim();
            checkCurrent(cur, "<expression>");

            compileExpression();

            //symbol ;

            writ.writePop("temp", 0);

            writ.writePop("pointer", 1);

            writ.writePush("temp", 0);

            writ.writePop("that", 0);
        }
        else {
            //<expression>
            cur = in.nextLine().trim();
            checkCurrent(cur, "<expression>");

            compileExpression();
            // cur = ;

            if(subroutineTable.contains(name)) {
                writ.writePop(subroutineTable.kindOf(name), subroutineTable.indexOf(name));
            }
            else if(classTable.contains(name)) {
                writ.writePop("this", classTable.indexOf(name));
            }
            else {
                Iterator it = subroutineTable.table.iterator();
                while(it.hasNext()){
                    SymbolTableBlock t = (SymbolTableBlock) it.next();
                    System.out.println(t.name+" "+t.kind+" "+t.type);
                }
                System.out.println("var not found " + name);
            }
        }
        // </letStatement>
        cur = in.nextLine().trim();
        checkCurrent(cur, "</letStatement>");
    }

    void doStatement() {
        // keyword do
        cur = in.nextLine().trim();
        // name
        cur = in.nextLine().trim();
        String name = type(cur)[1];

        // symbol . (
        cur = in.nextLine().trim();

        compileSubroutineCall(name);

        //symbol ;
        //</doStatement>
        cur = in.nextLine().trim();
        checkCurrent(cur, "</doStatement>");

        //pop void value
        writ.writePop("temp", 0);
    }

    void ifStatement() {
        // keyword if
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "if");

        String endLabel = "END_"+Integer.toString(label_unique++);
        String elseLabel = "ELSE_"+Integer.toString(label_unique++);

        //symbol (
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "(");

        //<expression>
        cur = in.nextLine().trim();
        compileExpression();

        checkCurrent(type(cur)[1], ")");

        writ.writeArithmetic("not");
        writ.writeIfGoto(elseLabel);

        // symbol {
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "{");

        // <statements>
        cur = in.nextLine().trim();
        checkCurrent(cur, "<statements>");

        //type of statement
        cur = in.nextLine().trim();
        while (true) {
            if(type(cur)[1].equals("}"))
                break;
            compileStatement();
            cur = in.nextLine().trim();
        }

        // symbol }

        writ.writeLabel(endLabel);
        writ.writeLabel(elseLabel);
        // else or not
        cur = in.nextLine().trim();
        if(type(cur)[1].equals("else")){
            // symbol {
            cur = in.nextLine().trim();
            checkCurrent(type(cur)[1], "{");

            //<statement>
            cur= in.nextLine().trim();
            checkCurrent(cur, "<statements>");
            // type of statements
            cur = in.nextLine().trim();
            while (true) {
                //System.out.println(cur+" ppp");
                if(type(cur)[1].equals("}"))
                    break;
                compileStatement();
                cur = in.nextLine().trim();
            }

            writ.writeLabel(endLabel);
            // cur= }

            // </Ifstatment>
            cur = in.nextLine().trim();
        }
    }

    void whileStatement() {
        // keyword while
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "while");

        String continueLabel = "CONTINUE_"+Integer.toString(label_unique++);
        String topLabel = "CONTINUE_"+Integer.toString(label_unique++);
        writ.writeLabel(topLabel);

        // symbol (
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "(");

        //<expression>
        cur = in.nextLine().trim();
        compileExpression();

        //symbol )
        checkCurrent(type(cur)[1], ")");

        writ.writeArithmetic("not");
        writ.writeIfGoto(continueLabel);

        // symbol {
        cur = in.nextLine().trim();
        checkCurrent(type(cur)[1], "{");

        // <statements>
        cur = in.nextLine().trim();
        checkCurrent(cur, "<statements>");

        //type of statement
        cur = in.nextLine().trim();
        while (true) {
            if(type(cur)[1].equals("}"))
                break;
            compileStatement();
            cur = in.nextLine().trim();
        }

        // cur = }

        writ.writeGoto(topLabel);
        writ.writeLabel(continueLabel);

        // </whileStatement>
        cur = in.nextLine().trim();
        checkCurrent(cur, "</whileStatement>");
    }

    int compileExpressionList() {
        int toRet = 0;
        while (!cur.equals("</expressionList>")) {
            if (type(cur)[1].equals(","))
                cur = in.nextLine().trim();
            compileExpression();
            toRet++;
        }
        return toRet;
    }

    void compileTerm() {
        // <term>
        checkCurrent(cur, "<term>");
        // identifer, integerConstant, Array, (
        cur = in.nextLine().trim();

        if(type(cur)[0].equals("integerConstant")) {
            writ.writePush("constant", Integer.valueOf(type(cur)[1]));
            //</term>
            cur = in.nextLine().trim();
            checkCurrent(cur, "</term>");
        }
        else if(type(cur)[0].equals("stringConstant")){
            String st = type(cur)[1].trim();
            //System.out.println(st);
            //System.out.println(st.length());
            writ.writePush("constant", st.length());
            writ.writeCall("String.new", 1);
            for(int i =0;i<st.length();i++){
                writ.writePush("constant", (int) st.charAt(i));
                writ.writeCall("String.appendChar", 2);
            }
            cur = in.nextLine().trim();
        }
        else if(type(cur)[1].equals("true") || type(cur)[1].equals("false")
                || type(cur)[1].equals("null") || type(cur)[1].equals("this")){
            if(type(cur)[1].equals("false") || type(cur)[1].equals("null"))
                writ.writePush("constant", 0);
            else if(type(cur)[1].equals("this"))
                writ.writePush("pointer", 0);
            else if(type(cur)[1].equals("true")){
                writ.writePush("constant", 0);
                writ.writeArithmetic("not");
            }
            cur = in.nextLine().trim();
        }
        else if(type(cur)[1].equals("-") || type(cur)[1].equals("~")){
            String lastSymbol = type(cur)[1];
            cur = in.nextLine().trim();
            compileTerm();
            if(lastSymbol.equals("-"))
                writ.writeArithmetic("neg");
            else
                writ.writeArithmetic("not");
        }
        else if(type(cur)[0].equals("identifier")){
            String varName = type(cur)[1];
            cur = in.nextLine().trim();
            if(type(cur)[1].equals("[")){
                //<expression>
                cur = in.nextLine().trim();
                checkCurrent(cur, "<expression>");
                if(subroutineTable.contains(varName)){
                    writ.writePush(subroutineTable.kindOf(varName),
                            subroutineTable.indexOf(varName));
                    compileExpression();
                    //symbol ]
                    checkCurrent(type(cur)[1], "]");
                    // </term>
                    cur = in.nextLine().trim();
                    checkCurrent(cur, "</term>");
                    writ.writeArithmetic("add");
                    writ.writePop("pointer", 1);
                    writ.writePush("that", 0);
                }
                else if(classTable.contains(varName)){
                    writ.writePush("this",
                            classTable.indexOf(varName));
                    compileExpression();
                    //symbol ]
                    checkCurrent(type(cur)[1], "]");

                    // </term>
                    cur = in.nextLine().trim();
                    checkCurrent(cur, "</term>");
                    writ.writeArithmetic("add");
                    writ.writePop("pointer", 1);
                    writ.writePush("that", 0);
                }
                else
                    System.out.println("No var found Array");
            }
            else if(type(cur)[1].equals("(") || type(cur)[1].equals(".")){
                compileSubroutineCall(varName);
            }
            else if(subroutineTable.contains(varName))
                writ.writePush(subroutineTable.kindOf(varName), subroutineTable.indexOf(varName));
            else if(classTable.contains(varName))
                writ.writePush("this", classTable.indexOf(varName));
        }
        else if(type(cur)[1].equals("(")){
            // </expression>
            cur = in.nextLine().trim();
            checkCurrent(cur, "<expression>");
            compileExpression();

            //symbol )
            //</term>
            cur = in.nextLine().trim();
            checkCurrent(cur, "</term>");
        }
        // next command
        cur = in.nextLine().trim();
    }

    void compileSubroutineCall(String name){
        int numArg = 0;
        if(type(cur)[1].equals("(")){
            writ.writePush("pointer", 0);
            // expression list
            cur = in.nextLine().trim();
            checkCurrent(cur, "<expressionList>");

            cur = in.nextLine().trim();
            if(!cur.equals("</expressionList>"))
                numArg = 1 + compileExpressionList();
            else
                numArg = 1;
            writ.writeCall(className+"."+name, numArg);

            //symbol )
            cur = in.nextLine().trim();
        }
        else if(type(cur)[1].equals(".")){
            cur = in.nextLine().trim();
            String methodName = type(cur)[1];
            if(subroutineTable.contains(name)){
                writ.writePush(subroutineTable.kindOf(name), subroutineTable.indexOf(name));

                //symbol (
                cur = in.nextLine().trim();
                checkCurrent(type(cur)[1], "(");

                //<expressionList>
                cur = in.nextLine().trim();
                checkCurrent(cur, "<expressionList>");

                cur = in.nextLine().trim();
                if(!cur.equals("</expressionList>"))
                    numArg = 1 + compileExpressionList();
                else
                    numArg = 1;

                writ.writeCall(subroutineTable.typeOf(name)+"."+methodName, numArg);

                //symbol )
                cur = in.nextLine().trim();
                checkCurrent(type(cur)[1], ")");
            }
            else if(classTable.contains(name)){
                writ.writePush("this", classTable.indexOf(name));
                //symbol (
                cur = in.nextLine().trim();
                checkCurrent(type(cur)[1], "(");

                //<expressionList>
                cur = in.nextLine().trim();
                checkCurrent(cur, "<expressionList>");

                cur = in.nextLine().trim();
                if(!cur.equals("</expressionList>"))
                    numArg = 1 + compileExpressionList();
                else
                    numArg = 1;

                writ.writeCall(classTable.typeOf(name)+"."+methodName, numArg);

                //symbol )
                cur = in.nextLine().trim();
                checkCurrent(type(cur)[1], ")");
            }
            //function
            else{
                //symbol (
                cur = in.nextLine().trim();
                // <expressionList>
                cur = in.nextLine().trim();
                checkCurrent(cur, "<expressionList>");

                cur = in.nextLine().trim();
                if(!cur.equals("</expressionList>"))
                    numArg = compileExpressionList();
                writ.writeCall(name+"."+methodName, numArg);
                //symbol )
                cur = in.nextLine().trim();
            }
        }
        cur = in.nextLine().trim();
    }

    void compileExpression() {
        //<expression>
        //<term>
        cur = in.nextLine().trim();
        checkCurrent(cur, "<term>");
        compileTerm();
        //System.out.println("expression "+cur);

        while (type(cur)[1].equals("+") || type(cur)[1].equals("-") || type(cur)[1].equals("*")
                || type(cur)[1].equals("/") || type(cur)[1].equals("=") || type(cur)[1].equals("&lt;")
                || type(cur)[1].equals("&gt;") || type(cur)[1].equals("&amp;") || type(cur)[1].equals("|")) {
            String var = type(cur)[1];
            cur = in.nextLine().trim();
            //System.out.println(cur);
            compileTerm();
            //System.out.println(cur+" oo");
            if (var.equals("+"))
                writ.writeArithmetic("add");
            else if (var.equals("-"))
                writ.writeArithmetic("sub");
            else if (var.equals("*"))
                writ.writeArithmetic("call Math.multiply 2");
            else if (var.equals("/"))
                writ.writeArithmetic("call Math.divide 2");
            else if (var.equals("="))
                writ.writeArithmetic("eq");
            else if (var.equals("&lt;"))
                writ.writeArithmetic("lt");
            else if (var.equals("&gt;"))
                writ.writeArithmetic("gt");
            else if (var.equals("&amp;"))
                writ.writeArithmetic("and");
            else if (var.equals("|"))
                writ.writeArithmetic("or");

            //cur = in.nextLine().trim();
        }

        // symbol ; or )
        cur = in.nextLine().trim();
    }
}

public class Compiler{
    public static void main(String[] args) throws Exception{
        Scanner in;
        PrintWriter writ;
/*

        There are some bugs in Analyzer
        File jackInput = new File(args[0]+".jack");
        File tokanizedOutput = new File(args[0]+"Token.xml");
        in = new Scanner(jackInput);
        writ = new PrintWriter(tokanizedOutput);
        Tokenizer toker = new Tokenizer(in, writ);
        writ.flush();
        writ.close();
        System.out.println("Tokenisation complete");

        File tokanizedInput = new File(args[0]+"Token.xml");
        File analyzedOutput = new File(args[0]+"Analyzed.xml");
        in = new Scanner(tokanizedInput);
        writ = new PrintWriter(analyzedOutput);
        Analyzer analyzed = new Analyzer(in, writ);
        analyzed.compiler();
        writ.flush();
        writ.close();
        System.out.println("Analysation complete");
*/
        File analyzedInput = new File(args[0]+"Analyzed.xml");
        File compiledOutput = new File(args[0]+"compiled.vm");
        in = new Scanner(analyzedInput);
        writ = new PrintWriter(compiledOutput);
        CompilationEngine compiler = new CompilationEngine(in, writ);
        compiler.CompileClass();
        writ.flush();
        writ.close();
        System.out.println("compilation complete");
    }
}