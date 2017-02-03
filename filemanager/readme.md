# 文件管理器
Material Design File Explorer。

## Todo
1. 多媒体文件扫描
- 观察者模式
- Uri
- Intent 及 IntentFilter
- DrawerLayout
- Fragment(参考：[Fragment 详解](http://www.jianshu.com/p/f5fd3ce8f490))
- ViewPager
    - 懒加载
- ActionBar 及 Toolbar
    - SearchView
- 性能分析
- 自定义文字
- 外置SD读写
    - [Android4.4之后的外置SD卡文件读写的解决方法](http://blog.csdn.net/mzm489321926/article/details/49329443)
    - [How to use the new SD card access API presented for Android 5.0 (Lollipop)?](http://stackoverflow.com/questions/26744842/how-to-use-the-new-sd-card-access-api-presented-for-android-5-0-lollipop)

## 思路
- 文件扫描：通过File.listFile()来实现。防止多媒体数据库未及时更新。
- 对于获取音频、视频、照片等，可以通过数据库来实现，直接过滤，效率更高。

- 拷贝、复制进度：获取拷贝文件的id，遍历查询其下所有的文件，获取拷贝文件数和大小。文件遍历拷贝、移动，通知到UI更新

## 总结
1. 获取文件及文件夹的方式
    - 通过数据库扫描的方式存在延时
    - 通过File.listFile()方式直接有效
    - 对于扫描整个存储的文件来说，数据库的方式效率更高，但是可能会存在数据库未及时更新。
    - 通过发送广播的方式通知文件删除修改，**对目录下有文件或子目录的无效**，正在寻找解决方案
2. 根目录与普通文件获取大小方式不一样
    - 根目录：File.getTotalSpace()
    - 普通文件：File.lenght()
3. 运行时权限：如果 targetSdkVersion > 21，则即使在 AndroidManifest.xml 文件中申明了读写权限，也会造成不能读写
    - 运行时权限
    - targetSdkVersion 改为 21 以下
4. 弹出 SD 卡后，系统仍能获取到 SD 卡路径，必须手动移除。通过判断 SD 卡是否可读、可写、可执行判断 SD 卡是否挂载。

## 注意事项
1. Android 中通过 File.setLastModified() 方法设置文件最后修改时间不可行。如果通过第三方备份的话，可能导致时间不同步。


## Crash
1. 未授权进行 SD卡 读写

```
java.lang.SecurityException: Permission Denial: opening provider com.android.externalstorage.ExternalStorageProvider from ProcessRecord{b740084 9263:com.littlejie.demo/u0a91} (pid=9263, uid=10091) requires android.permission.MANAGE_DOCUMENTS or android.permission.MANAGE_DOCUMENTS
     at android.os.Parcel.readException(Parcel.java:1683)
     at android.os.Parcel.readException(Parcel.java:1636)
     at android.app.ActivityManagerProxy.getContentProvider(ActivityManagerNative.java:4169)
     at android.app.ActivityThread.acquireProvider(ActivityThread.java:5434)
     at android.app.ContextImpl$ApplicationContentResolver.acquireUnstableProvider(ContextImpl.java:2267)
     at android.content.ContentResolver.acquireUnstableProvider(ContentResolver.java:1527)
     at android.content.ContentResolver.acquireUnstableContentProviderClient(ContentResolver.java:1618)
     at android.provider.DocumentsContract.createDocument(DocumentsContract.java:1063)
     at android.support.v4.provider.DocumentsContractApi21.createFile(DocumentsContractApi21.java:37)
     at android.support.v4.provider.TreeDocumentFile.createFile(TreeDocumentFile.java:34)
     at com.littlejie.demo.modules.base.media.activity.SDCardActivity.write2SDCard(SDCardActivity.java:110)
     at com.littlejie.demo.modules.base.media.activity.SDCardActivity.onClick(SDCardActivity.java:67)
     at android.view.View.performClick(View.java:5610)
     at android.view.View$PerformClick.run(View.java:22265)
     at android.os.Handler.handleCallback(Handler.java:751)
     at android.os.Handler.dispatchMessage(Handler.java:95)
     at android.os.Looper.loop(Looper.java:154)
     at android.app.ActivityThread.main(ActivityThread.java:6077)
     at java.lang.reflect.Method.invoke(Native Method)
     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:865)
     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:755)
```

