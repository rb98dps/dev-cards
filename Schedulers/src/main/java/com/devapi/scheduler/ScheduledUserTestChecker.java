package com.devapi.scheduler;


import com.devapi.dao.UserProblemRepository;
import com.devapi.dao.UserTestRepository;
import com.devapi.services.UserService;
import com.devapi.util.DevApiUtilities;

import com.devapi.entities.UserTest;
import com.devapi.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class ScheduledUserTestChecker {

    @Autowired
    private UserTestRepository userTestRepository;

    @Autowired
    private UserProblemRepository userProblemRepository;

    @Autowired
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Scheduled(cron = "0 0 * * * *") // Run every second
    public void checkEndTimeAndMarkAsEnded() {
        List<UserTest> userTests = userTestRepository.findByEndedIsFalseOrEndedIsNullAndEndTimeIsNotNull();
        Timestamp currentTime = DevApiUtilities.getCurrentTimeStamp();

        for (UserTest userTest : userTests) {
            if (userTest.getEndTime() != null && userTest.getEndTime().before(currentTime)) {
                userTest.setEnded(true);
            }
        }
        userTestRepository.saveAll(userTests);
    }
    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void removeDummyUserWhereTestEnded() throws Exception {
        User user = userService.getDummyUser();
        List<UserTest> userTests = userTestRepository.findUserTestsByUserAndEndedIsTrue(user);
        for (UserTest userTest : userTests) {
            userProblemRepository.deleteUserProblemByUserAndTest(user,userTest.getTest());
        }
        userTestRepository.deleteAll(userTests);
    }
}
