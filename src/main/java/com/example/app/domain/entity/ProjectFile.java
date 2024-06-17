package com.example.app.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tbl_project_file")
public class ProjectFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proCode", foreignKey = @ForeignKey(name = "FK_ProjectFile_Project",
            foreignKeyDefinition = "FOREIGN KEY(proCode) REFERENCES tbl_project(proCode) ON DELETE CASCADE ON UPDATE CASCADE"))
    private Project project; // 부모 엔터티 파일 자체를 칼럼으로 지정하는 것을 주의!

    public static ProjectFile toProjectFileEntity(Project project, String originalFileName, String storedFileName) {
        ProjectFile projectFile = new ProjectFile();
        projectFile.setOriginalFileName(originalFileName);
        projectFile.setStoredFileName(storedFileName);
        projectFile.setProject(project);
        return projectFile;
    }

}
