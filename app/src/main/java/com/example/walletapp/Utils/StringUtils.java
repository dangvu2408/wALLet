package com.example.walletapp.Utils;


public class StringUtils {
    public static String convertNumberToTextVND(long total) {
        try {
            String rs = "";
            String[] ch = { "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín" };
            String[] rch = { "lẻ", "mốt", "", "", "", "lăm" };
            String[] u = { "", "mươi", "trăm", "ngàn", "", "", "triệu", "", "", "tỷ", "", "", "ngàn", "", "", "triệu" };
            String nstr = String.valueOf(total);
            long[] n = new long[nstr.length()];
            int len = n.length;
            for (int i = 0; i < len; i++) {
                n[len - 1 - i] = Long.valueOf(nstr.substring(i, i + 1));
            }
            for (int i = len - 1; i >= 0; i--) {
                if (i % 3 == 2) {
                    if (n[i] == 0 && n[i - 1] == 0 && n[i - 2] == 0) continue;
                } else if (i % 3 == 1) {
                    if (n[i] == 0) {
                        if (n[i - 1] == 0) { continue; }
                        else {
                            rs += " " + rch[(int) n[i]];
                            continue;
                        }
                    }
                    if (n[i] == 1) {
                        rs += " mười";
                        continue;
                    }
                } else if (i != len - 1) {
                    if (n[i] == 0) {
                        if (i + 2 <= len - 1 && n[i + 2] == 0 && n[i + 1] == 0) continue;
                        rs += " " + (i % 3 == 0 ? u[i] : u[i % 3]);
                        continue;
                    }
                    if (n[i] == 1) {
                        rs += " " + ((n[i + 1] == 1 || n[i + 1] == 0) ? ch[(int) n[i]] : rch[(int) n[i]]);
                        rs += " " + (i % 3 == 0 ? u[i] : u[i % 3]);
                        continue;
                    }
                    if (n[i] == 5) {
                        if (n[i + 1] != 0) {
                            rs += " " + rch[(int) n[i]];
                            rs += " " + (i % 3 == 0 ? u[i] : u[i % 3]);
                            continue;
                        }
                    }
                }
                rs += (" ") + ch[(int) n[i]];
                rs += " " + (i % 3 == 0 ? u[i] : u[i % 3]);
            }

            if (rs.length() > 2) {
                String rs1 = rs.substring(0, 2);
                rs = rs.substring(2);
                rs = rs1 + rs;
            }
            return rs.trim().replace("lẻ,", "lẻ").replace("mươi,", "mươi").replace("trăm,", "trăm").replace("mười,", "mười");
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public static String convertStringToNumberText(String str) {
        boolean isNegative = false;

        if (str.startsWith("-")) {
            isNegative = true;
            str = str.substring(1);
        }

        str = str.replace(".", "").replace(",", ".");

        String[] parts = str.split("\\.");
        long integerPart = Long.parseLong(parts[0]);
        String decimalPart = parts.length > 1 ? parts[1] : "";

        String result = convertNumberToTextVND(integerPart);

        if (!decimalPart.isEmpty()) {
            result += " phẩy " + convertDecimalToText(decimalPart);
        }

        if (isNegative) {
            result = "âm " + result;
        }

        return result;
    }

    public static String convertDecimalToText(String decimal) {
        String[] ch = { "không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín" };
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < decimal.length(); i++) {
            result.append(ch[Integer.parseInt(String.valueOf(decimal.charAt(i)))]);
            if (i != decimal.length() - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }
}
