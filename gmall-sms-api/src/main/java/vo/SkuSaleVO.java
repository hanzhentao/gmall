package vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuSaleVO {
    private Long SkuId;
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<Integer> work;

    private Integer fullCount;
    private BigDecimal discount;
    private BigDecimal price;
    private Integer ladderaddOther;

    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fulladdOther;
}
