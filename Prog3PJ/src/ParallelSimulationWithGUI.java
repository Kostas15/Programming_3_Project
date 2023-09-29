import javax.swing.*;
import java.awt.*;

public class ParallelSimulationWithGUI extends ParallelSimulation {
    private JFrame frame;
    private DrawingPanel drawingPanel;

    public ParallelSimulationWithGUI(int numParticles, int numCycles, int numThreads) {
        super(numParticles, numCycles, numThreads);
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Charged Particles Simulation (Parallel)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        drawingPanel = new DrawingPanel();
        frame.add(drawingPanel);
        /*timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cyclesCompleted < numCycles) {
                    updateSimulation();
                    drawingPanel.repaint();
                } else {
                    timer.stop();
                }
            }
        });*/
        frame.setVisible(true);
        startSimulation();
    }

    public void startSimulation() {
        long startTime = System.currentTimeMillis();
        //timer.start();
        int cyclesCompleted;
        for (cyclesCompleted = 0; cyclesCompleted < numCycles; cyclesCompleted++) {
            updateSimulation();
            drawingPanel.repaint();
            System.out.println("Number of cycles: " + cyclesCompleted);

        }
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel simulation completed in " + (endTime - startTime) + " milliseconds.");

        int option = JOptionPane.showConfirmDialog(frame, "Do you want to exit the program?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void updateSimulation() {
        runSimulationForNCycles(1);

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
        int numThreads = Runtime.getRuntime().availableProcessors();
        ParallelSimulationWithGUI sim = new ParallelSimulationWithGUI(numParticles, numCycles, numThreads);
        //sim.startSimulation();
    }
}