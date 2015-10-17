# iGDUFE
广东财经大学校园助手--入口信息门户,扩展其他对接门户网站:教务系统,考务系统等

效果图如下:

Splash 欢迎界面

![Splash 欢迎界面](http://7xlhko.com1.z0.glb.clouddn.com/QQ20151017-0@2x.png)

登陆

![登陆界面](http://7xlhko.com1.z0.glb.clouddn.com/login.png)

课程表界面

![课程表界面](http://7xlhko.com1.z0.glb.clouddn.com/kcb.png)

侧滑栏菜单

![侧滑栏菜单](http://7xlhko.com1.z0.glb.clouddn.com/slidingmenu.png)

成绩表及选择学期切换时间界面

![成绩表及选择学期切换时间界面](http://7xlhko.com1.z0.glb.clouddn.com/pickmenu.png)

实现主要使用了三个开源组件的结合:

Litepal 负责数据库

Jsoup 负责网页数据的分析和抓取

Android-Asnyc-Httpclient 负责网络请求

思路就是模拟登陆学校的信息门户然后各种跳转, get 和 post 数据.....

抓包和网页解析就可以完成了....

