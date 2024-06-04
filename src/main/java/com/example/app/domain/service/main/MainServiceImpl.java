package com.example.app.domain.service.main;

import com.example.app.domain.dto.ProjectDto;
import com.example.app.domain.entity.Project;
import com.example.app.domain.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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


    public Page<ProjectDto> getAllProjectsByProName(String proName,Pageable pageable){
        Page<Project> projectPage = projectRepository.findByProNameLike(proName,pageable);
        return projectPage.map(ProjectDto::ToDto);
    }


    public List<ProjectDto> getAllProject(){
        List<Project> projects =  projectRepository.findAll();
        List<ProjectDto> projectDtos = projects.stream()
                .map(ProjectDto::ToDto)
                .collect(Collectors.toList());
        return projectDtos;
    }

    public List<ProjectDto> getAllProjectByProCategory(String proCategory){
        List<Project> projects = projectRepository.findAllByProCategory(proCategory);
        List<ProjectDto> projectDtos = projects.stream()
                .map(ProjectDto::ToDto)
                .collect(Collectors.toList());
        return projectDtos;
    }





}
