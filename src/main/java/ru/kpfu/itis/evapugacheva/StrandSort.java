package ru.kpfu.itis.evapugacheva;

import java.util.LinkedList;
import java.util.List;

final class StrandSort {
    private static LinkedList<Integer> resultList;
    private static int k;
    private static int iterations;

    public static List<Integer> sort(List<Integer> inputList) {
        resultList = new LinkedList<Integer>();
        k = 0;
        iterations = 0;

        strandSortIterative(new LinkedList<>(inputList));
        return resultList;
    }

    public static int getIterations() {
        return iterations;
    }

    private static void strandSortIterative(LinkedList<Integer> origList) {

        iterations++;
        if (origList.isEmpty()) return;

        LinkedList<Integer> subList = new LinkedList<>();
        subList.add(origList.getFirst());
        origList.removeFirst();

        int index = 0;
        for (int j = 0; j < origList.size(); j++) {
            iterations++;
            if (origList.get(j) > subList.get(index)) {
                subList.add(origList.get(j));
                origList.remove(j);
                j = j - 1;
                index = index + 1;
            }
        }
        iterations++;
        if (k == 0) {
            for (int i = 0; i < subList.size(); i++) {
                iterations++;
                resultList.add(subList.get(i));
                k = k + 1;
            }
        } else {
            int subEnd = subList.size() - 1;
            int solStart = 0;
            while (!subList.isEmpty()) {
                iterations++;
                if (subList.get(subEnd) > resultList.get(solStart)) {
                    solStart++;
                } else {
                    resultList.add(solStart, subList.get(subEnd));
                    subList.remove(subEnd);
                    subEnd--;
                    solStart = 0;
                }
            }
        }
        strandSortIterative(origList);
    }
}