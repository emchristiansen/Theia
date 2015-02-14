package theia.mitsuba

import org.scalatest.fixture
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.Gen
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import st.sparse.sundry.FunGeneratorSuite
import st.sparse.sundry._
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.PixelTools
import theia.TestUtil
import theia._

////////////////////////////////////////////////////////////////////////////////

@RunWith(classOf[JUnitRunner])
class TestRender extends FunGeneratorSuite with TestUtil {
  test("render cbox", MediumTest, InteractiveTest) {
    val m = Model(zippedCBox, "scene_template.xml")
    
    val d = Degrees(39.3077)
    val o = Origin(Vector3D(278, 272, -800))
    val u = Up(Vector3D(0, 1, 0))
    val ld = LookDirection(Vector3D(0, 0, 1))
    val cf = CameraFrame(d, o, ld, u)
    val s = Sensor(cf, 64, 16)
    
    val rendering = Render.render(m, s)
  }
}