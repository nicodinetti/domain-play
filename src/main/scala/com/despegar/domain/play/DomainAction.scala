package com.despegar.domain.play

import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent._
import org.apache.commons.lang3.LocaleUtils
import com.despegar.domain.model.BusinessDomain
import scala.util._

class DomainRequest[A](val businessDomain: BusinessDomain, request: Request[A]) extends WrappedRequest(request)

object DomainAction extends ActionBuilder[DomainRequest] {
  def invokeBlock[A](request: Request[A], block: (DomainRequest[A]) => Future[Result]) = {
    Try {
      BusinessDomain(
        request.tags.get(BusinessDomain.COUNTRYCODE_TAG_NAME).get,
        request.tags.get(BusinessDomain.BRANDNAME_TAG_NAME).get,
        request.tags.get(BusinessDomain.BRANDID_TAG_NAME).get.toInt,
        LocaleUtils.toLocale(request.tags.get(BusinessDomain.LOCALE_TAG_NAME).get))
    } match {
      case Success(businessDomain) => block(new DomainRequest(businessDomain, request))
      case Failure(_) => Future.successful(BadRequest)
    }
  }
}