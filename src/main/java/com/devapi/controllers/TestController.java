package com.devapi.controllers;

import com.devapi.dao.*;
import com.devapi.model.entities.*;
import com.devapi.model.requestentities.CreateTestRequest;
import com.devapi.model.requestentities.UpdateTestRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.ProblemDetailResponse;
import com.devapi.responseObjects.TestDetailsResponse;
import com.devapi.services.UserService;
import com.devapi.util.DevApiUtilities;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static com.devapi.util.DevApiUtilities.getRandomSubset;
import static com.devapi.util.DevApiUtilities.subtractDays;

@RepositoryRestController(path = "/tests")
public class TestController {

    @Autowired
    TestRepository testRepository;

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    UserProblemDao userProblemDao;

    @Autowired
    SubTopicRepository subTopicRepository;
    private UserService userService;

    @Autowired
    UserProblemRepository userProblemRepository;

    @Autowired
    private UserTestRepository userTestRepository;

    @Autowired
    private TestProblemRepository testProblemRepository;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("create-test")
    @Transactional
    public ResponseEntity<?> createTest(@RequestBody @NonNull CreateTestRequest createTestRequest) throws Exception {
        UUID subTopicId = createTestRequest.getSubTopicId();
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new EntityNotFoundException("SubTopic not found with ID: " + subTopicId));
        Timestamp threeDaysAgoTimestamp = subtractDays(new Timestamp(System.currentTimeMillis()), 3, "+05:30");
        List<Problem> finalProblems = new ArrayList<>();
        Optional<User> user = userService.findById(createTestRequest.getUserId());
        Test test = new Test();

        if (null != createTestRequest.getUserId() && user.isPresent()) {

            List<Problem> ProblemsSeenNDaysAgo = problemRepository.getProblems(createTestRequest.getUserId(), subTopic, threeDaysAgoTimestamp);

            List<Problem> ProblemsNotSeen = userProblemDao.getProblemsNotinProgress(createTestRequest.getUserId(), createTestRequest.getNoOfQuestions());

            System.out.println(ProblemsSeenNDaysAgo);
            System.out.println(ProblemsNotSeen);

            int totalProblems = ProblemsSeenNDaysAgo.size() + ProblemsNotSeen.size();


            if (totalProblems < createTestRequest.getNoOfQuestions()) {
                List<Problem> ProblemsSeenInNDays = problemRepository.getProblems1(createTestRequest.getUserId(), subTopic, threeDaysAgoTimestamp);
                finalProblems.addAll(ProblemsSeenNDaysAgo);
                finalProblems.addAll(ProblemsNotSeen);
                if (totalProblems + ProblemsSeenInNDays.size() < createTestRequest.getNoOfQuestions())
                    throw new EntityNotFoundException("No. of Problems Requested more than their exist");
                for (int i = totalProblems; i < createTestRequest.getNoOfQuestions(); i++)
                    finalProblems.add(ProblemsSeenInNDays.get(i - totalProblems));
            } else {
                finalProblems.addAll(ProblemsNotSeen);
                finalProblems.addAll(ProblemsSeenNDaysAgo);
            }


        } else {
            finalProblems.addAll(problemRepository.getProblemBySubTopicIs(subTopic));
        }
        //add ratio of the seen and not seen later on
        finalProblems = getRandomSubset(finalProblems, createTestRequest.getNoOfQuestions());


        test.setTotalProblems(finalProblems.size());
        test.setTotalScore(finalProblems.stream()
                .mapToInt(Problem::getProblemMarks)
                .sum());
        testRepository.save(test);
        int order = 0;
        for (Problem problem : finalProblems) {
            TestProblemId testProblemId = new TestProblemId(test.getId(), problem.getId());
            TestProblem testProblem = TestProblem.builder().id(testProblemId).orderNo(order).problem(problem).test(test).build();
            testProblemRepository.save(testProblem);
            test.addTestProblem(testProblem);
            order++;

        }
        testRepository.save(test);
        UUID userId = createTestRequest.getUserId();
        if (null != userId || user.isEmpty()) {
            user = Optional.ofNullable(userService.getDummyUser());
        }
        UserTestId userTestId = new UserTestId(userId, test.getId());
        UserTest userTest = UserTest.builder().id(userTestId).user(user.get()).score(0)
                .problemsAttempted(0).test(test).givenTime(createTestRequest.getTime()).build();
        userTestRepository.save(userTest);

        return new ResponseEntity<>(new BasicResponse<>("Test Created ", HttpStatus.OK.value(), test), HttpStatus.OK);
    }

    @PostMapping("update-test")
    @Transactional
    public ResponseEntity<?> updateTest(@RequestBody @NonNull UpdateTestRequest updateTestRequest) {
        UUID testId = updateTestRequest.getTestId();
        UUID userId = updateTestRequest.getUserId();
        UUID problemId = updateTestRequest.getProblemId();
        UserProblemId userProblemId = new UserProblemId(userId, problemId, testId);
        UserProblem userProblem = UserProblem.builder().id(userProblemId).attempted(true).attemptTime(updateTestRequest.getTimeTaken()).build();
        Timestamp currentTimeStamp = subtractDays(new Timestamp(System.currentTimeMillis()), 0, "+05:30");
        userProblem.setAttemptDate(currentTimeStamp);
        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new EntityNotFoundException("Problem not found "));
        HashMap<UUID, Boolean> mcqAnswersList = updateTestRequest.getAnswers();

        UserTest userTest = userTestRepository.findById(UserTestId.builder().userId(userId).testId(testId).build())
                .orElseThrow(() -> new EntityNotFoundException("Test not found for the user"));
        userProblem.setTest(userTest.getTest());
        userProblem.setProblem(problem);
        userProblem.setUser(userTest.getUser());
        List<Mcq> mcqList = problem.getMcqs();
        mcqList.sort(Comparator.comparingInt(Mcq::getOrderNo));
        boolean correctAnswerFlag = true;
        StringBuilder answers = new StringBuilder();
        for (Mcq mcq : mcqList) {
            if (mcq.getAnswerType() != mcqAnswersList.get(mcq.getId())) {
                correctAnswerFlag = false;

            }
            if (mcqAnswersList.get(mcq.getId())) {
                answers.append("1|");
            } else {
                answers.append("0|");
            }
        }

        if (correctAnswerFlag) {
            userTest.setScore(userTest.getScore() + problem.getProblemMarks());
        }

        userProblem.setOptionSelected(answers.substring(0, answers.length() - 1));
        if (userProblemRepository.findById(userProblemId).isEmpty()) {
            userTest.setProblemsAttempted(userTest.getProblemsAttempted() + 1);
        }
        userProblemRepository.save(userProblem);
        if (updateTestRequest.isEndTest()) {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(new Timestamp(System.currentTimeMillis()).toInstant(), ZoneOffset.of("+05:30"));
            userTest.setEndTime(new Date(zonedDateTime.toEpochSecond()));
        }
        userTestRepository.save(userTest);
        return new ResponseEntity<>(new BasicResponse<>("Updated Successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @GetMapping("get-test-details")
    @Transactional
    public ResponseEntity<?> getTestDetails(@RequestParam @NonNull UUID testId, @RequestParam UUID userId) throws Exception {

        Test test = testRepository.findById(testId).orElseThrow(() -> new EntityNotFoundException("Test not found"));
        TestDetailsResponse testDetailsResponse;
        List<ProblemDetailResponse> problemDetailResponseList = new ArrayList<>();
        for (TestProblem testProblem : test.getTestProblems()) {
            ProblemDto problemDto = DevApiUtilities.mapObjectToObjectWithModelMapper(testProblem.getProblem(),ProblemDto.class);
            ProblemDetailResponse problemDetailResponse = ProblemDetailResponse.builder()
                    .problem(problemDto).orderNo(testProblem.getOrderNo()).build();
            problemDetailResponseList.add(problemDetailResponse);
        }
        problemDetailResponseList.sort(Comparator.comparingInt(ProblemDetailResponse::getOrderNo));
        if (userId == null) {
            userId = userService.getDummyUser().getId();
        }
        UserTest usertest = userTestRepository.findById(new UserTestId(userId, testId)).orElseThrow(() -> new EntityNotFoundException("User test details not found"));
        testDetailsResponse = TestDetailsResponse.builder().startTime(usertest.getStartTime())
                .endTime(usertest.getEndTime()).givenTime(usertest.getGivenTime())
                .totalScore(usertest.getScore()).attempted(usertest.getProblemsAttempted()).build();
        testDetailsResponse.setProblemDetailResponseList(problemDetailResponseList);
        testDetailsResponse.setTotalProblems(test.getTotalProblems());

        return new ResponseEntity<>(new BasicResponse<>("Test Details", HttpStatus.OK.value(),testDetailsResponse), HttpStatus.OK);
    }
}
