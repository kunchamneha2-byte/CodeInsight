package com.codeinsight.service;

import com.codeinsight.dto.QuestionDto;
import com.codeinsight.dto.QuestionSummaryDto;
import com.codeinsight.entity.Difficulty;
import com.codeinsight.entity.Question;
import com.codeinsight.entity.Submission;
import com.codeinsight.exception.ResourceNotFoundException;
import com.codeinsight.repository.QuestionRepository;
import com.codeinsight.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubmissionRepository submissionRepository;

    public QuestionService(QuestionRepository questionRepository,
                           SubmissionRepository submissionRepository) {
        this.questionRepository = questionRepository;
        this.submissionRepository = submissionRepository;
    }

    public List<QuestionSummaryDto> getQuestions(Long topicId, String topicSlug, Difficulty difficulty, String studentIdentifier) {
        List<Question> questions;
        if (topicId != null) {
            questions = questionRepository.findByTopicId(topicId);
        } else if (topicSlug != null && !topicSlug.trim().isEmpty()) {
            questions = questionRepository.findByTopicSlug(topicSlug);
        } else if (difficulty != null) {
            questions = questionRepository.findByDifficulty(difficulty);
        } else {
            questions = questionRepository.findAllWithTopic();
        }

        List<Submission> submissions = submissionRepository.findByStudentIdentifierOrderBySubmittedAtDesc(studentIdentifier);
        Set<Long> solvedIds = submissions.stream()
                .filter(Submission::getIsCorrect)
                .map(s -> s.getQuestion().getId())
                .collect(Collectors.toSet());

        Map<Long, Long> attemptCountMap = submissions.stream()
                .collect(Collectors.groupingBy(s -> s.getQuestion().getId(), Collectors.counting()));

        return questions.stream()
                .map(q -> {
                    QuestionSummaryDto dto = new QuestionSummaryDto();
                    dto.setId(q.getId());
                    dto.setTitle(q.getTitle());
                    dto.setDifficulty(q.getDifficulty());
                    dto.setTopicId(q.getTopic().getId());
                    dto.setTopicName(q.getTopic().getName());
                    dto.setTopicSlug(q.getTopic().getSlug());
                    dto.setSolved(solvedIds.contains(q.getId()));
                    dto.setAttemptCount(attemptCountMap.getOrDefault(q.getId(), 0L).intValue());
                    return dto;
                })
                .sorted(Comparator.comparing(QuestionSummaryDto::getId))
                .collect(Collectors.toList());
    }

    public QuestionDto getQuestionById(Long id, String studentIdentifier) {
        Question q = questionRepository.findByIdWithTopic(id);
        if (q == null) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }

        List<Submission> userSubmissions = submissionRepository.findByStudentIdentifierAndQuestionIdOrderBySubmittedAtDesc(studentIdentifier, id);
        boolean solved = userSubmissions.stream().anyMatch(Submission::getIsCorrect);
        String lastCode = userSubmissions.isEmpty() ? null : userSubmissions.get(0).getSubmittedCode();
        String lastLang = userSubmissions.isEmpty() ? null : userSubmissions.get(0).getLanguage();

        QuestionDto dto = new QuestionDto();
        dto.setId(q.getId());
        dto.setTitle(q.getTitle());
        dto.setDifficulty(q.getDifficulty());
        dto.setTopicId(q.getTopic().getId());
        dto.setTopicName(q.getTopic().getName());
        dto.setTopicSlug(q.getTopic().getSlug());
        dto.setPrompt(q.getPrompt());
        dto.setPromptPython(q.getPromptPython());
        dto.setPromptC(q.getPromptC());
        dto.setPromptCpp(q.getPromptCpp());
        dto.setStarterCode(q.getStarterCode());
        dto.setStarterCodePython(q.getStarterCodePython());
        dto.setStarterCodeC(q.getStarterCodeC());
        dto.setStarterCodeCpp(q.getStarterCodeCpp());
        dto.setExpectedBehavior(q.getExpectedBehavior());
        dto.setHint(q.getHint());
        dto.setSolutionExplanation(q.getSolutionExplanation());
        dto.setTestCasesJson(q.getTestCasesJson());
        dto.setSolved(solved);
        dto.setAttemptCount(userSubmissions.size());
        dto.setLastSubmittedCode(lastCode);
        dto.setLastSubmittedLanguage(lastLang);

        // Populate language maps
        Map<String, String> starterCodes = new LinkedHashMap<>();
        starterCodes.put("JAVA", q.getStarterCode());
        starterCodes.put("PYTHON", q.getStarterCodePython() != null ? q.getStarterCodePython() : generatePythonTemplate(q));
        starterCodes.put("C", q.getStarterCodeC() != null ? q.getStarterCodeC() : generateCTemplate(q));
        starterCodes.put("CPP", q.getStarterCodeCpp() != null ? q.getStarterCodeCpp() : generateCppTemplate(q));
        dto.setStarterCodes(starterCodes);

        Map<String, String> prompts = new LinkedHashMap<>();
        prompts.put("JAVA", q.getPrompt());
        prompts.put("PYTHON", q.getPromptPython() != null ? q.getPromptPython() : generatePythonPrompt(q));
        prompts.put("C", q.getPromptC() != null ? q.getPromptC() : generateCPrompt(q));
        prompts.put("CPP", q.getPromptCpp() != null ? q.getPromptCpp() : generateCppPrompt(q));
        dto.setPrompts(prompts);

        Map<String, String> fileNames = new LinkedHashMap<>();
        fileNames.put("JAVA", "Solution.java");
        fileNames.put("PYTHON", "solution.py");
        fileNames.put("C", "solution.c");
        fileNames.put("CPP", "solution.cpp");
        dto.setFileNames(fileNames);

        return dto;
    }

    private String generatePythonTemplate(Question q) {
        String title = q.getTitle().toLowerCase();
        if (title.contains("sum of first n")) {
            return "def sum_up_to(n: int) -> int:\n    total = 0\n    # Write loop in Python\n    \n    return total\n";
        } else if (title.contains("count even")) {
            return "def count_evens(nums: list[int]) -> int:\n    count = 0\n    # Iterate through nums and count even elements\n    \n    return count\n";
        } else if (title.contains("countdown")) {
            return "def accumulate_countdown(start: int) -> int:\n    total = 0\n    current = start\n    # Implement while loop in Python\n    \n    return total\n";
        } else if (title.contains("find max")) {
            return "def find_max(nums: list[int]) -> int:\n    max_val = nums[0]\n    # Traverse list from index 1 to len(nums) - 1\n    \n    return max_val\n";
        } else if (title.contains("reverse array")) {
            return "def reverse_array(arr: list[int]) -> None:\n    n = len(arr)\n    # Swap elements symmetrically in-place\n    \n";
        } else if (title.contains("duplicate")) {
            return "def contains_duplicate(nums: list[int]) -> bool:\n    # Return True if duplicate exists, otherwise False\n    return False\n";
        } else if (title.contains("circle") || title.contains("area")) {
            return "import math\n\ndef calculate_area(radius: float) -> float:\n    # If radius < 0 return -1.0, else return math.pi * radius * radius\n    return 0.0\n";
        } else if (title.contains("fahrenheit")) {
            return "def to_fahrenheit(celsius: float) -> float:\n    # Convert Celsius to Fahrenheit: (celsius * 9.0 / 5.0) + 32.0\n    return 0.0\n";
        } else if (title.contains("prime")) {
            return "def is_prime(n: int) -> bool:\n    if n <= 1:\n        return False\n    # Check factors\n    return True\n";
        } else if (title.contains("factorial")) {
            return "def factorial(n: int) -> int:\n    # Base case: if n <= 1 return 1\n    # Recursive case: n * factorial(n - 1)\n    return 1\n";
        } else if (title.contains("fibonacci")) {
            return "def fib(n: int) -> int:\n    # Base cases: if n <= 0 return 0; if n == 1 return 1\n    # Recursive case: fib(n - 1) + fib(n - 2)\n    return 0\n";
        } else if (title.contains("digits")) {
            return "def sum_digits(n: int) -> int:\n    # Base case: if n == 0 return 0\n    # Recursive case: (n % 10) + sum_digits(n // 10)\n    return 0\n";
        }
        return "# Write your Python solution here\n";
    }

    private String generateCTemplate(Question q) {
        String title = q.getTitle().toLowerCase();
        if (title.contains("sum of first n")) {
            return "#include <stdio.h>\n\nint sumUpTo(int n) {\n    int sum = 0;\n    // Write your for-loop here\n    \n    return sum;\n}\n";
        } else if (title.contains("count even")) {
            return "#include <stdio.h>\n\nint countEvens(int nums[], int length) {\n    int count = 0;\n    // Iterate through array and count evens\n    \n    return count;\n}\n";
        } else if (title.contains("countdown")) {
            return "#include <stdio.h>\n\nint accumulateCountdown(int start) {\n    int total = 0;\n    int current = start;\n    // Implement while loop here\n    \n    return total;\n}\n";
        } else if (title.contains("find max")) {
            return "#include <stdio.h>\n\nint findMax(int nums[], int length) {\n    int max = nums[0];\n    // Traverse array from index 1 to length - 1\n    \n    return max;\n}\n";
        } else if (title.contains("reverse array")) {
            return "#include <stdio.h>\n\nvoid reverseArray(int arr[], int length) {\n    // Swap elements symmetrically in-place\n    \n}\n";
        } else if (title.contains("duplicate")) {
            return "#include <stdio.h>\n#include <stdbool.h>\n\nbool containsDuplicate(int nums[], int length) {\n    // Return true if duplicate exists, otherwise false\n    return false;\n}\n";
        } else if (title.contains("circle") || title.contains("area")) {
            return "#include <stdio.h>\n#define PI 3.141592653589793\n\ndouble calculateArea(double radius) {\n    // If radius < 0 return -1.0\n    return 0.0;\n}\n";
        } else if (title.contains("fahrenheit")) {
            return "#include <stdio.h>\n\ndouble toFahrenheit(double celsius) {\n    // Return (celsius * 9.0 / 5.0) + 32.0\n    return 0.0;\n}\n";
        } else if (title.contains("prime")) {
            return "#include <stdio.h>\n#include <stdbool.h>\n\nbool isPrime(int n) {\n    if (n <= 1) return false;\n    // Check divisibility\n    return true;\n}\n";
        } else if (title.contains("factorial")) {
            return "#include <stdio.h>\n\nint factorial(int n) {\n    // 1. Base case:\n    // 2. Recursive call:\n    return 1;\n}\n";
        } else if (title.contains("fibonacci")) {
            return "#include <stdio.h>\n\nint fib(int n) {\n    // Base cases and recursive step\n    return 0;\n}\n";
        } else if (title.contains("digits")) {
            return "#include <stdio.h>\n\nint sumDigits(int n) {\n    // Base case and recursive step\n    return 0;\n}\n";
        }
        return "// Write your C solution here\n";
    }

    private String generateCppTemplate(Question q) {
        String title = q.getTitle().toLowerCase();
        if (title.contains("sum of first n")) {
            return "#include <iostream>\n\nint sumUpTo(int n) {\n    int sum = 0;\n    // Write your for-loop here\n    \n    return sum;\n}\n";
        } else if (title.contains("count even")) {
            return "#include <iostream>\n#include <vector>\n\nint countEvens(const std::vector<int>& nums) {\n    int count = 0;\n    // Count even elements in vector\n    \n    return count;\n}\n";
        } else if (title.contains("countdown")) {
            return "#include <iostream>\n\nint accumulateCountdown(int start) {\n    int total = 0;\n    int current = start;\n    // Implement while loop here\n    \n    return total;\n}\n";
        } else if (title.contains("find max")) {
            return "#include <iostream>\n#include <vector>\n\nint findMax(const std::vector<int>& nums) {\n    int max = nums[0];\n    // Traverse elements\n    \n    return max;\n}\n";
        } else if (title.contains("reverse array")) {
            return "#include <iostream>\n#include <vector>\n\nvoid reverseArray(std::vector<int>& arr) {\n    // Swap elements in-place\n    \n}\n";
        } else if (title.contains("duplicate")) {
            return "#include <iostream>\n#include <vector>\n\nbool containsDuplicate(const std::vector<int>& nums) {\n    // Check for duplicates\n    return false;\n}\n";
        } else if (title.contains("circle") || title.contains("area")) {
            return "#include <iostream>\n#include <cmath>\n\ndouble calculateArea(double radius) {\n    if (radius < 0) return -1.0;\n    return M_PI * radius * radius;\n}\n";
        } else if (title.contains("fahrenheit")) {
            return "#include <iostream>\n\ndouble toFahrenheit(double celsius) {\n    return (celsius * 9.0 / 5.0) + 32.0;\n}\n";
        } else if (title.contains("prime")) {
            return "#include <iostream>\n\nbool isPrime(int n) {\n    if (n <= 1) return false;\n    for (int i = 2; i * i <= n; i++) {\n        if (n % i == 0) return false;\n    }\n    return true;\n}\n";
        } else if (title.contains("factorial")) {
            return "#include <iostream>\n\nint factorial(int n) {\n    if (n <= 1) return 1;\n    return n * factorial(n - 1);\n}\n";
        } else if (title.contains("fibonacci")) {
            return "#include <iostream>\n\nint fib(int n) {\n    if (n <= 0) return 0;\n    if (n == 1) return 1;\n    return fib(n - 1) + fib(n - 2);\n}\n";
        } else if (title.contains("digits")) {
            return "#include <iostream>\n\nint sumDigits(int n) {\n    if (n == 0) return 0;\n    return (n % 10) + sumDigits(n / 10);\n}\n";
        }
        return "// Write your C++ solution here\n";
    }

    private String generatePythonPrompt(Question q) {
        String base = q.getPrompt();
        String title = q.getTitle().toLowerCase();
        if (title.contains("sum of first n")) {
            return "Write a Python function `sum_up_to(n: int) -> int` that computes and returns the sum of all integers from 1 up to and including `n` using a `for` loop.\n\n" +
                    "**Example:**\n- Input: `n = 5`\n- Output: `15` (1 + 2 + 3 + 4 + 5 = 15)\n\n" +
                    "**Python Tip:** Remember that `range(1, n)` does not include `n`. Use `range(1, n + 1)`.";
        } else if (title.contains("count even")) {
            return "Write a Python function `count_evens(nums: list[int]) -> int` that counts how many numbers in the list are even using a loop and `% 2 == 0`.\n\n" +
                    "**Example:**\n- Input: `[1, 2, 3, 4, 6]`\n- Output: `3` (numbers 2, 4, and 6 are even)";
        } else if (title.contains("countdown")) {
            return "Write a Python function `accumulate_countdown(start: int) -> int` using a `while` loop that counts down from `start` to 1, adding each value to `total`, and returning the total sum.\n\n" +
                    "**Example:**\n- Input: `start = 4`\n- Output: `10`";
        } else if (title.contains("find max")) {
            return "Write a Python function `find_max(nums: list[int]) -> int` that finds and returns the maximum element in a list of integers. Initialize your maximum with `nums[0]`.\n\n" +
                    "**Example:**\n- Input: `[3, 7, 2, 9, 5]`\n- Output: `9`";
        } else if (title.contains("reverse array")) {
            return "Write a Python function `reverse_array(arr: list[int]) -> None` that reverses the elements of a list in-place by swapping symmetrical elements from the outside towards the middle.\n\n" +
                    "**Example:**\n- Input: `[1, 2, 3, 4, 5]`\n- Result: `[5, 4, 3, 2, 1]`";
        } else if (title.contains("duplicate")) {
            return "Write a Python function `contains_duplicate(nums: list[int]) -> bool` that returns `True` if any value appears at least twice in the list, or `False` if every element is distinct.\n\n" +
                    "**Example:**\n- Input: `[1, 2, 3, 1]` -> `True`";
        } else if (title.contains("circle") || title.contains("area")) {
            return "Write a Python function `calculate_area(radius: float) -> float` that returns the area of a circle using `math.pi * radius * radius`.\n\n" +
                    "**Validation:**\n- If `radius < 0`, return `-1.0`.\n- Example: `radius = 3.0` -> `28.2743`";
        } else if (title.contains("fahrenheit")) {
            return "Write a Python function `to_fahrenheit(celsius: float) -> float` that converts Celsius to Fahrenheit using `(celsius * 9.0 / 5.0) + 32.0`.\n\n" +
                    "**Example:**\n- Input: `0.0` -> `32.0`\n- Input: `100.0` -> `212.0`";
        } else if (title.contains("prime")) {
            return "Write a Python function `is_prime(n: int) -> bool` that returns `True` if `n > 1` is prime, and `False` otherwise.\n\n" +
                    "**Example:**\n- Input: `n = 7` -> `True`\n- Input: `n = 4` -> `False`";
        } else if (title.contains("factorial")) {
            return "Write a recursive Python function `factorial(n: int) -> int` that computes `n!`.\n\n" +
                    "**Recursive Definition:**\n- Base case: `n <= 1` -> returns `1`\n- Recursive step: `n * factorial(n - 1)`\n\n" +
                    "**Example:** `factorial(4)` -> `24`";
        } else if (title.contains("fibonacci")) {
            return "Write a recursive Python function `fib(n: int) -> int` that returns the n-th Fibonacci number.\n\n" +
                    "**Definition:**\n- `fib(0) = 0`, `fib(1) = 1`\n- `fib(n) = fib(n - 1) + fib(n - 2)`\n\n" +
                    "**Example:** `fib(6)` -> `8`";
        } else if (title.contains("digits")) {
            return "Write a recursive Python function `sum_digits(n: int) -> int` that returns the sum of digits of `n`.\n\n" +
                    "**Recursive Step:** `(n % 10) + sum_digits(n // 10)`\n**Base Case:** `if n == 0: return 0`";
        }
        return base;
    }

    private String generateCPrompt(Question q) {
        String title = q.getTitle().toLowerCase();
        if (title.contains("sum of first n")) {
            return "Write a C function `int sumUpTo(int n)` that computes and returns the sum of all integers from 1 up to and including `n` using a `for` loop.\n\n" +
                    "**Example:** `sumUpTo(5)` -> `15`";
        } else if (title.contains("count even")) {
            return "Write a C function `int countEvens(int nums[], int length)` that counts how many numbers in the array are even using a loop and `% 2 == 0`.\n\n" +
                    "**Example:** `nums = [1, 2, 3, 4, 6]`, `length = 5` -> returns `3`";
        } else if (title.contains("find max")) {
            return "Write a C function `int findMax(int nums[], int length)` that finds and returns the maximum element in an integer array. Initialize max with `nums[0]`.\n\n" +
                    "**Example:** `nums = [3, 7, 2, 9, 5]`, `length = 5` -> returns `9`";
        } else if (title.contains("factorial")) {
            return "Write a recursive C function `int factorial(int n)` that computes `n!`.\n\n" +
                    "**Recursive Case:** `return n * factorial(n - 1);`\n**Base Case:** `if (n <= 1) return 1;`";
        }
        return q.getPrompt().replace("Java", "C").replace("public class Solution", "C Function");
    }

    private String generateCppPrompt(Question q) {
        String title = q.getTitle().toLowerCase();
        if (title.contains("sum of first n")) {
            return "Write a C++ function `int sumUpTo(int n)` that computes and returns the sum of all integers from 1 up to and including `n` using a `for` loop.\n\n" +
                    "**Example:** `sumUpTo(5)` -> `15`";
        } else if (title.contains("count even")) {
            return "Write a C++ function `int countEvens(const std::vector<int>& nums)` that counts how many numbers in the vector are even using a loop and `% 2 == 0`.\n\n" +
                    "**Example:** `nums = {1, 2, 3, 4, 6}` -> returns `3`";
        } else if (title.contains("find max")) {
            return "Write a C++ function `int findMax(const std::vector<int>& nums)` that finds and returns the maximum element in a vector. Initialize max with `nums[0]`.\n\n" +
                    "**Example:** `nums = {3, 7, 2, 9, 5}` -> returns `9`";
        } else if (title.contains("factorial")) {
            return "Write a recursive C++ function `int factorial(int n)` that computes `n!`.\n\n" +
                    "**Recursive Case:** `return n * factorial(n - 1);`\n**Base Case:** `if (n <= 1) return 1;`";
        }
        return q.getPrompt().replace("Java", "C++").replace("public class Solution", "C++ Function");
    }
}
