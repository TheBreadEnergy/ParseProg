package com.company;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        ArrayList<String> list = null;
        ArrayList<String> setting = null;
        int a = 0;
        int j = 0;
        int sms = 0;
        String str;
        String ch;

        //считывания настроек
        try {
            File file = new File("1_run.bat");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            setting = new ArrayList<String>();

            while (line != null) {
                line = line.trim();
                setting.add(line);
                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //считывание из файла
        try {
            str = (setting.get(0));
            str = str.replaceAll(".+>+", "");
            str.trim();
            File file = new File(str);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            list = new ArrayList<String>();
            while (line != null) {
                line = line.trim();
                list.add(line);
                line = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //запись результата
        try {
            str = (setting.get(1));
            str = str.replaceAll(".+:", "");
            str.trim();
            FileOutputStream result = new FileOutputStream((str));
        result.write("teleServiceCode;serviceCentreAddress;calledPartyNumber;originatingAddress;originatingAddressName;\n".getBytes());
        while (j<list.size()) {
            str = list.get(j);
            char[] dst = null;
            if (str.contains("teleServiceCode                                             21'H")) {
                a++;
                dst = new char[str.length() - 60];
                str.getChars(60, str.length(), dst, 0);
                str = String.valueOf(dst);
                str = str.replaceAll("'.+", "");
                result.write((str+";").getBytes());
                for (int h = 0; h<7; h++) {
                    str = list.get(j + h);
                    if (str.contains("calledPartyNumber")) {
                        dst = new char[str.length() - 60];
                        str.getChars(60, str.length(), dst, 0);
                        str = String.valueOf(dst);
                        str = str.replaceAll("'.+", "");
                        result.write((str+";").getBytes());
                    }else if (str.contains("serviceCentreAddress")) {
                        dst = new char[str.length() - 60];
                        str.getChars(60, str.length(), dst, 0);
                        str = String.valueOf(dst);
                        str = str.replaceAll("'.+", "");
                        result.write((str+";").getBytes());
                    }
                }
                for (int h = 0; h<18; h++) {
                    str = list.get(j - h);
                    if (str.contains("calledPartyNumber")) {
                        dst = new char[str.length() - 60];
                        str.getChars(60, str.length(), dst, 0);
                        str = String.valueOf(dst);
                        str = str.replaceAll("'.+", "");
                        result.write((str+";").getBytes());
                    }
                    else if (str.contains("serviceCentreAddress")) {
                        dst = new char[str.length() - 60];
                        str.getChars(60, str.length(), dst, 0);
                        str = String.valueOf(dst);
                        str = str.replaceAll("'.+", "");
                        result.write((str+";").getBytes());
                    }
                }
                for (int i = 0; i<8; i++) {
                    str = list.get(j + i);
                    if (str.contains("originatingAddress")) {
                        dst = new char[str.length() - 60];
                        str.getChars(60, str.length(), dst, 0);
                        ch = String.valueOf(dst);
                        ch = ch.replaceAll("'.+", "");
                        result.write((ch+";").getBytes());
                        if (ch.charAt(0) != '1') {
                            sms++;
                            ch = deleteCharacters(ch, 0, 2);
                            for (int swapNum = 0; swapNum<ch.length(); swapNum++) {
                                ch = swapChars(ch, swapNum, ++swapNum);
                            }
                            ch = hexToBinary(ch);
                            List<String> reck = splitEqually(ch,8);
                            StringBuilder buff = new StringBuilder();
                            for (int g = 0; g<reck.size(); g++){
                                str = reck.get(g);
                                str = new StringBuffer(str).reverse().toString();
                                buff.append(str);
                            }
                            ch = buff.toString();
                            List<String> ret = splitEqually(ch,7);
                            StringBuilder buffer = new StringBuilder();
                            for (int g = 0; g<ret.size(); g++){
                                str = ret.get(g);
                                str = new StringBuffer(str).reverse().toString();
                                buffer.append(translate(str));
                            }
                            result.write((buffer.toString() + ";").getBytes());

                        }
                        else{
                            dst = new char[str.length() - 60];
                            str.getChars(60, str.length(), dst, 0);
                            str = String.valueOf(dst);
                            str = str.replaceAll("'.+", "");
                            result.write((str+";").getBytes());
                        }
                        result.write("\n".getBytes());
                    }
                }
            }
            j++;
        }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //запись статистики
        try {
            FileOutputStream statistics = new FileOutputStream("statistics.txt");
        statistics.write(("teleServiceCode = 21'H : " + a +"\n").getBytes());
        statistics.write(("Messages from them : " + sms +"\n").getBytes());
        statistics.write(("Total lines : " + list.size()).getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String translate(String str){
        switch (str){
            case "0000000":
                str = "@";
                break;
            case "0000001":
                str = "£";
                break;
            case "0000010":
                str = "$";
                break;
            case "0000011":
                str = "¥";
                break;
            case "0000100":
                str = "ê";
                break;
            case "0000101":
                str = "é";
                break;
            case "0000110":
                str = "ú";
                break;
            case "0000111":
                str = "í";
                break;
            case "0001000":
                str = "ó";
                break;
            case "0001001":
                str = "ç";
                break;
            case "0001010":
                str = "LF";
                break;
            case "0001011":
                str = "Ô";
                break;
            case "0001100":
                str = "ô";
                break;
            case "0001101":
                str = "CR";
                break;
            case "0001110":
                str = "Á";
                break;
            case "0001111":
                str = "á";
                break;
            case "0010000":
                str = "Δ";
                break;
            case "0010001":
                str = "_";
                break;
            case "0010010":
                str = "a";
                break;
            case "0010011":
                str = "Ç";
                break;
            case "0010100":
                str = "À";
                break;
            case "0010101":
                str = "∞";
                break;
            case "0010110":
                str = "^";
                break;
            case "0010111":
                str = "\\";
                break;
            case "0011000":
                str = "€";
                break;
            case "0011001":
                str = "Ó";
                break;
            case "0011010":
                str = "|";
                break;
            case "0011011":
                str = "1)";
                break;
            case "0011100":
                str = "Â";
                break;
            case "0011101":
                str = "â";
                break;
            case "0011110":
                str = "Ê";
                break;
            case "0011111":
                str = "É";
                break;
            case "0100000":
                str = "SP";
                break;
            case "0100001":
                str = "!";
                break;
            case "0100010":
                str = "\"";
                break;
            case "0100011":
                str = "#";
                break;
            case "0100100":
                str = "o";
                break;
            case "0100101":
                str = "%";
                break;
            case "0100110":
                str = "&";
                break;
            case "0100111":
                str = "'";
                break;
            case "0101000":
                str = "(";
                break;
            case "0101001":
                str = ")";
                break;
            case "0101010":
                str = "*";
                break;
            case "0101011":
                str = "+";
                break;
            case "0101100":
                str = ",";
                break;
            case "0101101":
                str = "-";
                break;
            case "0101110":
                str = ".";
                break;
            case "0101111":
                str = "/";
                break;
            case "0110000":
                str = "0";
                break;
            case "0110001":
                str = "1";
                break;
            case "0110010":
                str = "2";
                break;
            case "0110011":
                str = "3";
                break;
            case "0110100":
                str = "4";
                break;
            case "0110101":
                str = "5";
                break;
            case "0110110":
                str = "6";
                break;
            case "0110111":
                str = "7";
                break;
            case "0111000":
                str = "8";
                break;
            case "0111001":
                str = "9";
                break;
            case "0111010":
                str = ":";
                break;
            case "0111011":
                str = ";";
                break;
            case "0111100":
                str = "<";
                break;
            case "0111101":
                str = "=";
                break;
            case "0111110":
                str = ">";
                break;
            case "0111111":
                str = "?";
                break;
            case "1000000":
                str = "Í";
                break;
            case "1000001":
                str = "A";
                break;
            case "1000010":
                str = "B";
                break;
            case "1000011":
                str = "C";
                break;
            case "1000100":
                str = "D";
                break;
            case "1000101":
                str = "E";
                break;
            case "1000110":
                str = "F";
                break;
            case "1000111":
                str = "G";
                break;
            case "1001000":
                str = "H";
                break;
            case "1001001":
                str = "I";
                break;
            case "1001010":
                str = "J";
                break;
            case "1001011":
                str = "K";
                break;
            case "1001100":
                str = "L";
                break;
            case "1001101":
                str = "M";
                break;
            case "1001110":
                str = "N";
                break;
            case "1001111":
                str = "O";
                break;
            case "1010000":
                str = "P";
                break;
            case "1010001":
                str = "Q";
                break;
            case "1010010":
                str = "R";
                break;
            case "1010011":
                str = "S";
                break;
            case "1010100":
                str = "";
                break;
            case "1010101":
                str = "T";
                break;
            case "1010110":
                str = "U";
                break;
            case "1010111":
                str = "V";
                break;
            case "1011000":
                str = "W";
                break;
            case "1011001":
                str = "X";
                break;
            case "1011010":
                str = "Y";
                break;
            case "1011011":
                str = "Z";
                break;
            case "1011100":
                str = "Ã";
                break;
            case "1011101":
                str = "Õ";
                break;
            case "1011110":
                str = "Ú";
                break;
            case "1011111":
                str = "§";
                break;
            case "1100000":
                str = "~";
                break;
            case "1100001":
                str = "a";
                break;
            case "1100010":
                str = "b";
                break;
            case "1100011":
                str = "c";
                break;
            case "1100100":
                str = "d";
                break;
            case "1100101":
                str = "e";
                break;
            case "1100110":
                str = "f";
                break;
            case "1100111":
                str = "g";
                break;
            case "1101000":
                str = "h";
                break;
            case "1101001":
                str = "i";
                break;
            case "1101010":
                str = "j";
                break;
            case "1101011":
                str = "k";
                break;
            case "1101100":
                str = "l";
                break;
            case "1101101":
                str = "m";
                break;
            case "1101110":
                str = "n";
                break;
            case "1101111":
                str = "o";
                break;
            case "1110000":
                str = "p";
                break;
            case "1110001":
                str = "q";
                break;
            case "1110010":
                str = "r";
                break;
            case "1110011":
                str = "s";
                break;
            case "1110100":
                str = "t";
                break;
            case "1110101":
                str = "u";
                break;
            case "1110110":
                str = "v";
                break;
            case "1110111":
                str = "w";
                break;
            case "1111000":
                str = "x";
                break;
            case "1111001":
                str = "y";
                break;
            case "1111010":
                str = "z";
                break;
            case "1111011":
                str = "ã";
                break;
            case "1111100":
                str = "õ";
                break;
            case "1111101":
                str = "`";
                break;
            case "1111110":
                str = "ü";
                break;
            case "1111111":
                str = "à";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    private static String deleteCharacters(String str, int from, int to) {
        return str.substring(0,from)+str.substring(to);
    }

    private static String swapChars(String str, int lIdx, int rIdx) {
        StringBuilder sb = new StringBuilder(str);
        char l = sb.charAt(lIdx), r = sb.charAt(rIdx);
        sb.setCharAt(lIdx, r);
        sb.setCharAt(rIdx, l);
        return sb.toString();
    }

    private static String hexToBinary(String str) {
        Number number = new Number(NumerationSystemType._16, str);
        Number result = convertNumberToOtherNumerationSystem(number, NumerationSystemType._2);
        str = String.valueOf(result);
        return str;


    }

    private static Number convertNumberToOtherNumerationSystem(Number number, NumerationSystem expectedNumerationSystem) {
        Number result = null;
        int system = number.getNumerationSystem().getNumerationSystemIntValue();

        try {
            String strNum = "" + new BigInteger(number.getDigit(), system);
            BigInteger a= new BigInteger(strNum);
            String s = a.toString(expectedNumerationSystem.getNumerationSystemIntValue());
            result = new Number(expectedNumerationSystem, s);

        } catch (Exception e){
            throw new NumberFormatException();
        }
        return result;
    }

    private interface NumerationSystem {
        int getNumerationSystemIntValue();
    }

    private enum NumerationSystemType implements NumerationSystem {
        _16,
        _12,
        _10,
        _9,
        _8,
        _7,
        _6,
        _5,
        _4,
        _3,
        _2;

        @Override
        public int getNumerationSystemIntValue() {
            return Integer.parseInt(this.name().substring(1));
        }
    }

    private static class Number {
        private NumerationSystem numerationSystem;
        private String digit;

        public Number(NumerationSystem numerationSystem, String digit) {
            this.numerationSystem = numerationSystem;
            this.digit = digit;
        }

        public NumerationSystem getNumerationSystem() {
            return numerationSystem;
        }

        public String getDigit() {
            return digit;
        }

        @Override
        public String toString() {
            return digit;
        }
    }

    public static List<String> splitEqually(String text, int size) {
        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }
}
