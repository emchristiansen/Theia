package theia.mitsuba

import theia.View
import theia.mitsuba.Config._
import theia.Integrator
import theia.Sensor
import scala.util.Random
import java.io.File
import org.apache.commons.io.FileUtils
import scala.sys.process._

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
}