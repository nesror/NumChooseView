package cn.yzapp.numchooseviewlib.model;

import cn.yzapp.numchooseviewlib.NumChooseView;

/**
 * @author: nestor
 * time: 1/22 022-13:58.
 * email: nestor@yzapp.cn
 * desc:
 */
public class NumBean {
    private int showStorage = NumChooseView.NOT_LIMIT;//库存
    private int leastBuyNum = 1;//最少购买数量
    private int limitNum = NumChooseView.NOT_LIMIT;//限购数量
    private int basicNum = 1;//购买基数

    public int getShowStorage() {
        return showStorage;
    }

    public void setShowStorage(int showStorage) {
        this.showStorage = showStorage;
    }

    public int getLeastBuyNum() {
        return leastBuyNum;
    }

    public void setLeastBuyNum(int leastBuyNum) {
        this.leastBuyNum = leastBuyNum;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getBasicNum() {
        return basicNum;
    }

    public void setBasicNum(int basicNum) {
        this.basicNum = basicNum;
    }
}
