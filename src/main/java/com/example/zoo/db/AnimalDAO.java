package com.example.zoo.db;

import java.sql.*;

/**
 * DAO for the base Animals table.
 * Animals(AnimalID PK, Age, Happiness, ZooId FK, AnimalType)
 *
 * NOTE: This DAO only touches the Animals table itself.
 * Every animal also has a matching row in exactly one subtype table
 * (PREDATOR / FISH / PENGUIN) - see PredatorDAO / FishDAO / PenguinDAO
 * for the transactional insert/delete that keeps both tables in sync.
 */
public class AnimalDAO {

    public void searchAllAnimals() throws SQLException {
        String sql = "SELECT * FROM Animals";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("animalid") +
                        ", Age: " + rs.getInt("age") +
                        ", Happiness: " + rs.getInt("happiness") +
                        ", ZooId: " + rs.getInt("zooid") +
                        ", Type: " + rs.getString("animaltype"));
            }
        }
    }

    public void findAnimalById(int animalId) throws SQLException {
        String sql = "SELECT * FROM Animals WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, animalId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("ID: " + rs.getInt("animalid") +
                            ", Age: " + rs.getInt("age") +
                            ", Happiness: " + rs.getInt("happiness") +
                            ", ZooId: " + rs.getInt("zooid") +
                            ", Type: " + rs.getString("animaltype"));
                } else {
                    System.out.println("No animal found with AnimalID " + animalId);
                }
            }
        }
    }

    public void updateAnimalAge(int animalId, int newAge) throws SQLException {
        String sql = "UPDATE Animals SET Age = ? WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newAge);
            ps.setInt(2, animalId);
            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " row(s).");
        }
    }

    /**
     * Deletes ONLY the base Animals row.
     * WARNING: this will fail with a foreign key violation if the animal
     * still has a matching row in PREDATOR / FISH / PENGUIN.
     * Use PredatorDAO.deletePredator / FishDAO.deleteFish / PenguinDAO.deletePenguin
     * instead - they delete both rows together, in the correct order, in one transaction.
     */
    public void deleteAnimalOnly(int animalId) throws SQLException {
        String sql = "DELETE FROM Animals WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, animalId);
            int rows = ps.executeUpdate();
            System.out.println("Deleted " + rows + " row(s).");
        }
    }
}
