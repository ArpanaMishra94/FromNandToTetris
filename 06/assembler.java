import java.io.*;
import java.util.*;

public class assembler {
    public static void main(String args[]) throws Exception {
        File assm = new File(args[0] + ".asm");
        System.out.println(args[0]);
        Scanner in = new Scanner(assm);
        String s, out = "";
        HashMap<String, String> destination = new HashMap<String, String>();
        HashMap<String, String> comp = new HashMap<String, String>();
        HashMap<String, String> jump = new HashMap<String, String>();
        HashMap<String, String> symbols = new HashMap<String, String>();
        int pointer=16,counter=0;

        symbols.put("R0","0");
        symbols.put("R1","1");
        symbols.put("R2","2");
        symbols.put("R3","3");
        symbols.put("R4","4");
        symbols.put("R5","5");
        symbols.put("R6","6");
        symbols.put("R7","7");
        symbols.put("R8","8");
        symbols.put("R9","9");
        symbols.put("R10","10");
        symbols.put("R11","11");
        symbols.put("R12","12");
        symbols.put("R13","13");
        symbols.put("R14","14");
        symbols.put("R15","15");
        symbols.put("SCREEN","16384");
        symbols.put("KBD","24576");
        symbols.put("SP","0");
        symbols.put("LCL","1");
        symbols.put("ARG","2");
        symbols.put("THIS","3");
        symbols.put("THAT","4");

        jump.put("null", "000");
        jump.put("JGT", "001");
        jump.put("JEQ", "010");
        jump.put("JGE", "011");
        jump.put("JLT", "100");
        jump.put("JNE", "101");
        jump.put("JLE", "110");
        jump.put("JMP", "111");

        destination.put("null", "000");
        destination.put("M", "001");
        destination.put("D", "010");
        destination.put("MD", "011");
        destination.put("A", "100");
        destination.put("AM", "101");
        destination.put("AD", "110");
        destination.put("AMD", "111");

        comp.put("0", "101010");
        comp.put("1", "111111");
        comp.put("-1", "111010");
        comp.put("D", "001100");
        comp.put("A", "110000");
        comp.put("M", "110000");
        comp.put("!D", "001101");
        comp.put("!A", "110001");
        comp.put("!M", "110001");
        comp.put("-D", "001111");
        comp.put("-A", "110011");
        comp.put("-M", "110011");
        comp.put("D+1", "011111");
        comp.put("A+1", "110111");
        comp.put("M+1", "110111");
        comp.put("D-1", "001110");
        comp.put("A-1", "110010");
        comp.put("M-1", "110010");
        comp.put("D+A", "000010");
        comp.put("D+M", "000010");
        comp.put("D-A", "010011");
        comp.put("D-M", "010011");
        comp.put("A-D", "000111");
        comp.put("M-D", "000111");
        comp.put("D&A", "000000");
        comp.put("D&M", "000000");
        comp.put("D|A", "010101");
        comp.put("D|M", "010101");

        PrintWriter writ = new PrintWriter(args[0] + ".hack");

        while (in.hasNext()) {
            s = in.nextLine();
            s = s.replace(" ", "");
            if (s.contains("//")) {
                if (s.indexOf("/") == 0)
                    continue;
                else
                    s = s.split("//")[0];
            }
            if (!s.isEmpty()) {
                if (s.contains("@"))
                    counter++;

                if (s.contains("(")) {
                    s = s.replace("(", "");
                    s = s.replace(")", "");
                    if (!symbols.containsKey(s))
                        symbols.put(s, String.valueOf(counter));
                }
                if (s.contains("="))
                    counter++;
                if (s.contains(";"))
                    counter++;
            }
        }
        in.close();
        in = new Scanner(assm);
        int le;
        while (in.hasNext()) {
            s = in.nextLine();
            s = s.replace(" ", "");
            if (s.contains("//")) {
                if (s.indexOf("/") == 0)
                    continue;
                else
                    s = s.split("//")[0];
            }
            //System.out.println(s);

            if (!s.isEmpty()) {
                out="";
                if(s.contains("("))
                    continue;
                //System.out.println(s);
                if (s.contains("@")) {
                    out = "0";
                    //System.out.println(symbols.containsKey(s.split("@")[1]));
                    if (!isInteger(s.split("@")[1])) {
                        if(!symbols.containsKey(s.split("@")[1]))
                            symbols.put(s.split("@")[1], String.valueOf(pointer++));
                        s = s.replace(s.split("@")[1], symbols.get(s.split("@")[1]));
                    }
                    //System.out.println(s);
                    s = Integer.toBinaryString(Integer.parseInt(s.split("@")[1]));
                    le = 15 - s.length();
                    while (le-- > 0)
                        out = out + "0";
                    out = out + s;
                }
                if (s.contains("=")) {
                    out = "111";
                    if (s.split("=")[1].contains("M"))
                        out = out + "1";
                    else
                        out = out + "0";
                    out += comp.get(s.split("=")[1]);
                    out += destination.get(s.split("=")[0]);
                    out += "000";
                }
                if (s.contains(";")) {
                    out = "111";
                    if (s.split(";")[0].contains("M"))
                        out = out + "1";
                    else
                        out = out + "0";
                    out += comp.get(s.split(";")[0]);
                    out += "000";
                    out += jump.get(s.split(";")[1]);
                }
                //System.out.println(out);
                writ.write(out+"\n");
            }
        }
        writ.flush();
        writ.close();
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}