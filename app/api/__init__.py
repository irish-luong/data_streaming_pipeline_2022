# Pure Python libraries
from logging.config import dictConfig

# Framework modules
from fastapi import FastAPI, Depends

# Project modules
from app.api.settings import API_KEY
from app.api.containers import Container
from app.api.settings import LOGGING, ApplicationInfo
from app.api.v1.middleware.authorization import Authorization
from app.api.v1.router.invoice import router as invoice_router


def create_app() -> FastAPI:
    # Config log format in project
    dictConfig(LOGGING)

    # Create app instance
    app = FastAPI(
        title=ApplicationInfo.title,
        contact=ApplicationInfo.contact,
        version=ApplicationInfo.version,
        openapi_tags=ApplicationInfo.tag_metadata,
        description=ApplicationInfo.descriptions,
    )

    # Inject container
    app.container = Container()

    # Init authorization
    auth_instance = Authorization(auth_key=API_KEY)

    # Add router
    app.include_router(invoice_router, dependencies=[Depends(auth_instance.validate_bear_token)])

    # Enable ping api
    @app.get('/ping')
    def ping():
        return {'result': 'pong'}

    return app
