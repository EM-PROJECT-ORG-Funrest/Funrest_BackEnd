package com.example.app.domain.service.notify;

import com.example.app.domain.dto.NotifyDto;
import com.example.app.domain.entity.Notify;
import com.example.app.domain.entity.Project;
import com.example.app.domain.entity.User;
import com.example.app.domain.repository.NotifyRepository;
import com.example.app.domain.repository.ProjectRepository;
import com.example.app.domain.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class NotifyService {

    @Autowired
    private EntityManager em;
    @Autowired
    private NotifyRepository notifyRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean registerNotification(int proCode) throws ParseException {
        log.info("NotifyService's registerNotification() proCode : " + proCode);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // UserRepository - userId 조회 - User타입 저장
        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new NullPointerException("Invalid User Id"));
        // ProjectRepository - proCode 조회 - Project 타입 저장
        Project project = projectRepository.findByProCode(proCode);
        if(project == null) {
            throw new NullPointerException("Invalid Project Code");
        }

        // notifyDate setting 위해 proStartDate 조회
        String strDate = projectRepository.findProStartDateByProCode(proCode).orElseThrow(() ->
                new NullPointerException("Start date not found for project code: " + proCode));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date date = formatter.parse(strDate);

        NotifyDto notifyDto = new NotifyDto();
        notifyDto.setUserId(userId);
        notifyDto.setProCode(proCode);
        notifyDto.setNotifyDate(date);

        // 중복 알림 신청 방지
        Optional<Notify> existingNotification = notifyRepository.findByProCodeAndUserId(project, user);
        if(!existingNotification.isEmpty()) {
            throw new DuplicateKeyException("해당 프로젝트에 대한 알림 설정이 이미 등록되어 있습니다.");
        } else {
            Notify notify = Notify.NotifyDtoToEntity(notifyDto);
            notifyRepository.save(notify);
            return true;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateProNotifyCnt(int proCode){
        // 알림 신청 성공 후 Project 테이블 proNotifyCnt 업데이트
        log.info("NotifyService's updateProNotifyCnt() proCode : " + proCode);

        Project project = em.find(Project.class, proCode);
        log.info("before proNotifyCnt : " + project.getProNotifyCnt());
        project.setProNotifyCnt(project.getProNotifyCnt() + 1);
        log.info("after proNotifyCnt : " + project.getProNotifyCnt());

        return true;
    }
}
