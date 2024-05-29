package com.example.app.domain.service;

import com.example.app.domain.dto.UserAnalyticsDto;
import com.example.app.domain.entity.User;
import com.example.app.domain.entity.UserAnalytics;

public class DtoEntityTransferTestService {

    public UserAnalyticsDto saveUserInfo(UserAnalyticsDto userAnalyticsDto) {
        // DTO TO ENTITY
        UserAnalytics entity = UserAnalytics.toEntity(userAnalyticsDto);
        System.out.println("To ENTITY : " + entity.toString());

        // SAVE ENTITY
//        UserAnalytics saveEntity = repostiory.save(entity);
//        System.out.println("save Entity : " + saveEntity);

        // ENTITY TO DTO
//        UserAnalyticsDto dto = UserAnalyticsDto.toDto(saveEntity);
//        System.out.println("To DTO : " + dto)
        return null;


        
    }

}
