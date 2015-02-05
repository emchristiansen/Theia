package theia.mitsuba

import theia._
import theia.MSR._

trait ShowXML[T] {
  def toXML(x: T): String
}

object Config {
  implicit object ShowXMLIntegrator extends ShowXML[Integrator] {
    override def toXML(i: Integrator) = i match {
      case RGB => "<integrator type=\"path\"/>"
      case Position =>
        """
<integrator type="field">
  <string name="field" value="position"/>
</integrator>
"""
      case Depth =>
        """
<integrator type="field">
  <string name="field" value="distance"/>
</integrator>
"""
    }
  }

  def formatVector(v: Vector3D): String = s"$v.data(0),$v.data(1),$v.data(2)"

  implicit object ShowXMLSensorInt extends ShowXML[(Sensor, Int)] {
    override def toXML(si: (Sensor, Int)) = {
      val (s, numChannels) = si
      assert(numChannels == 1 || numChannels == 3)

      val target = Vector3D(s.cameraFrame.origin.data.data +
        s.cameraFrame.lookDirection.data.data)

      val d: Double = s.cameraFrame.fieldOfView
      val o = formatVector(s.cameraFrame.origin)
      val t = formatVector(target)
      val u = formatVector(s.cameraFrame.up)
      val sc = s.sampleCount
      val r = s.resolution
      val pixelFormat = if (numChannels == 1) "luminance" else "rgb"

      s"""
<sensor type="perspective">
  <float name="nearClip" value="10"/>
  <float name="farClip" value="2800"/>
  <float name="focusDistance" value="1000"/>
  <float name="fov" value="$d"/>

  <transform name="toWorld">
    <lookAt origin="$o" target="$t" up="$u"/>
  </transform>

  <sampler type="ldsampler">
    <integer name="sampleCount" value="$sc"/>
  </sampler>

  <film type="mfilm">
    <string name="fileFormat" value="numpy"/>
    <integer name="width" value="$r"/>
    <integer name="height" value="$r"/>
    <string name="pixelFormat" value="$pixelFormat"/>
    <rfilter type="gaussian"/>
  </film>
</sensor>"""
    }
  }
}
