package com.example.zoo.db;

import java.sql.*;

/**
 * DAO for MEDICAL_TREATMENT.
 * MEDICAL_TREATMENT(TreatmentID PK, TreatmentDescription, TreatmentDate, AnimalID FK -> Animals)
 *
 * NOTE: No manual transaction needed here - inserting a treatment only
 * touches ONE table from Java's point of view. The trig_after_treatment
 * trigger (PL/pgSQL, AFTER INSERT ON MEDICAL_TREATMENT) automatically
 * raises the animal's Happiness by 5 inside the SAME database transaction
 * as the INSERT itself, so it is always atomic even without extra code here.
 */
public class MedicalTreatmentDAO {

    public void insertTreatment(int treatmentId, String description, Date treatmentDate, int animalId) throws SQLException {
        String sql = "INSERT INTO MEDICAL_TREATMENT (TreatmentID, TreatmentDescription, TreatmentDate, AnimalID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, treatmentId);
            ps.setString(2, description);
            ps.setDate(3, treatmentDate);
            ps.setInt(4, animalId);
            ps.executeUpdate();
            System.out.println("Treatment inserted (trigger raised Happiness by 5 automatically).");
        }
    }

    public void findTreatmentsByAnimal(int animalId) throws SQLException {
        String sql = "SELECT * FROM MEDICAL_TREATMENT WHERE AnimalID = ? ORDER BY TreatmentDate";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, animalId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getInt("treatmentid") + " - " +
                            rs.getString("treatmentdescription") + " on " +
                            rs.getDate("treatmentdate"));
                }
            }
        }
    }

    public void deleteTreatment(int treatmentId) throws SQLException {
        String sql = "DELETE FROM MEDICAL_TREATMENT WHERE TreatmentID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, treatmentId);
            int rows = ps.executeUpdate();
            System.out.println("Deleted " + rows + " row(s).");
        }
    }
}
