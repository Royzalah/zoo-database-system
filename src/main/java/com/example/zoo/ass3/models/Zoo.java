package com.example.zoo.ass3.models;

import com.example.zoo.ass3.exceptions.*;
import com.example.zoo.ass3.general.Address;
import com.example.zoo.ass3.general.FoodSummary;
import com.example.zoo.ass3.general.enums.*;

import java.io.Serializable;
import java.util.*;

import static com.example.zoo.ass3.general.DataUtils.*;

public class Zoo implements Serializable {
    private final String name;
    private final Address address;
    private final Map<String, List<Animal>> animals;
    private final VeterinaryClinic<Animal> veterinaryClinic;

    public Zoo(String name, Address address) {
        this.name = name;
        this.address = address;
        animals = new HashMap<>();
        animals.put(Predator.class.getSimpleName(), new ArrayList<>());
        animals.put(Fish.class.getSimpleName(), new ArrayList<>());
        animals.put(Penguin.class.getSimpleName(), new ArrayList<>());
        veterinaryClinic = new VeterinaryClinic<>();
    }

    public void addPenguin(String name, int age, double height) throws GeneralException {
        if (!isValidMinimumPhysicalSize(age) || !isValidMinimumPhysicalSize(height)) {
            throw new PenguinHeightException();
        }
        addPenguin(new Penguin(name, age, height));
    }

    public void addPenguin(Penguin penguin) throws PenguinHeightException {
        Penguin leader = getPenguinsLeader();

        if (leader == null) {
            animals.get(Penguin.class.getSimpleName()).add(penguin);
            return;
        }

        if (penguin.getHeight() > leader.getHeight()) {
            throw new PenguinHeightException();
        }

        leader.addPenguin(penguin);
    }

    private Penguin getPenguinsLeader() {
        List<Animal> list = animals.get(Penguin.class.getSimpleName());
        if (list == null || list.isEmpty()) {
            return null;
        }
        return (Penguin) animals.get(Penguin.class.getSimpleName()).get(0);
    }

    public void addPredator(String name, int age, double weight, String gender, PredatorsTypes type) throws GeneralException {
        if (!isValidMinimumPhysicalSize(age)) {
            throw new AgeException();
        }
        if (!isValidMinimumPhysicalSize(weight)) {
            throw new WeightException();
        }
        Gender g = getGender(gender);
        if (g == null) {
            throw new GenderException();
        }
        switch (type) {
            case Lion -> addPredator(new Lion(name, age, weight, g));
            case Tiger -> addPredator(new Tiger(name, age, weight, g));
        }
    }

    public void addPredator(Predator predator) {
        animals.get(Predator.class.getSimpleName()).add(predator);
    }

    public void addFish(int age, double length, String patternStr, String[] colorsStr, FishTypes type) throws GeneralException {
        Color[] colors;
        switch (type) {
            case AquariumFish -> {
                Pattern pattern = getPattern(patternStr);
                if (pattern == null) {
                    throw new PatternException();
                }
                colors = createColorArr(colorsStr);
                animals.get(Fish.class.getSimpleName()).add(new AquariumFish(age, length, pattern, colors));
            }
            case ClownFish -> {
                colors = createColorArr(colorsStr);
                animals.get(Fish.class.getSimpleName()).add(new ClownFish(age, length, colors));
            }
            case GoldFish -> {
                colors = createColorArr(colorsStr);
                animals.get(Fish.class.getSimpleName()).add(new GoldFish(age, length, colors[0]));
            }
        }
    }

    private Color[] createColorArr(String[] colorsStr) throws ColorNotExistException {
        if (colorsStr == null || colorsStr.length == 0) {
            throw new ColorNotExistException();
        }
        Color[] colors = new Color[colorsStr.length];
        Color temp;
        for (int i = 0; i < colorsStr.length; i++) {
            temp = getColor(colorsStr[i]);
            if (temp == null) {
                throw new ColorNotExistException();
            }
            colors[i] = temp;
        }
        return colors;
    }

    public List<FoodSummary> feedAnimals() {
        Map<String, FoodSummary> totals = new HashMap<>();

        for (List<Animal> animalList : animals.values()) {
            if (animalList == null) continue;
            for (Animal a : animalList){
                String type = a.getClass().getSimpleName();
                double eaten = a.feed();
                a.setHappiness(100);

                String unit =
                        (a instanceof Penguin)  ? "fish" :
                        (a instanceof Predator) ? "Kg of meat" :
                        (a instanceof Fish)     ? "dish of fish food" :
                                                "units";

                FoodSummary fs = totals.computeIfAbsent(type, k -> new FoodSummary(type, 0.0, unit));
                fs.setAmount(set2Digits(fs.getAmount() + eaten));
            }
        }

        return new ArrayList<>(totals.values());
    }

    public String showAnimalsSounds() {
        StringBuilder sb = new StringBuilder();
        for (List<Animal> list : animals.values()) {
            for (Animal animal : list) {
                sb.append(animal.makeNoise());
            }
        }
        return sb.toString();
    }

    private int getNumOfPenguins(Penguin penguin) {
        if (penguin == null) {
            return 0;
        }
        return 1 + getNumOfPenguins(penguin.getNext());
    }

    public List<String> increasingAgeOneYear() {
        List<String> results = new ArrayList<>();
        Random random = new Random();
        results.add("All Animals was added one year..");

        for (String type : animals.keySet()) {
            List<Animal> newList = new ArrayList<>();

            for (Animal animal : animals.get(type)) {
                if (animal instanceof Penguin leader) {
                    Penguin updatedLeader = processPenguinChain(leader, random, results);

                    if (updatedLeader != null) {
                        updatedLeader.setLeader(true);
                        newList.add(updatedLeader);
                    }
                    continue;
                }

                if (updateAndCheckSurvival(animal, random, results)) {
                    newList.add(animal);
                }
            }

            animals.put(type, newList);
        }
        return results;
    }

    private boolean updateAndCheckSurvival(Animal animal, Random random, List<String> results) {
        animal.ageOneYear();

        if (animal.getAge() > animal.getMaxAge()) {
            results.add(animal + " was deleted");
            return false;
        }

        animal.setHappiness(animal.getHappiness() - random.nextInt(15, 31));
        if (animal.getHappiness() <= 0) {
            results.add(animal + " was deleted");
            return false;
        }

        return true;
    }

    private Penguin processPenguinChain(Penguin leader, Random random, List<String> results) {
        Penguin newLeader = null;
        Penguin tail = null;

        for (Penguin p = leader; p != null; ) {
            Penguin next = p.getNext();
            if (!updateAndCheckSurvival(p, random, results)) {
                p = next;
                continue;
            }

            p.setNext(null);
            p.setLeader(false);

            if (newLeader == null) {
                newLeader = p;
            } else {
                tail.setNext(p);
            }
            tail = p;

            p = next;
        }

        // new leader
        if (newLeader != null) {
            newLeader.setLeader(true);
        }

        return newLeader; // may be null if all penguins died
    }

    public void init() throws GeneralException {
        Penguin penguin = new Penguin("Private", 6, 200f);
        addPenguin(penguin);
        penguin = new Penguin("Kowalski", 2, 51.5);
        addPenguin(penguin);
        penguin = new Penguin("Skipper", 5, 162.8);
        addPenguin(penguin);
        veterinaryClinic.addAnimal(penguin);
        veterinaryClinic.addTreatment(penguin, new MedicalTreatment("Antibiotic treatment for infection"));
        Lion lion = new Lion("Pini", 5, 80, Gender.MALE);
        addPredator(lion);
        lion = new Lion("Simba", 12, 180, Gender.MALE);
        addPredator(lion);
        lion = new Lion("Adi Himelbloy", 3, 50, Gender.FEMALE);
        addPredator(lion);
        veterinaryClinic.addAnimal(lion);
        veterinaryClinic.addTreatment(lion, new MedicalTreatment("Dental check-up"));
        lion = new Lion("Shaked", 15, 200, Gender.FEMALE);
        addPredator(lion);
        Tiger tiger = new Tiger("Rita", 5, 80, Gender.FEMALE);
        addPredator(tiger);
        tiger = new Tiger("Rami", 9, 120, Gender.MALE);
        veterinaryClinic.addAnimal(tiger);
        veterinaryClinic.addTreatment(tiger, new MedicalTreatment("Routine health examination"));
        addPredator(tiger);
        addFish(10);

    }

    private void addFish(int amount) throws GeneralException {
        Random r = new Random();
        for (int i = 0; i < amount; i++) {
            createAqFish(r);
        }
    }

    public String createFish(int amount) throws GeneralException {
        Random r = new Random();
        FishTypes fishTypes = FishTypes.values()[r.nextInt(FishTypes.values().length)];
        switch (fishTypes) {
            case AquariumFish -> {
                for (int i = 0; i < amount; i++) {
                    createAqFish(r);
                }
            }
            case ClownFish -> {
                for (int i = 0; i < amount; i++) {
                    createClownFish(r);
                }
            }
            case GoldFish -> {
                for (int i = 0; i < amount; i++) {
                    createGoldFish(r);
                }
            }
        }
        return amount + " fish created successfully";
    }

    private void createAqFish(Random r) throws GeneralException {
        double length;
        Color[] colors;
        int numOfColors;
        int age;
        Pattern pattern;
        int patternSize = Pattern.values().length, colorSize = Color.values().length;
        age = r.nextInt(15) + 1;
        length = set2Digits(r.nextDouble(4) + 1);
        pattern = Pattern.values()[r.nextInt(patternSize)];
        numOfColors = r.nextInt(4) + 1;
        colors = new Color[numOfColors];
        Color color;
        boolean colorExist;
        for (int j = 0; j < numOfColors; j++) {
            do {
                color = Color.values()[r.nextInt(colorSize)];
                colorExist = colorExist(colors, j, color);
            } while (colorExist);
            colors[j] = color;
        }
        List<Animal> fish = animals.get(Fish.class.getSimpleName());
        if (fish == null) {
            fish = new ArrayList<>();
        }
        fish.add(new AquariumFish(age, length, pattern, colors));
        animals.put(Fish.class.getSimpleName(), fish);
    }

    private void createClownFish(Random r) throws GeneralException {
        double length;
        Color[] colors;
        int numOfColors;
        int age;
        age = r.nextInt(15) + 1;
        length = set2Digits(r.nextDouble(4) + 1);
        numOfColors = r.nextInt(2) + 1;
        colors = new Color[numOfColors];
        Color color;
        boolean colorExist;
        for (int j = 0; j < numOfColors; j++) {
            do {
                color = ClownFish.AVAILABLE_COLORS[r.nextInt(ClownFish.AVAILABLE_COLORS.length)];
                colorExist = colorExist(colors, j, color);
            } while (colorExist);
            colors[j] = color;
        }
        List<Animal> fish = animals.get(Fish.class.getSimpleName());
        if (fish == null) {
            fish = new ArrayList<>();
        }
        fish.add(new ClownFish(age, length, colors));
        animals.put(Fish.class.getSimpleName(), fish);
    }

    private void createGoldFish(Random r) throws GeneralException {
        double length;
        int age;
        age = r.nextInt(15) + 1;
        length = set2Digits(r.nextDouble(4) + 1);
        Color color = GoldFish.AVAILABLE_COLORS[r.nextInt(GoldFish.AVAILABLE_COLORS.length)];
        List<Animal> fish = animals.get(Fish.class.getSimpleName());
        if (fish == null) {
            fish = new ArrayList<>();
        }
        fish.add(new GoldFish(age, length, color));
        animals.put(Fish.class.getSimpleName(), fish);
    }

    public String getZooTitle() {
        return name + ", " + address;
    }

    public int[] getPredatorSummary() {
        int[] numOfPredators = new int[PredatorsTypes.values().length];
        List<Animal> predators = animals.get(Predator.class.getSimpleName());
        if (predators != null && !predators.isEmpty()) {
            for (Animal animal : predators) {
                numOfPredators[PredatorsTypes.valueOf(animal.getClass().getSimpleName()).ordinal()]++;
            }
        }
        return numOfPredators;
    }

    public int getPenguinAmount() {
        Penguin leader = getPenguinsLeader();
        if (leader == null) {
            return 0;
        }
        return getNumOfPenguins(leader);
    }

    public int[] getFishSummary() {
        int[] numOfFish = new int[FishTypes.values().length];
        List<Animal> fish = animals.get(Fish.class.getSimpleName());
        if (fish != null && !fish.isEmpty()) {
            for (Animal animal : fish) {
                numOfFish[FishTypes.valueOf(animal.getClass().getSimpleName()).ordinal()]++;
            }
        }
        return numOfFish;
    }

    public Map<PredatorsTypes, List<Predator>> getPredators() {
        Map<PredatorsTypes, List<Predator>> predatorsMap = new HashMap<>();
        List<Animal> predators = animals.get(Predator.class.getSimpleName());
        if (predators != null && !predators.isEmpty()) {
            for (Animal animal : predators) {
                PredatorsTypes type = PredatorsTypes.valueOf(animal.getClass().getSimpleName());
                predatorsMap.computeIfAbsent(type, k -> new ArrayList<>());
                predatorsMap.get(type).add((Predator) animal);
                predatorsMap.put(type, predatorsMap.get(type));
            }
        }
        return predatorsMap;
    }

    public Map<FishTypes, List<Fish>> getFish() {
        Map<FishTypes, List<Fish>> fishMap = new HashMap<>();
        List<Animal> fish = animals.get(Fish.class.getSimpleName());
        if (fish != null && !fish.isEmpty()) {
            for (Animal animal : fish) {
                FishTypes type = FishTypes.valueOf(animal.getClass().getSimpleName());
                fishMap.computeIfAbsent(type, k -> new ArrayList<>());
                fishMap.get(type).add((Fish) animal);
                fishMap.put(type, fishMap.get(type));
            }
        }
        return fishMap;
    }

    public String getDominantColors() {
        int[] dominantColors = new int[Color.values().length];
        List<Animal> fish = animals.get(Fish.class.getSimpleName());
        if (fish != null && !fish.isEmpty()) {
            for (Animal animal : fish) {
                updateDominantColors(dominantColors, ((Fish) animal).getColors());
            }
        }
        Color[] colors = Color.values();
        int[] maxIndex = {-1, -1};
        updateMaxIndex(dominantColors, maxIndex);
        String res = "";
        if (maxIndex[0] != -1) {
            res += colors[maxIndex[0]];
            if (maxIndex[1] != -1) {
                res += "," + colors[maxIndex[1]];
            }
        }
        return res;
    }

    private void updateMaxIndex(int[] dominantColors, int[] maxIndex) {
        int max1 = -1, max2 = -1;
        for (int i = 0; i < dominantColors.length; i++) {
            if (dominantColors[i] > max1) {
                maxIndex[1] = maxIndex[0];
                maxIndex[0] = i;
                max2 = max1;
                max1 = dominantColors[i];
            } else if (dominantColors[i] > max2) {
                maxIndex[1] = i;
                max2 = dominantColors[i];
            }
        }
    }

    private void updateDominantColors(int[] dominantColors, Color[] colors) {
        for (Color color : colors) {
            dominantColors[color.ordinal()]++;
        }
    }

    public List<Penguin> getPenguins(String sortBy) {
        List<Penguin> penguins = new ArrayList<>();
        Penguin leader = getPenguinsLeader();
        Penguin temp = leader;
        while (temp != null) {
            Penguin copy = new Penguin(temp);
            copy.setLeader(temp == leader);
            penguins.add(copy);
            temp = temp.getNext();
        }

        List<Penguin> sorted = new ArrayList<>(penguins); // copy

        switch (sortBy.toLowerCase()) {
            case "name" -> sorted.sort(new ComparePenguinByName()); // A -> Z
            case "age" -> sorted.sort(new ComparePenguinByAge()); // ascending
            case "height" -> sorted.sort(null); // natural order (Comparable)
            default -> {}
        }

        return sorted;
    }

    public Map<String, Object> veterinarySummary() {
        Map<String, Object> summary = new HashMap<>();
        Map<String, Integer> byType = new HashMap<>();

        Set<Animal> allAnimals = (veterinaryClinic != null) ? veterinaryClinic.getAllAnimals() : null;
        if (allAnimals == null || allAnimals.isEmpty()) {
            summary.put("totalSick", 0);
            summary.put("byType", byType);
            return summary;
        }

        for (Animal a : allAnimals) {
            String type = a.getClass().getSimpleName();
            byType.compute(type, (k, count) -> (count == null) ? 1 : count + 1);
        }

        summary.put("totalSick", allAnimals.size());
        summary.put("byType", byType);
        return summary;
    }

    public Map<Animal, List<MedicalTreatment>> getVeterinaryClinic() {
        return veterinaryClinic.getRecords();
    }
}
