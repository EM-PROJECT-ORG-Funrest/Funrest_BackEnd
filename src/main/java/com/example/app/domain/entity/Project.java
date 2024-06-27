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
    private Date proDate; // 프로젝트 신청일자
    @Column(name = "proPermitDate")
    private Date proPermitDate; // 프로젝트 승인일자
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

    @Column(name = "proMainFilePaths", length = 1000)
    private List<String> proMainFilePaths = new ArrayList<>();
    @Column(name = "proSubFilePaths", length = 1000)
    private List<String> proSubFilePaths = new ArrayList<>();

    public Project(int proCode) {
        this.proCode = proCode;
    }

    // Dto to Entity
    // 파일이 없는 경우 toSaveEntity 메소드 호출
    public static Project toEntity(ProjectDto projectDto) {
        Project project = new Project();
        project.setProCode(projectDto.getProCode());
        project.setUserId(new User(projectDto.getUserId()));
        project.setProCategory(projectDto.getProCategory());
        project.setProName(projectDto.getProName());
        project.setProPrice(projectDto.getProPrice());
        project.setProGoalAmount(projectDto.getProGoalAmount());
        project.setProDate(projectDto.getProDate());
        project.setProPermitDate(projectDto.getProPermitDate());
        //projectDto에서 바로 넣으면 여기 두개에는 NUll값 들어감 그래서 밑에 overloading 해놓음
        project.setProStartDate(projectDto.getProStartDate());
        project.setProEndDate(projectDto.getProEndDate());
        project.setProStatus(projectDto.getProStatus());
        project.setProPaidCnt(projectDto.getProPaidCnt());
        project.setProNotifyCnt(projectDto.getProNotifyCnt());
        project.setProScript(projectDto.getProScript());
        project.setSellerName(projectDto.getSellerName());
        project.setSellerDetail(projectDto.getSellerDetail());
        return project;
    }

}
