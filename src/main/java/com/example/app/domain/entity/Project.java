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
    @Column(name = "proPrice", nullable = false)
    private String proPrice;
    @Column(name = "proGoalAmount", nullable = false)
    private String proGoalAmount;
    @Column(name = "proDate")
    private Date proDate; // project's permission date
    @Column(name = "proStartDate", nullable = false)
    private String proStartDate;
    @Column(name = "proEndDate", nullable = false)
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
    private int fileAttached;   // proMainImg 파일 첨부 여부 (1:첨부, 0:미첨부)
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectFile> projectFileList = new ArrayList<>();

    @Column
    private int subFileAttached; // proSubImg 파일 첨부 여부 (1:첨부, 0:미첨부)
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectSubFile> projectSubFileList = new ArrayList<>();

    public Project(int proCode) {
    } //orderService 때문에 추가


    // Dto to Entity
    // 파일이 없는 경우 toSaveEntity 메소드 호출
    public static Project toSaveEntity(ProjectDto projectDto) {
        Project project = new Project();
        project.setProCode(projectDto.getProCode());
        project.setUserId(new User(projectDto.getUserId()));
        project.setProCategory(projectDto.getProCategory());
        project.setProName(projectDto.getProName());
        project.setProPrice(projectDto.getProPrice());
        project.setProGoalAmount(projectDto.getProGoalAmount());
        project.setProDate(projectDto.getProDate());
        //projectDto에서 바로 넣으면 여기 두개에는 NUll값 들어감 그래서 밑에 overloading 해놓음
        project.setProStartDate(projectDto.getProStartDate());
        project.setProEndDate(projectDto.getProEndDate());
        project.setProStatus(projectDto.getProStatus());
        project.setProPaidCnt(projectDto.getProPaidCnt());
        project.setProNotifyCnt(projectDto.getProNotifyCnt());
        project.setProScript(projectDto.getProScript());
        project.setSellerName(projectDto.getSellerName());
        project.setSellerDetail(projectDto.getSellerDetail());
        project.setFileAttached(0); // mainImg 파일 없음.
        project.setSubFileAttached(0); // subImg 파일 없음.
        return project;
    }

    // Dto to Entity
    // 파일이 있는 경우 toSaveFileEntity 메소드 호출
    public static Project toSaveFileEntity(ProjectDto projectDto) {
        Project project = new Project();
        project.setProCode(projectDto.getProCode());
        project.setUserId(new User(projectDto.getUserId()));
        project.setProCategory(projectDto.getProCategory());
        project.setProName(projectDto.getProName());
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
        project.setFileAttached(1); // mainImg 파일 있음.
        project.setSubFileAttached(1); // subImg 파일 있음.
        return project;
    }

}
