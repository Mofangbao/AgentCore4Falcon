# 基本原理
插件收集特定项目的数据，打印到控制台，主程序收集后传输到云端
# 插件开发方式
可以使用任意语言进行开发，只需要最终将监控项输出到控制台即可，监控项使用json格式，如下所示：
```json
    [{"metric":"cpu.idle","value":1,"counterType":"GAUGE","tags":""}]
```
插件只需要打印这三个参数即可，其余的主程序会补全。

支持同时采集多个监控项的，如：
```json
[{"metric":"cpu.idle","value":1,"counterType":"GAUGE","tags":""},{"metric":"memory.free ","value":0.9,"counterType":"GAUGE","tags":""}]
```
如果项目本身就是java的，可以将此程序直接作为库引入，在Agent.java中提供了相应的方法直接提交数据到服务器。
# 插件命名规则
interval_name 
规则跟官方的插件一致。
interval表示这个脚本被调用的间隔，也就是数据上报间隔，name是实际的名字，两者之间用短下划线分隔，如 60_disk_on_line.sh。
  
# 插件存放方式
插件必须放在plugins目录，使用shell脚本（windows下bat脚本）对插件进行调用封装，如写了一个java小程序进行监控，那么就需要在plugins目录下写一个封装插件执行的脚本，如 60_sample.bat内容如下：
```bat
@echo off
java -jar ./jars/sample.jar
```
表示被调用的时候实际执行的是plugins目录下的jars目录下的sample.jar文件
    
注意：bat脚本第一行需要加上 @echo off，否则系统会回显调用语句，输出的就不是纯json了。Shell脚本直接写上java -jar ./jars/sample.jar就可以了。

脚本被执行时，工作目录被设定在plugins目录，因此所有相对目录都是从plugins目录开始算的。
    
建议不同语言写的插件在plugins目录下建立不同的文件夹存放，这样便于管理，如java程序的就放在jars目录下，python程序的就放在python目录下。
    
所有文本相关的编码都必须为utf8，否则中文会乱码，如果使用默认的agent.py，需要先安装python的psutil库，同时python需要在环境变量中，否则需要指明python全路径。
# 致谢
示例中的windows状态收集程序使用了 [https://github.com/freedomkk-qfeng/falcon-scripts/tree/master/windows_collect](https://github.com/freedomkk-qfeng/falcon-scripts/tree/master/windows_collect)
