NumChooseView
========

<img src="img\img_1.png" width="200" height="100"/>

<img src="img\img_3.gif" width="800" height="450"/>

Add NumChooseView to your project
----------------------------
Gradle:
```
   compile 'cn.yzapp.numchooseview:numchooseviewlib:[look download]@aar'
```

Maven:
```
<dependency>
  <groupId>cn.yzapp.numchooseview</groupId>
  <artifactId>numchooseviewlib</artifactId>
  <version>[look download]</version>
  <type>aar</type>
</dependency>
```
[ ![Download](https://api.bintray.com/packages/nesror/maven/NumChooseView/images/download.svg) ](https://bintray.com/artifact/download/nesror/maven/cn/yzapp/numchooseview/numchooseviewlib/1.5.7/numchooseviewlib-1.5.7.aar)

Use
----------------------------
 * 布局文件
````
<cn.yzapp.numchooseviewlib.NumChooseView
            android:id="@+id/num_view"
            android:layout_width="wrap_content"
            android:layout_height="30dp"
            app:numchoose_textColor="#886666"
            app:numchoose_textSize="13sp" />
````

 * 得到数量
~~~~
getBuyNum()
~~~~
 * 设置限制条件
~~~~
/**
  * 设置限制条件
  *
  * @param showStorage 库存
  * @param leastbuyNum 最少购买数量
  * @param limitNum    限购数量
  * @param basicNum    购买基数
  * NOT_LIMIT 不做限制
*/
setTerm(long showStorage, long leastbuyNum, long limitNum, long basicNum)
~~~~
 * 设置根视图，用于监听键盘是否收起
~~~~
setRootView(final View rootView)
~~~~
 * 设置是否显示提示
~~~~
setCanShowHint(boolean showToast)
~~~~

Release History
------------------------
[CHANGELOG](CHANGELOG.md)
