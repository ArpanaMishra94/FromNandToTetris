import java.io.*;
import java.util.*;

public class VMTranslator {
    static int line = -1;
    static String filename;
    static int returnCount=0;
    public static void main(String args[]) throws Exception {
        File red = new File(args[0]);
        String[] files;
        PrintWriter writ;
        if(red.isDirectory()) {
            files = red.list();
            writ = new PrintWriter(args[0] + ".asm");
        }
        else {
            files = new String[1];
            files[0]=args[0];
            System.out.println(files[0]);
            writ = new PrintWriter(args[0] + ".asm");
        }
        writ.println("@256\nD=A\n@SP\nM=D");
        writ.println(callFun("Sys.init",0));
        for(String f : files) {
            filename = f;
            if(!f.contains(".vm"))
                continue;
            System.out.println(f);
            Scanner in = new Scanner(new File(f));

            while (in.hasNext()) {
                String entry = in.nextLine();
                String toRet = "";
                writ.println("//" + entry);
                if (entry.contains("//"))
                    entry = entry.split("//")[0];
                if (entry == null || entry.length() == 0)
                    continue;
                System.out.println(entry.split(" ").length == 3 ? Integer.parseInt(entry.split(" ")[2].trim()) : entry);
                if (entry.contains("push")) {
                    //System.out.println(entry.split(" ")[1]);
                    writ.println(push(entry.split(" ")[1], Integer.parseInt(entry.split(" ")[2])));
                } else if (entry.contains("pop")) {
                    //System.out.println(entry.split(" ")[1]);
                    writ.println(pop(entry.split(" ")[1], Integer.parseInt(entry.split(" ")[2].trim())));
                } else if (entry.contains("add"))
                    writ.println(add());
                else if (entry.contains("sub"))
                    writ.println(sub());
                else if (entry.contains("neg"))
                    writ.println(neg());
                else if (entry.contains("eq"))
                    writ.println(equl());
                else if (entry.contains("gt"))
                    writ.println(greaterThan());
                else if (entry.contains("lt"))
                    writ.println(lessThan());
                else if (entry.contains("and"))
                    writ.println(and());
                else if (entry.contains("or"))
                    writ.println(or());
                else if (entry.contains("not"))
                    writ.println(not());
                else if (entry.contains("label")) {
                    System.out.println(entry.split(" ")[1] + " LABEL");
                    writ.println(programFlow("label", entry.split(" ")[1]));
                } else if (entry.contains("if-goto")) {
                    System.out.println(entry.split(" ")[1] + " ifgoto");
                    writ.println(programFlow("if", entry.split(" ")[1]));
                } else if (entry.contains("goto"))
                    writ.println(programFlow("goto", entry.split(" ")[1]));
                else if (entry.contains("function"))
                    writ.println(functionCalleName(entry.split(" ")[1].trim(), Integer.parseInt(entry.split(" ")[2].trim())));
                else if (entry.contains("return"))
                    writ.println(functionReturn());
                else if(entry.contains("call"))
                    writ.println(callFun(entry.split(" ")[1].trim(), Integer.parseInt(entry.split(" ")[2].trim())));
                else
                    System.out.println("No command found "+entry);

            }
        }
        System.out.println(line);
        writ.flush();
        writ.close();
    }
    static String callFun(String name, int nArgs){
        line+=50;
        String returnname = "RETURN_"+String.valueOf(returnCount++);
        String toRet = "@"+returnname+"\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
        toRet+= "@LCL\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n@ARG\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n@THIS\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n@THAT\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
        toRet+="@SP\nD=M\n@"+String.valueOf(nArgs)+"\nD=D-A\n@5\nD=D-A\n@ARG\nM=D\n@SP\nD=M\n@LCL\nM=D\ngoto "+name+"\n0;JMP\n("+returnname+")\n";
        return toRet;
    }

    static String programFlow(String todo, String argu) {
        if (todo.equals("label")) {
            line+=1;
            return "(" + argu + ")\n";
        }
        else if(todo.equals("goto")) {
            line+=2;
            return "@"+ argu+"\n0;JMP\n";
        }
        else{
            line+=6;
            return "@SP\nM=M-1\nA=M\nD=M\n@"+argu+"\nD;JNE\n";
        }
    }

    static String functionCalleName(String name, int n){
        String toRet = "("+name+")\n";
        line++;
        for(int i =0;i<n;i++)
            toRet+=push("constant", 0);
        return toRet;
    }

    static String functionReturn() {
        line+=50;
        String toRet = "@LCL\nD=M\n@FRAME\nM=D\n@5\nA=D-A\nD=M\n@RET\nM=D\n@SP\nA=M-1\nD=M\n@ARG\nA=M\nM=D\n@ARG\nD=M\n@SP\nM=D+1\n";
        toRet += "@1\nD=A\n@FRAME\nA=M-D\nD=M\n@THAT\nM=D\n@2\nD=A\n@FRAME\nA=M-D\nD=M\n@THIS\nM=D\n@3\nD=A\n@FRAME\nA=M-D\nD=M\n@ARG\nM=D\n@4\nD=A\n@FRAME\nA=M-D\nD=M\n@LCL\nM=D\n@RET\nA=M\n0;JMP\n";
        return toRet;
    }

    static String push(String flag, int n){
        //System.out.println(flag);
        if(flag.equals("constant")) {
            line += 7;
            String toRet = "@";
            toRet += Integer.toString(n);
            toRet += "\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
            return toRet;
        }
        else if(flag.equals("local")){
            line+=11;
            return "@LCL\nD=M\n@"+ Integer.toString(n)+"\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
        }
        else if(flag.equals("argument")){
            line+=11;
            return "@ARG\nD=M\n@"+ Integer.toString(n)+"\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
        }
        else if(flag.equals("this")){
            line+=11;
            return "@THIS\nD=M\n@"+ Integer.toString(n)+"\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
        }
        else if(flag.equals("that")){
            line+=11;
            return "@THAT\nD=M\n@"+ Integer.toString(n)+"\nD=D+A\nA=D\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
        }
        else if(flag.equals("pointer")){
            line+=6;
            return "@"+Integer.toString(n+3)+"\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
        }
        else if(flag.equals("static")){
            line+=7;
            return "@"+filename+"."+Integer.toString(n)+"\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
        }
        else{
            line+=7;
            return "@"+Integer.toString(5+n)+"\nD=M\n@SP\nA=M\nM=D\n@SP\nM=M+1";
        }
    }

    static String pop(String flag, int n) {
        //System.out.println(flag);
        if (flag.equals("local")) {
            line += 15;
            return "@LCL\nD=M\n@R13\nM=D\n@" + Integer.toString(n) + "\nD=A\n@R13\nM=D+M\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D\n";
        } else if (flag.equals("argument")) {
            line += 15;
            return "@ARG\nD=M\n@R13\nM=D\n@" + Integer.toString(n) + "\nD=A\n@R13\nM=D+M\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D\n";
        } else if (flag.equals("this")) {
            line += 15;
            return "@THIS\nD=M\n@R13\nM=D\n@" + Integer.toString(n) + "\nD=A\n@R13\nM=D+M\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D\n";
        } else if (flag.equals("that")) {
            line += 15;
            return "@THAT\nD=M\n@R13\nM=D\n@" + Integer.toString(n) + "\nD=A\n@R13\nM=D+M\n@SP\nM=M-1\nA=M\nD=M\n@R13\nA=M\nM=D\n";
        } else if (flag.equals("static")) {
            line += 6;
            return "@SP\nM=M-1\nA=M\nD=M\n" + "@" + filename + "." + Integer.toString(n) + "\nM=D\n";
        } else if (flag.equals("pointer")) {
            line += 6;
            return "@SP\nM=M-1\nA=M\nD=M\n" + "@" + Integer.toString(3 + n) + "\nM=D\n";
        } else {
            line += 6;
            return "@SP\nM=M-1\nA=M\nD=M\n" + "@" + Integer.toString(5 + n) + "\nM=D\n";
        }
    }

    static String add() {
        line += 7;
        return "//->add\n@SP\nM=M-1\nA=M\nD=M\n@SP\nA=M-1\nM=M+D\n";
    }

    static String sub() {
        line += 8;
        return "//->sub\n@SP\nM=M-1\n@SP\nA=M\nD=M\n@SP\nA=M-1\nM=M-D\n";
    }

    static String neg() {
        line += 3;
        return "//->neg\n@SP\nA=M-1\nM=-M\n";
    }

    static String lessThan() {
        String toRet = "//->lessThan\n" + sub();
        toRet += "@SP\nA=M-1\nD=M\n";
        line += 5;
        toRet += "@" + Integer.toString(line + 6) + "\nD;JLT" + falsePrint() + "@" + Integer.toString(line + 6) + "\n0;JMP" + truePrint() + "\n";
        line += 2;
        return toRet;
    }

    static String greaterThan() {
        String toRet = "//->greaterThan\n" + sub();
        toRet += "@SP\nA=M-1\nD=M\n";
        line += 5;
        toRet += "@" + Integer.toString(line + 6) + "\nD;JGT" + falsePrint() + "@" + Integer.toString(line + 6) + "\n0;JMP" + truePrint() + "\n";
        line += 2;
        return toRet;
    }

    static String equl() {
        String toRet = "//->equl\n" + sub();
        toRet += "@SP\nA=M-1\nD=M\n";
        line += 5;
        toRet += "@" + Integer.toString(line + 6) + "\nD;JEQ" + falsePrint() + "@" + Integer.toString(line + 6) + "\n0;JMP" + truePrint() + "\n";
        line += 2;
        return toRet;
    }

    static String or() {
        line += 7;
        return "//->or\n@SP\nM=M-1\nA=M\nD=M\n@SP\nA=M-1\nM=D|M\n";
    }

    static String and() {
        line += 8;
        return "//->or\n@SP\nM=M-1\nA=M\nD=M\n@SP\nA=M-1\nM=D&M\n";
    }

    static String not() {
        line += 3;
        return "//->not\n@SP\nA=M-1\nM=!M\n";
    }

    static String truePrint() {
        line += 3;
        return "//->true\n@SP\nA=M-1\nM=-1\n";
    }

    static String falsePrint() {
        line += 3;
        return "//->true\n@SP\nA=M-1\nM=0\n";
    }
}
