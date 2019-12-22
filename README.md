# sparkPractice
Spark Streaming实时计算


### 准备知识
- maven

    类似于npm，有本地和[远程库](https://mvnrepository.com/)……
    
### 开发细节
- 开发环境

    软件版本 | 设置
    --- | ---
    Java 1.8.0_191 | [安装JDK&JRE](https://www.howtoing.com/how-to-install-java-with-apt-on-ubuntu-18-04)
    Scala 2.11.12 | 
    zookeeper 3.4.10 | 
    Spark 2.4.0 | Spark集群搭建
    kafka_2.12-2.2.0 | Kafka集群搭建
    Redis 4.0.9 | 
- 开发工具
	- 打包工具（任选一）
		- [安装sbt](https://www.scala-sbt.org/download.html)
		- 安装Maven
	- 安装[Intellij-idea](https://docs.scala-lang.org/getting-started-intellij-track/getting-started-with-scala-in-intellij.html)
		- 安装Scala插件（Settings -> Plugins）。
		- 配置Maven home。Settings -> 搜'maven'
	 	- 添加JDK、Spark相关jar包、Scala sdk
	 		- 下载spark并解压
	 		- File -> Project Structure -> Platform Settings
	 			- -> SDKs -> + -> JDK
	 			- -> Global Libraries -> +
	 				- -> Java，选择{spark}/jars目录
	 				- -> Scala SDK -> Download（很慢）
	 					[下载](https://www.scala-lang.org/download/) -> Browse
- 新建项目
	- File -> New -> Project
		- -> Maven -> scala-archetype-simple（完成之后：项目右键add framework support，勾选scala）
	 	- 或-> Scala -> SBT/IDEA
	- 修改pom.xml，添加依赖
        ```
        properties
            spark.version
            scala.version
        
        dependencies（根据机器上Scala、Spark、Kafka的版本选择相应版本依赖）
            spark-core_${scala.version}
            spark-streaming_${scala.version}
            spark-streaming-kafka-0-8_2.11
            kafka_2.12
            kafka-clients
        ```
- 编写应用
	- ……
- 打包、提交
	- File -> Project Structure -> Artifacts -> + JAR -> From modules……
	- Build- > Build Artifacts
	- 上传jar包到spark客户端
		```
		spark-submit ……
		```

### 踩坑
- 基本上都是版本和依赖问题

现象 | 解决
--- | ---
pom文件检查速度慢 |[修改maven仓库](https://www.jianshu.com/p/80384612ee1d)
pom文件配置正确，但是代码显示红色错误“cannot resolve symbol apache” | 试一下File->Invalidate Caches/Restart，清除缓存重启。还不行就Maven -> Reimport
运行项目报错：error while loading junit4 scala signature junit4 has wrong version | 删除src/test目录
[SLF4J: Class path contains multiple SLF4J bindings.](https://stackoverflow.com/questions/14024756/slf4j-class-path-contains-multiple-slf4j-bindings)|

### 应用
- 分析用户使用手机APP行为
    - 故事背景
    
        APP上线之后，运营经理过来说，希望看到用户在APP的操作，点击了什么页面，停留多久，何时卸载APP……希望能在后台图表看到这一切，来进行用户行为分析并帮助进行运营决策。
    - 实现
        
        数据源(APP) -> 数据缓存(Kafka) -> 流式引擎(Spark Streaming) -> 结果存储(Redis)
    - [代码](/src/main/scala/com/dingqing/KafkaWordProducer.scala)
    <!-- - 代码 -->
        
    - 运行
	    ```
	    启动Kafka集群(https://kafka.apache.org/quickstart)
	    > cd softwares/kafka_2.12-2.2.0/
	    新开终端> bin/zookeeper-server-start.sh config/zookeeper.properties
	    新开终端> bin/kafka-server-start.sh config/server.properties
	    新开终端，创建并查看topics:
	    > bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic wordsender
	    > bin/kafka-topics.sh --list --bootstrap-server localhost:9092
	    
	    启动producer端：
	    新开终端> cd softwares/spark-2.4.0-bin-hadoop2.7
	    > ./bin/spark-submit --class "com.dingqing.KafkaWordProducer" /home/hello/sparkPractice/out/artifacts/sparkPractice_jar/sparkPractice.jar localhost:9092,localhost:9092,localhost:9092 wordsender 3 5

	    启动consumer端：
	    新开终端> ./bin/spark-submit --class "com.dingqing.KafkaWordCount" /home/hello/sparkPractice/out/artifacts/sparkPractice_jar/sparkPractice.jar
	    ```