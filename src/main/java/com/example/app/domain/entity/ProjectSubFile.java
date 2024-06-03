package com.example.app.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tbl_project_subFile")
public class ProjectSubFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String subOriginalFileName;

    @Column
    private String subStoredFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proCode")
    private Project project; // 부모 엔터티 파일 자체를 칼럼으로 지정하는 것을 주의!

    public static ProjectSubFile toProjectSubFileEntity(Project project, String originalFileName, String storedFileName) {
        ProjectSubFile projectFile = new ProjectSubFile();
        projectFile.setSubOriginalFileName(originalFileName);
        projectFile.setSubStoredFileName(storedFileName);
        projectFile.setProject(project);
        return projectFile;
    }

}
