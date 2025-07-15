FROM public.ecr.aws/lambda/python:3.11

# Copy code and install dependencies
COPY app.py ./
COPY requirements.txt ./
RUN pip install -r requirements.txt --target "${LAMBDA_TASK_ROOT}"

# Set the CMD to your handler (filename.handler-function)
CMD ["app.lambda_handler"]
