package com.devapi.services;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.Exceptions.UserException;
import com.devapi.dao.*;
import com.devapi.entities.*;
import com.devapi.util.DevApiUtilities;
import com.devapi.requestentities.CreateTestRequest;
import com.devapi.requestentities.UpdateTestRequest;
import com.devapi.responseObjects.ProblemDetailResponse;
import com.devapi.responseObjects.TestDetailsResponse;
import com.devapi.responseObjects.UserTestDetailsResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static com.devapi.util.DevApiUtilities.*;

@Service
public class TestServiceImpl implements TestService {


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

    @Override
    @Transactional
    public Test createTest(CreateTestRequest createTestRequest) throws Exception {
        UUID subTopicId = createTestRequest.getSubTopicId();
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new EntityNotFoundException("SubTopic not found with ID: " + subTopicId));
        Timestamp threeDaysAgoTimestamp = subtractDays(new Timestamp(System.currentTimeMillis()), 3, "+05:30");
        List<Problem> problems;
        UUID userId = createTestRequest.getUserId();
        Optional<User> user = userService.findById(userId);
        Test test = new Test();
        if (null != userId && user.isPresent()) {
            problems = getProblemsIfUserExist(createTestRequest, subTopic, threeDaysAgoTimestamp);
        } else {
            problems = problemRepository.getProblemBySubTopicIs(subTopic);
        }
        //add ratio of the seen and not seen later on
        List<Problem> finalProblems = getRandomSubset(problems, createTestRequest.getNoOfQuestions());
        addProblemsToTest(test, finalProblems);
        testRepository.save(test);
        if (null == userId || user.isEmpty()) {
            user = Optional.ofNullable(userService.getDummyUser());
        }
        assignTestToUser(user.get(), test, createTestRequest.getTime());
        return test;
    }

    private void assignTestToUser(User user, Test test, int time) {
        UserTestId userTestId = new UserTestId(user.getId(), test.getId());
        UserTest userTest = UserTest.builder().id(userTestId).user(user).score(0)
                .problemsAttempted(0).test(test).givenTime(time).build();
        userTestRepository.save(userTest);
    }

    private void addProblemsToTest(Test test, List<Problem> problems) {
        test.setTotalProblems(problems.size());
        test.setTotalScore(problems.stream()
                .mapToInt(Problem::getProblemMarks)
                .sum());

        int order = 0;
        for (Problem problem : problems) {
            TestProblemId testProblemId = new TestProblemId(test.getId(), problem.getId());
            TestProblem testProblem = TestProblem.builder().id(testProblemId).orderNo(order).problem(problem).test(test).build();
            testProblemRepository.save(testProblem);
            test.addTestProblem(testProblem);
            order++;

        }
    }

    private List<Problem> getProblemsIfUserExist(CreateTestRequest createTestRequest, SubTopic subTopic, Timestamp nDaysAgoTimestamp) {
        List<Problem> finalProblems = new ArrayList<>();
        List<Problem> ProblemsSeenNDaysAgo = problemRepository.getProblemsSeenBeforeTimeStamp(createTestRequest.getUserId(), subTopic, nDaysAgoTimestamp);
        List<Problem> ProblemsNotSeen = userProblemDao.getProblemsNotinProgress(createTestRequest.getUserId(), createTestRequest.getNoOfQuestions());
        int totalProblems = ProblemsSeenNDaysAgo.size() + ProblemsNotSeen.size();


        if (totalProblems < createTestRequest.getNoOfQuestions()) {
            List<Problem> ProblemsSeenInNDays = problemRepository.getProblemsSeenAfterTimeStamp(createTestRequest.getUserId(), subTopic, nDaysAgoTimestamp);
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

        return finalProblems;
    }
    @Transactional
    public List<UserTestDetailsResponse> getAllTestForUser(UUID userId) throws Exception {

        User user = userService.findById(userId).orElseThrow(()-> new CustomDevApiException(UserException.USER_NOT_FOUND));
        List<UserTest> userTests = userTestRepository.findUserTestsByUserAndEndedIsTrue(user);
        List<UserTestDetailsResponse> userTestDetailsResponses = new ArrayList<>();
        for(UserTest userTest: userTests){
            UserTestDetailsResponse userTestDetailsResponse = getUserTestDetailsResponse(userTest);
            userTestDetailsResponses.add(userTestDetailsResponse);
        }
        return userTestDetailsResponses;
    }

    private UserTestDetailsResponse getUserTestDetailsResponse(UserTest userTest) {
        Test test = userTest.getTest();
        Set<String> topicNames = new HashSet<>();
        Set<String> subTopicNames = new HashSet<>();
        for(TestProblem testProblem: test.getTestProblems()){
            Problem problem = testProblem.getProblem();
            topicNames.add(problem.getTopic().getName());
            subTopicNames.add(problem.getSubTopic().getName());
        }
        return UserTestDetailsResponse.builder().score(userTest.getScore())
                .startTime(userTest.getStartTime()).totalProblems(test.getTotalProblems()).givenTime(userTest.getGivenTime())
                .totalScore(test.getTotalScore()).subTopicName(subTopicNames).topicName(topicNames).build();
    }

    public void startTest(UpdateTestRequest updateTestRequest) throws Exception {
        UUID testId = updateTestRequest.getTestId();
        UUID userId = updateTestRequest.getUserId();
        UserTest userTest = userTestRepository.findById(UserTestId.builder().userId(userId).testId(testId).build())
                .orElseThrow(() -> new EntityNotFoundException("Test not found for the user"));
        if(checkTestStarted(userTest)){
            throw new CustomDevApiException("Test already started");
        }
        if(checkTestEnded(userTest)){
            throw new CustomDevApiException("Test cannot be started as it has already ended");
        }
        Timestamp currtimestamp = DevApiUtilities.getCurrentTimeStamp();
        userTest.setStartTime(currtimestamp);
        int givenTime = userTest.getGivenTime();
        Instant instant = currtimestamp.toInstant();
        Instant sumInstant = instant.plusSeconds(givenTime);
        userTest.setEndTime(Timestamp.from(sumInstant));
        userTest.setEnded(false);
        userTestRepository.save(userTest);
        
    }
    private boolean checkTestEnded(UserTest userTest) {
        Timestamp currtimestamp = DevApiUtilities.getCurrentTimeStamp();
        if (userTest.getEndTime() == null)
            return false;

        Timestamp testEndtimestamp = Timestamp.from(userTest.getEndTime().toInstant());
        if (testEndtimestamp.before(currtimestamp)) {
            userTest.setEnded(true);
        }
        return userTest.isEnded();
    }

    private boolean checkTestStarted(UserTest userTest) {
        return userTest.getStartTime() != null;
    }

    @Transactional
    public void updateTest(UpdateTestRequest updateTestRequest) throws CustomDevApiException {
        UUID testId = updateTestRequest.getTestId();
        UUID userId = updateTestRequest.getUserId();

        UserTest userTest = userTestRepository.findById(UserTestId.builder().userId(userId).testId(testId).build())
                .orElseThrow(() -> new CustomDevApiException("Test not found for the user"));
        if(!checkTestStarted(userTest)){
            throw new CustomDevApiException("Test not started yet");
        }
        if(checkTestEnded(userTest)){
            throw new CustomDevApiException("Test already ended");
        }

        UUID problemId = updateTestRequest.getProblemId();

        Problem problem = problemRepository.findById(problemId).orElseThrow(() -> new CustomDevApiException("Problem not found "));
        HashMap<UUID, Boolean> mcqAnswersList = updateTestRequest.getAnswers();


        StringBuilder answers = checkMcqForCorrectAnswers(problem, mcqAnswersList, userTest);
        UserProblemId userProblemId = new UserProblemId(userId, problemId, testId);
        UserProblem userProblem = UserProblem.builder().id(userProblemId).attempted(true).attemptTime(updateTestRequest.getTimeTaken()).build();
        Timestamp currentTimeStamp = getCurrentTimeStamp();
        userProblem.setAttemptDate(currentTimeStamp);
        userProblem.setTest(userTest.getTest());
        userProblem.setProblem(problem);
        userProblem.setUser(userTest.getUser());
        userProblem.setOptionSelected(answers.substring(0, answers.length() - 1));

        if (userProblemRepository.findById(userProblemId).isEmpty()) {
            userTest.setProblemsAttempted(userTest.getProblemsAttempted() + 1);
        }
        userProblemRepository.save(userProblem);

        if (updateTestRequest.isEndTest() && !userTest.isEnded()) {
            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(new Timestamp(System.currentTimeMillis()).toInstant(), ZoneOffset.of("+05:30"));
            userTest.setEndTime(new Date(zonedDateTime.toEpochSecond()));
            userTest.setEnded(true);
        }
        userTestRepository.save(userTest);
    }

    private StringBuilder checkMcqForCorrectAnswers(Problem problem, HashMap<UUID, Boolean> mcqAnswersList, UserTest userTest) {
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
        return answers;
    }

    public TestDetailsResponse getTestDetails(UUID testId, UUID userId) throws Exception {
        Test test = testRepository.findById(testId).orElseThrow(() -> new CustomDevApiException("Test not found"));
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
        UserTest usertest = userTestRepository.findById(new UserTestId(userId, testId)).orElseThrow(() -> new CustomDevApiException(UserException.USER_NOT_FOUND));
        testDetailsResponse = TestDetailsResponse.builder().startTime(usertest.getStartTime())
                .endTime(usertest.getEndTime()).givenTime(usertest.getGivenTime())
                .totalScore(usertest.getScore()).attempted(usertest.getProblemsAttempted()).build();
        testDetailsResponse.setProblemDetailResponseList(problemDetailResponseList);
        testDetailsResponse.setTotalProblems(test.getTotalProblems());
        return testDetailsResponse;
    }

}
