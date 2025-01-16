package com.stockexchange.orderservice.service;

import com.matchingengine.model.Execution;
import com.matchingengine.repository.ExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExecutionService {
    
    @Autowired
    private ExecutionRepository executionRepository;
    
    public void saveAllExecutions(List<Execution> executions) {
        executionRepository.saveAll(executions);
    }
}
