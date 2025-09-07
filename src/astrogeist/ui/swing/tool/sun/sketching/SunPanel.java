package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public final class SunPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private double paddingFraction = 0.18;

    private Color background = Color.WHITE;
    private Color sunFill = new Color(255, 220, 120);
    private Color limbStroke = new Color(230, 160, 60);

    public SunPanel() {
        setOpaque(true);
    }

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

    @Override public Dimension getPreferredSize() { return new Dimension(600, 600); }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Background behind the Sun.
            g2.setPaint(background);
            g2.fillRect(0, 0, getWidth(), getHeight());

            Rectangle2D.Double sun = getSunBoundsPx();

            // Photosphere.
            g2.setPaint(sunFill);
            g2.fill(new Ellipse2D.Double(sun.x, sun.y, sun.width, sun.height));

            // Limb.
            g2.setStroke(new BasicStroke(2f));
            g2.setPaint(limbStroke);
            g2.draw(new Ellipse2D.Double(sun.x, sun.y, sun.width, sun.height));
        } finally {
            g2.dispose();
        }
    }
}
