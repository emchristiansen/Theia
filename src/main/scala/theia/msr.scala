package theia

import breeze.linalg._
import org.scalatest.DiagrammedAssertions._

import MSR._
import st.sparse.sundry.Box

case class Model(val zippedDirectory: List[Byte], val sceneTemplatePath: String)

sealed abstract class Integrator
object RGB extends Integrator
object Position extends Integrator
object Depth extends Integrator

case class Degrees(data: Double) extends Box[Double] {
  assert(data > 0)
  assert(data < 180)
}

case class Vector3D(data: DenseVector[Double]) extends Box[DenseVector[Double]] {
  assert(data.size == 3)
}

object Vector3D {
  def apply(x0: Double, x1: Double, x2: Double): Vector3D = Vector3D(DenseVector(x0, x1, x2))
}

case class Origin(data: Vector3D) extends Box[Vector3D]

case class LookDirection(data: Vector3D) extends Box[Vector3D] {
  // TODO(emchristiansen): Ensure this is unit length.
}

case class Up(data: Vector3D) extends Box[Vector3D] {
  // TODO(emchristiansen): Ensure this is unit length.
}

case class CameraFrame(
  fieldOfView: Degrees,
  origin: Origin,
  lookDirection: LookDirection,
  up: Up) {
  // TODO(emchristiansen): Ensure the look direction is orthogonal to
  // the up direction.
}

case class Sensor(cameraFrame: CameraFrame, resolution: Int, sampleCount: Int)

case class View(integrator: Integrator, sensor: Sensor)

case class RGBImage(data: Matrix3Safe) extends Box[Matrix3Safe] {
  // TODO(emchristiansen): Assert that all values are in [0, 1].
}

case class PositionMap(data: Matrix3Safe) extends Box[Matrix3Safe]

case class DepthMap(data: Matrix1Safe) extends Box[Matrix1Safe] {
  // TODO(emchristiansen): Assert that all values are positive.
}

case class Rendering(rgb: RGBImage, position: PositionMap, depth: DepthMap) {
  assert(rgb.rows > 0)
  assert(rgb.cols > 0)
  assert(rgb.rows == position.rows)
  assert(rgb.cols == position.cols)
  assert(rgb.rows == depth.rows)
  assert(rgb.cols == depth.cols)
}

object MSR {
  type Matrix1 = DenseMatrix[Double]
  type Matrix3 = DenseMatrix[(Double, Double, Double)]

  type Matrix1Safe = DenseMatrix[Option[Double]]
  type Matrix3Safe = DenseMatrix[Option[(Double, Double, Double)]]

//  implicit def unBox[A](b: Box[A]): A = b.data

  type Renderer = (Model, Sensor) => Rendering
}
