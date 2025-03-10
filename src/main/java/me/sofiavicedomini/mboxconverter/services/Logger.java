package me.sofiavicedomini.mboxconverter.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Logger {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Update {
        private List<String> history;
        private String message;
    }

    private List<String> logHistory = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private List<Consumer<Update>> listeners = new ArrayList<>();

    public enum Level {
        INFO,
        DEBUG,
        WARNING,
        ERROR
    }

    // Metodo specifico per il livello INFO
    public void info(String message) {
        log(Level.INFO, message);
    }

    // Metodo specifico per il livello DEBUG
    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    // Metodo specifico per il livello WARNING
    public void warning(String message) {
        log(Level.WARNING, message);
    }

    // Metodo specifico per il livello ERROR
    public void error(String message) {
        log(Level.ERROR, message);
    }


    public void log(Level level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logMessage = String.format("[%s] [%s] - %s", timestamp, level, message);
        logHistory.add(logMessage);

        if (level == Level.ERROR) {
            System.err.println(logMessage); // Opzionale: stampare il messaggio nel console
        } else {
            System.out.println(logMessage); // Opzionale: stampare il messaggio nel console
        }

        var update = Update.builder().history(new ArrayList<>(logHistory)).message(logMessage).build();
        listeners.forEach(l -> l.accept(update));
    }

    public List<String> getLogHistory() {
        return new ArrayList<>(logHistory);
    }


    public void addLogListener(Consumer<Update> listener) {
        this.listeners.add(listener);
    }
}
