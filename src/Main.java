import java.util.*;
import java.io.*;

public class Main {

    //returns the 2d indexing equivalent of the randomly generated index number
    public static int[] unflattenIndex(int index, int squareLength) {
        int[] unflattened = new int[2];
        unflattened[0] = index / squareLength;
        unflattened[1] = index % squareLength;
        return unflattened;
    }

    public static String getHeaderValue(String[] header, ArrayList<int[]> modes, int mode) {
        return header[modes.get(mode)[1]];
    }

    public static String getPrompt(ArrayList<String[]> set, ArrayList<int[]> modes, int mode, int groupIndex) {
        //groupIndex decides which group is being asked about, e.g. HCl, hydrochloric acid, aqueous
        return set.get(groupIndex)[modes.get(mode)[0]];
    }

    public static String getAnswer(ArrayList<String[]> set, ArrayList<int[]> modes, int mode, int groupIndex) {
        return set.get(groupIndex)[modes.get(mode)[1]];
    }

    public static void main(String[] args) throws Exception {
        //TODO: after everything works, make a text-based save system
        ArrayList<String[]> set = new ArrayList<>();
        //TODO: something is going wrong with the input system, figure out why the first line is being read twice
        //TODO: change the method of reading from space-separated to CSV or something more robust
        Scanner readFile = new Scanner(new File("src/input.txt"));
        Scanner user = new Scanner(System.in);
        //header keeps track of the names of the different term/definition sets
        String[] header = readFile.nextLine().split(" ");
        while (readFile.hasNextLine()) {
            String[] s = readFile.nextLine().split(" ");
            set.add(s);
        }
        System.out.println("MULTI-WAY TESTER\n\n");
        System.out.println("Type 'Q' to end the session.\n\n==========\n");
        //modes will keep track of which directions are tested (e.g. set 2 to set 3?)
        //TODO: see if you can optimize this tracking
        ArrayList<int[]> modes = new ArrayList<>();
        //TODO: create a system for handling blank columns in an entry
        for (int i = 0; i < header.length; i++) {
            for (int j = 0; j < header.length; j++) {
                if (i != j) {
                    //puts T/F in modes - i.e. should it ask those types of questions or no?
                    System.out.print("Ask from " + header[i] + " to " + header[j] + "? (y/n): ");
                    if (user.next().equalsIgnoreCase("y")) {
                        //adds the term and definition set to modes
                        int[] direction = {i, j};
                        modes.add(direction);
                    }
                }
            }
        }
        int correct = 0;
        int wrong = 0;
        double accuracy;
        System.out.println("Setup complete!\n");
        System.out.println("==========\n");
        while (true) {
            /*qTypeIndex decides the type of question
              qIndex decides which entry to quiz on
            */
            int qTypeIndex = (int) (Math.random() * modes.size());
            int qIndex = (int) (Math.random() * set.size());
            /*termType decides the type of the term (just to be used in the question)
              prompt decides the "term"
              answer contains the direct definition for the prompt
            */
            String termType = getHeaderValue(header, modes, qTypeIndex);
            String prompt = getPrompt(set, modes, qTypeIndex, qIndex);
            String answer = getAnswer(set, modes, qTypeIndex, qIndex);
            String question = "Give the " + termType + " for " + prompt + ": ";
            System.out.print(question);
            //check if the user wants to leave
            String userAnswer = user.nextLine();
            if (userAnswer.trim().equalsIgnoreCase("q")) {
                System.out.println("\n----------\n\nEnding session...\n\n==========\n");
                break;
            }
            if (userAnswer.toLowerCase().trim().equals(answer)) {
                correct++;
                System.out.println("\nCorrect!\n");
            } else {
                wrong++;
                System.out.println("\nIncorrect; the answer was " + answer + ".\n");
            }
        }
        //report results
        int total = correct + wrong;
        accuracy = (double) correct / total;
        System.out.println("SESSION COMPLETE\n");
        System.out.println("Question Count: " + total);
        System.out.println("Correct: " + correct);
        System.out.println("Incorrect: " + wrong);
        System.out.print("Accuracy: ");
        System.out.printf("%.2f", (accuracy * 100));
        System.out.println("%\n");
    }
}