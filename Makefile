test_api:
	export PYTHONPATH=$(pwd)
	@echo ${PYTHONPATH};
	pytest app/api/v1

start_app:
	uvicorn app.api.main:app --host 0.0.0.0 --port 5000 --reload

	