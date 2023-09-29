import mpi.*;

public class DistributedSimulation extends Simulation {
    private final int numParticles;

    public DistributedSimulation(int numParticles, int numCycles) {
        super(numParticles, numCycles);
        this.numParticles = numParticles;
    }

    @Override
    public void runSimulationForNCycles(int nCycles) {
        double dt = 0.01;
        for (int cycle = 0; cycle < nCycles; cycle++) {
            System.out.println("Number of cycles: " + cycle);
            for (int i = 0; i < numParticles; i++) {
                int rank = MPI.COMM_WORLD.Rank();
                int size = MPI.COMM_WORLD.Size();
                if (i % size == rank) {
                    //System.out.println("Rank " + rank + " is processing particle " + i);
                    Particle1 particle = particle1.get(i);
                    double fx = 0;
                    double fy = 0;
                    for (int j = 0; j < numParticles; j++) {
                        if (i != j) {
                            Particle1 other = particle1.get(j);
                            double[] force = particle.calculateForce(other);
                            fx += force[0];
                            fy += force[1];
                        }
                    }
                    particle.updateVelocity(fx, fy, dt);
                    particle.updatePosition(dt);
                }
                MPI.COMM_WORLD.Barrier();
            }
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int numParticles = 1000;
        int numCycles = 500;
        DistributedSimulation sim = new DistributedSimulation(numParticles, numCycles);
        System.out.println("Distributed simulation started");
        long startTime = System.currentTimeMillis();
        sim.runSimulationForNCycles(numCycles);
        long endTime = System.currentTimeMillis();
        System.out.println("Rank " + MPI.COMM_WORLD.Rank() +" distributed simulation completed in " + (endTime - startTime) + " milliseconds.");

        MPI.Finalize();
    }
}
