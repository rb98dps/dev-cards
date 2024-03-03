package com.devapi.controllers;

import com.devapi.services.ProblemService;
import com.devapi.entities.Problem;
import com.devapi.requestentities.CreateProblemRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.responseObjects.Response;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RepositoryRestController(path = "/problems")
public class ProblemController {

    @Autowired
    ProblemService problemService;

    @PostMapping("create-problem")
    public Response createProblem(@RequestBody @NonNull CreateProblemRequest createProblemRequest) {
        Problem problem = problemService.createProblem(createProblemRequest);
        return new BasicResponse<>("Problem Created ", HttpStatus.OK.value(), problem);
    }
}
