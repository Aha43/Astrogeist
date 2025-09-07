package astrogeist.ui.swing.tool.sun.sketching;

import java.awt.geom.Point2D;

public final class SunLayoutTransform {
    public final double cx, cy;    // panel center (px)
    public final double rpx;       // sun radius in pixels

    public SunLayoutTransform(double cx, double cy, double rpx) {
        this.cx = cx; this.cy = cy; this.rpx = rpx; }

    /** Model → pixels. angleDeg: 0°=North, 90°=East. rho in R☉ (1.0=limb). */
    public Point2D.Double modelToPixel(double angleDeg, double rho) {
        double a = Math.toRadians(angleDeg);
        double dx =  rho * rpx * Math.sin(a);  // east-positive
        double dy = -rho * rpx * Math.cos(a);  // north-negative (screen y down)
        return new Point2D.Double(cx + dx, cy + dy);
    }

    /** Pixels → model (rho in R☉, angleDeg as above). */
    public Polar pixelToModel(double x, double y) {
        double dx = x - cx;
        double dy = y - cy;
        double rho = Math.hypot(dx, dy) / rpx;
        double angleDeg = Math.toDegrees(Math.atan2(dx, -dy)); // North→East
        if (angleDeg < 0) angleDeg += 360.0;
        return new Polar(angleDeg, rho);
    }

    public static final class Polar {
        public final double angleDeg;
        public final double rho;
        public Polar(double angleDeg, double rho) { this.angleDeg = angleDeg; this.rho = rho; }
    }
    
}
