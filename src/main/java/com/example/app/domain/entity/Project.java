package com.example.app.domain.entity;

import com.example.app.domain.dto.ProjectDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "tbl_project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proCode", nullable = false)
    private int proCode;
    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "FK_PROJECT_USER",
            foreignKeyDefinition = "FOREIGN KEY(userId) REFERENCES tbl_user(userId) ON DELETE CASCADE ON UPDATE CASCADE"))
    private User userId;
    @Column(name = "proCategory", nullable = false)
    private String proCategory;
    @Column(name = "proName", nullable = false)
    private String proName;
    // @Column(name = "proMainImg", nullable = false)
    // private MultipartFile proMainImg; // project's main image
    // @Column(name = "proImg", nullable = false)
    // private MultipartFile proImg; // project's all images
    @Column(name = "proPrice", nullable = false)
    private String proPrice;
    @Column(name = "proGoalAmount", nullable = false)
    private String proGoalAmount;
    @Column(name = "proDate")
    private Date proDate; // project's permission date
    @Column(name = "proStartDate")
    private String proStartDate;
    @Column(name = "proEndDate")
    private String proEndDate;
    @Column(name = "proStatus", nullable = false, columnDefinition = "integer default 0")
    private int proStatus; // project permission (승인:1 / 미승인:0)
    @Column(name = "proPaidCnt", nullable = false, columnDefinition = "integer default 0")
    private int proPaidCnt;
    @Column(name = "proNotifyCnt", nullable = false, columnDefinition = "integer default 0")
    private int proNotifyCnt;
    @Column(name = "proScript", nullable = false, length = 300)
    private String proScript;
    @Column(name = "sellerName", nullable = false)
    private String sellerName;
    @Column(name = "sellerDetail", nullable = false, length = 300)
    private String sellerDetail;
    @Column
    private int fileAttached;   // 파일 첨부 여부 (1 = 첨부O, 0 = 첨부X)
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectFile> projectFileList = new ArrayList<>();

    // 파일이 없는 경우 toSaveEntity 메소드 호출
    // Dto to Entity
    public static Project toSaveEntity(ProjectDto projectDto) {
        Project project = new Project();
        project.setProCode(projectDto.getProCode());
        project.setUserId(projectDto.getUserId());
        project.setProCategory(projectDto.getProCategory());
        project.setProName(projectDto.getProName());
        // project.setProImg(projectDto.getProImg());
        // project.setProMainImg(projectDto.getProMainImg());
        project.setProPrice(projectDto.getProPrice());
        project.setProGoalAmount(projectDto.getProGoalAmount());
        project.setProDate(projectDto.getProDate());
        project.setProStartDate(projectDto.getProStartDate());
        project.setProEndDate(projectDto.getProEndDate());
        project.setProStatus(projectDto.getProStatus());
        project.setProPaidCnt(projectDto.getProPaidCnt());
        project.setProNotifyCnt(projectDto.getProNotifyCnt());
        project.setProScript(projectDto.getProScript());
        project.setSellerName(projectDto.getSellerName());
        project.setSellerDetail(projectDto.getSellerDetail());
        project.setFileAttached(0); // 파일 없음.
        return project;
    }

    // 파일이 있는 경우 toSaveFileEntity 메소드 호출
    // Dto to Entity
    public static Project toSaveFileEntity(ProjectDto projectDto) {
        Project project = new Project();
        project.setProCode(projectDto.getProCode());
        project.setUserId(projectDto.getUserId());
        project.setProCategory(projectDto.getProCategory());
        project.setProName(projectDto.getProName());
        // project.setProImg(projectDto.getProImg());
        // project.setProMainImg(projectDto.getProMainImg());
        project.setProPrice(projectDto.getProPrice());
        project.setProGoalAmount(projectDto.getProGoalAmount());
        project.setProDate(projectDto.getProDate());
        project.setProStartDate(projectDto.getProStartDate());
        project.setProEndDate(projectDto.getProEndDate());
        project.setProStatus(projectDto.getProStatus());
        project.setProPaidCnt(projectDto.getProPaidCnt());
        project.setProNotifyCnt(projectDto.getProNotifyCnt());
        project.setProScript(projectDto.getProScript());
        project.setSellerName(projectDto.getSellerName());
        project.setSellerDetail(projectDto.getSellerDetail());
        project.setFileAttached(1); // 파일 있음.
        return project;
    }

    // 빌더 패턴은 setter 메서드가 되지 않아 잠시 보류
    // Dto to Entity
//    public static Project ToEntity(ProjectDto projectDto) {
//
//        return Project.builder()
//                .proCode(projectDto.getProCode())
//                .userId(projectDto.getUserId())
//                .proCategory(projectDto.getProCategory())
//                .proName(projectDto.getProName())
////                .proImg(projectDto.getProImg())
//                .proMainImg(projectDto.getProMainImg())
//                .proPrice(projectDto.getProPrice())
//                .proDate(projectDto.getProDate())
//                .proStartDate(projectDto.getProStartDate())
//                .proEndDate(projectDto.getProEndDate())
//                .proStatus(projectDto.getProStatus())
//                .proPaidCnt(projectDto.getProPaidCnt())
//                .proNotifyCnt(projectDto.getProNotifyCnt())
//                .proScript(projectDto.getProScript())
//                .sellerName(projectDto.getSellerName())
//                .sellerDetail(projectDto.getSellerDetail())
//                .fileAttached(projectDto.setFileAttached(0))
//                .build();
//    }
}
