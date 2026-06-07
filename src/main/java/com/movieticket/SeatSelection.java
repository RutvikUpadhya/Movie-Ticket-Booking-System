package com.movieticket;

// ─────────────────────────────────────────────────────────────
//  SeatSelection.java
//  Seat Selection Screen:
//    • 5×5 grid of seat buttons
//    • Green  = Available
//    • Red    = Booked (already in DB)
//    • Blue   = Selected by user right now
//    • Clicking available seat toggles it selected/deselected
//    • Shows running total (₹200 per seat)
//    • "Confirm Booking" saves to DB and shows confirmation
//    • "Back" returns to the Home Page
// ─────────────────────────────────────────────────────────────

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatSelection extends JFrame {

    // ── Colour constants ──
    private static final Color GREEN  = new Color(46, 160, 67);   // available
    private static final Color RED    = new Color(200, 50,  50);  // booked
    private static final Color BLUE   = new Color(33, 120, 210);  // selected
    private static final Color BG     = new Color(30, 30, 47);

    private static final int  TICKET_PRICE = 200;  // ₹200 per seat

    // ── State ──
    private final String movieName;
    private final JButton[][] seatButtons = new JButton[5][5]; // 5×5 grid
    private final List<String> selectedSeats  = new ArrayList<>(); // seats chosen now
    private final List<String> bookedSeats    = new ArrayList<>(); // seats from DB

    private JLabel totalLabel; // shows running total

    // ─────────────────────────────────────────────────────────
    //  Constructor
    // ─────────────────────────────────────────────────────────
    public SeatSelection(String movieName) {
        this.movieName = movieName;

        setTitle("Seat Selection – " + movieName);
        setSize(560, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load already-booked seats from the database
        loadBookedSeats();

        // ── Root panel ──
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Top: movie name header ──
        JLabel header = new JLabel("🎬  " + movieName, SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        root.add(header, BorderLayout.NORTH);

        // ── Centre: seat grid + legend ──
        JPanel centre = new JPanel(new BorderLayout(0, 10));
        centre.setBackground(BG);

        // Screen label at the top of the grid
        JLabel screenLabel = new JLabel("— SCREEN —", SwingConstants.CENTER);
        screenLabel.setForeground(new Color(200, 200, 200));
        screenLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        screenLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 100, 140)));
        centre.add(screenLabel, BorderLayout.NORTH);

        // Seat grid (5 rows × 5 columns)
        JPanel gridPanel = new JPanel(new GridLayout(5, 5, 8, 8));
        gridPanel.setBackground(BG);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                String seatId = getSeatId(row, col); // e.g. "A1", "B3"
                JButton btn = createSeatButton(seatId);
                seatButtons[row][col] = btn;
                gridPanel.add(btn);
            }
        }
        centre.add(gridPanel, BorderLayout.CENTER);

        // Colour legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        legend.setBackground(BG);
        legend.add(makeLegend(GREEN, "Available"));
        legend.add(makeLegend(RED,   "Booked"));
        legend.add(makeLegend(BLUE,  "Selected"));
        centre.add(legend, BorderLayout.SOUTH);

        root.add(centre, BorderLayout.CENTER);

        // ── Bottom: total label + buttons ──
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 5));
        bottomPanel.setBackground(BG);

        totalLabel = new JLabel("Total: ₹0  (0 seats selected)", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 15));
        totalLabel.setForeground(new Color(230, 200, 50));
        bottomPanel.add(totalLabel, BorderLayout.NORTH);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnRow.setBackground(BG);

        JButton backBtn = new JButton("← Back");
        styleButton(backBtn, new Color(100, 100, 120));
        backBtn.addActionListener(e -> {
            dispose();
            new HomePage(); // return to home
        });

        JButton confirmBtn = new JButton("Confirm Booking ✔");
        styleButton(confirmBtn, new Color(46, 160, 67));
        confirmBtn.addActionListener(e -> confirmBooking());

        btnRow.add(backBtn);
        btnRow.add(confirmBtn);
        bottomPanel.add(btnRow, BorderLayout.CENTER);

        root.add(bottomPanel, BorderLayout.SOUTH);

        add(root);
        setVisible(true);
    }

    // ─────────────────────────────────────────────────────────
    //  getSeatId(row, col)
    //  Converts grid position to a label like "A1", "C3", etc.
    // ─────────────────────────────────────────────────────────
    private String getSeatId(int row, int col) {
        char rowChar = (char) ('A' + row); // A, B, C, D, E
        return rowChar + String.valueOf(col + 1); // A1 … E5
    }

    // ─────────────────────────────────────────────────────────
    //  createSeatButton(seatId)
    //  Creates one seat button. If already booked → red + disabled.
    //  Otherwise → green, clicking toggles blue selection.
    // ─────────────────────────────────────────────────────────
    private JButton createSeatButton(String seatId) {
        JButton btn = new JButton(seatId);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if (bookedSeats.contains(seatId)) {
            // Already booked – show red and disable
            btn.setBackground(RED);
            btn.setEnabled(false);
        } else {
            // Available – show green
            btn.setBackground(GREEN);
            btn.addActionListener(e -> toggleSeat(btn, seatId));
        }
        return btn;
    }

    // ─────────────────────────────────────────────────────────
    //  toggleSeat(button, seatId)
    //  Switches seat between Selected (blue) and Available (green).
    // ─────────────────────────────────────────────────────────
    private void toggleSeat(JButton btn, String seatId) {
        if (selectedSeats.contains(seatId)) {
            // Deselect
            selectedSeats.remove(seatId);
            btn.setBackground(GREEN);
        } else {
            // Select
            selectedSeats.add(seatId);
            btn.setBackground(BLUE);
        }
        updateTotal();
    }

    // ─────────────────────────────────────────────────────────
    //  updateTotal()
    //  Recalculates and displays the running total amount.
    // ─────────────────────────────────────────────────────────
    private void updateTotal() {
        int count  = selectedSeats.size();
        int amount = count * TICKET_PRICE;
        totalLabel.setText("Total: ₹" + amount + "  (" + count + " seat(s) selected)");
    }

    // ─────────────────────────────────────────────────────────
    //  loadBookedSeats()
    //  Fetches all already-booked seat numbers for this movie
    //  from the database and stores them in bookedSeats list.
    // ─────────────────────────────────────────────────────────
    private void loadBookedSeats() {
        String sql = "SELECT seat_no FROM bookings WHERE movie_name = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, movieName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getString("seat_no"));
            }
        } catch (SQLException ex) {
            // If DB is not reachable, just show a warning and continue
            JOptionPane.showMessageDialog(
                null,
                "Could not load booked seats:\n" + ex.getMessage()
                + "\n\nMake sure MySQL is running and DB credentials in DBConnection.java are correct.",
                "Database Warning",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ─────────────────────────────────────────────────────────
    //  confirmBooking()
    //  Called when user clicks "Confirm Booking".
    //  1. Validates that at least one seat is selected.
    //  2. Saves each selected seat to the database.
    //  3. Shows a confirmation message.
    //  4. Returns to Home Page.
    // ─────────────────────────────────────────────────────────
    private void confirmBooking() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please select at least one seat before confirming.",
                "No Seat Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Save each selected seat to the database
        String sql = "INSERT INTO bookings (movie_name, seat_no) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (String seat : selectedSeats) {
                ps.setString(1, movieName);
                ps.setString(2, seat);
                ps.executeUpdate();
            }

            // Build confirmation message
            String seatsStr  = String.join(", ", selectedSeats);
            int    total     = selectedSeats.size() * TICKET_PRICE;

            String msg = "✅  Booking Confirmed!\n\n"
                       + "Movie  : " + movieName + "\n"
                       + "Seats  : " + seatsStr   + "\n"
                       + "Total  : ₹" + total     + "\n\n"
                       + "Enjoy the show! 🎉";

            JOptionPane.showMessageDialog(this, msg, "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to save booking:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            return; // stay on the screen if save failed
        }

        // Return to home page
        dispose();
        new HomePage();
    }

    // ─────────────────────────────────────────────────────────
    //  Helper – makeLegend(color, text)
    //  Creates a small coloured box + label for the legend.
    // ─────────────────────────────────────────────────────────
    private JPanel makeLegend(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setBackground(BG);

        JPanel box = new JPanel();
        box.setBackground(color);
        box.setPreferredSize(new Dimension(16, 16));

        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));

        p.add(box);
        p.add(lbl);
        return p;
    }

    // ─────────────────────────────────────────────────────────
    //  Helper – styleButton(button, bgColor)
    //  Applies a consistent style to action buttons.
    // ─────────────────────────────────────────────────────────
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
