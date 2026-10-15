import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BloodBankInventoryDonorMatcher {

    static Scanner sc = new Scanner(System.in);

    static String[] bloodGroups = {
        "O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"
    };

    static int[] stock = new int[8];

    static ArrayList<Donor> donors = new ArrayList<>();

    static Path folder = Path.of("bloodbank-data");
    static Path donorFile = folder.resolve("donors.csv");
    static Path stockFile = folder.resolve("stock.csv");

    static Map<String, List<String>> compatibility = createCompatibility();

    public static void main(String[] args) {

        try {
            Files.createDirectories(folder);
            loadDonors();
            loadStock();
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        int choice;

        do {
            System.out.println("\n==================================");
            System.out.println(" BLOOD BANK INVENTORY AND DONOR MATCHER");
            System.out.println("==================================");
            System.out.println("1. Register Donor");
            System.out.println("2. View All Donors");
            System.out.println("3. Search Donor");
            System.out.println("4. Add Blood Stock");
            System.out.println("5. Issue Blood");
            System.out.println("6. View Blood Inventory");
            System.out.println("7. Find Compatible Donors");
            System.out.println("8. Sort Donors");
            System.out.println("9. Generate Reports");
            System.out.println("10. Save Data");
            System.out.println("0. Exit");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerDonor();
                    break;
                case 2:
                    viewDonors();
                    break;
                case 3:
                    searchDonor();
                    break;
                case 4:
                    addStock();
                    break;
                case 5:
                    issueBlood();
                    break;
                case 6:
                    viewStock();
                    break;
                case 7:
                    matchDonor();
                    break;
                case 8:
                    sortDonors();
                    break;
                case 9:
                    generateReport();
                    break;
                case 10:
                    saveData();
                    break;
                case 0:
                    saveData();
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 0);

        sc.close();
    }

    // CO1, CO2: Data types, conditions and logical operators
    static void registerDonor() {

        System.out.println("\n--- Donor Registration ---");

        String id = readText("Enter donor ID: ");

        if (findById(id).isPresent()) {
            System.out.println("Donor ID already exists.");
            return;
        }

        String name = readText("Enter donor name: ");
        String group = readBloodGroup();
        int age = readInt("Enter age: ");
        String phone = readText("Enter phone number: ");
        String city = readText("Enter city: ");

        if (age < 18 || age > 65) {
            System.out.println(
                "Warning: Age is outside the example screening range of 18-65."
            );
            System.out.println(
                "Registration is allowed for this academic demonstration."
            );
        }

        Donor donor = new Donor(id, name, group, age, phone, city);
        donors.add(donor);

        System.out.println("Donor registered successfully!");

        saveDonors();
    }

    // CO3: ArrayList and enhanced for loop
    static void viewDonors() {

        System.out.println("\n--- Registered Donors ---");

        if (donors.isEmpty()) {
            System.out.println("No donors registered.");
            return;
        }

        donors.forEach(System.out::println);
    }

    // Module 5 and Module 6: String methods, streams and filtering
    static void searchDonor() {

        String keyword = readText(
            "Enter donor ID, name, blood group or city: "
        ).toLowerCase(Locale.ROOT);

        List<Donor> results = donors.stream()
            .filter(d ->
                d.id.toLowerCase(Locale.ROOT).contains(keyword)
                || d.name.toLowerCase(Locale.ROOT).contains(keyword)
                || d.bloodGroup.toLowerCase(Locale.ROOT).equals(keyword)
                || d.city.toLowerCase(Locale.ROOT).contains(keyword)
            )
            .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No matching donors found.");
        } else {
            results.forEach(System.out::println);
        }
    }

    // CO1: Arithmetic operators and compound assignment
    static void addStock() {

        String group = readBloodGroup();
        int index = getIndex(group);
        int units = readInt("Enter units to add: ");

        if (units <= 0) {
            System.out.println("Units must be greater than zero.");
            return;
        }

        stock[index] += units;

        System.out.println("Blood stock updated successfully.");
        System.out.println("Available units: " + stock[index]);

        saveStock();
    }

    static void issueBlood() {

        String group = readBloodGroup();
        int index = getIndex(group);
        int units = readInt("Enter units to issue: ");

        if (units <= 0) {
            System.out.println("Units must be greater than zero.");
        } else if (units > stock[index]) {
            System.out.println("Insufficient blood stock.");
            System.out.println("Available units: " + stock[index]);
        } else {
            stock[index] -= units;

            System.out.println("Stock updated successfully.");
            System.out.println("Remaining units: " + stock[index]);

            saveStock();
        }
    }

    // CO3: Fixed arrays and loops
    static void viewStock() {

        System.out.println("\n--- Blood Inventory ---");

        for (int i = 0; i < bloodGroups.length; i++) {
            System.out.println(
                bloodGroups[i] + " : " + stock[i] + " units"
            );
        }

        int total = Arrays.stream(stock).sum();
        System.out.println("Total units available: " + total);
    }

    // CO2: Nested loops, if statements and compatibility matching
    static void matchDonor() {

        String recipientGroup = readBloodGroup();

        List<String> compatibleGroups =
            compatibility.get(recipientGroup);

        System.out.println(
            "\nCompatible red-cell donor groups for "
            + recipientGroup + ": " + compatibleGroups
        );

        boolean found = false;

        for (Donor donor : donors) {
            for (String group : compatibleGroups) {

                if (donor.bloodGroup.equals(group)) {
                    System.out.println(donor);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            System.out.println("No matching registered donors found.");
        }

        System.out.println("\nAvailable compatible inventory:");

        for (String group : compatibleGroups) {
            int index = getIndex(group);

            if (stock[index] > 0) {
                System.out.println(
                    group + " : " + stock[index] + " units"
                );
            }
        }

        System.out.println(
            "Matching is for educational use only. "
            + "Qualified blood-bank staff must verify compatibility."
        );
    }

    // Module 5: Comparable and Comparator
    static void sortDonors() {

        System.out.println("\n1. Sort by name");
        System.out.println("2. Sort by age");
        System.out.println("3. Sort by blood group");

        int choice = readInt("Choose sorting method: ");

        ArrayList<Donor> sorted = new ArrayList<>(donors);

        switch (choice) {
            case 1:
                Collections.sort(sorted);
                break;

            case 2:
                sorted.sort(
                    Comparator.comparingInt(d -> d.age)
                );
                break;

            case 3:
                sorted.sort(
                    Comparator.comparing((Donor d) -> d.bloodGroup)
                        .thenComparing(d -> d.name)
                );
                break;

            default:
                System.out.println("Invalid sorting choice.");
                return;
        }

        sorted.forEach(System.out::println);
    }

    // Module 6: Streams, groupingBy and counting
    static void generateReport() {

        System.out.println("\n--- Blood Bank Report ---");

        System.out.println("Total registered donors: " + donors.size());

        long eligibleAgeRange = donors.stream()
            .filter(d -> d.age >= 18 && d.age <= 65)
            .count();

        System.out.println(
            "Donors in sample age range (18-65): " + eligibleAgeRange
        );

        Map<String, Long> groupCounts = donors.stream()
            .collect(Collectors.groupingBy(
                d -> d.bloodGroup,
                Collectors.counting()
            ));

        System.out.println("\nDonors by blood group:");

        for (String group : bloodGroups) {
            System.out.println(
                group + " : " + groupCounts.getOrDefault(group, 0L)
            );
        }

        System.out.println("\nLow-stock groups (fewer than 5 units):");

        List<String> lowStock = Arrays.stream(bloodGroups)
            .filter(g -> stock[getIndex(g)] < 5)
            .collect(Collectors.toList());

        if (lowStock.isEmpty()) {
            System.out.println("No low-stock groups.");
        } else {
            lowStock.forEach(System.out::println);
        }

        System.out.println(
            "Total inventory units: " + Arrays.stream(stock).sum()
        );
    }

    // Module 5: File writing using Java NIO
    static void saveData() {
        saveDonors();
        saveStock();
        System.out.println("Data saved.");
    }

    static void saveDonors() {

        try {
            Files.createDirectories(folder);

            List<String> lines = new ArrayList<>();

            lines.add("ID,Name,BloodGroup,Age,Phone,City");

            for (Donor d : donors) {
                lines.add(
                    String.join(",",
                        csv(d.id),
                        csv(d.name),
                        csv(d.bloodGroup),
                        String.valueOf(d.age),
                        csv(d.phone),
                        csv(d.city)
                    )
                );
            }

            Files.write(donorFile, lines);

        } catch (IOException e) {
            System.out.println("Error saving donors: " + e.getMessage());
        }
    }

    static void saveStock() {

        try {
            Files.createDirectories(folder);

            List<String> lines = new ArrayList<>();

            lines.add("BloodGroup,Units");

            for (int i = 0; i < bloodGroups.length; i++) {
                lines.add(bloodGroups[i] + "," + stock[i]);
            }

            Files.write(stockFile, lines);

        } catch (IOException e) {
            System.out.println("Error saving stock: " + e.getMessage());
        }
    }

    // Module 5: Reading files with Files.lines and try-with-resources
    static void loadDonors() throws IOException {

        if (!Files.exists(donorFile)) {
            return;
        }

        try (Stream<String> lines = Files.lines(donorFile)) {

            lines.skip(1)
                .filter(line -> !line.trim().isEmpty())
                .forEach(line -> {

                    String[] data = line.split(",", -1);

                    if (data.length == 6) {
                        try {
                            if (findById(data[0]).isEmpty()) {
                                donors.add(new Donor(
                                    data[0],
                                    data[1],
                                    data[2],
                                    Integer.parseInt(data[3]),
                                    data[4],
                                    data[5]
                                ));
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid donor record.");
                        }
                    }
                });
        }
    }

    static void loadStock() throws IOException {

        if (!Files.exists(stockFile)) {
            return;
        }

        try (Stream<String> lines = Files.lines(stockFile)) {

            lines.skip(1).forEach(line -> {

                String[] data = line.split(",");

                if (data.length == 2) {
                    int index = getIndex(data[0]);

                    if (index >= 0) {
                        try {
                            int units = Integer.parseInt(data[1]);

                            if (units >= 0) {
                                stock[index] = units;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Skipping invalid stock record.");
                        }
                    }
                }
            });
        }
    }

    static Optional<Donor> findById(String id) {

        return donors.stream()
            .filter(d -> d.id.equalsIgnoreCase(id.trim()))
            .findFirst();
    }

    static String readBloodGroup() {

        while (true) {

            System.out.print(
                "Enter blood group (O-, O+, A-, A+, B-, B+, AB-, AB+): "
            );

            String group = sc.nextLine()
                .trim()
                .toUpperCase(Locale.ROOT);

            if (getIndex(group) >= 0) {
                return group;
            }

            System.out.println("Invalid blood group.");
        }
    }

    static int getIndex(String group) {

        for (int i = 0; i < bloodGroups.length; i++) {
            if (bloodGroups[i].equalsIgnoreCase(group.trim())) {
                return i;
            }
        }

        return -1;
    }

    static int readInt(String message) {

        while (true) {

            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    static String readText(String message) {

        while (true) {

            System.out.print(message);
            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    static String csv(String value) {
        return value.replace(",", " ");
    }

    // Simplified ABO/Rh red-cell compatibility lookup for classroom demonstration.
    static Map<String, List<String>> createCompatibility() {

        Map<String, List<String>> map = new LinkedHashMap<>();

        map.put("O-", Arrays.asList("O-"));

        map.put("O+", Arrays.asList(
            "O-", "O+"
        ));

        map.put("A-", Arrays.asList(
            "O-", "A-"
        ));

        map.put("A+", Arrays.asList(
            "O-", "O+", "A-", "A+"
        ));

        map.put("B-", Arrays.asList(
            "O-", "B-"
        ));

        map.put("B+", Arrays.asList(
            "O-", "O+", "B-", "B+"
        ));

        map.put("AB-", Arrays.asList(
            "O-", "A-", "B-", "AB-"
        ));

        map.put("AB+", Arrays.asList(
            "O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"
        ));

        return Collections.unmodifiableMap(map);
    }

    // CO3: Custom class, encapsulation and Comparable interface
    static class Donor implements Comparable<Donor> {

        String id;
        String name;
        String bloodGroup;
        int age;
        String phone;
        String city;

        Donor(String id, String name, String bloodGroup,
              int age, String phone, String city) {

            this.id = id;
            this.name = name;
            this.bloodGroup = bloodGroup;
            this.age = age;
            this.phone = phone;
            this.city = city;
        }

        @Override
        public int compareTo(Donor other) {
            return this.name.compareToIgnoreCase(other.name);
        }

        @Override
        public String toString() {

            return "ID: " + id
                + " | Name: " + name
                + " | Blood Group: " + bloodGroup
                + " | Age: " + age
                + " | Phone: " + phone
                + " | City: " + city;
        }
    }
}