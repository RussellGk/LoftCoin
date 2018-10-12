package com.hardtm.loftcoin.data.model;

public enum Fiat {

    USD("$"),
    EUR("€"),
    RUB("\u20BD");

    public String symbol;

    Fiat(String symbol) {
        this.symbol = symbol;
    }
}
