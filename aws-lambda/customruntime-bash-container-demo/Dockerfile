FROM public.ecr.aws/lambda/provided:al2

COPY bootstrap ${LAMBDA_RUNTIME_DIR}/
COPY function.sh ${LAMBDA_TASK_ROOT}/

CMD [ "function.handler" ]
