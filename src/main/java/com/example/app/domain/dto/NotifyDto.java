package com.example.app.domain.dto;

import com.example.app.domain.entity.Notify;
import com.example.app.domain.entity.User;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotifyDto {
    private int notifyCode;
    private int proCode;
    private String userId;
    private Date notifyDate;

    public static NotifyDto EntityToNotifyDto(Notify notify) {

        return NotifyDto.builder()
                .notifyCode(notify.getNotifyCode())
                .notifyDate(notify.getNotifyDate())
                .userId(notify.getUserId().getUserId())
                .proCode(notify.getProCode().getProCode())
                .build();
    }
}
