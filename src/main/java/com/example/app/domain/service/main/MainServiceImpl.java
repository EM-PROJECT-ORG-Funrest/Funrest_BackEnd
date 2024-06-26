package com.example.app.domain.service.main;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.service.notify.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MainServiceImpl {

    @Autowired
    private ProjectRepository projectRepository;

    //페이지 처음 로드시
    public List<ProjectDto> getAllProjectsOrderedByProCode() {
        List<Project> projects = projectRepository.findAllByProStatusOrderByProCodeDesc(1);
        List<ProjectDto> projectDtos = projects.stream()
                .map(ProjectDto::toProjectDto)
                .collect(Collectors.toList());

        return projectDtos;
    }

    // 첫 페이지 로드시 인피니티 스크롤 + 카테고리 항목 중 "전체" 클릭시
    public Page<ProjectDto> getAllProjectsOrderedByProCode(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByProStatusOrderByProCodeDesc(pageable, 1);
        return projects.map(ProjectDto::toProjectDto);
    }

    // 카테고리 검색
    public Page<ProjectDto> getAllProjectByProCategory(String proCategory, Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAllByProCategoryAndProStatusOrderByProCodeDesc(proCategory, pageable, 1);

        if (projectPage.isEmpty() && pageable.getPageNumber() > 0) {
            Page<Project> projectPage2 = projectRepository.findAllByProCategoryAndProStatusOrderByProCodeDesc(proCategory, pageable.previousOrFirst(), 1);
            return projectPage2.map(ProjectDto::toProjectDto);
        }
        return projectPage.map(ProjectDto::toProjectDto);
    }

    // 카테고리 검색 - 오픈예정
    public Page<ProjectDto> getAllProjectByComingSoon(Pageable pageable) {
        // 프로젝트 조회
        List<Project> projects = projectRepository.findAllByProStatusOrderByProCode(1);
        // 날짜 비교를 위한 변수 설정
        LocalDate today = LocalDate.now();
        // ProjectDto 리스트로 변환
        List<ProjectDto> projectDtos = projects.stream()
                .map(project -> {
                    ProjectDto projectDto = ProjectDto.toProjectDto(project);
                    LocalDate target = LocalDate.parse(projectDto.getProStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    long remainingDays = ChronoUnit.DAYS.between(today, target);
                    projectDto.setProRemainingDay(remainingDays);
                    return projectDto;
                })
                .filter(projectDto -> projectDto.getProRemainingDay() > 0) // 남은 날짜가 0 이상인 것만 필터링
                .collect(Collectors.toList());

        // 페이지네이션 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), projectDtos.size());
        return new PageImpl<>(projectDtos.subList(start, end), pageable, projectDtos.size());
    }

    // List<Project> -> Page<ProjectDto>
    public Page<ProjectDto> entityListToPage(List<Project> projectList, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), projectList.size());
        Page<Project> projectListToPage = new PageImpl<>(projectList.subList(start, end), pageRequest, projectList.size());
        return projectListToPage.map(ProjectDto::toProjectDto);
    }

    // List<ProjectDto> -> Page<ProjectDto>
    public Page<ProjectDto> dtoListToPage(List<ProjectDto> projectList, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), projectList.size());
        Page<ProjectDto> projectListToPage = new PageImpl<>(projectList.subList(start, end), pageRequest, projectList.size());
        return projectListToPage;
    }

    // 키워드 검색
    public Page<ProjectDto> getProjectByProName(String proName, Pageable pageable) {
        Page<Project> projectPage = projectRepository.findByProNameContainingAndProStatus(proName, pageable, 1);

        // 첫번째 페이지에서 12개 이상이 안되면 페이지에 도출 자체가 안되서  이코드 추가
        if (projectPage.isEmpty() && pageable.getPageNumber() > 0) {
            Page<Project> projectPage2 = projectRepository.findByProNameContainingAndProStatus(proName, pageable.previousOrFirst(), 1);
            return projectPage2.map(ProjectDto::toProjectDto);
        }
        return projectPage.map(ProjectDto::toProjectDto);
    }

    // 키워드 검색할 때 아무 값도 안 넘겨준 경우
    public Page<ProjectDto> findAllByOrderByProCode(Pageable pageable) {
        Page<Project> projectPage = projectRepository.findAllByProStatusOrderByProCodeDesc(pageable, 1);

        if (projectPage.isEmpty() && pageable.getPageNumber() > 0) {
            Page<Project> projectPage2 = projectRepository.findAllByProStatusOrderByProCodeDesc(pageable.previousOrFirst(), 1);
            return projectPage2.map(ProjectDto::toProjectDto);
        }

        return projectPage.map(ProjectDto::toProjectDto);
    }

}
