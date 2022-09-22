package neo4j

import org.apache.spark.sql.types.{StringType, StructField, StructType}

import scala.util.Random
import org.apache.spark.sql.{DataFrame, Row, SparkSession};

case class Username(name: String,
                    client: String,
                    first_transaction: String,
                    time_created: String,
                    age: Int,
                    number_of_tnx: Int)

case class Device(name: String,
                  first_transaction: String,
                  time_created: String,
                  number_of_tnx: Int,
                  max_time_btw_transaction: Int,
                  age: Int)

case class Operator(name: String,
                    operator_id: Int)

case class BlockReason(name: String)


object Pipeline {

  def loadDF(spark: SparkSession, time_from: String, time_to: String): DataFrame = {

    val schema = StructType(Array(
      StructField("name", StringType, true),
      StructField("client", StringType, true),
      StructField("first_transaction", StringType, true)
    ))

    val data = Seq(
      (12, "John Bonham", "Drums"),
      (19, "John Mayer", "Guitar"),
      (32, "John Scofield", "Guitar"),
      (15, "John Butler", "Guitar")
    )

    val rowRDD = data.map(attributes => Row(attributes._1, attributes._2))
    spark.createDataFrame(data)

  }

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("neo4j")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._

    val df = loadDF(spark, args(0), args(1))

    df.show()
  }
}
