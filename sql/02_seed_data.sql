INSERT INTO ZooTable (ZooId, ZooName, City, Street, NUMBER)
VALUES (1, 'TVL Zoo', 'Tel Aviv', 'Bazel', '22');

                                                    -- add animals --

                    -- add Predator --
INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (1, 5, 100, 1, 'Predator');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (8, 8, 75, 1, 'Predator');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (2, 4, 95, 1, 'Predator');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (10, 3, 88, 1, 'Predator');

                    -- add Fish --
INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (3, 2, 80, 1, 'Fish');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (4, 1, 90, 1, 'Fish');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (5, 3, 85, 1, 'Fish');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (11, 2, 92, 1, 'Fish');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (12, 1, 85, 1, 'Fish');

                    -- add Penguin --
INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (6, 2, 100, 1, 'Penguin');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (9, 2, 90, 1, 'Penguin');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (7, 6, 100, 1, 'Penguin');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (13, 5, 95, 1, 'Penguin');

INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType)
VALUES (14, 4, 100, 1, 'Penguin');


                                                -- data of the animals --
                    -- data of the PREDATOR --

INSERT INTO PREDATOR (AnimalID, PredatorName, PredatorWeight, PredatorGender, PredatorSpecies)
VALUES (1, 'Simba', 190.5, 'MALE', 'Lion');

INSERT INTO PREDATOR (AnimalID, PredatorName, PredatorWeight, PredatorGender, PredatorSpecies)
VALUES (2, 'Shere Khan', 220.4, 'MALE', 'Tiger');

INSERT INTO PREDATOR (AnimalID, PredatorName, PredatorWeight, PredatorGender, PredatorSpecies)
VALUES (8, 'Nala', 150.2, 'FEMALE', 'Lion');

INSERT INTO PREDATOR (AnimalID, PredatorName, PredatorWeight, PredatorGender, PredatorSpecies)
VALUES (10, 'Bagheera', 210.5, 'MALE', 'Tiger');

                    -- data of the FISH --

INSERT INTO FISH (AnimalID, FishLength, FishPattern, FishSpecies)
VALUES (3, 12.5, 'SMOOTH', 'GoldFish');

INSERT INTO FISH (AnimalID, FishLength, FishPattern, FishSpecies)
VALUES (4, 30.5, 'STRIPED', 'Clownfish');

INSERT INTO FISH (AnimalID, FishLength, FishPattern, FishSpecies)
VALUES (5, 45.0, 'SPOTTED', 'Leopard Shark');

INSERT INTO FISH (AnimalID, FishLength, FishPattern, FishSpecies)
VALUES (11, 22.3, 'SPOTTED', 'Blue Tang');

INSERT INTO FISH (AnimalID, FishLength, FishPattern, FishSpecies)
VALUES (12, 8.5, 'STRIPED', 'Zebra Danio');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (3, 'GOLD');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (4, 'ORANGE');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (4, 'WHITE');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (5, 'GREY');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (5, 'BLACK');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (11, 'BLUE');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (11, 'YELLOW');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (12, 'BLACK');

INSERT INTO FISH_COLORS (AnimalID, FishColor)
VALUES (12, 'WHITE');

                -- data of the PENGUIN --

INSERT INTO PENGUIN (AnimalID, PenguinName, PenguinHeight, PenguinRank)
VALUES (6, 'Rico', 45.2, 1);

INSERT INTO PENGUIN (AnimalID, PenguinName, PenguinHeight, PenguinRank)
VALUES (7, 'Skipper', 50.1, 2);

INSERT INTO PENGUIN (AnimalID, PenguinName, PenguinHeight, PenguinRank)
VALUES (9, 'Kowalski', 48.7, 3);

INSERT INTO PENGUIN (AnimalID, PenguinName, PenguinHeight, PenguinRank)
VALUES (13, 'Private', 42.1, 4);

INSERT INTO PENGUIN (AnimalID, PenguinName, PenguinHeight, PenguinRank)
VALUES (14, 'Mumble', 46.8, 5);

                                    -- clinic data --

INSERT INTO MEDICAL_TREATMENT (TreatmentID, TreatmentDescription, TreatmentDate, AnimalID)
VALUES (1, 'Annual checkup', '2026-01-15', 1);

INSERT INTO MEDICAL_TREATMENT (TreatmentID, TreatmentDescription, TreatmentDate, AnimalID)
VALUES (2, 'Dental cleaning', '2026-03-10', 2);

INSERT INTO MEDICAL_TREATMENT (TreatmentID, TreatmentDescription, TreatmentDate, AnimalID)
VALUES (3, 'Fin repair surgery', '2026-04-02', 4);

INSERT INTO MEDICAL_TREATMENT (TreatmentID, TreatmentDescription, TreatmentDate, AnimalID)
VALUES (4, 'Vitamin booster shot', '2026-05-20', 7);

INSERT INTO MEDICAL_TREATMENT (TreatmentID, TreatmentDescription, TreatmentDate, AnimalID)
VALUES (5, 'Weight check & vitamin diet', '2026-06-01', 10);