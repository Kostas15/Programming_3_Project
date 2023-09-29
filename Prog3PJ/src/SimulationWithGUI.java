import javax.swing.*;
import java.awt.*;

public class SimulationWithGUI extends Simulation {
    private JFrame frame;
    private DrawingPanel drawingPanel;
    private int cyclesCompleted;

    public SimulationWithGUI(int numParticles, int numCycles) {
        super(numParticles, numCycles);
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Charged Particles Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        drawingPanel = new DrawingPanel();
        frame.add(drawingPanel);

        frame.setVisible(true);
    }

    public void startSimulation() {
        long startTime = System.currentTimeMillis();

        for (int cycle = 0; cycle < numCycles; cycle++) {
            runSimulationForNCycles(1);
            drawingPanel.repaint();
            cyclesCompleted++;
            System.out.println("Cycles completed: " + cyclesCompleted);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Sequential simulation completed in " + (endTime - startTime) + " milliseconds.");

        int option = JOptionPane.showConfirmDialog(frame, "Do you want to exit the program?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private class DrawingPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (Particle1 particle1 : particle1) {
                g.setColor(particle1.getCharge() > 0 ? Color.RED : Color.BLUE);
                int x = (int) particle1.getX();
                int y = (int) particle1.getY();
                g.fillOval(x - 2, y - 2, 4, 4);
            }
        }
    }

    public static void main(String[] args) {
        int numParticles = 1000;
        int numCycles = 1000;
        SimulationWithGUI sim = new SimulationWithGUI(numParticles, numCycles);
        sim.startSimulation();

    }
}
