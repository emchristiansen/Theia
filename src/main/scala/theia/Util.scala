package theia

import com.sksamuel.scrimage._
import theia.MSR._

object Util {
  def matrix3ToImage(m: Matrix3): Image = {
    val floats = m.activeValuesIterator.toList
    val flattened = floats.flatMap({case(r,g,b) => List(r,g,b)})
    val max = flattened.max
    val min = flattened.min
    val pixels = floats.map { case (r, g, b) =>
      def normalize(x: Double): Int = {
        val pixelMax = 255.0
        val float = pixelMax * (x - min) / (max -min)
        float.toInt
      }
      PixelTools.rgb(normalize(r), normalize(g), normalize(b))
    }
    Image(m.cols, m.rows, pixels.toArray)
  }
}