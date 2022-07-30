import os

print('Welcome to streaming application')

# Core configurations
DEBUG = os.getenv('DEBUG', 'false') == 'true'

LOGGING = {
        "version": 1,
        "disable_existing_loggers": True,
        "formatters": {
            "default": {
                "format": "%(message)s",
            },
            "access": {
                "format": "%(message)s",
            }
        },
        "handlers": {
            "console": {
                "level": "DEBUG" if DEBUG else "INFO",
                "class": "logging.StreamHandler",
                "formatter": "default" if DEBUG else "access",
                "stream": "ext://sys.stdout",
            }
        },
        "loggers": {
            "gunicorn.error": {
                "handlers": ["console"],
                "level": "INFO",
                "propagate": False,
            },
            "gunicorn.access": {
                "handlers": ["console"],
                "level": "INFO",
                "propagate": False,
            }
        },
        "root": {
            "level": "DEBUG" if DEBUG else "INFO",
            "handlers": ["console"],
        }
}