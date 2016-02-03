package cn.yzapp.numchooseviewlib.model;

import cn.yzapp.numchooseviewlib.NumChooseView;

/**
 * @author: nestor
 * time: 1/22 022-13:58.
 * email: nestor@yzapp.cn
 * desc:
 */
public class NumBean {
    private long showStorage = NumChooseView.NOT_LIMIT;//库存
    private long leastBuyNum = 1;//最少购买数量
    private long limitNum = NumChooseView.NOT_LIMIT;//限购数量
    private long basicNum = 1;//购买基数

    public long getShowStorage() {
        return showStorage;
    }

    public void setShowStorage(long showStorage) {
        this.showStorage = showStorage;
    }

    public long getLeastBuyNum() {
        return leastBuyNum;
    }

    public void setLeastBuyNum(long leastBuyNum) {
        this.leastBuyNum = leastBuyNum;
    }

    public long getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(long limitNum) {
        this.limitNum = limitNum;
    }

    public long getBasicNum() {
        return basicNum;
    }

    public void setBasicNum(long basicNum) {
        this.basicNum = basicNum;
    }
}
