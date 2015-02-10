package theia.mitsuba

import theia._
import theia.mitsuba.Config._
import theia.Integrator
import theia.Sensor
import scala.util.Random
import java.io.File
import org.apache.commons.io.FileUtils
import scala.sys.process._
import com.github.tototoshi.csv._
import theia.MSR._
import breeze.linalg.DenseMatrix

object Render {
  def makeMitsubaScript(template: String, numChannels: Int, v: View): String = {
    val sxi = implicitly[ShowXML[Integrator]]
    val sxsi = implicitly[ShowXML[(Sensor, Int)]]

    val replacedIntegrator = template.replace("$INTEGRATOR", sxi.show(v.integrator))
    template.replace("$SENSOR", sxsi.show((v.sensor, numChannels)))
  }

  def makeSceneDirectory(modelDirectory: File, mitsubaScript: String): (File, File) = {
    val salt = Random.alphanumeric.take(8).mkString

    val directory = new File("/tmp", modelDirectory.getPath.split("/").last + salt)
    println(s"Copying $modelDirectory to $directory")
    FileUtils.copyDirectory(modelDirectory, directory)

    val scriptFile = new File(directory, "generated_scene.xml")
    println(s"Writing Mitsuba script to $scriptFile")
    FileUtils.writeStringToFile(scriptFile, mitsubaScript)

    return (directory, scriptFile)
  }

  def callMitsuba(scriptPath: File, outPath: File) {
    s"/usr/bin/mitsuba $scriptPath -o $outPath" !
  }

  def makePythonScript(numChannels: Int, npyPath: File, csvPattern: (Int => String)): String = {
    val first = s"""
import numpy
arr = numpy.load("$npyPath"")
if len(arr.shape) == 2:
  arr = numpy.reshape(arr, (arr.shape[0], arr.shape[1], 1))
"""
    def save(channel: Int): String = {
      val cpi = csvPattern(channel)
      s"""
numpy.savetxt("$cpi", arr[:, :, $channel], delimiter=",")
"""
    }

    (first :: (0 until numChannels).toList.map(save)).mkString("\n")
  }

  def loadCSVs(numChannels: Int, csvPattern: (Int => String)): List[List[List[Double]]] = {
    for (c <- (0 until numChannels).toList) yield {
      val reader = CSVReader.open(new File(csvPattern(c)))
      reader.all.map(_.map(_.toDouble))
    }
  }

  def parseRenderingComponent(numChannels: Int, csv: List[List[List[Double]]]): Either[Matrix1, Matrix3] = {
    assert(numChannels == 1 || numChannels == 3)
    assert(csv.size > 0)
    assert(csv(0).size > 0)
    assert(csv(0)(0).size == numChannels)
    
    val numRows = csv.size
    val numColumns = csv(0).size
    
    if (numChannels == 1) {
      Left(DenseMatrix.tabulate[Double](numRows, numColumns){
        case (r, c) => csv(r)(c)(0)
      })
    } else {
      Right(DenseMatrix.tabulate[(Double, Double, Double)](numRows, numColumns){
        case (r, c) => (csv(r)(c)(0), csv(r)(c)(1), csv(r)(c)(2))
      })
    }
  }
  
  def renderComponent(m: Model, v: View): Either[Matrix1, Matrix3] = {
//    FileUtils.readFileToString(new File(m.directory.file)
    ???
  }
}



