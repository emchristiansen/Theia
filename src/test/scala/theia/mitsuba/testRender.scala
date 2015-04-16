package theia.mitsuba

import java.io.File
import javax.imageio.ImageIO

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import st.sparse.sundry.{FunGeneratorSuite, _}
import theia._

@RunWith(classOf[JUnitRunner])
class TestMitsuba extends FunGeneratorSuite with TestingUtil {
  test("use Mitsuba to render a scene", MediumTest, InteractiveTest) {
    val m = Model(zippedCBox, "scene_template.xml")
    val cf = CameraFrame(Degrees(40), Origin(Vector3D(1, 2, 3)), LookDirection(Vector3D(1, 2, 3)), Up(Vector3D(1, 2, 3)))
    val s = Sensor(cf, 256, 16)

    val rendering = Render.render(m, s)

    val rgbImage = Util.matrix3ToImage(rendering.rgb)
    val rgbFile = new File(outputRoot, "mitsuba_rgb.png")
    println(s"About to write: $rgbFile")
    ImageIO.write(rgbImage.awt, "png", rgbFile)

    //    val m = DenseMatrix.tabulate[(Double, Double, Double)](64, 64) {
    //      case (r, c) => (r.toDouble, c.toDouble, sin(r / 16.0))
    //    }
    //
    //    val i = Util.matrix3ToImage(m)
    //
    //    val f = new File(outputRoot, "matrix_to_image.png")
    //    println(s"About to write: $f")
    //    ImageIO.write(i.awt, "png", f)
  }
}