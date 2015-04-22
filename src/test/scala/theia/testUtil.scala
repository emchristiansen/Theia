package theia

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
import theia._
import breeze.linalg.DenseMatrix
import theia.mitsuba.Render
import scala.math._
import javax.imageio.ImageIO
import java.io.File

@RunWith(classOf[JUnitRunner])
class TestUtil extends FunGeneratorSuite with TestingUtil {
  test("matrix to image", MediumTest, InteractiveTest) {
    val m = DenseMatrix.tabulate[Option[(Double, Double, Double)]](64, 64) {
      case (r, c) => Some((r.toDouble, c.toDouble, sin(r / 16.0)))
    }
    
    val i = Util.matrix3SafeToImage(m)
    
    val f = new File(outputRoot, "matrix_to_image.png")
    println(s"About to write: $f")
    ImageIO.write(i.awt, "png", f)
  }
}