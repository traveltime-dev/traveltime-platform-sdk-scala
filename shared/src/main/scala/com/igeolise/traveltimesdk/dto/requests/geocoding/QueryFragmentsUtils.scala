package com.igeolise.traveltimesdk.dto.requests.geocoding

import com.igeolise.traveltimesdk.dto.common.Coords

object QueryFragmentsUtils {
  def queryFragments(query: String, coords: Option[Coords], countryCode: Option[String]): Seq[(String, String)] =
    ("query", query) +:
    Seq(
      ("focus.lat", coords.map(_.lat.toString)),
      ("focus.lng", coords.map(_.lng.toString)),
      ("within.country", countryCode)
    ).collect {
      case (key, Some(value)) => (key, value)
    }
}
