test_api:
	export PYTHONPATH=$(pwd)
	@echo ${PYTHONPATH};
	pytest app/api/v1

setup:
	@echo "Set up virtual environment"
	