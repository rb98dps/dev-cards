package com.devapi.services;

import com.devapi.entities.Problem;
import com.devapi.requestentities.CreateProblemRequest;

public interface ProblemService {

    public Problem createProblem(CreateProblemRequest createProblemRequest);
}
