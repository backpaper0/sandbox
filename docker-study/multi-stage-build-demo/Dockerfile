FROM ubuntu as foo
WORKDIR /workspace
RUN touch ./foo

FROM ubuntu as bar
WORKDIR /workspace
RUN touch ./bar

FROM foo
COPY --from=bar /workspace/bar ./
CMD ["ls", "/workspace"]

