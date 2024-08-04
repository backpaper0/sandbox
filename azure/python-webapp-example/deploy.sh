#!/bin/bash
source .vars
rm requirements.txt deploy.zip
poetry export --format=requirements.txt --output=requirements.txt
zip -r deploy.zip . -x .gitignore .vars '__pycache__/*' '.git/*' '.mypy_cache/*' '*.zip' deploy.sh poetry.lock pyproject.toml README.md
az webapp deploy --resource-group $AZURE_RESOURCE_GROUP --name $AZURE_APP_SERVICE_NAME --src-path deploy.zip --type zip