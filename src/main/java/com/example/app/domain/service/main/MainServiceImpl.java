package com.example.app.domain.service.main;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainServiceImpl {

    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectDto> getAllProjectsOrderedByProCode() {
        List<Project> projects = projectRepository.findAllByOrderByProCodeDesc();
        List<ProjectDto> projectDtos = projects.stream()
                .map(ProjectDto::ToDto)
                .collect(Collectors.toList());

        return projectDtos;
    }


    public Page<ProjectDto> getAllProjectsOrderedByProCode(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByOrderByProCodeDesc(pageable);
        return projects.map(ProjectDto::ToDto);
    }



    public List<ProjectDto> getAllProject(){
        List<Project> projects =  projectRepository.findAll();
        List<ProjectDto> projectDtos = projects.stream()
                .map(ProjectDto::ToDto)
                .collect(Collectors.toList());
        return projectDtos;
    }

    public Page<ProjectDto> getAllProjectByProCategory(String proCategory, Pageable pageable){
        Page<Project> projectPage = projectRepository.findAllByProCategoryOrderByProCode(proCategory, pageable);
        return projectPage.map(ProjectDto::ToDto);
    }

    public Page<ProjectDto> ListToPage(int page, int size, List<ProjectDto> projectDtoList){
        PageRequest pageRequest = PageRequest.of(page,size);
        int start =  (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), projectDtoList.size());
        Page<ProjectDto> projectDtoPage = new PageImpl<>(projectDtoList.subList(start,end), pageRequest, projectDtoList.size());
        return projectDtoPage;
    }

    public Page<ProjectDto> getProjectByProName(String proName, Pageable pageable){
        System.out.println("getProjectByProName proName : " + proName);
        Page<Project> projectPage = projectRepository.findByProNameContaining(proName, pageable);

        // 첫번째 페이지에서 12개 이상이 안되면 페이지에 도출 자체가 안되서  이코드 추가
        if (projectPage.isEmpty() && pageable.getPageNumber() > 0) {
            Page<Project> projectPage2 = projectRepository.findByProNameContaining(proName, pageable.previousOrFirst());
            return projectPage2.map(ProjectDto::ToDto);
        }
        return projectPage.map(ProjectDto::ToDto);
    }





}
