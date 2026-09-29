package com.examcraft.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.examcraft.dto.SubmitAttemptRequest;
import com.examcraft.entity.Attempt;
import com.examcraft.entity.TestPaper;
import com.examcraft.entity.TestPaperQuestion;
import com.examcraft.exception.BadRequestException;
import com.examcraft.exception.ResourceNotFoundException;
import com.examcraft.repository.AttemptRepository;
import com.examcraft.repository.TestPaperRepository;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final TestPaperRepository testPaperRepository;

    public AttemptService(
            AttemptRepository attemptRepository,
            TestPaperRepository testPaperRepository) {

        this.attemptRepository = attemptRepository;
        this.testPaperRepository = testPaperRepository;
    }

    @Transactional
    public Map<String, Object> submitAttempt(
            SubmitAttemptRequest request) {

        TestPaper testPaper =
                testPaperRepository.findById(
                        request.getTestPaperId()
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Test paper not found: "
                                        + request.getTestPaperId()
                        )
                );

        List<TestPaperQuestion> paperQuestions =
                testPaper.getQuestions();

        if (paperQuestions == null ||
                paperQuestions.isEmpty()) {

            throw new BadRequestException(
                    "This test paper has no questions."
            );
        }

        /*
         * Check whether extra question IDs
         * were submitted.
         */
        for (Long submittedQuestionId :
                request.getAnswers().keySet()) {

            boolean belongsToPaper =
                    paperQuestions.stream()
                            .anyMatch(
                                    item -> item.getQuestion()
                                            .getId()
                                            .equals(
                                                    submittedQuestionId
                                            )
                            );

            if (!belongsToPaper) {

                throw new BadRequestException(
                        "Question ID "
                                + submittedQuestionId
                                + " does not belong to this test paper."
                );
            }
        }

        int score = 0;

        /*
         * Auto-grade every objective question.
         *
         * Missing answer = wrong answer.
         */
        for (TestPaperQuestion paperQuestion :
                paperQuestions) {

            Long questionId =
                    paperQuestion.getQuestion().getId();

            String correctAnswer =
                    paperQuestion.getQuestion()
                            .getCorrectAnswer();

            String studentAnswer =
                    request.getAnswers()
                            .get(questionId);

            if (studentAnswer != null &&
                    correctAnswer != null &&
                    correctAnswer.trim()
                            .equalsIgnoreCase(
                                    studentAnswer.trim()
                            )) {

                score++;
            }
        }

        int totalMarks =
                paperQuestions.size();

        double percentage =
                totalMarks == 0
                        ? 0
                        : (score * 100.0) / totalMarks;

        /*
         * Save attempt.
         */
        Attempt attempt =
                new Attempt();

        attempt.setStudentName(
                request.getStudentName().trim()
        );

        attempt.setScore(score);

        attempt.setTotalMarks(totalMarks);

        attempt.setTestPaper(testPaper);

        attempt =
                attemptRepository.save(attempt);

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "attemptId",
                attempt.getId()
        );

        response.put(
                "studentName",
                attempt.getStudentName()
        );

        response.put(
                "testPaperId",
                testPaper.getId()
        );

        response.put(
                "testPaperTitle",
                testPaper.getTitle()
        );

        response.put(
                "score",
                score
        );

        response.put(
                "totalMarks",
                totalMarks
        );

        response.put(
                "percentage",
                Math.round(percentage * 100.0) / 100.0
        );

        response.put(
                "message",
                "Test submitted and graded successfully."
        );

        return response;
    }

    public List<Attempt> getAllAttempts() {
        return attemptRepository.findAll();
    }

    public Attempt getAttemptById(Long id) {

        return attemptRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Attempt not found: " + id
                        )
                );
    }
}