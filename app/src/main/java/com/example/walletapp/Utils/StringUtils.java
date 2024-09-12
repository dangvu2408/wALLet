package com.example.walletapp.Utils;


public class StringUtils {
    private static final String[] units = { "", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín" };
    private static final String[] tens = { "", "mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi", "bảy mươi", "tám mươi", "chín mươi" };
    private static final String[] hundreds = { "", "một trăm", "hai trăm", "ba trăm", "bốn trăm", "năm trăm", "sáu trăm", "bảy trăm", "tám trăm", "chín trăm" };

    public static String numberToWords(String input) {
        if (input == null || input.isEmpty()) return "Không đồng";

        String[] parts = input.split(",");
        String wholePartString = parts[0].replace(".", "");
        long wholePart = Long.parseLong(wholePartString);
        int decimalPart = (parts.length > 1) ? Integer.parseInt(parts[1]) : 0;

        StringBuilder result = new StringBuilder();
        result.append(convertWholePartToWords(wholePart));

        if (decimalPart > 0) {
            result.append(" phẩy ").append(convertDecimalToWords(decimalPart));
        }

        return result.toString().trim() + " đồng";
    }

    private static String convertWholePartToWords(long number) {
        if (number == 0) return "Không";

        StringBuilder result = new StringBuilder();

        long billions = number / 1_000_000_000;
        number = number % 1_000_000_000;
        long millions = number / 1_000_000;
        number = number % 1_000_000;
        long thousands = number / 1_000;
        long hundreds = number % 1_000;

        if (billions > 0) {
            result.append(convertThreeDigits(billions)).append(" tỉ ");
        }

        if (millions > 0) {
            result.append(convertThreeDigits(millions)).append(" triệu ");
        }

        if (thousands > 0) {
            result.append(convertThreeDigits(thousands)).append(" nghìn ");
        }

        if (hundreds > 0) {
            result.append(convertThreeDigits(hundreds)).append(" ");
        }

        return result.toString().trim();
    }

    private static String convertDecimalToWords(int number) {
        return convertTwoDigits(number);
    }

    private static String convertThreeDigits(long number) {
        StringBuilder result = new StringBuilder();

        int hundred = (int) (number / 100);
        int ten = (int) ((number % 100) / 10);
        int unit = (int) (number % 10);

        if (hundred > 0) {
            result.append(hundreds[hundred]).append(" ");
        }

        if (ten > 0 || unit > 0) {
            result.append(convertTwoDigits(ten * 10 + unit));
        }

        return result.toString().trim();
    }

    private static String convertTwoDigits(int number) {
        StringBuilder result = new StringBuilder();

        if (number >= 10) {
            int ten = number / 10;
            int unit = number % 10;

            result.append(tens[ten]).append(" ");
            if (unit > 0) {
                result.append(units[unit]);
            }
        } else {
            result.append(units[number]);
        }

        return result.toString().trim();
    }


}
