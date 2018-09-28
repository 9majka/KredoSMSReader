package com.oshchukin.smsreader.model;

import java.util.regex.Pattern;

public class Scheme {

    private final static String KREDO_WITHDRAW_PATTERN = ".*(KUPIVLIA|INTERNET|KREDOBANK PEREKAZ|KREDOBANK ZNYATIA HOTIVKY).* (?<amount>\\d{1,7}.\\d{2})UAH.*ZALISHOK (?<remains>\\d{1,7}.\\d{2}) UAH.*";
    private final static String KREDO_CREDITED_PATTERN = ".*(ZARAKHUVANIA).* (?<amount>\\d{1,7}.\\d{2})UAH.*ZALISHOK (?<remains>\\d{1,7}.\\d{2}) UAH.*";

    //"14.09.2018 10:31 ZARAKHUVANIA 0000000.68UAH CARD**5472 ZALISHOK 0000000.90 UAH OVER 0000000.00 UAH DOSTUPNO 0000000.90UAH";

    private final static String KREDOBANK_FILTER = "KREDOBANK";
    private final static String CREDITED_FILTER = "ZARAKHUVANIA";
    private final static String REMAINS_FILTER = "ZALISHOK";

    private static Pattern mWithDrawPattern = Pattern.compile(KREDO_WITHDRAW_PATTERN);
    private static Pattern mCreditedPattern = Pattern.compile(KREDO_CREDITED_PATTERN);

    public Pattern getWithDrawnPattern() {
        return mWithDrawPattern;
    }

    public Pattern getCreditedPattern() {
        return mCreditedPattern;
    }

    public String getWithDrawnPatternString() {
        return KREDO_WITHDRAW_PATTERN;
    }

    public String getCreditedPatternString() {
        return KREDO_CREDITED_PATTERN;
    }

    public String getSMSFilter() {
        return KREDOBANK_FILTER;
    }
    public String getCreditedFilter() {
        return CREDITED_FILTER;
    }
    public String getRemainsFilter() {
        return REMAINS_FILTER;
    }

    public String getRemainsGroupTag() {
        return "amount";
    }

    public String getAmountGroupTag() {
        return "remains";
    }
}
