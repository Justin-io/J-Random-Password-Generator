import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

class BlackGlassDialog extends JDialog {
    private int dragX, dragY;
    public BlackGlassDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setLayout(new BorderLayout());
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight(), arc = 28;
                g2d.setColor(new Color(10, 10, 15, 200));
                g2d.fillRoundRect(0, 0, w, h, arc, arc);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
                g2d.setColor(new Color(70, 130, 240));
                g2d.fillRoundRect(3, 3, w-6, h-6, arc-6, arc-6);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2d.setColor(new Color(100, 180, 255));
                g2d.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(1, 1, w-2, h-2, arc, arc);
                g2d.dispose();
            }
        };
        glassPanel.setOpaque(false);
        add(glassPanel, BorderLayout.CENTER);
        MouseAdapter dragHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragX = e.getX();
                dragY = e.getY();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - dragX, e.getYOnScreen() - dragY);
            }
        };
        addMouseListener(dragHandler);
        addMouseMotionListener(dragHandler);
    }
}

public class PasswordGeneratorApp extends JFrame {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/password_db";
    private static final String DB_USER = "appuser";
    private static final String DB_PASSWORD = "YourStrongPassword123!";

    private JTextField passwordField;
    private JSlider lengthSlider;
    private JLabel lengthLabel;
    private JCheckBox uppercaseCheck, lowercaseCheck, numbersCheck, symbolsCheck;
    private JTable passwordTable;
    private DefaultTableModel tableModel;
    private JButton generateButton, saveButton, copyButton, deleteButton, exitButton, showDbButton;
    private JLabel strengthLabel;
    private JProgressBar strengthBar;
    private JPanel tablePanel;
    private int mouseX, mouseY;

    public PasswordGeneratorApp() {
        initializeDatabase();
        initComponents();
        setupUI();
        setupEventHandlers();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS password_db");
            stmt.executeUpdate("USE password_db");
            String createTableSQL = "CREATE TABLE IF NOT EXISTS passwords (" +
                                   "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                   "password VARCHAR(255) NOT NULL, " +
                                   "description VARCHAR(255), " +
                                   "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            showBlackGlassMessage("Database error: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initComponents() {
        setTitle("J-Random PassWord Gen");
        setSize(920, 720);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight(), arc = 34;
                g2d.setColor(new Color(8, 8, 12, 220));
                g2d.fillRoundRect(0, 0, w, h, arc, arc);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.12f));
                g2d.setColor(new Color(70, 130, 240));
                g2d.fillRoundRect(4, 4, w-8, h-8, arc-8, arc-8);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
                g2d.setColor(new Color(100, 180, 255));
                g2d.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(1, 1, w-2, h-2, arc, arc);
                g2d.dispose();
            }
        };
        mainPanel.setOpaque(false);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setOpaque(false);

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setOpaque(false);
        titleBar.setBorder(new EmptyBorder(16, 24, 16, 20));
        JLabel titleLabel = new JLabel("🖤 J-Random PassWord Gen");
        titleLabel.setForeground(new Color(200, 210, 230));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JButton closeButton = new JButton("✕") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    g2d.setColor(new Color(220, 100, 100));
                } else if (getModel().isRollover()) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15f));
                    g2d.setColor(new Color(220, 100, 100));
                } else {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                    g2d.setColor(new Color(220, 100, 100));
                }
                g2d.fillOval(0, 0, getWidth(), getHeight());
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setColor(new Color(220, 100, 100));
                g2d.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawOval(0, 0, getWidth()-1, getHeight()-1);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        closeButton.setForeground(new Color(220, 100, 100));
        closeButton.setOpaque(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        closeButton.setFocusable(false);
        closeButton.addActionListener(e -> confirmExit());
        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(closeButton, BorderLayout.EAST);
        northPanel.add(titleBar, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setOpaque(false);
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        passwordField = new JTextField(20) {
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setColor(new Color(100, 180, 255));
                g2d.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                g2d.dispose();
            }
        };
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Segoe UI", Font.BOLD, 20));
        passwordField.setForeground(new Color(220, 230, 255));
        passwordField.setBorder(new EmptyBorder(16, 22, 16, 22));
        passwordField.setOpaque(false);
        passwordField.setBackground(new Color(0, 0, 0, 0));

        lengthSlider = new JSlider(8, 32, 16) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2d.setColor(new Color(100, 180, 255));
                g2d.setStroke(new BasicStroke(3.5f));
                int y = getHeight() / 2;
                g2d.drawLine(0, y, getWidth(), y);
                g2d.dispose();
            }
        };
        lengthSlider.setOpaque(false);
        lengthSlider.setForeground(new Color(220, 230, 255));
        lengthSlider.setFocusable(false);
        lengthSlider.setUI(new javax.swing.plaf.metal.MetalSliderUI() {
            @Override public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                g2d.setColor(new Color(70, 130, 240));
                g2d.fillOval(thumbRect.x-3, thumbRect.y-3, thumbRect.width+6, thumbRect.height+6);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g2d.setColor(new Color(200, 230, 255));
                g2d.setStroke(new BasicStroke(2.0f));
                g2d.drawOval(thumbRect.x-3, thumbRect.y-3, thumbRect.width+6, thumbRect.height+6);
                g2d.dispose();
            }
        });
        lengthLabel = new JLabel("Password Length: 16");
        lengthLabel.setForeground(new Color(200, 210, 230));
        lengthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lengthLabel.setBorder(new EmptyBorder(12, 18, 12, 18));

        uppercaseCheck = createBlackGlassCheckBox("Uppercase (A-Z)", true);
        lowercaseCheck = createBlackGlassCheckBox("Lowercase (a-z)", true);
        numbersCheck = createBlackGlassCheckBox("Numbers (0-9)", true);
        symbolsCheck = createBlackGlassCheckBox("Symbols (!@#$...)", true);

        generateButton = createBlackGlassButton("Generate", new Color(70, 130, 240));
        copyButton = createBlackGlassButton("Copy", new Color(80, 180, 140));
        saveButton = createBlackGlassButton("Save", new Color(100, 150, 220));
        deleteButton = createBlackGlassButton("Delete", new Color(220, 100, 100));
        showDbButton = createBlackGlassButton("Show Database", new Color(120, 180, 200));
        exitButton = createBlackGlassButton("Exit", new Color(180, 120, 200));

        strengthLabel = new JLabel("Password Strength: ");
        strengthLabel.setForeground(new Color(200, 210, 230));
        strengthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        strengthLabel.setBorder(new EmptyBorder(12, 18, 12, 18));

        strengthBar = new JProgressBar(0, 100) {
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
                g2d.setColor(new Color(100, 180, 255));
                g2d.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2d.dispose();
            }
        };
        strengthBar.setStringPainted(true);
        strengthBar.setBorder(new EmptyBorder(12, 12, 12, 12));
        strengthBar.setOpaque(false);
        strengthBar.setBackground(new Color(0, 0, 0, 0));
        strengthBar.setUI(new javax.swing.plaf.metal.MetalProgressBarUI() {
            @Override public void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Insets b = progressBar.getInsets();
                int amountFull = getAmountFull(b, progressBar.getWidth(), progressBar.getHeight());
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
                g2d.setColor(progressBar.getForeground());
                g2d.fillRoundRect(b.left, b.top, amountFull,
                    progressBar.getHeight() - b.top - b.bottom, 8, 8);
                g2d.dispose();
                if (progressBar.isStringPainted()) {
                    paintString(g, b.left, b.top, progressBar.getWidth(),
                        progressBar.getHeight(), amountFull, b);
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(14, 18, 14, 18);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; controlPanel.add(passwordField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; controlPanel.add(lengthLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; controlPanel.add(lengthSlider, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; controlPanel.add(uppercaseCheck, gbc);
        gbc.gridx = 1; gbc.gridy = 3; controlPanel.add(lowercaseCheck, gbc);
        gbc.gridx = 0; gbc.gridy = 4; controlPanel.add(numbersCheck, gbc);
        gbc.gridx = 1; gbc.gridy = 4; controlPanel.add(symbolsCheck, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; controlPanel.add(strengthLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; controlPanel.add(strengthBar, gbc);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1; controlPanel.add(generateButton, gbc);
        gbc.gridx = 1; gbc.gridy = 7; controlPanel.add(copyButton, gbc);
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 1; controlPanel.add(saveButton, gbc);
        gbc.gridx = 1; gbc.gridy = 8; controlPanel.add(deleteButton, gbc);
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; controlPanel.add(showDbButton, gbc);
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2; controlPanel.add(exitButton, gbc);

        northPanel.add(controlPanel, BorderLayout.CENTER);

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        tablePanel.setVisible(false);

        JLabel dbTitleLabel = new JLabel("🔐 Password Database");
        dbTitleLabel.setForeground(new Color(200, 210, 230));
        dbTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dbTitleLabel.setBorder(new EmptyBorder(10, 16, 10, 16));
        tablePanel.add(dbTitleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Password", "Description", "Created At"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        passwordTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) ((JComponent) c).setOpaque(false);
                return c;
            }
        };
        passwordTable.getColumnModel().getColumn(0).setMinWidth(0);
        passwordTable.getColumnModel().getColumn(0).setMaxWidth(0);
        passwordTable.getColumnModel().getColumn(0).setWidth(0);
        passwordTable.setRowHeight(36);
        passwordTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        passwordTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        passwordTable.getTableHeader().setOpaque(false);
        passwordTable.getTableHeader().setBackground(new Color(70, 130, 240, 100));
        passwordTable.getTableHeader().setForeground(new Color(220, 230, 255));
        passwordTable.setBackground(new Color(0, 0, 0, 0));
        passwordTable.setForeground(new Color(220, 230, 255));
        passwordTable.setSelectionBackground(new Color(100, 180, 255, 120));
        passwordTable.setSelectionForeground(new Color(240, 245, 255));
        passwordTable.setGridColor(new Color(100, 180, 255, 80));
        passwordTable.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(passwordTable) {
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2d.setColor(new Color(100, 180, 255));
                g2d.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2d.dispose();
            }
        };
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(16, 16, 16, 16));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        MouseAdapter frameDragHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { mouseX = e.getX(); mouseY = e.getY(); }
            @Override
            public void mouseDragged(MouseEvent e) { setLocation(e.getXOnScreen() - mouseX, e.getYOnScreen() - mouseY); }
        };
        addMouseListener(frameDragHandler);
        addMouseMotionListener(frameDragHandler);
    }

    private JCheckBox createBlackGlassCheckBox(String text, boolean selected) {
        JCheckBox cb = new JCheckBox(text, selected);
        cb.setOpaque(false);
        cb.setForeground(new Color(200, 210, 230));
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cb.setFocusable(false);
        cb.setBorder(new EmptyBorder(8, 8, 8, 8));
        cb.setIcon(new Icon() {
            @Override public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (cb.isSelected()) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
                    g2d.setColor(new Color(70, 130, 240));
                    g2d.fillRoundRect(x, y, getIconWidth(), getIconHeight(), 6, 6);
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine(x + 4, y + 9, x + 9, y + 13);
                    g2d.drawLine(x + 9, y + 13, x + 15, y + 6);
                } else {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                    g2d.setColor(new Color(100, 180, 255));
                    g2d.drawRoundRect(x, y, getIconWidth(), getIconHeight(), 6, 6);
                }
                g2d.dispose();
            }
            @Override public int getIconWidth() { return 17; }
            @Override public int getIconHeight() { return 17; }
        });
        return cb;
    }

    private JButton createBlackGlassButton(String text, Color accent) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                    g2d.setColor(accent);
                } else if (getModel().isRollover()) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
                    g2d.setColor(accent);
                } else {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                    g2d.setColor(accent);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusable(false);
        btn.setBorder(new EmptyBorder(14, 20, 14, 20));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        return btn;
    }

    private void showBlackGlassMessage(String message, String title, int type) {
        BlackGlassDialog dialog = new BlackGlassDialog(this, "", true);
        dialog.setSize(440, 200);
        dialog.setLocationRelativeTo(this);
        JPanel content = new JPanel(new BorderLayout(14, 14));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 30, 24, 30));
        Color iconColor = type == JOptionPane.ERROR_MESSAGE ? new Color(220, 100, 100) :
                         type == JOptionPane.WARNING_MESSAGE ? new Color(230, 170, 80) :
                         new Color(70, 130, 240);
        JLabel iconLabel = new JLabel("●");
        iconLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        iconLabel.setForeground(iconColor);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JTextArea msgArea = new JTextArea(message);
        msgArea.setWrapStyleWord(true);
        msgArea.setLineWrap(true);
        msgArea.setEditable(false);
        msgArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        msgArea.setForeground(new Color(220, 230, 255));
        msgArea.setOpaque(false);
        msgArea.setBorder(new EmptyBorder(0, 14, 0, 0));
        JButton okBtn = createBlackGlassButton("OK", new Color(70, 130, 240));
        okBtn.addActionListener(e -> dialog.dispose());
        content.add(iconLabel, BorderLayout.WEST);
        content.add(msgArea, BorderLayout.CENTER);
        content.add(okBtn, BorderLayout.SOUTH);
        ((JPanel)dialog.getContentPane()).add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private String showBlackGlassInput(String message) {
        BlackGlassDialog dialog = new BlackGlassDialog(this, "", true);
        dialog.setSize(460, 180);
        dialog.setLocationRelativeTo(this);
        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(24, 30, 24, 30));
        JLabel label = new JLabel(message);
        label.setForeground(new Color(220, 230, 255));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        JTextField input = new JTextField(20) {
            @Override protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2d.setColor(new Color(100, 180, 255));
                g2d.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                g2d.dispose();
            }
        };
        input.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        input.setForeground(new Color(220, 230, 255));
        input.setOpaque(false);
        input.setBackground(new Color(0, 0, 0, 0));
        input.setBorder(new EmptyBorder(10, 14, 10, 14));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        btnPanel.setOpaque(false);
        JButton ok = createBlackGlassButton("OK", new Color(70, 130, 240));
        JButton cancel = createBlackGlassButton("Cancel", new Color(120, 120, 140));
        AtomicReference<String> result = new AtomicReference<>(null);
        ok.addActionListener(e -> {
            result.set(input.getText());
            dialog.dispose();
        });
        cancel.addActionListener(e -> {
            result.set(null);
            dialog.dispose();
        });
        btnPanel.add(ok);
        btnPanel.add(cancel);
        content.add(label, BorderLayout.NORTH);
        content.add(input, BorderLayout.CENTER);
        content.add(btnPanel, BorderLayout.SOUTH);
        ((JPanel)dialog.getContentPane()).add(content, BorderLayout.CENTER);
        dialog.getRootPane().setDefaultButton(ok);
        input.requestFocusInWindow();
        dialog.setVisible(true);
        return result.get();
    }

    private void confirmExit() {
        BlackGlassDialog dialog = new BlackGlassDialog(this, "", true);
        dialog.setSize(400, 160);
        dialog.setLocationRelativeTo(this);
        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 25, 20, 25));
        JLabel msg = new JLabel("Are you sure you want to exit?");
        msg.setForeground(new Color(220, 230, 255));
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        msg.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);
        JButton yes = createBlackGlassButton("Yes", new Color(220, 100, 100));
        JButton no = createBlackGlassButton("No", new Color(70, 130, 240));
        yes.addActionListener(e -> { dialog.dispose(); System.exit(0); });
        no.addActionListener(e -> dialog.dispose());
        btnPanel.add(yes);
        btnPanel.add(no);
        content.add(msg, BorderLayout.CENTER);
        content.add(btnPanel, BorderLayout.SOUTH);
        ((JPanel)dialog.getContentPane()).add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void setupUI() {
        lengthSlider.addChangeListener(e -> {
            lengthLabel.setText("Password Length: " + lengthSlider.getValue());
            generatePassword();
        });
    }

    private void setupEventHandlers() {
        generateButton.addActionListener(e -> generatePassword());
        copyButton.addActionListener(e -> copyToClipboard());
        saveButton.addActionListener(e -> savePassword());
        deleteButton.addActionListener(e -> deleteSelectedPassword());
        showDbButton.addActionListener(e -> {
            tablePanel.setVisible(!tablePanel.isVisible());
            showDbButton.setText(tablePanel.isVisible() ? "Hide Database" : "Show Database");
            if (tablePanel.isVisible()) {
                loadPasswords();
            }
        });
        exitButton.addActionListener(e -> confirmExit());
    }

    private void generatePassword() {
        int length = lengthSlider.getValue();
        boolean u = uppercaseCheck.isSelected(), l = lowercaseCheck.isSelected(),
                n = numbersCheck.isSelected(), s = symbolsCheck.isSelected();
        if (!u && !l && !n && !s) {
            showBlackGlassMessage("Select at least one character type!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringBuilder pwd = new StringBuilder();
        SecureRandom rand = new SecureRandom();
        String chars = (u ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ" : "") +
                      (l ? "abcdefghijklmnopqrstuvwxyz" : "") +
                      (n ? "0123456789" : "") +
                      (s ? "!@#$%^&*()_+-=[]{}|;:,.<>?" : "");
        for (int i = 0; i < length; i++) {
            pwd.append(chars.charAt(rand.nextInt(chars.length())));
        }
        passwordField.setText(pwd.toString());
        updatePasswordStrength(pwd.toString());
    }

    private void updatePasswordStrength(String pwd) {
        int s = 0;
        if (pwd.length() >= 8) s += 20;
        if (pwd.length() >= 12) s += 20;
        if (pwd.length() >= 16) s += 10;
        if (!pwd.equals(pwd.toLowerCase())) s += 15;
        if (!pwd.equals(pwd.toUpperCase())) s += 15;
        if (pwd.matches(".*\\d.*")) s += 15;
        if (!pwd.matches("[A-Za-z0-9]*")) s += 15;
        strengthBar.setValue(s);
        if (s < 40) {
            strengthBar.setForeground(new Color(220, 100, 100));
            strengthLabel.setText("Password Strength: Weak");
        } else if (s < 70) {
            strengthBar.setForeground(new Color(230, 170, 80));
            strengthLabel.setText("Password Strength: Medium");
        } else {
            strengthBar.setForeground(new Color(70, 130, 240));
            strengthLabel.setText("Password Strength: Strong");
        }
    }

    private void copyToClipboard() {
        String p = passwordField.getText();
        if (p.isEmpty()) {
            showBlackGlassMessage("No password to copy!", "Copy Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(p), null);
        showBlackGlassMessage("Password copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void savePassword() {
        String p = passwordField.getText();
        if (p.isEmpty()) {
            showBlackGlassMessage("No password to save!", "Save Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String desc = showBlackGlassInput("Enter a description for this password:");
        if (desc != null && !desc.trim().isEmpty()) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement ps = conn.prepareStatement("INSERT INTO passwords (password, description) VALUES (?, ?)")) {
                ps.setString(1, p);
                ps.setString(2, desc);
                ps.executeUpdate();
                if (tablePanel.isVisible()) {
                    loadPasswords();
                }
                showBlackGlassMessage("Password saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                showBlackGlassMessage("Error saving password: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadPasswords() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, password, description, created_at FROM passwords ORDER BY created_at DESC")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("password"),
                    rs.getString("description"),
                    rs.getTimestamp("created_at")
                });
            }
        } catch (SQLException e) {
            showBlackGlassMessage("Error loading passwords: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedPassword() {
        int viewRow = passwordTable.getSelectedRow();
        if (viewRow == -1) {
            showBlackGlassMessage("Please select a password to delete!", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int modelRow = passwordTable.convertRowIndexToModel(viewRow);
        int id = (int) tableModel.getValueAt(modelRow, 0);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM passwords WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            if (tablePanel.isVisible()) {
                loadPasswords();
            }
            showBlackGlassMessage("Password deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            showBlackGlassMessage("Error deleting password: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new PasswordGeneratorApp().setVisible(true);
        });
    }
}