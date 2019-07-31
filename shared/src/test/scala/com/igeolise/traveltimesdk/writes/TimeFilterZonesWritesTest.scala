package com.igeolise.traveltimesdk.writes

import java.time.ZonedDateTime

import com.igeolise.traveltimesdk.TestUtils
import com.igeolise.traveltimesdk.dto.common.{Coords, ZoneSearches}
import com.igeolise.traveltimesdk.dto.requests.common.CommonProperties.PropertyType.{Coverage, TravelTimeAll, TravelTimeReachable}
import com.igeolise.traveltimesdk.dto.requests.common.Transportation
import com.igeolise.traveltimesdk.dto.requests.common.Transportation.PublicTransportationParams
import com.igeolise.traveltimesdk.dto.requests.timefilter.{TimeFilterDistrictsRequest, TimeFilterSectorsRequest}
import com.igeolise.traveltimesdk.json.writes.timefilter.TimeFilterDistrictsWrites._
import com.igeolise.traveltimesdk.json.writes.timefilter.TimeFilterSectorsWrites._
import org.scalatest.Matchers
import org.scalatest.funspec.AnyFunSpec
import play.api.libs.json.Json

import scala.concurrent.duration._

class TimeFilterZonesWritesTest extends AnyFunSpec with Matchers  {
  val departures = ZoneSearches.DepartureSearch(
    "public transport from Trafalgar Square",
    Coords(51.507609, -0.128315),
    Transportation.PublicTransport(PublicTransportationParams(None, None)),
    ZonedDateTime.parse("2018-10-01T08:00:00Z"),
    Duration(1800, SECONDS),
    0.1,
    Seq(
      Coverage, TravelTimeReachable, TravelTimeAll
    )
  )

  val arrivals = ZoneSearches.ArrivalSearch(
    "public transport to Trafalgar Square",
    Coords(51.507609, -0.128315),
    Transportation.PublicTransport(PublicTransportationParams(None, None)),
    ZonedDateTime.parse("2018-10-01T08:00:00Z"),
    Duration(1800, SECONDS),
    0.1,
    Seq(
      Coverage, TravelTimeReachable, TravelTimeAll
    )
  )

  it("departure and arrival searches for TimeFilter Districts") {
    val postcodesRequest = TimeFilterDistrictsRequest(Seq(departures), Seq(arrivals))
    val jsonResource = TestUtils.resource("shared/src/test/resources/json/TimeFilterDistricts/request/DistrictsRequest.json")
    val timeFilterJson = Json.toJson(postcodesRequest)

    timeFilterJson should equal (Json.parse(jsonResource))
  }

  it("departure and arrival searches for TimeFilter Sectors") {
    val postcodesRequest = TimeFilterSectorsRequest(Seq(departures), Seq(arrivals))
    val jsonResource = TestUtils.resource("shared/src/test/resources/json/TimeFilterSectors/request/SectorsRequest.json")
    val timeFilterJson = Json.toJson(postcodesRequest)

    timeFilterJson should equal (Json.parse(jsonResource))
  }
}
