import mpi.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DistributedSimulationWithGUI extends DistributedSimulation {
    private JFrame frame;
    private DrawingPanel drawingPanel;

    public DistributedSimulationWithGUI(int numParticles, int numCycles) {
        super(numParticles, numCycles);
        initializeGUI();
    }

    private void initializeGUI() {
        //int rank = MPI.COMM_WORLD.Rank();
        //if (rank == 0) {
            frame = new JFrame("Charged Particles Simulation (Distributed)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            drawingPanel = new DrawingPanel();
            frame.add(drawingPanel);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    frame.dispose();  // Dispose the frame
                    MPI.COMM_WORLD.Abort(0);  // Terminate MPI processes
                }
            });
            frame.setVisible(true);
        //}
    }

    public void startSimulation() {
        long startTime = System.currentTimeMillis();
        int cyclesCompleted;
        for (cyclesCompleted = 0; cyclesCompleted < numCycles; cyclesCompleted++) {
            updateSimulation();
            drawingPanel.repaint();
            System.out.println("Rank " + MPI.COMM_WORLD.Rank() + " completed cycle " + cyclesCompleted);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Distributed simulation completed in " + (endTime - startTime) + " milliseconds.");

        MPI.COMM_WORLD.Barrier(); // Ensure all processes have completed the simulation

        //if (MPI.COMM_WORLD.Rank() == 0) {
            // Only the first process prints the final completion message
            System.out.println("Simulation completed by Rank " + MPI.COMM_WORLD.Rank());
        //}

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

            for (Particle1 particle : particle1) {
                g.setColor(particle.getCharge() > 0 ? Color.RED : Color.BLUE);
                int x = (int) particle.getX();
                int y = (int) particle.getY();
                g.fillOval(x - 2, y - 2, 4, 4);
            }
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int numParticles = 1000;
        int numCycles = 500;
        try{
            DistributedSimulationWithGUI sim = new DistributedSimulationWithGUI(numParticles, numCycles);
            sim.startSimulation();
        } finally {
            MPI.Finalize();
        }
    }
}

