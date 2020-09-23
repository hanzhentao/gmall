package api;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface WmsApi {
    @GetMapping("wms/waresku/{skuId}")
    Resp<List<WareSkuEntity>> queryWaresBySkuId(@PathVariable("skuId")Long skuID);
}
