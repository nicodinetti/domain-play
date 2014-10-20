package com.despegar.domain.play

import play.api.i18n._
import play.api.http._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import java.util.Locale
import play.api.libs.json._
import org.apache.commons.lang3._
import scala.concurrent._
import com.despegar.domain.model.BusinessDomain
import com.despegar.domain.provider.BusinessDomainProvider

object BusinessDomainFilter extends Filter with BusinessDomainProvider {

  implicit lazy val BusinessDomainJsonRead: Reads[BusinessDomain] = (
    (__ \ "countryCode").read[String]
    and (__ \ "brandName").read[String]
    and (__ \ "brandId").read[Int]
    and (__ \ "locale").read[String].map { x => LocaleUtils.toLocale(x) }
    and (__ \ "default").read[Boolean])(BusinessDomain.apply _)

  val LANGUAGE_QUERY_PARAMETER = "l"
  val X_LANG_COOKIE = "X-Lang"

  case class DomainRequest(request: RequestHeader, businessDomain: BusinessDomain) extends RequestHeader {

    val lang = businessDomain.locale.getLanguage() + '-' + businessDomain.locale.getCountry()

    val _tags = request.tags ++ Map(
      BusinessDomain.COUNTRYCODE_TAG_NAME -> businessDomain.countryCode,
      BusinessDomain.BRANDNAME_TAG_NAME -> businessDomain.brandName,
      BusinessDomain.BRANDID_TAG_NAME -> businessDomain.brandId.toString,
      BusinessDomain.LOCALE_TAG_NAME -> businessDomain.locale.toString)

    val _headers = new Headers {
      val data = (request.headers.toMap + (HeaderNames.ACCEPT_LANGUAGE -> Seq(lang))).toList
    }

    def id = request.id
    def tags = _tags
    def headers = _headers
    def queryString = request.queryString
    def path = request.path
    def uri = request.uri
    def method = request.method
    def version = request.version
    def remoteAddress = request.remoteAddress
    def secure = request.secure
  }

  def parseBusinessDomainJsonData(source: String): Map[String, BusinessDomain] = Json.parse(source).as[Map[String, JsValue]].mapValues { _.as[BusinessDomain](BusinessDomainJsonRead) }

  def apply(next: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
    val uri = (if (request.secure) "https://" else "http://") + request.host + "/"
    val langForceParam = request.getQueryString(LANGUAGE_QUERY_PARAMETER)
    val langForceHeader = request.headers.get(X_LANG_COOKIE)
    val langForceCookie = request.cookies.get(X_LANG_COOKIE).map(_.value)

    findBusinessDomain(uri, langForceParam orElse langForceHeader orElse langForceCookie) match {
      case Some(businessDomain) => {
        next(DomainRequest(request, businessDomain))
      }
      case None => Future.successful(BadRequest)
    }
  }

}
