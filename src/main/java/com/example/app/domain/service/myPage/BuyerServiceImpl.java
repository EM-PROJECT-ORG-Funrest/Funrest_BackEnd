package com.example.app.domain.service.myPage;

import com.example.app.domain.dto.NotifyDto;
import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Notify;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.NotifyRepository;
import com.example.app.domain.repository.OrderRepository;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class BuyerServiceImpl {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;


    //사용자별 알림 신청 횟수 찾기
    public Long countNotifyByUserId(User user) {
        return notifyRepository.countByUserId(user);
    }

    //사용자별 결제/환불 내역 횟수 찾기
    public Long countByUserIdAndOrderState(User userId, String orderState) {
        return orderRepository.countByUserIdAndOrderState(userId, orderState);
    }

    //사용자Id 로 사용자 정보 찾기
    public User findUserByUserId(String userId) {
        Optional<User> findUserName = userRepository.findByUserId(userId);

        return findUserName.orElse(null);
    }

    //사용자Id로 사용자 삭제
    public void deleteByUserId(String userId) {
        userRepository.deleteByUserId(userId);
    }

    //사용자Id로 알림 리스트 찾기
    public List<NotifyDto> getAllNotifyByUserId(String userId) {
        log.info("getAllNotifyByUserId() execute.. userId : " + userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Notify> notifies = notifyRepository.findAllByUserId(user);
        List<NotifyDto> notifyDtos = notifies.stream()
                .map(NotifyDto::EntityToNotifyDto)
                .collect(Collectors.toList());

        log.info("notifyDtos : " + notifyDtos);

        return notifyDtos;
    }

    //알림 리스트 -> 프로젝트 리스트
    public List<ProjectDto> getAllProjectByProCode(List<NotifyDto> notifyDtos) {
        log.info("getAllProjectByProCode() execute..");

        List<ProjectDto> projectDtos = new ArrayList<>();

        for (NotifyDto notifyDto : notifyDtos) {
            ProjectDto projectDto = ProjectDto.toDto(projectRepository.findByProCode(notifyDto.getProCode()));
            if (projectDto == null) {
                // proCode로 조회되는 프로젝트가 없을 때 throw error
                throw new RuntimeException();
            }
            projectDtos.add(projectDto);
        }

        return projectDtos;
    }
}
