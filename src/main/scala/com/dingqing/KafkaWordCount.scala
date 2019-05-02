package com.dingqing

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}

object KafkaWordCount {
  def main(args: Array[String]): Unit = {
//    LoggerPro.setStreamingLogLevels()
    val sc = new SparkConf().setAppName("KafkaWordCount").setMaster("local[2]")
    val ssc = new StreamingContext(sc, Seconds(10))
    // 设置检查点
    ssc.checkpoint("/home/hello/spark/checkpoint")
    // Zookeeper服务器地址
    val zkQuorum = "localhost:2181,localhost:2181,localhost:2181"
    // consumer所在的group，可在一个group中设置多个consumer，加快消息消费的速度
    val group = "handsome_boy"
    // topic的名称
    val topics = "wordsender"
    // 每个topic的分区数
    val numThreads = 3
    val topicMap = topics.split(",").map((_,numThreads.toInt)).toMap
    val lineMap = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap)
    val lines = lineMap.map(_._2)
    val words = lines.flatMap(_.split(" "))
    val pair = words.map(x => (x, 1))
    val wordCounts = pair.reduceByKeyAndWindow(_ + _, _ - _,Minutes(2), Seconds(10), 3)
    wordCounts.print
    ssc.start
    ssc.awaitTermination
  }
}