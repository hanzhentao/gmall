package com.atguigu.gmall.search.feign;

import api.WmsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("wms-service")
public interface GmallWmsClient extends WmsApi {
}
