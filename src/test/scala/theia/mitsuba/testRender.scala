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
    val cf = CameraFrame(Degrees(40), Origin(Vector3D(278, 273, -800)), LookDirection(Vector3D(0, 0, 1)), Up(Vector3D(0, 1, 0)))
    val s = Sensor(cf, 256, 32)

    val rendering = Render.render(m, s)

    val rgbImage = Util.matrix3SafeToImage(Util.gammaCorrectSafe(rendering.rgb))
    val rgbFile = new File(outputRoot, "mitsuba_rgb.png")
    println(s"About to write: $rgbFile")
    ImageIO.write(rgbImage.awt, "png", rgbFile)

    val positionImage = Util.matrix3SafeToImage(rendering.position)
    val positionFile = new File(outputRoot, "mitsuba_position.png")
    println(s"About to write: $positionFile")
    ImageIO.write(positionImage.awt, "png", positionFile)

    val depthImage = Util.matrix1SafeToImage(rendering.depth)
    val depthFile = new File(outputRoot, "mitsuba_depth.png")
    println(s"About to write: $depthFile")
    ImageIO.write(depthImage.awt, "png", depthFile)
  }
}