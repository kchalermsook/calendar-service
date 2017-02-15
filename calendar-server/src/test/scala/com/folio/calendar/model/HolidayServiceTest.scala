package com.folio.calendar.model

import java.time.LocalDate

import com.folio.calendar.idl.Calendar
import com.folio.calendar.module.QuillDbContextModule
import com.folio.calendar.service.HolidayService
import com.twitter.inject.app.TestInjector
import com.twitter.util.{Await, Future}
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

class HolidayServiceTest extends WordSpec with Matchers with BeforeAndAfterEach{

val injector = TestInjector(QuillDbContextModule)
  val repo = injector.instance[HolidayRepo]
  val service = injector.instance[HolidayService]

  override def beforeEach(): Unit = {
    repo.deleteAll.value
  }

  "insert and select" in {
    repo.insert(Holiday(Calendar.Jpx, LocalDate.of(2017, 2, 14), None)).value
    val res = service.getHolidays(Calendar.Jpx).value
    info(res.toString)
    res should have length 1
    res.head.calendar shouldBe Calendar.Jpx
    res.head.date shouldBe LocalDate.of(2017,2,14)
    res.head.note shouldBe None
  }



  //convenient implicit to add .value to Future type instead of calling Await.result
  implicit class RichFuture[T](future: Future[T]) {
    def value: T = {
      Await.result(future)
    }
  }
}
