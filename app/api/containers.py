

# 3PL modules
from dependency_injector import containers, providers


# Project modules
from libs.services import InvoiceService


class Container(containers.DeclarativeContainer):

	# Load modules which injected dependencies
	wiring_config = containers.WiringConfiguration(modules=[
			'.v1.router.invoice'
		])

	invoice_service = providers.Factory(InvoiceService)
