package com.oshchukin.smsreader.model;

import java.util.regex.Pattern;

public interface Scheme {

    Pattern getWithDrawnPattern();
    Pattern getCreditedPattern();
    String getWithDrawnPatternString();
    String getCreditedPatternString();
    String getSMSFilter();
    String getCreditedFilter();
    String getRemainsFilter();
    String getRemainsGroupTag();
    String getAmountGroupTag();
}
