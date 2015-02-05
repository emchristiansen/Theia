package theia.mitsuba

trait ShowXML[T] {
  def toXML(x: T): String
}

case class Foo(x: Int)

object Config {
  implicit object ShowXMLFoo extends ShowXML[Foo] {
    override def toXML(x: Foo) = "foo"
  }
}
