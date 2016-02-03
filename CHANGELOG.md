# 1.5.0
* 增加购买基数
* 设置限制参数方式修改
~~~~
setShowStorage(long showStorage)
setLeastBuyNum(long buyNum)
setLimitNum(long limitNum)
~~~~
改为
~~~~
/**
  * 设置限制条件
  *
  * @param showStorage 库存
  * @param leastbuyNum 最少购买数量
  * @param limitNum    限购数量
  * @param basicNum    购买基数
*/
setTerm(long showStorage, long leastbuyNum, long limitNum, long basicNum)
~~~~
* demo修改
