package com.movieticket;

// ─────────────────────────────────────────────────────────────
//  HomePage.java
//  The first screen the user sees.
//  Shows 3 movies, each with a poster placeholder and a
//  "Book Ticket" button that opens the Seat Selection screen.
// ─────────────────────────────────────────────────────────────

import javax.swing.*;
import java.awt.*;

public class HomePage extends JFrame {

    // ── Constructor – builds the entire Home Page ──
    public HomePage() {
        setTitle("🎬 Movie Ticket Booking System");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen
        setResizable(false);

        // ── Main panel with a light background ──
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 30, 47));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Title label at the top ──
        JLabel titleLabel = new JLabel("🎬  Now Showing", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ── Panel that holds the 3 movie cards side by side ──
        JPanel moviesPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        moviesPanel.setBackground(new Color(30, 30, 47));

        // The 3 movies – just change these names to whatever you like
        String[] movies = {
            "Avengers: Endgame",
            "Interstellar",
            "The Dark Knight"
        };

        // Poster colours – used as a simple coloured rectangle placeholder
        Color[] posterColors = {
            new Color(180, 50, 50),
            new Color(50, 100, 180),
            new Color(60, 60, 60)
        };

        // Build one card for each movie
        for (int i = 0; i < movies.length; i++) {
            moviesPanel.add(createMovieCard(movies[i], posterColors[i]));
        }

        mainPanel.add(moviesPanel, BorderLayout.CENTER);

        // ── Footer ──
        JLabel footer = new JLabel("Select a movie to book your seats", SwingConstants.CENTER);
        footer.setForeground(new Color(180, 180, 180));
        footer.setFont(new Font("Arial", Font.ITALIC, 13));
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    // ─────────────────────────────────────────────────────────
    //  createMovieCard(movieName, posterColor)
    //  Builds a single movie card with:
    //    • a coloured poster rectangle (placeholder image)
    //    • the movie name
    //    • a "Book Ticket" button
    // ─────────────────────────────────────────────────────────
    private JPanel createMovieCard(String movieName, Color posterColor) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(45, 45, 68));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 110), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        // ── Poster placeholder (a coloured JPanel acting as an image) ──
        JPanel poster = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw a gradient rectangle to look like a movie poster
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(posterColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                // Draw a simple "🎥" emoji in the centre
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
                FontMetrics fm = g2d.getFontMetrics();
                String icon = "🎥";
                int x = (getWidth() - fm.stringWidth(icon)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 5;
                g2d.drawString(icon, x, y);
            }
        };
        poster.setPreferredSize(new Dimension(170, 200));
        poster.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        poster.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Movie name label ──
        JLabel nameLabel = new JLabel(
            "<html><div style='text-align:center;'>" + movieName + "</div></html>",
            SwingConstants.CENTER
        );
        nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // ── "Book Ticket" button ──
        JButton bookBtn = new JButton("Book Ticket");
        bookBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookBtn.setBackground(new Color(230, 126, 34));  // orange
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFont(new Font("Arial", Font.BOLD, 13));
        bookBtn.setFocusPainted(false);
        bookBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        bookBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // When clicked, open the Seat Selection screen for this movie
        bookBtn.addActionListener(e -> {
            dispose(); // close Home Page
            new SeatSelection(movieName); // open Seat Selection
        });

        card.add(poster);
        card.add(nameLabel);
        card.add(bookBtn);

        return card;
    }
}
