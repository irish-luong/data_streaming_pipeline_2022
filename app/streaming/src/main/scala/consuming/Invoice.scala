package consuming

import org.apache.spark.sql.SparkSession
import org.apache.log4j.Level
import org.apache.log4j.Logger

object Invoice {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR);

    var spark = SparkSession
        .builder()
        .appName("InvoiceStreaming")
        .master("local[*]")
        .config("spark.sql.streaming.checkpointLocation", "checkpoint")
        .getOrCreate()

//      Set log level
      spark.sparkContext.setLogLevel("WARN")

      var invoiceDF = spark
        .readStream
        .format("kafka")
        .option("kafka.bootstrap.servers", "localhost:9092")
        .option("subscribe", "test")
        .option("startingOffsets", "latest")
        .load()

//      Process schema
      invoiceDF.printSchema()

//    Write stream
      invoiceDF
        .writeStream
        .format("console")
        .outputMode("append")
//        .format("kafka")
//        .option("kafka.bootstrap.servers", "localhost:9092")
//        .option("topic", "test-forward")
        .start()
        .awaitTermination()




  }
}