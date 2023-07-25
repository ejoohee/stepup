package com.pi.stepup.domain.dance.service;

import com.pi.stepup.domain.dance.dao.DanceRepository;
import com.pi.stepup.domain.dance.domain.RandomDance;
import com.pi.stepup.domain.dance.dto.DanceRequestDto.DanceSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DanceServiceImpl implements DanceService {

    private final DanceRepository danceRepository;

    @Override
    @Transactional
    public RandomDance create(DanceSaveRequestDto danceSaveRequestDto) {
        RandomDance randomDance = danceSaveRequestDto.toEntity();
        RandomDance createdDance = danceRepository.insert(randomDance);

        return createdDance;
    }

    @Override
    public RandomDance readOne(Long randomDanceId) {
        return danceRepository.findOne(randomDanceId);
    }

    @Override
    @Transactional
    public RandomDance update(DanceSaveRequestDto danceSaveRequestDto) {
        RandomDance randomDance = danceSaveRequestDto.toEntity();
        return danceRepository.update(randomDance);
    }

    @Override
    @Transactional
    public void delete(Long randomDanceId) {
        danceRepository.delete(randomDanceId);
    }

}