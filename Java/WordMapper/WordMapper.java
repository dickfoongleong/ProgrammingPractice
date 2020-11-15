import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordMapper {
    /**
     * Class with the count and the indices of the sentence that the word was found.
     */
    private static class WordInfo {
        private int count;
        private List<Integer> appearanceSentenceIdx;

        public WordInfo() {
            this.count = 0;
            this.appearanceSentenceIdx = new ArrayList<>();
        }

        /**
         * Add the index of the sentence that the word was found.
         * 
         * @param sentenceIdx Sentence index.
         */
        public void addAppearance(int sentenceIdx) {
            count++;
            appearanceSentenceIdx.add(sentenceIdx);
        }

        /**
         * Override the Object.toString() method for better result output.
         */
        @Override
        public String toString() {
            StringBuilder strBuilder = new StringBuilder("{");
            strBuilder.append(count).append(":").append(appearanceSentenceIdx.get(0));
            for (int i = 1; i < appearanceSentenceIdx.size(); i++) {
                strBuilder.append(",").append(appearanceSentenceIdx.get(i));
            }
            strBuilder.append("}");
            return strBuilder.toString();
        }
    }

    /**
     * Method to print out all the occurances of each word. <br>
     * <br>
     * This method assumed the sentences do not contain the following punctuation
     * marks, which are used as the delimeters of splitting sentences.
     * <ul>
     * <li>period(.)</li>
     * <li>question mark(?)</li>
     * <li>exclamation mark(!)</li>
     * </ul>
     * 
     * @param inputStrs List of input to be unwrapped.
     */
    public static void wordUnwrap(List<String> inputStrs) {
        Map<String, WordInfo> wordMap = new HashMap<>();
        int sentenceIdx = 0;
        for (String line : inputStrs) {
            String[] sentences = line.split("\\.|\\?|!"); // Cut line into sentences by (.), (?) or (!).
            for (String sentence : sentences) {
                sentenceIdx++;
                String[] words = sentence.split("\\W+"); // Retrieve words from the sentence.
                for (String word : words) {
                    // Ingore empty string as a word.
                    if (!word.isEmpty()) {
                        WordInfo info;
                        if (wordMap.containsKey(word.toLowerCase())) {
                            // Word is added to the map previously.
                            info = wordMap.get(word.toLowerCase());
                        } else {
                            // Word is new to the map.
                            info = new WordInfo();
                        }
                        info.addAppearance(sentenceIdx);
                        wordMap.put(word.toLowerCase(), info);
                    }
                }
            }
        }

        // Populate the results.
        List<String> result = new ArrayList<>();
        for (String word : wordMap.keySet()) {
            result.add(word + ": " + wordMap.get(word).toString());
        }
        Collections.sort(result);

        // Print the results.
        for (String r : result) {
            System.out.println(r);
        }
    }

    public static void main(String[] args) {
        List<String> input = new ArrayList<>();
        input.add("Wait a minute. Wait a minute, Doc.");
        input.add("Are you telling me that you built a time machine out of a Delorean?");
        wordUnwrap(input);
    }
}