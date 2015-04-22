package theia

import breeze.numerics.sqrt
import com.sksamuel.scrimage._
import theia.MSR._

object Util {
  def gammaCorrect(m: Matrix3): Matrix3 = {
    m.mapValues({ case (r, g, b) => (math.pow(r, 1/2.2), math.pow(g, 1/2.2), math.pow(b, 1/2.2))})
  }

  def matrix1ToImage(m: Matrix1): Image = {
    val floats = m.activeValuesIterator.toList
    val max = floats.max
    val min = floats.min
    val pixels = floats.map { case g =>
      def normalize(x: Double): Int = {
        val pixelMax = 255.0
        val float = pixelMax * (x - min) / (max - min)
        float.toInt
      }
      val n = normalize(g)
      PixelTools.rgb(n, n, n)
    }
    Image(m.cols, m.rows, pixels.toArray)
  }

  def matrix3ToImage(m: Matrix3): Image = {
    val floats = m.activeValuesIterator.toList
    val flattened = floats.flatMap({case(r,g,b) => List(r,g,b)})
    val max = flattened.max
    val min = flattened.min
    val pixels = floats.map { case (r, g, b) =>
      def normalize(x: Double): Int = {
        val pixelMax = 255.0
        val float = pixelMax * (x - min) / (max - min)
        float.toInt
      }
      PixelTools.rgb(normalize(r), normalize(g), normalize(b))
    }
    Image(m.cols, m.rows, pixels.toArray)
  }
}