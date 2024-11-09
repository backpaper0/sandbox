FROM mcr.microsoft.com/azure-functions/python:4-python3.11

COPY pyproject.toml /
RUN pip install poetry && poetry -C / export -f requirements.txt -o requirements.txt

# To enable ssh & remote debugging on app service change the base image to the one below
# FROM mcr.microsoft.com/azure-functions/python:4-python3.11-appservice
FROM mcr.microsoft.com/azure-functions/python:4-python3.11

ENV AzureWebJobsScriptRoot=/home/site/wwwroot \
    AzureFunctionsJobHost__Logging__Console__IsEnabled=true

COPY --from=0 /requirements.txt /
RUN pip install -r /requirements.txt

COPY . /home/site/wwwroot