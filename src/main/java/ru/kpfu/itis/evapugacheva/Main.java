package ru.kpfu.itis.evapugacheva;

import java.io.*;
import java.util.*;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) {
        int count_lists = 100;
        String filesPrefix = "output/random_list";
        String filesPostfix = ".txt";
        createRandomLists(count_lists, filesPrefix, filesPostfix);

        List<TestResult> results = new ArrayList<>(count_lists * 2);
        for (int i = 1; i <= count_lists; i++) {
            results.add(testSort(filesPrefix + i + filesPostfix));
        }

        saveTestResultsToFile(results, "benchmark_results.csv");
    }

    public static void saveTestResultsToFile(List<TestResult> testResults, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("listLength iterations timeInMs\n");

            for (TestResult result : testResults) {
                writer.write(result.getListLength() + " " + result.getIterations() + " " + result.getTimeInMs() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static TestResult testSort(String filename) {
        List<Integer> list = deserializeList(filename);

        long startTime = System.nanoTime();
        StrandSort.sort(list);
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        long timeInMs = duration / 1_000_000;

        return new TestResult(timeInMs, list.size(), StrandSort.getIterations());


    }

    private static void createRandomLists(int count, String filesPrefix, String filesPostfix) {
        for (int i = 1; i <= count; i++) {
            String filename = filesPrefix + i + filesPostfix;
            File file = new File(filename);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            serializeList(generateRandomList(), filename);
        }
    }

    private static List<Integer> generateRandomList() {
        int size = random.nextInt(9901) + 100;

        List<Integer> list = new LinkedList<>();
        for (int j = 0; j < size; j++) {
            list.add(random.nextInt());
        }

        return list;
    }

    public static void serializeList(List<Integer> list, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename, false))) {
            oos.writeObject(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> deserializeList(String filename) {
        List<Integer> list;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            list = (List<Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    public static class TestResult {
        private long timeInMs;
        private int listLength;
        private int iterations;

        public TestResult(long timeInMs, int listLength, int iterations) {
            this.timeInMs = timeInMs;
            this.listLength = listLength;
            this.iterations = iterations;
        }

        public long getTimeInMs() {
            return timeInMs;
        }

        public int getListLength() {
            return listLength;
        }

        public int getIterations() {
            return iterations;
        }
    }
}