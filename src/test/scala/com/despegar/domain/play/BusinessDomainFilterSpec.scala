package com.despegar.domain.play

import scala.io.Source
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner._

@RunWith(classOf[JUnitRunner])
class BusinessDomainFilterSpec extends Specification {

  "The 'Hello world' string" should {
    val resource = getClass.getResource("/business_domains.json")
    val source = Source.fromURL(resource).getLines.mkString("\n")

    "contain 11 characters" in {
      val rules = BusinessDomainFilter.parseBusinessDomainJsonData(source)
      rules must not be empty
    }
  }

}