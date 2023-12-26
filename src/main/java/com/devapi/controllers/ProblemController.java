package com.devapi.controllers;

import com.devapi.dao.McqRepository;
import com.devapi.dao.ProblemRepository;
import com.devapi.dao.SubTopicRepository;
import com.devapi.model.entities.Mcq;
import com.devapi.model.entities.Problem;
import com.devapi.model.entities.SubTopic;
import com.devapi.model.requestentities.CreateProblemRequest;
import com.devapi.model.requestentities.McqCreateRequest;
import com.devapi.responseObjects.BasicResponse;
import com.devapi.util.DevApiUtilities;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RepositoryRestController(path = "/problems")
public class ProblemController {

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    SubTopicRepository subTopicRepository;

    @Autowired
    McqRepository mcqRepository;
    @PostMapping("create-problem")
    @Transactional
    public ResponseEntity<?> createProblem(@RequestBody @NonNull CreateProblemRequest createProblemRequest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        System.out.println(createProblemRequest);
        UUID subTopicId = createProblemRequest.getSubTopicId();
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new EntityNotFoundException("SubTopic not found with ID: " + subTopicId));
        //System.out.println(subTopic);
        Problem problem = (Problem) DevApiUtilities.mapObjectToObject(createProblemRequest,Problem.class);
        List<Mcq> mcqs = new ArrayList<>();
        for(McqCreateRequest listMcq: createProblemRequest.getMcqList()){
            Mcq mcq = (Mcq) DevApiUtilities.mapObjectToObject(listMcq, Mcq.class);
            mcq.getProblems().add(problem);
            mcqRepository.save(mcq);
            mcqs.add(mcq);
        }
        problem.setSubTopic(subTopic);
        problem.setMcqs(mcqs);
        problem.setTopic(subTopic.getTopic());
        problemRepository.save(problem);
        return new ResponseEntity<>(new BasicResponse<>("Problem Created ", HttpStatus.OK.value(), problem), HttpStatus.OK);
    }
}
