package de.lightplugins.kimetsu.enums;

public enum OptionDataPath {

    /*
        Command option data names
     */

    COINS_SET_LOGIN_NAME("loginName"),
    COINS_SET_AMOUNT("amount"),
    ;

    private final String path;
    OptionDataPath(String path) { this.path = path; }
    public String getName() {
        return path;
    }
}
