package com.codeinsight.init;

import com.codeinsight.entity.Difficulty;
import com.codeinsight.entity.Question;
import com.codeinsight.entity.Topic;
import com.codeinsight.repository.QuestionRepository;
import com.codeinsight.repository.TopicRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;

    public DataInitializer(TopicRepository topicRepository, QuestionRepository questionRepository) {
        this.topicRepository = topicRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (topicRepository.count() > 0) {
            return; // Data already initialized
        }

        // 1. Topic: Loops
        Topic loops = new Topic(
                "Loops & Iteration",
                "loops",
                "Master for-loops, while-loops, loop bounds, boundary conditions, and accumulator variables.",
                "Repeat",
                "#3B82F6",
                1
        );
        topicRepository.save(loops);

        createQuestion(
                loops,
                "Sum of First N Numbers",
                Difficulty.EASY,
                "Write a method `sumUpTo(int n)` that computes and returns the sum of all integers from 1 up to and including `n` using a `for` loop.\n\n" +
                        "**Example 1:**\n- Input: `n = 5`\n- Output: `15` (1 + 2 + 3 + 4 + 5 = 15)\n\n" +
                        "**Constraints:**\n- `n >= 1`\n- Return an integer.",
                "public class Solution {\n    public static int sumUpTo(int n) {\n        int sum = 0;\n        // Write your for-loop here\n        \n        return sum;\n    }\n}",
                "Iterate from 1 up to n inclusive and add each i to sum.",
                "for;sum;=",
                null,
                "Start your loop at `int i = 1` and run until `i <= n`, incrementing by `i++`. Inside, accumulate `sum += i;`.",
                "A standard for-loop with condition `i <= n` visits each number between 1 and n once, giving O(n) time and O(1) space complexity.",
                "public class Solution {\n    public static int sumUpTo(int n) {\n        int sum = 0;\n        for (int i = 1; i <= n; i++) {\n            sum += i;\n        }\n        return sum;\n    }\n}",
                "[{\"input\":\"5\",\"expected\":\"15\"},{\"input\":\"10\",\"expected\":\"55\"}]"
        );

        createQuestion(
                loops,
                "Count Even Numbers",
                Difficulty.EASY,
                "Write a method `countEvens(int[] nums)` that counts how many numbers in the array are even using a loop and the remainder operator `%`.\n\n" +
                        "**Example 1:**\n- Input: `[1, 2, 3, 4, 6]`\n- Output: `3` (numbers 2, 4, and 6 are even)\n\n" +
                        "**Constraints:**\n- Length of array >= 0.",
                "public class Solution {\n    public static int countEvens(int[] nums) {\n        int count = 0;\n        // Iterate through nums and count even elements\n        \n        return count;\n    }\n}",
                "Count numbers where num % 2 == 0.",
                "for;count;% 2 == 0",
                null,
                "Use an enhanced for-loop `for (int num : nums)` or standard index loop. If `num % 2 == 0`, increment `count++`.",
                "Using `num % 2 == 0` correctly identifies even integers. An enhanced for-loop prevents index out-of-bounds risks.",
                "public class Solution {\n    public static int countEvens(int[] nums) {\n        int count = 0;\n        for (int num : nums) {\n            if (num % 2 == 0) {\n                count++;\n            }\n        }\n        return count;\n    }\n}",
                "[{\"input\":\"[1, 2, 3, 4, 6]\",\"expected\":\"3\"},{\"input\":\"[1, 3, 5]\",\"expected\":\"0\"}]"
        );

        createQuestion(
                loops,
                "Accumulate Countdown",
                Difficulty.MEDIUM,
                "Write a method `accumulateCountdown(int start)` using a `while` loop that counts down from `start` to 1, adding each value to `total`, and returning the total sum.\n\n" +
                        "**Example:**\n- Input: `start = 4`\n- Output: `10` (4 + 3 + 2 + 1 = 10)\n\n" +
                        "**Constraints:**\n- Must use a `while` loop.\n- Decrement the counter on each iteration.",
                "public class Solution {\n    public static int accumulateCountdown(int start) {\n        int total = 0;\n        int current = start;\n        // Implement while loop here\n        \n        return total;\n    }\n}",
                "Use while (current > 0) and decrement current.",
                "while;total;current",
                null,
                "Use `while (current > 0) { total += current; current--; }`.",
                "In while-loops, always ensure the condition counter progresses toward the termination condition to prevent infinite loops.",
                "public class Solution {\n    public static int accumulateCountdown(int start) {\n        int total = 0;\n        int current = start;\n        while (current > 0) {\n            total += current;\n            current--;\n        }\n        return total;\n    }\n}",
                "[{\"input\":\"4\",\"expected\":\"10\"},{\"input\":\"1\",\"expected\":\"1\"}]"
        );

        // 2. Topic: Arrays
        Topic arrays = new Topic(
                "Arrays & Data Structures",
                "arrays",
                "Learn 0-based indexing, array traversal, boundary guards, and in-place transformations.",
                "Layers",
                "#10B981",
                2
        );
        topicRepository.save(arrays);

        createQuestion(
                arrays,
                "Find Maximum in Array",
                Difficulty.EASY,
                "Write a method `findMax(int[] nums)` that finds and returns the maximum element in an integer array.\n\n" +
                        "**Example 1:**\n- Input: `[3, 7, 2, 9, 5]`\n- Output: `9`\n\n" +
                        "**Edge Case Note:**\n- Initialize `max` with `nums[0]`, not `0`, so negative numbers are supported correctly.",
                "public class Solution {\n    public static int findMax(int[] nums) {\n        int max = nums[0];\n        // Traverse the array from index 1 to length - 1\n        \n        return max;\n    }\n}",
                "Traverse array and update max if nums[i] > max.",
                "for;max;nums[",
                null,
                "Loop from `i = 1` up to `i < nums.length`. If `nums[i] > max`, set `max = nums[i];`.",
                "Initializing `max = nums[0]` guarantees correct behavior even if all elements in the array are negative.",
                "public class Solution {\n    public static int findMax(int[] nums) {\n        int max = nums[0];\n        for (int i = 1; i < nums.length; i++) {\n            if (nums[i] > max) {\n                max = nums[i];\n            }\n        }\n        return max;\n    }\n}",
                "[{\"input\":\"[3, 7, 2, 9, 5]\",\"expected\":\"9\"},{\"input\":\"[-5, -2, -10]\",\"expected\":\"-2\"}]"
        );

        createQuestion(
                arrays,
                "Reverse Array In-Place",
                Difficulty.MEDIUM,
                "Write a method `reverseArray(int[] arr)` that reverses the elements of an array in-place without creating a new array.\n\n" +
                        "**Example:**\n- Input: `[1, 2, 3, 4, 5]`\n- Result: `[5, 4, 3, 2, 1]`\n\n" +
                        "**Requirements:**\n- Swap elements from index `0` up to `arr.length / 2`.",
                "public class Solution {\n    public static void reverseArray(int[] arr) {\n        int n = arr.length;\n        // Swap elements symmetrically\n        \n    }\n}",
                "Swap arr[i] with arr[n - 1 - i] using a temporary variable.",
                "for;temp;arr[",
                null,
                "Loop with `int i = 0; i < n / 2; i++`. Swap using `int temp = arr[i]; arr[i] = arr[n - 1 - i]; arr[n - 1 - i] = temp;`.",
                "Only loop up to `n / 2`. Looping up to `n` would swap elements twice, leaving the array in its original order!",
                "public class Solution {\n    public static void reverseArray(int[] arr) {\n        int n = arr.length;\n        for (int i = 0; i < n / 2; i++) {\n            int temp = arr[i];\n            arr[i] = arr[n - 1 - i];\n            arr[n - 1 - i] = temp;\n        }\n    }\n}",
                "[{\"input\":\"[1, 2, 3, 4, 5]\",\"expected\":\"[5, 4, 3, 2, 1]\"}]"
        );

        createQuestion(
                arrays,
                "Check for Duplicates",
                Difficulty.MEDIUM,
                "Write a method `containsDuplicate(int[] nums)` that returns `true` if any value appears at least twice in the array, or `false` if every element is distinct.\n\n" +
                        "**Example 1:**\n- Input: `[1, 2, 3, 1]` -> `true`\n\n" +
                        "**Example 2:**\n- Input: `[1, 2, 3, 4]` -> `false`",
                "public class Solution {\n    public static boolean containsDuplicate(int[] nums) {\n        // Compare elements to find any duplicates\n        \n        return false;\n    }\n}",
                "Compare each pair nums[i] and nums[j] or track visited elements.",
                "for;nums[;return true",
                null,
                "You can use nested loops: outer loop `i` from 0 to `nums.length`, inner loop `j` from `i + 1` to `nums.length`. If `nums[i] == nums[j]`, return `true`.",
                "Nested loop checks all unique pairs in O(n^2) time with O(1) space. In later lessons, you can optimize to O(n) using a HashSet.",
                "public class Solution {\n    public static boolean containsDuplicate(int[] nums) {\n        for (int i = 0; i < nums.length; i++) {\n            for (int j = i + 1; j < nums.length; j++) {\n                if (nums[i] == nums[j]) {\n                    return true;\n                }\n            }\n        }\n        return false;\n    }\n}",
                "[{\"input\":\"[1, 2, 3, 1]\",\"expected\":\"true\"},{\"input\":\"[1, 2, 3, 4]\",\"expected\":\"false\"}]"
        );

        // 3. Topic: Functions
        Topic functions = new Topic(
                "Functions & Methods",
                "functions",
                "Understand parameters, return types, edge case validation, and modular logic decomposition.",
                "Code2",
                "#8B5CF6",
                3
        );
        topicRepository.save(functions);

        createQuestion(
                functions,
                "Calculate Area of Circle",
                Difficulty.EASY,
                "Write a method `calculateArea(double radius)` that returns the area of a circle using `Math.PI * radius * radius`.\n\n" +
                        "**Input Validation:**\n- If `radius < 0`, return `-1.0` to signal invalid input.\n\n" +
                        "**Example:**\n- Input: `radius = 3.0`\n- Output: `28.274333882308138`",
                "public class Solution {\n    public static double calculateArea(double radius) {\n        // Validate radius and return area\n        \n        return 0.0;\n    }\n}",
                "Check radius < 0 and compute Math.PI * radius * radius.",
                "if;radius < 0;Math.PI;return",
                null,
                "Add an early guard clause: `if (radius < 0) return -1.0;`. Then return `Math.PI * radius * radius;`.",
                "Guard clauses at the top of a method catch invalid arguments early, avoiding unnecessary calculations.",
                "public class Solution {\n    public static double calculateArea(double radius) {\n        if (radius < 0) {\n            return -1.0;\n        }\n        return Math.PI * radius * radius;\n    }\n}",
                "[{\"input\":\"3.0\",\"expected\":\"28.2743\"},{\"input\":\"-2.0\",\"expected\":\"-1.0\"}]"
        );

        createQuestion(
                functions,
                "Celsius to Fahrenheit",
                Difficulty.EASY,
                "Write a method `toFahrenheit(double celsius)` that converts Celsius to Fahrenheit using the standard conversion formula: `(celsius * 9.0 / 5.0) + 32.0`.\n\n" +
                        "**Example 1:**\n- Input: `0.0` -> Output: `32.0`\n**Example 2:**\n- Input: `100.0` -> Output: `212.0`",
                "public class Solution {\n    public static double toFahrenheit(double celsius) {\n        // Convert and return Fahrenheit\n        \n        return 0.0;\n    }\n}",
                "Compute (celsius * 9.0 / 5.0) + 32.0.",
                "celsius;* 9;+ 32;return",
                null,
                "Use `return (celsius * 9.0 / 5.0) + 32.0;`. Be sure to use floating point numbers (`9.0` and `5.0`) to avoid integer truncation.",
                "In Java, dividing integers `9 / 5` truncates to `1`. Writing `9.0 / 5.0` ensures accurate floating-point arithmetic.",
                "public class Solution {\n    public static double toFahrenheit(double celsius) {\n        return (celsius * 9.0 / 5.0) + 32.0;\n    }\n}",
                "[{\"input\":\"0.0\",\"expected\":\"32.0\"},{\"input\":\"100.0\",\"expected\":\"212.0\"}]"
        );

        createQuestion(
                functions,
                "Check Prime Number",
                Difficulty.MEDIUM,
                "Write a method `isPrime(int n)` that returns `true` if `n` is a prime number, and `false` otherwise.\n\n" +
                        "**Rules:**\n- Numbers `<= 1` are not prime.\n- Check divisibility by integers from `2` up to `i * i <= n`.\n\n" +
                        "**Example 1:** `n = 7` -> `true`\n**Example 2:** `n = 4` -> `false`",
                "public class Solution {\n    public static boolean isPrime(int n) {\n        if (n <= 1) return false;\n        // Check factors\n        \n        return true;\n    }\n}",
                "Check if n is divisible by any i where i * i <= n.",
                "if;for;% i == 0;return false;return true",
                null,
                "Loop from `i = 2` while `i * i <= n`. If `n % i == 0`, immediately return `false`. If the loop finishes, return `true`.",
                "You only need to check factors up to the square root of n (`i * i <= n`), reducing time complexity from O(n) to O(sqrt(n)).",
                "public class Solution {\n    public static boolean isPrime(int n) {\n        if (n <= 1) return false;\n        for (int i = 2; i * i <= n; i++) {\n            if (n % i == 0) {\n                return false;\n            }\n        }\n        return true;\n    }\n}",
                "[{\"input\":\"7\",\"expected\":\"true\"},{\"input\":\"4\",\"expected\":\"false\"}]"
        );

        // 4. Topic: Recursion
        Topic recursion = new Topic(
                "Recursion & Divide-and-Conquer",
                "recursion",
                "Master base cases, self-referential invocations, stack frames, and recursive reduction.",
                "GitFork",
                "#F59E0B",
                4
        );
        topicRepository.save(recursion);

        createQuestion(
                recursion,
                "Factorial with Recursion",
                Difficulty.EASY,
                "Write a recursive method `factorial(int n)` that computes `n!`.\n\n" +
                        "**Recursive Definition:**\n- Base case: `n <= 1` -> returns `1`\n- Recursive step: `n * factorial(n - 1)`\n\n" +
                        "**Example:**\n- Input: `n = 4` -> Output: `24` (4 * 3 * 2 * 1 = 24)",
                "public class Solution {\n    public static int factorial(int n) {\n        // 1. Base case:\n        \n        // 2. Recursive call:\n        return 1;\n    }\n}",
                "Base case: if (n <= 1) return 1. Recursive case: return n * factorial(n - 1).",
                "if;factorial;n - 1;return",
                null,
                "Write your base case first: `if (n <= 1) return 1;`. Then return `n * factorial(n - 1);`.",
                "Every recursive function must have a base case that returns a constant without calling itself, preventing infinite call stacks.",
                "public class Solution {\n    public static int factorial(int n) {\n        if (n <= 1) {\n            return 1;\n        }\n        return n * factorial(n - 1);\n    }\n}",
                "[{\"input\":\"4\",\"expected\":\"24\"},{\"input\":\"5\",\"expected\":\"120\"}]"
        );

        createQuestion(
                recursion,
                "Fibonacci Number",
                Difficulty.MEDIUM,
                "Write a recursive method `fib(int n)` that calculates the n-th Fibonacci number.\n\n" +
                        "**Definition:**\n- `fib(0) = 0`\n- `fib(1) = 1`\n- `fib(n) = fib(n - 1) + fib(n - 2)`\n\n" +
                        "**Example:**\n- Input: `n = 6` -> Output: `8`",
                "public class Solution {\n    public static int fib(int n) {\n        // Base cases: if n <= 0 return 0; if n == 1 return 1;\n        // Recursive case:\n        return 0;\n    }\n}",
                "Base case handles n <= 0 and n == 1. Recursive case returns fib(n - 1) + fib(n - 2).",
                "if;fib(n - 1);fib(n - 2);return",
                null,
                "Handle base cases: `if (n <= 0) return 0; if (n == 1) return 1;`. Then return `fib(n - 1) + fib(n - 2);`.",
                "Tree recursion creates multiple branching calls. This demonstrates how a problem breaks down into identical smaller subproblems.",
                "public class Solution {\n    public static int fib(int n) {\n        if (n <= 0) return 0;\n        if (n == 1) return 1;\n        return fib(n - 1) + fib(n - 2);\n    }\n}",
                "[{\"input\":\"6\",\"expected\":\"8\"},{\"input\":\"0\",\"expected\":\"0\"}]"
        );

        createQuestion(
                recursion,
                "Sum of Digits",
                Difficulty.MEDIUM,
                "Write a recursive method `sumDigits(int n)` that returns the sum of the digits of a non-negative number `n`.\n\n" +
                        "**Example:**\n- Input: `n = 126` -> Output: `9` (1 + 2 + 6 = 9)\n\n" +
                        "**Recursive Logic:**\n- Base case: `if (n == 0) return 0;`\n- Recursive step: `(n % 10) + sumDigits(n / 10)`",
                "public class Solution {\n    public static int sumDigits(int n) {\n        // Base case: if n == 0 return 0\n        // Recursive case: add last digit (n % 10) to sumDigits of rest (n / 10)\n        return 0;\n    }\n}",
                "Base case if (n == 0) return 0. Recursive case: (n % 10) + sumDigits(n / 10).",
                "if;sumDigits;n / 10;n % 10;return",
                null,
                "`n % 10` extracts the rightmost digit, and `n / 10` drops it. Return `(n % 10) + sumDigits(n / 10);`.",
                "Extracting one digit per stack frame reduces the input size geometrically until `n` reaches 0.",
                "public class Solution {\n    public static int sumDigits(int n) {\n        if (n == 0) return 0;\n        return (n % 10) + sumDigits(n / 10);\n    }\n}",
                "[{\"input\":\"126\",\"expected\":\"9\"},{\"input\":\"49\",\"expected\":\"13\"}]"
        );
    }

    private void createQuestion(Topic topic, String title, Difficulty difficulty,
                                String prompt, String starterCode, String expectedBehavior,
                                String requiredPatterns, String forbiddenPatterns,
                                String hint, String solutionExplanation,
                                String sampleSolution, String testCasesJson) {
        Question q = new Question();
        q.setTopic(topic);
        q.setTitle(title);
        q.setDifficulty(difficulty);
        q.setPrompt(prompt);
        q.setStarterCode(starterCode);
        q.setExpectedBehavior(expectedBehavior);
        q.setRequiredPatterns(requiredPatterns);
        q.setForbiddenPatterns(forbiddenPatterns);
        q.setHint(hint);
        q.setSolutionExplanation(solutionExplanation);
        q.setSampleSolution(sampleSolution);
        q.setTestCasesJson(testCasesJson);
        questionRepository.save(q);
    }
}
