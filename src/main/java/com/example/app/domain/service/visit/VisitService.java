package com.example.app.domain.service.visit;

import com.example.app.domain.dto.VisitDto;
import com.example.app.domain.entity.Visit;
import com.example.app.domain.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class VisitService {

    @Autowired
    VisitRepository visitRepository;

    // 당일 방문자 기록
    @Transactional
    public void recordVisit() {
        LocalDate today = LocalDate.now();
        Visit visit = visitRepository.findVisitByDate(today);

        if (visit == null) {
            visit = new Visit();
            visit.setVisitDate(today);
            visit.setVisitNum(1);
            visitRepository.save(visit);
        } else {
            visit.setVisitNum(visit.getVisitNum() + 1);
            visitRepository.save(visit);
        }
    }

    // 방문 데이터 검색
    public List<VisitDto> getVisitData() {
        return visitRepository.findAll().stream()
                .map(visit -> new VisitDto(visit.getVisitDate(), visit.getVisitNum()))
                .collect(Collectors.toList());
    }
}
