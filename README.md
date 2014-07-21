Play Domain
=================

A library for get the business domain in Play2.

## How to use?

    object Global extends WithFilters(BusinessDomainFilter) with GlobalSettings`

    import com.despegar.domain.play.DomainAction
    import play.api.mvc.Controller
    object DataController extends Controller {
        def data = DomainAction.async { request =>
            val countryCode = request.businessDomain.countryCode
            ...
        }
    }
