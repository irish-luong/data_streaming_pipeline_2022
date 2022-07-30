import os

from app import settings




# FastAPI application version = '1.0'

class ApplicationInfo:

	version = '1.0'
	descriptions = """
		Hello world! We are Space19x
	"""
	tag_metadata = [
		{
			'name': 'eInvoid',
			'description': 'Handle E-Invoid request'
		}

	]

	title = 'Space19x application'
	contact = {
		'Team': 'Space19x',
		'URL': 'https://github.com/space19x'
	}
