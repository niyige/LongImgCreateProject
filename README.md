# 生成长图并分享

把一大串数据生成一张长图，比如把一个列表的数据生成一张长图，
生成后会保存到相册的同时（相册名：长图），弹出系统分享框。

#注意事项

android6.0以上：需要动态获取文件存储权限，项目里面有相关工具类：<br/>

android7.0以上：主要是针对文件传递相关，如果该app使用带有"file://" 开头的
intent，就会爆异常，解决方案是用fileProvider，构建一个虚拟路径，
对真实的filepath进行映射。（使用系统分享的时候使用到了文件传递）<br/>

生成长图本文的实现原理是：计算好长图总共的height，然后通过这个height创建一个bitmap，
通过画布，绘制后保存为一张图片。 这里面记得bitmap及时recycle(),
由于长图比较大，微信分享对于超过32K的图片会爆异常，所以未使用相关第三方分享。：<br/>

#项目体验
本项目根目录有个apk文件夹

#未处理的瑕疵
生成的长图保存在 长图 相册目录，但是打开系统相册，出现在其他相册里面，
如何才能与截屏，其他相册之类的目录平级。。有没有大佬提供下线索
qq: 893007592

#小项目页面展示

首页：<br/>
![image](https://github.com/niyige/LongImgCreateProject/blob/master/screenshot/index.jpg)
<br/>
调用本地分享：<br/>
![image](https://github.com/niyige/LongImgCreateProject/blob/master/screenshot/1.jpg)
<br/>
![image](https://github.com/niyige/LongImgCreateProject/blob/master/screenshot/2.jpg)
<br/>
![image](https://github.com/niyige/LongImgCreateProject/blob/master/screenshot/3.jpg)
<br/>
保存到相册：<br/>
![image](https://github.com/niyige/LongImgCreateProject/blob/master/screenshot/相册1.jpg)
<br/>
![image](https://github.com/niyige/LongImgCreateProject/blob/master/screenshot/相册2.jpg)
<br/>
![image](https://github.com/niyige/LongImgCreateProject/blob/master/screenshot/相册3.jpg)