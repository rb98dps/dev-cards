package com.devapi.services;

import com.devapi.dao.McqRepository;
import com.devapi.dao.ProblemRepository;
import com.devapi.dao.SubTopicRepository;
import com.devapi.util.DevApiUtilities;
import com.devapi.entities.Mcq;
import com.devapi.entities.Problem;
import com.devapi.entities.SubTopic;
import com.devapi.requestentities.CreateProblemRequest;
import com.devapi.requestentities.McqCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Service
public class ProblemServiceImpl implements ProblemService{

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    SubTopicRepository subTopicRepository;

    @Autowired
    McqRepository mcqRepository;
    @Transactional
    public Problem createProblem(CreateProblemRequest createProblemRequest){
        UUID subTopicId = createProblemRequest.getSubTopicId();
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
                .orElseThrow(() -> new EntityNotFoundException("SubTopic not found with ID: " + subTopicId));
        //System.out.println(subTopic);
        Problem problem = DevApiUtilities.mapObjectToObjectWithModelMapper(createProblemRequest,Problem.class);
        List<Mcq> mcqs = new ArrayList<>();
        for(McqCreateRequest listMcq: createProblemRequest.getMcqList()){
            Mcq mcq = DevApiUtilities.mapObjectToObjectWithModelMapper(listMcq, Mcq.class);
            mcq.getProblems().add(problem);
            mcqRepository.save(mcq);
            mcqs.add(mcq);
        }
        problem.setSubTopic(subTopic);
        problem.setMcqs(mcqs);
        problem.setTopic(subTopic.getTopic());
        problemRepository.save(problem);
        return problem;
    }
}
