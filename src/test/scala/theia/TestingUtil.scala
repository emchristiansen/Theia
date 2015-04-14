package theia

import java.io.File
import st.sparse.sundry._
import com.sksamuel.scrimage._
import java.nio.file.Files
import st.sparse.sundry._
import scala.pickling._
import scala.pickling.binary._
import scala.util.Random
import theia.mitsuba.Render

trait TestingUtil extends Logging {
  lazy val configureLogger = {
    // Must be one of: "trace", "debug", "info", "warn", or "error".
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info")
  }

  configureLogger

  val random = new Random(0)

  val resourceRoot = ExistingDirectory(
    new File(getClass.getResource("/").getPath))
    
  val zippedCBox = Render.zip(ExistingDirectory(new File(resourceRoot, "cbox")))
    
//  val goldfishGirl = Image(ExistingFile(new File(
//    resourceRoot,
//    "/goldfish_girl.png")))

  val outputRoot =
    ExistingDirectory(Files.createTempDirectory("TestTheiaOutputRoot").toFile)

  implicit val logRoot = {
    val directory = new File(outputRoot, "log")
    if (!directory.isDirectory) directory.mkdir()
    LogRoot(ExistingDirectory(directory))
  }
}