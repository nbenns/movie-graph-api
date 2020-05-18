package com.kaizen.api

final case class PageParams(count: Long, before: Option[String], after: Option[String])
