package edu.illinois.cs.cogcomp;

/**
 * Created by haowu4 on 1/28/17.
 */
public class ProcessFreebaseMaps {

    public static String unescapeFreebaseKey(String in) {
        StringBuilder out = new StringBuilder(in.length());
        String[] parts = in.split("[$]");
        out.append(parts[0]);
        for(int i=1; i<parts.length; i++) {
            String hexSymbols=parts[i].substring(0,4);
            String remainder="";
            if(parts[i].length()>4) {
                remainder=parts[i].substring(4);
            }
            int codePoint=Integer.parseInt(hexSymbols,16);
            char[] character=Character.toChars(codePoint);
            out.append(character);
            out.append(remainder);
        }
        return out.toString();
    }

    public static void main(String[] args) {

    }
}
