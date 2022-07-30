# Pure Python libraries
from logging.config import dictConfig

# Framework modules
from fastapi import FastAPI


# Project modules
from app.api.containers import Container
from app.api.settings import LOGGING, ApplicationInfo


def create_app() -> FastAPI:

	# Config log format in project
	dictConfig(LOGGING)

	# Create app instance
	app = FastAPI(
		version=ApplicationInfo.version,
		title=ApplicationInfo.title,
		openapi_tags=ApplicationInfo.tag_metadata
	)

