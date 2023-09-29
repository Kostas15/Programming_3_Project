import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelSimulation extends Simulation {
    private ExecutorService executorService;
    private final int numThreads;

    public ParallelSimulation(int numParticles, int numCycles, int numThreads) {
        super(numParticles, numCycles);
        this.numThreads = numThreads;
        executorService = Executors.newFixedThreadPool(numThreads);
    }

    @Override
    public void runSimulationForNCycles(int nCycles) {
        double dt = 0.01;
        for (int cycle = 0; cycle < nCycles; cycle++) {
            //System.out.println("Number of cycles: " + cycle);
            for (int i = 0; i < numParticles; i += numThreads) {
                //System.out.println("Num of Particles:"+i);
                int start = i;
                int end = Math.min(start + numThreads, numParticles);
                executorService.submit(() -> {
                    for (int idx = start; idx < end; idx++) {
                        Particle1 particle11 = particle1.get(idx);
                        double fx = 0;
                        double fy = 0;
                        for (int j = 0; j < numParticles; j++) {
                            if (idx != j) {
                                Particle1 other = particle1.get(j);
                                double[] force = particle11.calculateForce(other);
                                fx += force[0];
                                fy += force[1];
                            }
                        }
                        particle11.updateVelocity(fx, fy, dt);
                        particle11.updatePosition(dt);
                    }
                });
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService = Executors.newFixedThreadPool(numThreads);
        }
    }

    public static void main(String[] args) {
        int numParticles = 2000;
        int numCycles = 1000;
        int numThreads = Runtime.getRuntime().availableProcessors();
        ParallelSimulation sim = new ParallelSimulation(numParticles, numCycles, numThreads);
        System.out.println("Parallel simulation started");
        long startTime = System.currentTimeMillis();
        sim.runSimulationForNCycles(numCycles);
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel simulation of "+numCycles+" cycles completed in " + (endTime - startTime) + " milliseconds.");
    }
}
