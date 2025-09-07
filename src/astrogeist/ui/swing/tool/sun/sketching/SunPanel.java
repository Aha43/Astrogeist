package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public final class SunPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public enum Tool { NONE, ADD_SUNSPOT }

    private double paddingFraction = 0.18;
    private Color background = Color.WHITE;
    private Color sunFill = new Color(255, 220, 120);
    private Color limbStroke = new Color(230, 160, 60);
    private Tool tool = Tool.NONE;

    // Simple feature model: sunspots in normalized units
    public static final class Sunspot {
        public double angleDeg; // 0°=North, 90°=East
        public double rho;      // 0..1 on disk
        public double sizeR;    // spot radius in R☉
        public Sunspot(double angleDeg, double rho, double sizeR) {
            this.angleDeg = angleDeg; this.rho = rho; this.sizeR = sizeR;
        }
    }
    private final List<Sunspot> sunspots = new ArrayList<>();
    private double defaultSpotSizeR = 0.012; // ~1.2% of solar radius

    public SunPanel() {
        setOpaque(true);

        // Click-to-place behavior when a tool is active
        addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (tool == Tool.ADD_SUNSPOT) {
                    SunLayoutTransform xform = currentTransform();
                    SunLayoutTransform.Polar p = xform.pixelToModel(e.getX(), e.getY());
                    if (p.rho <= 1.0) {
                        sunspots.add(new Sunspot(p.angleDeg, p.rho, defaultSpotSizeR));
                        repaint();
                    } else {
                        Toolkit.getDefaultToolkit().beep(); // off-disk click is ignored
                    }
                }
            }
        });
    }

    // --- public API used by controls ---
    public void setTool(Tool t) {
        this.tool = t;
        setCursor(t == Tool.NONE ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    public Tool getTool() { return tool; }

    public void clearSunspots() { sunspots.clear(); repaint(); }
    public List<Sunspot> getSunspots() { return List.copyOf(sunspots); }
    public void setDefaultSpotSizeR(double sizeR) { this.defaultSpotSizeR = Math.max(0, sizeR); }

    public void setPaddingFraction(double value) {
        paddingFraction = Math.max(0.0, Math.min(0.45, value));
        repaint();
    }
    public double getPaddingFraction() { return paddingFraction; }

    public void setBackgroundColor(Color c) { this.background = c; repaint(); }
    public Color getBackgroundColor() { return background; }

    public void setSunFill(Color c) { this.sunFill = c; repaint(); }
    public Color getSunFill() { return sunFill; }

    public void setLimbStroke(Color c) { this.limbStroke = c; repaint(); }
    public Color getLimbStroke() { return limbStroke; }

    public Point2D.Double getSunCenterPx() {
        int w = getWidth(), h = getHeight();
        return new Point2D.Double(w / 2.0, h / 2.0);
    }

    public double getSunRadiusPx() {
        int w = getWidth(), h = getHeight();
        int min = Math.min(w, h);
        double diskDiameter = Math.max(0, min * (1.0 - 2.0 * paddingFraction));
        return diskDiameter / 2.0;
    }

    public Rectangle2D.Double getSunBoundsPx() {
        Point2D.Double c = getSunCenterPx();
        double r = getSunRadiusPx();
        return new Rectangle2D.Double(c.x - r, c.y - r, 2 * r, 2 * r);
    }

    private SunLayoutTransform currentTransform() {
        Point2D.Double c = getSunCenterPx();
        return new SunLayoutTransform(c.x, c.y, getSunRadiusPx());
    }

    @Override public Dimension getPreferredSize() { return new Dimension(600, 600); }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background
            g2.setPaint(background);
            g2.fillRect(0, 0, getWidth(), getHeight());

            Rectangle2D.Double sun = getSunBoundsPx();

            // Disk
            g2.setPaint(sunFill);
            g2.fill(new Ellipse2D.Double(sun.x, sun.y, sun.width, sun.height));

            // Limb
            g2.setStroke(new BasicStroke(2f));
            g2.setPaint(limbStroke);
            g2.draw(new Ellipse2D.Double(sun.x, sun.y, sun.width, sun.height));

            // Features: sunspots
            SunLayoutTransform xf = currentTransform();
            g2.setPaint(new Color(30, 30, 30));
            for (Sunspot s : sunspots) {
                Point2D c = xf.modelToPixel(s.angleDeg, s.rho);
                double rpx = s.sizeR * xf.rpx;
                Shape dot = new Ellipse2D.Double(c.getX() - rpx, c.getY() - rpx, 2 * rpx, 2 * rpx);
                g2.fill(dot);
            }
        } finally {
            g2.dispose();
        }
    }
}
