test_api:
	export PYTHONPATH=$(pwd)
	@echo ${PYTHONPATH};
	pytest app/api/v1

start_app:
	export DEBUG="true"
	uvicorn app.api.main:app --host 0.0.0.0 --port 5000 --reload

build-spark:
	docker build ./docker/ -f ./docker/Dockerfile -t cluster-apache-spark:3.2.0