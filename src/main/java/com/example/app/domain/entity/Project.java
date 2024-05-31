package com.example.app.domain.entity;

import com.example.app.domain.dto.ProjectDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

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
    //@Column(name = "proMainImg", nullable = false)
    //private MultipartFile proMainImg; // project's main image
//    @Column(name = "proImg", nullable = false)
//    private MultipartFile proImg; // project's all images
    @Column(name = "proPrice", nullable = false)
    private String proPrice;
    @Column(name = "proDate")
    private Date proDate; // project's permission date
    @Column(name = "proStartDate")
    private Date proStartDate;
    @Column(name = "proEndDate")
    private Date proEndDate;
    @Column(name = "proStatus", nullable = false, columnDefinition = "integer default 0")
    private int proStatus; // project permission (승인:1 / 미승인:0)
    @Column(name = "proPaidCnt", nullable = false, columnDefinition = "integer default 0")
    private int proPaidCnt;
    @Column(name = "proNotifyCnt", nullable = false, columnDefinition = "integer default 0")
    private int proNotifyCnt;
    @Column(name = "proScript", nullable = false)
    private String proScript;
    @Column(name = "sellerName", nullable = false)
    private String sellerName;
    @Column(name = "sellerDetail", nullable = false)
    private String sellerDetail;

    @Column
    private int fileAttached;   // 1 or 0

    // Dto to Entity
    public static Project toSaveEntity(ProjectDto projectDto){
        Project project = new Project();
        project.setProCode(projectDto.getProCode());
        project.setUserId(new User(projectDto.getUserId()));
        project.setProCategory(projectDto.getProCategory());
        project.setProName(projectDto.getProName());
        // project.setProImg(projectDto.getProImg());
        //project.setProMainImg(projectDto.getProMainImg());
        project.setProPrice(projectDto.getProPrice());
        project.setProDate(projectDto.getProDate());
        //projectDto에서 바로 넣으면 여기 두개에는 NUll값 들어감 그래서 밑에 overloading 해놓음
        project.setProStartDate(new Date(projectDto.getProStartDate()));
        project.setProEndDate(new Date(projectDto.getProEndDate()));
        project.setProStatus(projectDto.getProStatus());
        project.setProPaidCnt(projectDto.getProPaidCnt());
        project.setProNotifyCnt(projectDto.getProNotifyCnt());
        project.setProScript(projectDto.getProScript());
        project.setSellerName(projectDto.getSellerName());
        project.setSellerDetail(projectDto.getSellerDetail());
        project.setFileAttached(0); // 파일 없음.
        return project;
    }

    // Project 엔티티 변환 메서드 수정
    public static Project toSaveEntity(ProjectDto projectDto, Date proStartDate, Date proEndDate) {
        Project project = new Project();
        project.setProCode(projectDto.getProCode());
        project.setUserId(new User(projectDto.getUserId()));
        project.setProCategory(projectDto.getProCategory());
        project.setProName(projectDto.getProName());
        // project.setProImg(projectDto.getProImg());
        // project.setProMainImg(projectDto.getProMainImg());
        project.setProPrice(projectDto.getProPrice());
        project.setProDate(projectDto.getProDate());
        project.setProStartDate(proStartDate); // Date 객체 설정
        project.setProEndDate(proEndDate); // Date 객체 설정
        project.setProStatus(projectDto.getProStatus());
        project.setProPaidCnt(projectDto.getProPaidCnt());
        project.setProNotifyCnt(projectDto.getProNotifyCnt());
        project.setProScript(projectDto.getProScript());
        project.setSellerName(projectDto.getSellerName());
        project.setSellerDetail(projectDto.getSellerDetail());
        project.setFileAttached(0); // 파일 없음
        return project;
    }



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
