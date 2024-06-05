package com.example.app.controller;

import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.ProjectFile;
import com.example.app.domain.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/th/project")
public class ProjectDetailController {

    @Autowired
    ProjectRepository projectRepository;

    @GetMapping("/project/{proCode}")
    String project(@PathVariable("proCode") String proCode, Model model) {
        System.out.println(proCode);
        Integer projectCode = Integer.parseInt(proCode);
        Optional<Project> project = projectRepository.findByProCode(projectCode);
        List<ProjectFile> projectFileList = project.get().getProjectFileList();
        System.out.println(project);
        System.out.println(projectFileList);
        // project 객체 null, 빈 값 체크
        if (project.isPresent()) {
            model.addAttribute("Project",project.get());
            model.addAttribute("image1",projectFileList.get(0).getStoredFileName());
            model.addAttribute("image2",projectFileList.get(1).getStoredFileName());
            model.addAttribute("image3",projectFileList.get(2).getStoredFileName());

            return "th/project/project.html";
        } else {
            return "redirect:/th/main/main";
        }

    }
}
