package theia

import breeze.linalg._

import MSR._

case class Model(val files: List[Byte], val sceneTemplatePath: String)

sealed abstract class Integrator
object RGB extends Integrator
object Position extends Integrator
object Depth extends Integrator

case class Degrees(data: Double) {
  assert(data > 0)
  assert(data < 180)
}

case class Vector3D(data: DenseVector[Double]) {
  assert(data.size == 3)
}

case class Origin(data: Vector3D)

case class LookDirection(data: Vector3D)

case class Up(data: LookDirection)

case class CameraFrame(
  fieldOfView: Degrees,
  origin: Origin,
  lookDirection: LookDirection,
  up: Up
) {
  // TODO(emchristiansen): Ensure the look direction is orthogonal to
  // the up direction.
}

case class Sensor(cameraFrame: CameraFrame, resolution: Int, sampleCount: Int)

case class View(integrator: Integrator, sensor: Sensor)

case class RGBImage(data: Matrix3) {
  // TODO(ericmc): Assert that all values are in [0, 1].
}

case class PositionMap(data: Matrix3)

case class DepthMap(data: Matrix1) {
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

  implicit def degreesToDouble(x: Degrees): Double = x.data
  implicit def rgbimageToMatrix(x: RGBImage): Matrix3 = x.data
  implicit def positionmapToMatrix(x: PositionMap): Matrix3 = x.data
  implicit def depthmapToMatrix(x: DepthMap): Matrix1 = x.data

  type Renderer = Model => Sensor => Rendering
}
