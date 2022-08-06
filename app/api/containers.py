

# 3PL modules
from dependency_injector import containers, providers


# Project modules
from app.settings import BOOTSTRAP_SERVERS
from libs.services import InvoiceService
from libs.adapters.message_broker.kafka.producer import Producer


class Container(containers.DeclarativeContainer):

	# Load modules which injected dependencies
	wiring_config = containers.WiringConfiguration(modules=[
			'.v1.router.invoice'
		])

	producer = providers.Singleton(Producer, bootstrap_servers=BOOTSTRAP_SERVERS)

	invoice_service = providers.Factory(InvoiceService, producer=producer)
