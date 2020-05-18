package com.kaizen.api.graphql

final case class PageParams(count: Long, before: Option[String], after: Option[String])
