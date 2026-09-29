package com.examcraft.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcraft.dto.GeneratePaperRequest;
import com.examcraft.entity.Question;
import com.examcraft.entity.TestPaper;
import com.examcraft.entity.TestPaperQuestion;
import com.examcraft.exception.BadRequestException;
import com.examcraft.exception.ResourceNotFoundException;
import com.examcraft.repository.QuestionRepository;
import com.examcraft.repository.TestPaperQuestionRepository;
import com.examcraft.repository.TestPaperRepository;

@Service
public class TestPaperService {

    private static final List<String> LEVELS =
            List.of("Easy", "Medium", "Hard");

    private final QuestionRepository questionRepository;
    private final TestPaperRepository testPaperRepository;
    private final TestPaperQuestionRepository testPaperQuestionRepository;

    public TestPaperService(
            QuestionRepository questionRepository,
            TestPaperRepository testPaperRepository,
            TestPaperQuestionRepository testPaperQuestionRepository) {

        this.questionRepository = questionRepository;
        this.testPaperRepository = testPaperRepository;
        this.testPaperQuestionRepository =
                testPaperQuestionRepository;
    }

    /*
     * Generate randomized test paper
     */
    @Transactional
    public TestPaper generate(
            GeneratePaperRequest request) {

        validateRequest(request);

        int total =
                request.getTotalQuestions();

        long available =
                questionRepository.count();

        if (available < total) {

            throw new BadRequestException(
                    "Only "
                            + available
                            + " questions are available, but "
                            + total
                            + " questions were requested."
            );
        }

        /*
         * Calculate desired difficulty counts.
         */
        Map<String, Integer> required =
                calculateCounts(request);

        /*
         * Create separate question pools.
         */
        Map<String, List<Question>> pools =
                new LinkedHashMap<>();

        for (String level : LEVELS) {

            List<Question> questions =
                    new ArrayList<>(
                            questionRepository
                                    .findByDifficultyIgnoreCase(
                                            level
                                    )
                    );

            /*
             * Randomize each pool.
             */
            Collections.shuffle(questions);

            pools.put(level, questions);
        }

        /*
         * Initially select according to
         * requested difficulty distribution.
         */
        Map<String, Integer> selected =
                new LinkedHashMap<>();

        int selectedTotal = 0;

        for (String level : LEVELS) {

            int requested =
                    required.get(level);

            int availableInPool =
                    pools.get(level).size();

            int count =
                    Math.min(
                            requested,
                            availableInPool
                    );

            selected.put(level, count);

            selectedTotal += count;
        }

        /*
         * Redistribute missing questions when
         * one difficulty level has insufficient data.
         */
        while (selectedTotal < total) {

            String bestLevel = null;

            double bestNeed =
                    Double.NEGATIVE_INFINITY;

            for (String level : LEVELS) {

                int current =
                        selected.get(level);

                int availableInPool =
                        pools.get(level).size();

                if (current >= availableInPool) {
                    continue;
                }

                double target =
                        getTargetCount(
                                level,
                                total,
                                request
                        );

                double need =
                        target - current;

                if (need > bestNeed) {

                    bestNeed = need;
                    bestLevel = level;
                }
            }

            if (bestLevel == null) {

                throw new BadRequestException(
                        "Not enough questions to generate the requested paper."
                );
            }

            selected.put(
                    bestLevel,
                    selected.get(bestLevel) + 1
            );

            selectedTotal++;
        }

        /*
         * Collect selected questions.
         */
        List<Question> selectedQuestions =
                new ArrayList<>();

        for (String level : LEVELS) {

            int count =
                    selected.get(level);

            selectedQuestions.addAll(
                    pools.get(level)
                            .subList(
                                    0,
                                    count
                            )
            );
        }

        /*
         * Shuffle final paper order.
         */
        Collections.shuffle(selectedQuestions);

        /*
         * Service-level duplicate protection.
         */
        Set<Long> questionIds =
                new HashSet<>();

        for (Question question :
                selectedQuestions) {

            if (!questionIds.add(
                    question.getId()
            )) {

                throw new BadRequestException(
                        "Duplicate question detected."
                );
            }
        }

        /*
         * Create TestPaper.
         */
        TestPaper paper =
                new TestPaper();

        paper.setTitle(
                request.getTitle().trim()
        );

        paper.setTotalQuestions(
                total
        );

        paper.setEasyPercentage(
                request.getEasyPercent()
        );

        paper.setMediumPercentage(
                request.getMediumPercent()
        );

        paper.setHardPercentage(
                request.getHardPercent()
        );

        paper.setGeneratedAt(
                LocalDateTime.now()
        );

        paper =
                testPaperRepository.save(paper);

        /*
         * Create paper-question links.
         */
        List<TestPaperQuestion> links =
                new ArrayList<>();

        int order = 1;

        for (Question question :
                selectedQuestions) {

            /*
             * Database-aware duplicate check.
             */
            if (testPaperQuestionRepository
                    .existsByTestPaper_IdAndQuestion_Id(
                            paper.getId(),
                            question.getId()
                    )) {

                throw new BadRequestException(
                        "Question "
                                + question.getId()
                                + " is already present in this paper."
                );
            }

            TestPaperQuestion link =
                    new TestPaperQuestion();

            link.setTestPaper(paper);

            link.setQuestion(question);

            link.setQuestionOrder(order++);

            links.add(link);

            /*
             * Increase question usage frequency.
             */
            Integer usage =
                    question.getUsageCount();

            int currentUsage =
                    usage == null
                            ? 0
                            : usage;

            question.setUsageCount(
                    currentUsage + 1
            );
        }

        /*
         * Save updated question usage.
         */
        questionRepository.saveAll(
                selectedQuestions
        );

        /*
         * Save paper-question mappings.
         */
        testPaperQuestionRepository.saveAll(
                links
        );

        paper.setQuestions(links);

        return paper;
    }

    /*
     * Get all generated papers.
     */
    public List<TestPaper> findAll() {
        return testPaperRepository.findAll();
    }

    /*
     * Get one paper by ID.
     */
    public TestPaper findById(Long id) {

        return testPaperRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Test paper not found: " + id
                        )
                );
    }

    /*
     * Validate generation request.
     */
    private void validateRequest(
            GeneratePaperRequest request) {

        int totalPercentage =
                request.getEasyPercent()
                        + request.getMediumPercent()
                        + request.getHardPercent();

        if (totalPercentage != 100) {

            throw new BadRequestException(
                    "Easy + Medium + Hard percentages must equal 100."
            );
        }
    }

    /*
     * Calculate desired number of questions
     * for each difficulty.
     */
    private Map<String, Integer> calculateCounts(
            GeneratePaperRequest request) {

        int total =
                request.getTotalQuestions();

        double easy =
                total *
                        request.getEasyPercent()
                        / 100.0;

        double medium =
                total *
                        request.getMediumPercent()
                        / 100.0;

        double hard =
                total *
                        request.getHardPercent()
                        / 100.0;

        int easyFloor =
                (int) Math.floor(easy);

        int mediumFloor =
                (int) Math.floor(medium);

        int hardFloor =
                (int) Math.floor(hard);

        Map<String, Integer> result =
                new LinkedHashMap<>();

        result.put(
                "Easy",
                easyFloor
        );

        result.put(
                "Medium",
                mediumFloor
        );

        result.put(
                "Hard",
                hardFloor
        );

        int remaining =
                total
                        - easyFloor
                        - mediumFloor
                        - hardFloor;

        Map<String, Double> remainder =
                new HashMap<>();

        remainder.put(
                "Easy",
                easy - easyFloor
        );

        remainder.put(
                "Medium",
                medium - mediumFloor
        );

        remainder.put(
                "Hard",
                hard - hardFloor
        );

        while (remaining > 0) {

            String best =
                    remainder.entrySet()
                            .stream()
                            .max(
                                    Map.Entry.comparingByValue()
                            )
                            .orElseThrow()
                            .getKey();

            result.put(
                    best,
                    result.get(best) + 1
            );

            /*
             * Prevent selecting the same remainder
             * again during this calculation.
             */
            remainder.put(
                    best,
                    -1.0
            );

            remaining--;
        }

        return result;
    }

    /*
     * Get target question count for one
     * difficulty level.
     */
    private double getTargetCount(
            String level,
            int total,
            GeneratePaperRequest request) {

        return switch (level) {
            case "Easy" ->
                    total *
                            request.getEasyPercent()
                            / 100.0;

            case "Medium" ->
                    total *
                            request.getMediumPercent()
                            / 100.0;

            case "Hard" ->
                    total *
                            request.getHardPercent()
                            / 100.0;

            default ->
                    0.0;
        };
    }
}