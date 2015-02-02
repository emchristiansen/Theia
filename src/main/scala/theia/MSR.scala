package theia

import breeze.linalg._

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

case class RGBImage(data: DenseMatrix[(Double, Double, Double)]) {
  // TODO(ericmc): Assert that all values are in [0, 1].
}

case class PositionMap(data: DenseMatrix[(Double, Double, Double)])

case class DepthMap(data: DenseMatrix[Double]) {
  // TODO(emchristiansen): Assert that all values are positive.
}

case class Rendering(rgb: RGBImage, position: PositionMap, depth: DepthMap)
