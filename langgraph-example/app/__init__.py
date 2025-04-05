import mlflow

# mlflow.set_tracking_uri("http://localhost:5000")
#   or MLFLOW_TRACKING_URI=http://localhost:5000
mlflow.langchain.autolog()
mlflow.set_experiment("langgraph-example")