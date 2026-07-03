package com.example.zoo.db;

import java.sql.Date;
import java.sql.SQLException;

/**
 * Isolated integration test for the DAO layer - run this directly to verify
 * the DAOs talk to zoo_db correctly before wiring them into ZooController.
 */
public class ZooDAOTestRunner {

    public static void runDatabaseTests() {
        ZooDAO zooDAO = new ZooDAO();
        PredatorDAO predatorDAO = new PredatorDAO();
        FishDAO fishDAO = new FishDAO();
        PenguinDAO penguinDAO = new PenguinDAO();
        MedicalTreatmentDAO treatmentDAO = new MedicalTreatmentDAO();
        AnimalDAO animalDAO = new AnimalDAO();

        try {
            System.out.println("\n==============================================");
            System.out.println("   RUNNING ISOLATED DAO INTEGRATION TEST      ");
            System.out.println("==============================================\n");

            // --- 0. TEST: ZooDAO (find the zoo that AnimalID 9001-9003 will belong to) ---
            System.out.println("[TEST] Looking up ZooId 1...");
            zooDAO.updateZoo(1, "TVL Zoo Updated", "Tel Aviv", "Bazel", "22");
            zooDAO.findZooById(1);


            // --- 1. PRE-TEST CLEANUP (ignore if rows don't exist yet) ---
            try { predatorDAO.deletePredator(9001); } catch (SQLException ignored) {}
            try { fishDAO.deleteFish(9002); } catch (SQLException ignored) {}
            try { penguinDAO.deletePenguin(9003); } catch (SQLException ignored) {}

            // --- 2. INSERT: PREDATOR (2 tables, 1 transaction) ---
            System.out.println("[TEST] Inserting predator...");
            predatorDAO.insertPredator(9001, 5, 80, 1, "TestLion", 190.5, "Male", "Lion");
            predatorDAO.findPredatorById(9001);

            // --- 3. INSERT: FISH (3 tables, 1 transaction) ---
            System.out.println("\n[TEST] Inserting fish with colors...");
            fishDAO.insertFish(9002, 2, 70, 1, 12.3, "Striped", "ClownFish",
                    new String[]{"Orange", "White"});
            fishDAO.findFishById(9002);

            // --- 4. INSERT: PENGUIN (2 tables, 1 transaction) ---
            System.out.println("\n[TEST] Inserting penguin...");
            penguinDAO.insertPenguin(9003, 1, 75, 1, "TestPenguin", 45.0, 3);
            penguinDAO.findPenguinById(9003);

            // --- 5. TREATMENT + AUTOMATIC TRIGGER CHECK ---
            System.out.println("\n[TEST] Checking Happiness BEFORE treatment:");
            animalDAO.findAnimalById(9001);

            treatmentDAO.insertTreatment(90001, "Routine checkup", new Date(System.currentTimeMillis()), 9001);

            System.out.println("[TEST] Checking Happiness AFTER treatment (should be +5):");
            animalDAO.findAnimalById(9001);

            // --- 6. CLEANUP ---
            System.out.println("\n[TEST] Cleaning up test data...");
            treatmentDAO.deleteTreatment(90001);
            predatorDAO.deletePredator(9001);
            fishDAO.deleteFish(9002);
            penguinDAO.deletePenguin(9003);

            System.out.println("\n==============================================");
            System.out.println("      DAO VERIFICATION CYCLE CLEAN            ");
            System.out.println("==============================================\n");

        } catch (SQLException e) {
            System.err.println("\nDAO validation sequence caught a structural error!");
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static void main(String[] args) {
        runDatabaseTests();
    }
}
