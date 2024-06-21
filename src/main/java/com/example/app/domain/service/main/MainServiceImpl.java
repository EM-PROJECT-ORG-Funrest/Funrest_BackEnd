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
        Page<Project> projects = projectRepository.findAllByProStatusOrderByProCodeDesc(pageable,1);
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
    public Page<ProjectDto> getAllProjectByComingSoon(Pageable pageable, int page, int size){
        List<Project> projectPage = projectRepository.findAllByProStatusOrderByProCode(1);

        if (projectPage.isEmpty() && pageable.getPageNumber() > 0) {
            List<Project> projectPage2 = projectRepository.findAllByProStatusOrderByProCode(1);
            return entityListToPage(projectPage2, page, size);
        }

        int projectListSize = projectPage.size();
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate target = null;

        List<ProjectDto> projectDtos = projectPage.stream()
                .map(ProjectDto::toProjectDto)
                .collect(Collectors.toList()); // Stream을 List로 변환하여 ListIterator 사용 가능

        ListIterator<ProjectDto> iterator = projectDtos.listIterator();
        while (iterator.hasNext()) {
            ProjectDto projectDto = iterator.next();
            target = LocalDate.parse(projectDto.getProStartDate(), DATE_FORMATTER);
            long remainingDays = java.time.temporal.ChronoUnit.DAYS.between(today, target);
            projectDto.setProRemainingDay(remainingDays);
            log.info("remainingDays : " + remainingDays);
            if (remainingDays < 0) {
                iterator.remove();
                projectListSize--;
            }
        }

        if (projectListSize == 0) {
            List<Project> projectPage2 = projectRepository.findAllByProStatusOrderByProCode(1);
            return entityListToPage(projectPage2, page, size);
        }

        List<ProjectDto> projectPageResult = new ArrayList<>();
        while(iterator.hasNext()) {
            projectPageResult.add(iterator.next());
        }

        return dtoListToPage(projectPageResult, page, size);
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
    public Page<ProjectDto> getProjectByProName(String proName, Pageable pageable){
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


//    public Page<ProjectDto> findByProNameAndCategory(String proName, String proCategory, String sortingMethod, Pageable pageable) {
//
//        // sortingMethod == 1
//            // proName - N && proCategory - N
//            Page<Project> projectPage = projectRepository.find
//
//            // proName - N
//
//            // proCategory - N
//
//            // NN
//
//        // sortingMethod == 2
//
//
//
//        // sortingMethod == 3
//
//
//        if (projectPage.isEmpty() && pageable.getPageNumber() > 0) {
//            Page<Project> projectPage2 = projectRepository.findByProNameContaining(proName, pageable.previousOrFirst());
//            return projectPage2.map(ProjectDto::toProjectDto);
//        }
//
//        return projectPage.map(ProjectDto::toProjectDto);
//    }


//    public Page<ProjectDto> ListToPage(int page, int size, List<ProjectDto> projectDtoList){
//        PageRequest pageRequest = PageRequest.of(page, size);
//        int start = (int) pageRequest.getOffset();
//        int end = Math.min((start + pageRequest.getPageSize()), projectDtoList.size());
//
//        // 페이지가 유효한 범위를 벗어나는 경우 빈 페이지를 반환
//        if (start > projectDtoList.size()) {
//            return new PageImpl<>(List.of(), pageRequest, projectDtoList.size());
//        }
//
//        List<ProjectDto> subList = projectDtoList.subList(start, end);
//        return new PageImpl<>(subList, pageRequest, projectDtoList.size());
//    }
}
