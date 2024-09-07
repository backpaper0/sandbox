import logging

import azure.functions as func

app = func.FunctionApp()


@app.route(route="HttpExample", auth_level=func.AuthLevel.ANONYMOUS)
def HttpExample(req: func.HttpRequest) -> func.HttpResponse:
    logging.info("Python HTTP trigger function processed a request.")

    name = req.params.get("name")
    if not name:
        try:
            req_body = req.get_json()
        except ValueError:
            pass
        else:
            name = req_body.get("name")

    if name:
        return func.HttpResponse(
            f"Hello, {name}. This HTTP triggered function executed successfully."
        )
    else:
        return func.HttpResponse(
            "This HTTP triggered function executed successfully. Pass a name in the query string or in the request body for a personalized response.",
            status_code=200,
        )


@app.function_name(name="CosmosDBTrigger")
@app.cosmos_db_trigger(
    arg_name="documents",
    connection="CosmosDBConnectionString",
    database_name="example_db",
    container_name="example_container",
    create_lease_container_if_not_exists=True,
)
def test_function(documents: func.DocumentList) -> None:
    if documents:
        for index, doc in enumerate(documents):
            logging.info(f"{index} Document: {dict(doc)}")
    else:
        logging.info("No documents found")
