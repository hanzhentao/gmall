package com.atguigu.gmall.pms.feign;

import api.SmsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("sms-service")
public interface SmsClient extends SmsApi {
}
