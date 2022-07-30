

import abc

class IBroker(abc.ABC):

	name: str

	def __repr__(self):
		return 'Broker name: {name}'.format(name=self.name)

