
class Solution {
    public int minOperations(String s) {
        int n = s.length();

        String sorted = s.chars()
                .sorted()
                .collect(StringBuilder::new,
                         StringBuilder::appendCodePoint,
                         StringBuilder::append)
                .toString();

        if (s.equals(sorted)) return 0;

        if (s.charAt(0) == sorted.charAt(0) ||
            s.charAt(n - 1) == sorted.charAt(n - 1)) {
            return 1;
        }

        if (n == 2) return -1;

        char max = 'a';
        char min = 'z';

        for (int i = 0; i < n; i++) {
            max = (char) Math.max(max, s.charAt(i));
            min = (char) Math.min(min, s.charAt(i));
        }

        if (s.charAt(0) == max && s.charAt(n - 1) == min) {
            int maxCount = 0, minCount = 0;

            for (int i = 0; i < n; i++) {
                if (s.charAt(i) == max) maxCount++;
                if (s.charAt(i) == min) minCount++;
            }

            if (maxCount == 1 && minCount == 1) {
                return 3;
            }
        }

        return 2;
    }
}
