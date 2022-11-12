import java.io.*;
import java.util.*;

public class Analyzer {
    static HashSet<String> keyWord;
    static HashSet<String> symbol;
    public static void main(String args[]) throws Exception {
        String[] keyWordArr = {"class", "constructor", "function", "method", "field", "static", "var", "int", "char", "boolean", "void", "true", "false", "null", "this",
                "let", "do", "if", "else", "while", "return"};
        String[] symbolArr = {"{", "}", "(", ")", "[", "]", ".", ",", ";", "+", "-", "*", "/", "&", "|", "<", ">", "=", "~"};
        keyWord = new HashSet<String>(Arrays.asList(keyWordArr));
        symbol = new HashSet<String>(Arrays.asList(symbolArr));
        File f = new File(args[0]+".jack");
        Scanner in = new Scanner(f);
        File toke = new File(args[0] + "Token.xml");
        PrintWriter writ = new PrintWriter(toke);
        token(in, writ);
    }

    static void token(Scanner in, PrintWriter writ) {

        writ.println("<tokens>");
        String cur;
        String[] typeval;
        while (in.hasNext()) {
            cur = in.nextLine();
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
    static String[] Finder(String cur){
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

    static String[] integerConstant(String cur) {
        String inte = "";
        int len = cur.length(), i = 0;
        for (; i < len && Character.compare(cur.charAt(i), '0') >= 0 && Character.compare(cur.charAt(i), '9') <= 0; i++)
            inte += cur.charAt(i);
        cur = cur.substring(i).trim();
        String[] toRet = {"integerConstant", inte, cur};
        return toRet;
    }

    static String[] stringConstant(String cur){
        String temp = "";
        int i=1;
        for(;i<cur.length() && cur.charAt(i)!='"'; i++)
            temp += cur.charAt(i);
        System.out.println("aa"+temp);
        cur=cur.substring(++i).trim();
        String[] toRet = {"stringConstant", temp, cur};
        return toRet;
    }

    static boolean isKeyword(String cur){
        Iterator it = keyWord.iterator();
        while (it.hasNext())
            if(cur.startsWith((String)it.next()))
                return true;
        return false;
    }

    static boolean isSymbol(String c){
        if(symbol.contains(c))
            return true;
        else
            return false;
    }

    static String[] identifier(String cur){
        String temp = "";
        int i =0;
        for(;i<cur.length() && !isSymbol(cur.substring(i,i+1)) && cur.charAt(i)!=' ';i++)
            temp+=cur.charAt(i);
        cur= cur.substring(i).trim();
        String[] toRet = {"identifier", temp, cur};
        return toRet;
    }
}