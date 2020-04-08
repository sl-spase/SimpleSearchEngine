package search;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Strategy strategy = new Strategy(args);
        strategy.execute();
    }

}

class Strategy {

    private SearchingStrategy searchingStrategy;
    private ArrayList<String> people;
    private final List<String> suitableStrategy = List.of("ALL", "ANY", "NONE");

    public Strategy(String[] args) {
        people = readDataFromFile(args[1]);

    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int menuOption = Integer.parseInt(scanner.nextLine());
            switch (menuOption) {
                case 1:
                    findPerson(scanner, people);
                    break;
                case 2:
                    pintAllPeople(people);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Incorrect option! Try again.");
                    break;
            }
        }
    }

    private void findPerson(Scanner scanner, ArrayList<String> people) {


        System.out.println("\nSelect a matching strategy: ALL, ANY, NONE");

        String strategyInput = scanner.nextLine();

        while (!suitableStrategy.contains(strategyInput)) {
            System.out.println("Typo! Try again!");
            strategyInput = scanner.nextLine();
        }

        System.out.println("\nEnter a name or email to search all suitable people.");

        String searchPeople = scanner.nextLine().toLowerCase();

        if ("ALL".equals(strategyInput)) {
            searchingStrategy = new AllStrategy();
        } else if ("ANY".equals(strategyInput)) {
            searchingStrategy = new AnyStrategy();
        } else if ("NONE".equals(strategyInput)) {
            searchingStrategy = new NoneStrategy();
        }

        searchingStrategy.process(people, searchPeople);
    }

    public void pintAllPeople(ArrayList<String> people) {
        System.out.println("=== List of people ===");
        for (String str : people) {
            System.out.println(str);
        }
    }

    public void printMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
    }

    @NotNull
    private ArrayList<String> readDataFromFile(String arg) {
        File file = new File(arg);

        ArrayList<String> people = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String inputLine = scanner.nextLine();
                people.add(inputLine);
            }
        } catch (Exception e) {
            System.out.println("No such file!");
        }
        return people;
    }
}


interface SearchingStrategy {
    void process(List<String> people, String search);

    static void fillPeopleMap(List<String> people, Map<String, List<Integer>> peopleMap) {
        for (int i = 0; i < people.size(); i++) {
            for (String str : people.get(i).split("\\s+")) {
                str = str.toLowerCase();
                if (peopleMap.containsKey(str)) {
                    peopleMap.get(str).add(i);
                } else {
                    ArrayList<Integer> arrayList = new ArrayList<>();
                    arrayList.add(i);
                    peopleMap.put(str, arrayList);
                }
            }
        }
    }

    Map<String, List<Integer>> peopleMap = new HashMap<>();
}

class AllStrategy implements SearchingStrategy {

    @Override
    public void process(List<String> people, String search) {

        SearchingStrategy.fillPeopleMap(people, peopleMap);

        List<Integer> arrayForFirstLine = null;

        String[] searchArray = search.split("\\s+");
        int lenSearchArray = searchArray.length;

        if (peopleMap.containsKey(searchArray[0])) {
            arrayForFirstLine = peopleMap.get(searchArray[0]);
        }
        List<Integer> arrayAfterFirstLine = null;
        for (int i = 1; lenSearchArray > 1 && i < lenSearchArray; i++) {
            if (peopleMap.containsKey(searchArray[i])) {
                arrayAfterFirstLine = peopleMap.get(searchArray[i]);
                if (arrayForFirstLine != null) {
                    arrayAfterFirstLine.retainAll(arrayForFirstLine);
                }
            }
        }

        if (arrayForFirstLine != null && arrayAfterFirstLine == null) {
            for (Integer integer : arrayForFirstLine) {
                System.out.println(people.get(integer));
            }
            arrayForFirstLine.clear();
        } else if (arrayAfterFirstLine == null) {
            System.out.println("No match found!");
        } else {
            for (Integer integer : arrayAfterFirstLine) {
                System.out.println(people.get(integer));
            }
            arrayAfterFirstLine.clear();
        }
    }
}

class AnyStrategy implements SearchingStrategy {

    public Map<String, List<Integer>> peopleMap = new HashMap<>();

    @Override
    public void process(List<String> people, String search) {
        SearchingStrategy.fillPeopleMap(people, peopleMap);
        String[] searchArray = search.toLowerCase().split("\\s+");
        List<Integer> outputArray;
        Set<Integer> outputSet = new HashSet<>();

        for (String s : searchArray) {
            if (peopleMap.containsKey(s)) {
                outputArray = peopleMap.get(s);
                outputSet.addAll(outputArray);
            }
        }
        for (Integer integer : outputSet) {
            System.out.println(people.get(integer));
        }
        outputSet.clear();
    }
}

class NoneStrategy implements SearchingStrategy {

    private List<String> results = new ArrayList<>();

    @Override
    public void process(List<String> people, String search) {
        SearchingStrategy.fillPeopleMap(people, peopleMap);

        String[] searchArray = search.toLowerCase().split("\\s+");

        Set<Integer> set = new HashSet<>();
        for (String str : searchArray) {
            if (peopleMap.containsKey(str.toLowerCase())) {
                List<Integer> list = peopleMap.get(str);
                set.addAll(list);
            }
        }

        for (int i = 0; i < people.size(); i++) {
            if (!set.contains(i)) {
                results.add(people.get(i));
            }
        }

        results.forEach(System.out::println);
    }
}

