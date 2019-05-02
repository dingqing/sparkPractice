package com.dingqing

import org.apache.log4j.{Level, Logger}
import org.apache.spark.internal.Logging

object LoggerPro extends Logging{

  def setStreamingLogLevels(): Unit ={
    val log4jInitialized = Logger.getRootLogger.getAllAppenders.hasMoreElements
    if(!log4jInitialized){
      logInfo("Setting log level to [ERROR] for streaming example." +
        " To override add a custom log4j.properties to the classPath")
      Logger.getRootLogger.setLevel(Level.ERROR)
    }
  }

}