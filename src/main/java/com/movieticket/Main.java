package com.movieticket;

// ─────────────────────────────────────────────────────────────
//  Main.java
//  Entry point of the application.
//  Simply launches the Home Page window.
// ─────────────────────────────────────────────────────────────

public class Main {

    public static void main(String[] args) {
        // Launch the Home Page on the Swing event thread (best practice)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new HomePage(); // open the home screen
        });
    }
}
