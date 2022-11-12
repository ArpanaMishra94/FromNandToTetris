import java.io.*;
import java.util.*;

public class ArithmeticAssembler {
    static int line=-1;
    public static void main(String args[]) throws Exception {
        File red = new File(args[0]+".vm");
        System.out.println(args[0]);
        Scanner in = new Scanner(red);
        PrintWriter writ = new PrintWriter(args[0]+".asm");
        while (in.hasNext()) {
            String entry = in.nextLine();
            String toRet="";
            writ.println("//"+entry);
            if(entry.contains("//"))
                continue;
            else if(entry.contains("push"))
                writ.println(push(Integer.parseInt(entry.split(" ")[2])));
            else if(entry.contains("add"))
                writ.println(add());
            else if(entry.contains("sub"))
                writ.println(sub());
            else if(entry.contains("neg"))
                writ.println(neg());
            else if(entry.contains("eq"))
                writ.println(equl());
            else if(entry.contains("gt"))
                writ.println(greaterThan());
            else if(entry.contains("lt"))
                writ.println(lessThan());
            else if(entry.contains("and"))
                writ.println(and());
            else if(entry.contains("or"))
                writ.println(or());
            else if(entry.contains("not"))
                writ.println(not());
        }
        System.out.println(line);
        writ.flush();
        writ.close();
    }
    static String push(int n){
        line+=7;
        String toRet = "@";
        toRet+=Integer.toString(n);
        toRet+="\nD=A\n@SP\nA=M\nM=D\n@SP\nM=M+1\n";
        return toRet;
    }
    static String add(){
        line+=7;
        return "//->add\n@SP\nM=M-1\nA=M\nD=M\n@SP\nA=M-1\nM=M+D\n";
    }

    static String sub(){
        line+=8;
        return "//->sub\n@SP\nM=M-1\n@SP\nA=M\nD=M\n@SP\nA=M-1\nM=M-D\n";
    }
    static String neg(){
        line+=3;
        return "//->neg\n@SP\nA=M-1\nM=-M\n";
    }
    static String lessThan(){
        String toRet = "//->lessThan\n"+sub();
        toRet+="@SP\nA=M-1\nD=M\n";
        line+=5;
        toRet+="@"+Integer.toString(line+6)+"\nD;JLT"+falsePrint()+"@"+Integer.toString(line+6)+"\n0;JMP"+truePrint()+"\n";
        line+=2;
        return toRet;
    }
    static String greaterThan(){
        String toRet = "//->greaterThan\n"+sub();
        toRet+="@SP\nA=M-1\nD=M\n";
        line+=5;
        toRet+="@"+Integer.toString(line+6)+"\nD;JGT"+falsePrint()+"@"+Integer.toString(line+6)+"\n0;JMP"+truePrint()+"\n";
        line+=2;
        return toRet;
    }
    static String equl(){
        String toRet = "//->equl\n"+sub();
        toRet+="@SP\nA=M-1\nD=M\n";
        line+=5;
        toRet+="@"+Integer.toString(line+6)+"\nD;JEQ"+falsePrint()+"@"+Integer.toString(line+6)+"\n0;JMP"+truePrint()+"\n";
        line+=2;
        return toRet;
    }
    static String or(){
        line+=7;
        return "//->or\n@SP\nM=M-1\nA=M\nD=M\n@SP\nA=M-1\nM=D|M\n";
    }
    static String and(){
        line+=8;
        return "//->or\n@SP\nM=M-1\nA=M\nD=M\n@SP\nA=M-1\nM=D&M\n";
    }
    static String not(){
        line+=3;
        return "//->not\n@SP\nA=M-1\nM=!M\n";
    }
    static String truePrint(){
        line+=3;
        return "//->true\n@SP\nA=M-1\nM=-1\n";
    }
    static String falsePrint(){
        line+=3;
        return "//->true\n@SP\nA=M-1\nM=0\n";
    }
}
