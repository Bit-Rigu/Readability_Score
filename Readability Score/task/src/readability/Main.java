package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static java.lang.Math.*;

public class Main {
    public static void main(String[] args) {

        try {
            final var text = Files.readString(Path.of(args[0]));
            final var characters = text.replaceAll("\\s", "").length();
            //    final var letters = text.replaceAll("\\s", "").replaceAll("[,.?!-]","").length();
            final var words = text.split(" ").length;
            final var sentences = text.split("[!?.]+").length;
            final var syllables = countSyllables(text, true);
            final var polysyllables = countSyllables(text, false);
            final var L = (double) characters / words * 100;
            final var S = (double) sentences / words * 100;
            final var scoreARI = 4.71 * characters / words + 0.5 * words / sentences - 21.43;
            final var scoreFKR = 0.39 * words / sentences + 11.8 * syllables / words - 15.59;
            final var scoreSMOG = 1.043 * sqrt(polysyllables * 30.0 / sentences) + 3.1291;
            final var scoreCLI = 0.0588 * L - 0.296 * S - 15.8;

            System.out.printf("The text is:%n%s%n"
                            + "Words: %d%nSentences: %d%nCharacters: %d%nSyllables: %d%nPolysyllables: %d%n",
                    text, words, sentences, characters, syllables, polysyllables);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
            String chose = scanner.next();
            System.out.println();
            switch (chose) {
                case "ARI" :
                    System.out.printf("Automated Readability Index: %.2f" + " (about %s-year-olds)%n",
                                        scoreARI, calculateAge(scoreARI));
                    break;
                case "FK" :
                    System.out.printf("Flesch–Kincaid readability tests: %.2f" + " (about %s-year-olds)%n",
                            scoreFKR, calculateAge(scoreFKR));
                    break;
                case "SMOG" :
                    System.out.printf("Simple Measure of Gobbledygook: %.2f" + " (about %s-year-olds)%n",
                            scoreSMOG, calculateAge(scoreSMOG));
                    break;
                case "CL" :
                    System.out.printf("Coleman–Liau index: %.2f" + " (about %s-year-olds)%n",
                            scoreCLI, calculateAge(scoreCLI));
                    break;
                default :
                    System.out.printf("Automated Readability Index: %.2f" + " (about %s-year-olds)%n",
                            scoreARI, calculateAge(scoreARI));
                    System.out.printf("Flesch–Kincaid readability tests: %.2f" + " (about %s-year-olds)%n",
                            scoreFKR, calculateAge(scoreFKR));
                    System.out.printf("Simple Measure of Gobbledygook: %.2f" + " (about %s-year-olds)%n",
                            scoreSMOG, calculateAge(scoreSMOG));
                    System.out.printf("Coleman–Liau index: %.2f" + " (about %s-year-olds)%n",
                            scoreCLI, calculateAge(scoreCLI));
                    System.out.println();
                    double average =    (Double.parseDouble(calculateAge(scoreARI)) +
                                        Double.parseDouble(calculateAge(scoreFKR)) +
                                        Double.parseDouble(calculateAge(scoreSMOG)) +
                                        Double.parseDouble(calculateAge(scoreCLI))) / 4;
                            System.out.printf("This text should be understood in average by %.2f-year-olds.", average);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static String calculateAge(double score) {
        final var ageGroups = new String[]{"6", "7", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "24"};
        final int level = min(13, max(1, (int) round(score))) - 1;
        return ageGroups[level];

    }

    static int countSyllables(String text, boolean flag) {
        int sum = 0;
        int count = 0;
        String[] arr = text.split("\\s");
        for (String temp : arr) {
            temp = temp.replaceFirst("\\b[AIYUEOaiueoy]","na");
            temp = temp.replaceFirst("e\\b","n");
            temp = temp.replaceFirst("[aiuoy]\\b","an");
            String[] str = temp.split("[aiueoy]{1,3}");
            if (flag) {
                sum += str.length > 1 ? str.length - 1 : 1;
            } else {
                if (str.length > 3) {
                    count++;
                }
            }
        }
        if (flag) return sum;
        else return count;
    }

}