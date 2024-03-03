package com.devapi.services;

import com.devapi.Exceptions.CustomDevApiException;
import com.devapi.entities.Test;
import com.devapi.requestentities.CreateTestRequest;
import com.devapi.requestentities.UpdateTestRequest;
import com.devapi.responseObjects.TestDetailsResponse;
import com.devapi.responseObjects.UserTestDetailsResponse;

import java.util.List;
import java.util.UUID;

public interface TestService {
    public Test createTest(CreateTestRequest createTestRequest) throws Exception;

    public void startTest(UpdateTestRequest updateTestRequest) throws Exception;

    public List<UserTestDetailsResponse> getAllTestForUser(UUID userId) throws Exception;

    public TestDetailsResponse getTestDetails(UUID testId, UUID userId) throws Exception;

    public void updateTest(UpdateTestRequest updateTestRequest) throws CustomDevApiException;
}
