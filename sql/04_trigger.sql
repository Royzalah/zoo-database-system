-- ============================================================
-- TRIGGER: Automatically increase an animal's happiness
-- whenever it receives a medical treatment.
-- Rationale: A treated animal is cared for, so its happiness
-- should reflect that automatically, without any extra code
-- in the Java program.
-- ============================================================

-- STEP 1: Create the function that holds the trigger's logic.
-- This function runs UPDATE Animals, raising Happiness by 5,
-- for the specific animal that was just treated.
-- NEW.AnimalID refers to the AnimalID column of the new row
-- that was just inserted into MEDICAL_TREATMENT.
CREATE OR REPLACE FUNCTION increase_happiness_after_treatment()
RETURNS TRIGGER AS
$$
BEGIN
    UPDATE Animals
    SET Happiness = Happiness + 5
    WHERE AnimalID = NEW.AnimalID;

    RETURN NEW;
END
$$ LANGUAGE 'plpgsql';

-- STEP 2: Create the trigger itself.
-- Event: AFTER INSERT (runs after a new row is added)
-- Table: MEDICAL_TREATMENT (the trigger "watches" this table)
-- FOR EACH ROW: runs once per inserted row (not once per statement)
-- EXECUTE PROCEDURE: calls the function defined in Step 1
CREATE TRIGGER trig_after_treatment
AFTER INSERT
ON MEDICAL_TREATMENT
FOR EACH ROW
EXECUTE PROCEDURE increase_happiness_after_treatment();

-- STEP 3: Check the happiness value BEFORE the trigger fires.
-- (Animal 3 is a fish with Happiness = 80 from the seed data.)
SELECT Happiness FROM Animals WHERE AnimalID = 3;

-- STEP 4: Insert a new treatment for animal 3.
-- This INSERT is the event that fires the trigger automatically.
-- We did not write any code to update Happiness ourselves --
-- the trigger does it on its own.
INSERT INTO MEDICAL_TREATMENT (TreatmentID, TreatmentDescription, TreatmentDate, AnimalID)
VALUES (6, 'Routine checkup', '2026-06-20', 3);

-- STEP 5: Check the happiness value AFTER the trigger fires.
-- Expected result: 85 (80 + 5), proving the trigger ran
-- automatically as soon as the INSERT happened.
SELECT Happiness FROM Animals WHERE AnimalID = 3;