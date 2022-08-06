package consuming

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.SparkSession
import org.apache.log4j.Level
import org.apache.log4j.Logger

object Invoice {

  def getModel: StructType = {
    new StructType(
      Array(
        StructField("InvoiceNo", StringType),
        StructField("StockCode", StringType),
        StructField("Description", StringType),
        StructField("Quantity", IntegerType),
        StructField("UnitPrice", FloatType),
        StructField("CustomerID", IntegerType),
        StructField("Country", StringType),
        StructField("InvoiceDate", StringType)
      )
    )
  }
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR);

    var spark = SparkSession
        .builder()
        .appName("InvoiceStreaming")
        .master("spark://localhost:7077")
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
      .selectExpr("CAST(value AS STRING) as message", "CAST(key AS STRING) as key")
      .select(from_json(col("message"), getModel).as("json"), col("key"))
      .select(
        col("json.*"),
        col("key").as("Key"),
        to_timestamp(col("json.InvoiceDate"), "yyyy-MM-dd HH:mm:ss").as("created_at")
      )

    var summary = invoiceDF
      .withWatermark("created_at", "1 seconds")
      .groupBy(
        window(col("created_at"), "1 seconds"),
        col("InvoiceNo")
      )
      .agg(count("InvoiceNo"))

    summary
      .writeStream
      .format("console")
      .outputMode("append")
      .option("truncate", "false")
//      .option("checkpointLocation", "/tmp")
//        .format("kafka")
//        .option("kafka.bootstrap.servers", "localhost:9092")
//        .option("topic", "test-forward")
      .start()
      .awaitTermination()
  }
}