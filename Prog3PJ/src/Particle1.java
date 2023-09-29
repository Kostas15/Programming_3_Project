public class Particle1 {
    private double x;
    private double y;
    private double vx;
    private double vy;
    private int charge;

    public Particle1(double x, double y, double vx, double vy, int charge) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.charge = charge;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getCharge() {
        return charge;
    }

    public void updatePosition(double dt) {
        x += vx * dt;
        y += vy * dt;
        // Handle boundary conditions
        double radius = 2.0;
        if (x < radius || x > 800 - radius) {
            vx = -vx;
        }
        if (y < radius || y > 600 - radius) {
            vy = -vy;
        }
    }
    public void updateVelocity(double fx, double fy, double dt) {
        vx += fx * dt;
        vy += fy * dt;
    }
    public double distance(Particle1 other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    public double[] calculateForce(Particle1 other) {
        double dx = other.x - x;
        double dy = other.y - y;
        double distance = distance(other);
        double forceMagnitude = (charge * other.charge) / (distance * distance);
        double[] force = { forceMagnitude * dx / distance, forceMagnitude * dy / distance };
        return force;
    }
}
