package com.example.app.domain.service.myPage;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.OrderRepository;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SellerServiceImpl {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    public List<ProjectDto> findProjectByUserId(User user){
        List<Project> projectList = projectRepository.findByUserId(user);
        List<ProjectDto> projectDtoList = new ArrayList<>();
        for (Project project : projectList){
            projectDtoList.add(ProjectDto.toProjectDto(project));
        }
        return projectDtoList;
    }

    public Long countProjectByUserId(User user){
        Long countByUserId = projectRepository.countByUserId(user);
        return countByUserId;
    }

    public User findUserByUserId(String userId){
        Optional<User> findUserName = userRepository.findByUserId(userId);

        return findUserName.orElse(null);
    }

    //달성률 구하기
    public void proAchievementRate(List<ProjectDto> projectDtoList) {
        for (ProjectDto projectDto : projectDtoList) {
            // 문자열을 정수로 변환
            double proGoalAmount = Double.parseDouble(projectDto.getProGoalAmount().replace(",", ""));
            double proPrice = Double.parseDouble(projectDto.getProPrice().replace(",", ""));
            double proPaidCnt = projectDto.getProPaidCnt();
            // 달성률 계산 (소수점까지 계산)
            double proAchievementRate = (proPrice * proPaidCnt / proGoalAmount) * 100.0;
            // 계산된 달성률을 반올림하여 정수로 변환
            int roundedAchievementRate = (int) Math.round(proAchievementRate);
            // projectDto에 달성률 설정
            projectDto.setProAchievementRate(roundedAchievementRate);
        }
    }

    // 신청 날짜 보여주기 포매팅
    public void formattedDate(List<ProjectDto> projectDtoList){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (ProjectDto projectDto : projectDtoList) {
            Date proDate = projectDto.getProDate();
            String formattedDate = outputFormat.format(proDate);
            projectDto.setFormattedProDate(formattedDate);
        }
    }

    // 프로젝트 기간 가져오기
    public void dateTime(List<ProjectDto> projectDtoList){
        for (ProjectDto projectDto : projectDtoList) {
            String proStartDate = projectDto.getProStartDate();
            String proEndDate = projectDto.getProEndDate();
            String dateTime = proStartDate + " ~ " + proEndDate;
            projectDto.setProDateTime(dateTime);
        }
    }

    // 프로젝트 참여 인원 가져오기
    public void proPaidCnt(List<ProjectDto> projectDtoList) {
        for (ProjectDto projectDto : projectDtoList) {
            int proCode = projectDto.getProCode();
            Optional<Project> optionalProject = projectRepository.findById(proCode);
            Project project = new Project();
            if (optionalProject.isPresent()) {
                project = optionalProject.get();
            }
            Long proPaidCnt = orderRepository.countByProCode(project);
            projectDto.setProPaidCnt(Math.toIntExact(proPaidCnt));
        }
    }
}
