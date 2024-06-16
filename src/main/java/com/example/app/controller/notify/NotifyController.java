package com.example.app.controller.notify;

import com.example.app.domain.service.notify.NotifyService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.util.Map;

@Controller
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@RequestMapping("/th/notify")
public class NotifyController {

    @Autowired
    private NotifyService notifyService;

    // 알림 신청
    @PostMapping("/applyNotification")
    public ResponseEntity applyNotification(@RequestBody Map<String, String> request) {
        log.info("post /th/notify/applyNotification..");
        int proCode = Integer.parseInt(request.get("proCode"));

        try {
            notifyService.createNotification(proCode);
            notifyService.updateProNotifyCnt(proCode);
            return new ResponseEntity("Apply Notification Successful", HttpStatus.OK);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity("Already Registered Notification", HttpStatus.BAD_REQUEST);
        } catch (NullPointerException e ) {
            return new ResponseEntity("Can not found userId or proCode", HttpStatus.NOT_FOUND);
        } catch (ParseException e) {
            return new ResponseEntity("Parsing Date Error", HttpStatus.BAD_GATEWAY);
        } catch (Exception e) {
            return new ResponseEntity("Internal Server Error", HttpStatus.BAD_GATEWAY);
        }
    }

    // 알림 취소
    @PostMapping("/cancelNotification")
    public ResponseEntity cancelNotification(@RequestBody Map<String, String> request) {
        int proCode = Integer.parseInt(request.get("proCode"));
        log.info("post /th/notify/cancelNotification.." + proCode);

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(notifyService.deleteNotification(proCode, userId)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
