package theia

import breeze.numerics.sqrt
import com.sksamuel.scrimage._
import theia.MSR._

object Util {
  def gammaCorrectSafe(m: Matrix3Safe): Matrix3Safe = {
    m.mapValues(_.map({ case (r, g, b) => (math.pow(r, 1/2.2), math.pow(g, 1/2.2), math.pow(b, 1/2.2))}))
  }

  def matrix1SafeToImage(m: Matrix1Safe): Image = {
    val options = m.activeValuesIterator.toList
    val doubles = options.flatten
    val max = doubles.max
    val min = doubles.min
    val pixels = options.map { case og => og match {
      case None => PixelTools.argb(0, 0, 0, 0)
      case Some(g) => {
        def normalize(x: Double): Int = {
          val pixelMax = 255.0
          val float = pixelMax * (x - min) / (max - min)
          float.toInt
        }
        val n = normalize(g)
        PixelTools.rgb(n, n, n)
      }
    }
    }
    Image(m.cols, m.rows, pixels.toArray)
  }

  def matrix3SafeToImage(m: Matrix3Safe): Image = {
    val options = m.activeValuesIterator.toList
    val doubles = options.flatten.flatMap({case(r,g,b) => List(r,g,b)})
    val max = doubles.max
    val min = doubles.min
    val pixels = options.map { case og => og match {
      case None => PixelTools.argb(0, 0, 0, 0)
      case Some((r, g, b)) => {
        def normalize(x: Double): Int = {
          val pixelMax = 255.0
          val float = pixelMax * (x - min) / (max - min)
          float.toInt
        }
        PixelTools.rgb(normalize(r), normalize(g), normalize(b))
      }
    }
    }
    Image(m.cols, m.rows, pixels.toArray)
  }

//  def matrix3ToImage(m: Matrix3): Image = {
//    val floats = m.activeValuesIterator.toList
//    val flattened = floats.flatMap({case(r,g,b) => List(r,g,b)})
//    val max = flattened.max
//    val min = flattened.min
//    val pixels = floats.map { case (r, g, b) =>
//      def normalize(x: Double): Int = {
//        val pixelMax = 255.0
//        val float = pixelMax * (x - min) / (max - min)
//        float.toInt
//      }
//      PixelTools.rgb(normalize(r), normalize(g), normalize(b))
//    }
//    Image(m.cols, m.rows, pixels.toArray)
//  }
}