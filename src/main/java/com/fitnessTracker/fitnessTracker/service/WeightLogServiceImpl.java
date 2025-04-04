package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.WeightLog;
import com.fitnessTracker.fitnessTracker.repository.WeightLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WeightLogServiceImpl implements WeightLogService {

    private final WeightLogRepository weightLogRepository;

    @Autowired
    public WeightLogServiceImpl(WeightLogRepository weightLogRepository) {
        this.weightLogRepository = weightLogRepository;
    }

    @Override
    public WeightLog saveWeightLog(WeightLog weightLog) {
        return weightLogRepository.save(weightLog);
    }

    @Override
    public List<WeightLog> getWeightLogsForUser(Long userId) {
        return weightLogRepository.findByUser_Id(userId);
    }

    @Override
    public List<WeightLog> getWeightLogsForUserBetweenDates(Long userId, LocalDate startDate, LocalDate endDate) {
        return weightLogRepository.findByUser_IdAndLogDateBetween(userId, startDate, endDate);
    }
}