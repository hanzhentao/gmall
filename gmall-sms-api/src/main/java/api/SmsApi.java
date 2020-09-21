package api;

import com.atguigu.core.bean.Resp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vo.SkuSaleVO;

public interface SmsApi {
    @PostMapping("sms/skubounds/saveSale")
    Resp<Object> saveSaleAttr(@RequestBody SkuSaleVO skuSaleVO);
}
