package com.example.zadanie25_1;

public enum TaskCategory {
    HOME("dom"),
    WORK("praca"),
    SPORT("sport"),
    FAMILY("rodzina"),
    ADDITIONAL("dodatkowe");

    private final String polishDescription;

    TaskCategory(String polishDescription) {
        this.polishDescription = polishDescription;
    }

    public String getPolishDescription() {
        return polishDescription;
    }
}
