package com.rockthejvm.reviewboard.pages

import com.raquo.laminar.api.L.*
import com.raquo.laminar.api.L.given
import frontroute.*
import com.rockthejvm.reviewboard.components.Anchors
import com.rockthejvm.reviewboard.domain.data.*
import com.rockthejvm.reviewboard.common.Constants

object CompaniesPage {

  val dummyCompany = Company(
    1L,
    "simple-company",
    "Simple Company",
    "http://dummy.com",
    Some("Anywhere"),
    Some("On Mars"),
    Some("space travel"),
    None,
    Some(List("space", "scala"))
  )

  def apply() = sectionTag(
    cls := "section-1",
    div(
      cls := "container company-list-hero",
      h1(
        cls := "company-list-title",
        "Rock the JVM Companies Board"
      )
    ),
    div(
      cls := "container",
      div(
        cls := "row jvm-recent-companies-body",
        div(
          cls := "col-lg-4",
          div("TODO filter panel here")
        ),
        div(
          cls := "col-lg-8",
          renderCompany(dummyCompany),
          renderCompany(dummyCompany)
        )
      )
    )
  )

  private def renderCompanyPicture(company: Company) = img(
    cls := "img-fluid",
    src := company.image.getOrElse(Constants.companyLogoPlaceholder),
    alt := company.name
  )

  private def renderDetail(icon: String, value: String) = div(
    cls := "company-detail",
    i(cls := s"fa fa-${icon} company-detail-icon"),
    p(
      cls := "company-detail-value",
      value
    )
  )

  private def fullLocationString(company: Company): String =
    (company.location, company.country) match {
      case (Some(location), Some(country)) => s"$location, $country"
      case (Some(location), None)          => location
      case (None, Some(country))           => country
      case (None, None)                    => ""
    }

  private def renderOverview(company: Company) = div(
    cls := "company-summary",
    renderDetail("location-dot", fullLocationString(company)),
    renderDetail("tags", company.tags.getOrElse(List("")).mkString(", "))
  )

  private def renderAction(company: Company) = div(
    cls := "jvm-recent-companies-card-btn-apply",
    a(
      href   := company.url,
      target := "blank",
      button(
        `type` := "button",
        cls    := "btn btn-danger rock-action-btn",
        "Website"
      )
    )
  )

  def renderCompany(company: Company) = div(
    cls := "jvm-recent-companies-cards",
    div(
      cls := "jvm-recent-companies-card-img",
      renderCompanyPicture(company)
    ),
    div(
      cls := "jvm-recent-companies-card-contents",
      h5(
        Anchors.renderNavLink(
          company.name,
          s"/company/${company.id}",
          "company-title-link"
        )
      ),
      renderOverview(company)
    ),
    renderAction(company)
  )
}
