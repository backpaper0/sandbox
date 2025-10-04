FROM python:3.12-slim

ARG UV_VERSION=0.8.22
ARG USERNAME=app

WORKDIR /app

RUN <<EOF
useradd -m -s /bin/bash -u 1000 -U $USERNAME
chown -R $USERNAME /app
EOF

USER $USERNAME

ENV PATH=/home/$USERNAME/.local/bin:$PATH

COPY --chown=$USERNAME .python-version /app
COPY --chown=$USERNAME main.py /app
COPY --chown=$USERNAME pyproject.toml /app
COPY --chown=$USERNAME README.md /app
COPY --chown=$USERNAME uv.lock /app

RUN <<EOF
pip install --no-cache-dir uv==$UV_VERSION
uv sync
EOF

CMD ["uv", "run", "main.py"]
