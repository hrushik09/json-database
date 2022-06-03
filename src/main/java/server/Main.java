package server;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String[] database;

    public static void main(String[] args) {
        database = new String[100];
        Arrays.fill(database, "");
        Scanner scanner = new Scanner(System.in);
        String input;
        String[] arr;

        while (true) {
            input = scanner.nextLine();
            if (input.equals("exit")) {
                break;
            }

            arr = getUpdatedInput(input);
            if (arr == null) {
                continue;
            }

            int index;
            switch (arr[0]) {
                case "get":
                    index = Integer.parseInt(arr[1]) - 1;
                    if (isCellEmpty(index) || isIndexIncorrect(index)) {
                        System.out.println("ERROR");
                    } else {
                        System.out.println(get(index));
                    }
                    break;
                case "set":
                    index = Integer.parseInt(arr[1]) - 1;
                    if (isIndexIncorrect(index)) {
                        System.out.println("ERROR");
                    } else {
                        set(index, arr[2]);
                        System.out.println("OK");
                    }
                    break;
                case "delete":
                    index = Integer.parseInt(arr[1]) - 1;
                    if (isIndexIncorrect(index)) {
                        System.out.println("ERROR");
                    } else {
                        set(index, "");
                        System.out.println("OK");
                    }
                    break;
            }
        }
    }

    private static void set(int index, String text) {
        database[index] = text;
    }

    private static String get(int index) {
        return database[index];
    }

    private static boolean isCellEmpty(int index) {
        return database[index].equals("");
    }

    private static boolean isIndexIncorrect(int index) {
        return index < 0 || index > 99;
    }

    private static String[] getUpdatedInput(String input) {
        String[] arr;
        if (input.startsWith("get")) {
            arr = input.split(" ");
        } else if (input.startsWith("set")) {
            arr = input.replaceFirst(" ", "#").replaceFirst(" ", "#")
                    .split("#");
        } else if (input.startsWith("delete")) {
            arr = input.split(" ");
        } else {
            System.out.println("Invalid input");
            return null;
        }
        return arr;
    }
}