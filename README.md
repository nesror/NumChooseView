NumChooseView
========

<img src="img\img_1.png" width="200" height="100"/>
<embed src="img\device-2016-02-01-222858.mp4" allowFullScreen="true" quality="high" width="480" height="800" align="middle" allowScriptAccess="always" />

Add NumChooseView to your project
----------------------------
Gradle:
```
   compile 'cn.yzapp.numchooseview:numchooseviewlib:0.9.8@aar'
```

Maven:
```
<dependency>
  <groupId>cn.yzapp.numchooseview</groupId>
  <artifactId>numchooseviewlib</artifactId>
  <version>0.9.8</version>
  <type>aar</type>
</dependency>
```
[ ![Download](https://api.bintray.com/packages/nesror/maven/NumChooseView/images/download.svg) ]()

Use
----------------------------
````
<cn.yzapp.numchooseviewlib.NumChooseView
            android:id="@+id/num_view"
            android:layout_width="wrap_content"
            android:layout_height="30dp" />
````

 * 得到数量
  * getBuyNum()
 * 设置库存
  * setShowStorage(long showStorage)
 * 设置最少购买数量
  * setLeastBuyNum(long buyNum)
 * 设置限购数量
  * setLimitNum(long limitNum)
 * 设置根视图，用于监听键盘是否收起
  * setRootView(final View rootView)
 * 设置是否显示提示
  * setCanShowHint(boolean showToast)

Release History
------------------------
[CHANGELOG](CHANGELOG.md)
