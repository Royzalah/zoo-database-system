-- 1. Each predator's name + happiness level
SELECT p.PredatorName, a.Happiness
FROM Animals a
JOIN PREDATOR p ON p.AnimalID = a.AnimalID;

-- 2. Each fish + its colors
SELECT f.FishSpecies, fc.FishColor
FROM FISH f
JOIN FISH_COLORS fc  ON fc.AnimalID = f.AnimalID;

-- 3. Each animal that received medical treatment + treatment description
SELECT a.AnimalID , a.AnimalType , mt.TreatmentDescription , mt.TreatmentDate
FROM Animals a
JOIN MEDICAL_TREATMENT mt ON mt.AnimalID = a.AnimalID;

-- 4. Number of animals per type
SELECT AnimalType, COUNT(*) AS num_animals
FROM Animals
GROUP BY AnimalType;

-- 5. Average age per animal type
SELECT AnimalType, AVG(Age) AS avg_age
FROM Animals
GROUP BY AnimalType;

-- 6. Number of colors per fish
SELECT f.AnimalID, f.FishSpecies, COUNT(fc.FishColor) AS num_colors
FROM FISH f
JOIN FISH_COLORS fc ON fc.AnimalID = f.AnimalID
GROUP BY f.AnimalID, f.FishSpecies;

-- 7. Happiest animals (Happiness above 90)
SELECT AnimalID, AnimalType, Happiness
FROM Animals
WHERE Happiness > 90
ORDER BY Happiness DESC;

-- 8. The heaviest predator
SELECT p.PredatorName, p.PredatorWeight
FROM PREDATOR p
ORDER BY p.PredatorWeight DESC
LIMIT 1;

-- 9. Penguins ordered by rank
SELECT PenguinName, PenguinRank
FROM PENGUIN
ORDER BY PenguinRank;

-- 10. Animal type with the highest average age
SELECT AnimalType, AVG(Age) AS avg_age
FROM Animals
GROUP BY AnimalType
ORDER BY avg_age DESC
LIMIT 1;