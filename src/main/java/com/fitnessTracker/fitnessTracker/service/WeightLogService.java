package com.fitnessTracker.fitnessTracker.service;

import com.fitnessTracker.fitnessTracker.model.WeightLog;

import java.time.LocalDate;
import java.util.List;

public interface WeightLogService {

    WeightLog saveWeightLog(WeightLog weightLog);

    List<WeightLog> getWeightLogsForUser(Long userId);

    List<WeightLog> getWeightLogsForUserBetweenDates(Long userId, LocalDate startDate, LocalDate endDate);
}