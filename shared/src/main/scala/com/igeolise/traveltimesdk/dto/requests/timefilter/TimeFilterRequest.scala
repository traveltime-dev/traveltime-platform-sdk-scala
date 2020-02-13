package com.igeolise.traveltimesdk.dto.requests.timefilter

import cats.Monad
import com.igeolise.traveltimesdk.json.reads.timefilter.TimeFilterReads._
import com.igeolise.traveltimesdk.json.writes.timefilter.TimeFilterWrites._
import com.igeolise.traveltimesdk.dto.requests.RequestUtils
import com.igeolise.traveltimesdk.dto.requests.RequestUtils.TravelTimePlatformRequest
import com.igeolise.traveltimesdk.dto.requests.common.CommonProperties.TimeFilterRequestProperty
import com.igeolise.traveltimesdk.dto.requests.common.RangeParams.FullRangeParams
import com.igeolise.traveltimesdk.dto.requests.common.Location
import com.igeolise.traveltimesdk.dto.responses.TravelTimeSdkError
import com.igeolise.traveltimesdk.dto.responses.timefilter.TimeFilterResponse
import com.softwaremill.sttp._
import play.api.libs.json.Json

import scala.concurrent.duration.FiniteDuration
import java.time.ZonedDateTime

import com.igeolise.traveltimesdk.dto.requests.common.Transportation.CommonTransportation

import scala.language.higherKinds

case class TimeFilterRequest(
  locations: Seq[Location],
  departureSearches: Seq[TimeFilterRequest.DepartureSearch],
  arrivalSearches:   Seq[TimeFilterRequest.ArrivalSearch]
) extends TravelTimePlatformRequest[TimeFilterResponse] {
  val endpoint = TimeFilterRequest.endpoint

  override def send[R[_] : Monad, S](
    sttpRequest: RequestUtils.SttpRequest[R, S]
  ): R[Either[TravelTimeSdkError, TimeFilterResponse]] =
    RequestUtils.send(
      sttpRequest,
      _.validate[TimeFilterResponse]
    )

  override def sttpRequest(host: Uri): Request[String, Nothing] =
    RequestUtils.makePostRequest(
      Json.toJson(this),
      endpoint,
      host
    ).headers(HeaderNames.Accept -> MediaTypes.Json)
}

object TimeFilterRequest {
  val endpoint = "v4/time-filter"

  sealed trait SearchType

  case class DepartureSearch(
    id: String,
    departureLocationId: String,
    arrivalLocationIds: Seq[String],
    transportation: CommonTransportation,
    travelTime: FiniteDuration,
    departureTime: ZonedDateTime,
    range: Option[FullRangeParams],
    properties: Seq[TimeFilterRequestProperty]
  ) extends SearchType

  case class ArrivalSearch(
    id: String,
    departureLocationIds: Seq[String],
    arrivalLocationId: String,
    transportation: CommonTransportation,
    travelTime: FiniteDuration,
    arrivalTime: ZonedDateTime,
    range: Option[FullRangeParams],
    properties: Seq[TimeFilterRequestProperty]
  ) extends SearchType
}


