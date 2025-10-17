ARG PYTHON_VERSION=3.13


FROM python:${PYTHON_VERSION} AS chrome_builder

ARG CHROME_VERSION=141.0.7390.78

ADD https://storage.googleapis.com/chrome-for-testing-public/${CHROME_VERSION}/linux64/chrome-linux64.zip /tmp/
ADD https://storage.googleapis.com/chrome-for-testing-public/${CHROME_VERSION}/linux64/chromedriver-linux64.zip /tmp/

RUN <<EOF
apt update
apt install -y --no-install-recommends \
    unzip
unzip /tmp/chrome-linux64.zip -d /tmp/
unzip /tmp/chromedriver-linux64.zip -d /tmp/
EOF


FROM python:${PYTHON_VERSION} AS python_builder

RUN pip install uv

WORKDIR /app

COPY pyproject.toml uv.lock .python-version /app

RUN <<EOF
uv export --format requirements-txt --output-file requirements.txt
pip install --no-cache-dir --prefix /install -r requirements.txt
EOF


FROM python:${PYTHON_VERSION} AS runtime

ARG TZ=Asia/Tokyo

RUN <<EOF
apt update
apt install -y --no-install-recommends \
    unzip \
    fontconfig \
    libxcomposite1 \
    libxcursor1 \
    libxdamage1 \
    libxext6 \
    libxi6 \
    libxtst6 \
    libcups2 \
    libxss1 \
    libxrandr2 \
    libasound2 \
    libatk1.0-0 \
    libgtk-3-0 \
    libnss3 \
    libdrm2 \
    xdg-utils \
    libxshmfence1 \
    libgbm1 \
    fonts-noto-cjk \
    locales \
    tzdata
apt clean
rm -rf /var/lib/apt/lists/*

sed -i 's/^# *ja_JP.UTF-8 UTF-8/ja_JP.UTF-8 UTF-8/' /etc/locale.gen
locale-gen
update-locale LANG=ja_JP.UTF-8 LC_ALL=ja_JP.UTF-8

ln -snf /usr/share/zoneinfo/$TZ /etc/localtime
echo $TZ > /etc/timezone
EOF

COPY --from=chrome_builder /tmp/chrome-linux64 /opt/chrome-linux64
COPY --from=chrome_builder /tmp/chromedriver-linux64/chromedriver /usr/bin

RUN <<EOF
ln -s /opt/chrome-linux64/chrome /usr/bin/chrome
chmod +x /usr/bin/chromedriver
EOF

WORKDIR /app
COPY --from=python_builder /install /usr/local
COPY main.py /app

ENV CHROME_PATH=/usr/bin/chrome
ENV WEBDRIVER_PATH=/usr/bin/chromedriver
ENV TZ=${TZ}

CMD ["python", "main.py"]