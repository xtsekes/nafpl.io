package dev.nafplio.web.model;

public enum ChatResponseType {
    INIT("init"),
    DATA("data"),
    ERROR("error"),
    END("end");

    private final String text;

    ChatResponseType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
