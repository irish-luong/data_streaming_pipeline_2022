

# 3PL modules
from dependency_injector import containers

class Container(containers.DeclarativeContainer):


	# Load modules which injected dependencies
	wiring_config = containers.WiringConfigurations(modules=[
			'.v1.router.e_invoice'
		])

	