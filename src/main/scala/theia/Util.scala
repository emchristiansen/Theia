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
        val float = (x - min) / (max -min)
        float.toInt
      }
      PixelTools.rgb(normalize(r), normalize(g), normalize(b))
    }
//    PixelTools.rgb(10)
//    val raster = new RGBRaster(m.cols, m.rows, ???)
    Image(m.cols, m.rows, pixels.toArray)
//    val raster = com.sksamuel.scrimage.Raster(1, 1, ???, ???)
   ??? 
  }
  
//  class RGBRaster(val width: Int, val height: Int, val model: Array[Byte])
}