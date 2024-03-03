package com.devapi.controllers;

import com.devapi.services.TestService;
import com.devapi.entities.Test;
import com.devapi.requestentities.CreateTestRequest;
import com.devapi.requestentities.UpdateTestRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.Response;
import com.devapi.responseObjects.TestDetailsResponse;
import com.devapi.responseObjects.UserTestDetailsResponse;
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

import java.util.List;
import java.util.UUID;

@RepositoryRestController(path = "/tests")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("create-test")
    public Response createTest(@RequestBody @NonNull CreateTestRequest createTestRequest) throws Exception {

        Test test = testService.createTest(createTestRequest);
        return new BasicResponse<>("Test Created ", HttpStatus.OK.value(), test);
    }

    @PostMapping("update-test")
    @Transactional
    public ResponseEntity<?> updateTest(@RequestBody @NonNull UpdateTestRequest updateTestRequest) throws Exception {
        testService.updateTest(updateTestRequest);
        return new ResponseEntity<>(new BasicResponse<>("Updated Successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }


    @GetMapping("get-test-details")
    @Transactional
    public ResponseEntity<?> getTestDetails(@RequestParam @NonNull UUID testId, @RequestParam UUID userId) throws Exception {

        TestDetailsResponse testDetailsResponse = testService.getTestDetails(testId, userId);
        return new ResponseEntity<>(new BasicResponse<>("Test Details", HttpStatus.OK.value(), testDetailsResponse), HttpStatus.OK);
    }


    @PostMapping("start-test")
    @Transactional
    public ResponseEntity<?> startTest(@RequestBody @NonNull UpdateTestRequest updateTestRequest) throws Exception {
        testService.startTest(updateTestRequest);
        return new ResponseEntity<>(new BasicResponse<>("Test Started Successfully", HttpStatus.OK.value()), HttpStatus.OK);
    }

    @GetMapping("get-user-tests")
    @Transactional
    public ResponseEntity<?> getAllTestForUser(@RequestParam @NonNull UUID userId) throws Exception {
        List<UserTestDetailsResponse> userTestDetailsResponses = testService.getAllTestForUser(userId);
        return new ResponseEntity<>(new BasicResponse<>("Test Started Successfully", HttpStatus.OK.value(), userTestDetailsResponses), HttpStatus.OK);
    }

}
