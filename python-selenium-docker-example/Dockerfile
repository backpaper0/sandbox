FROM python:3.13

ARG CHROME_VERSION=141.0.7390.78
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

RUN <<EOF
curl -o /tmp/chrome-linux64.zip https://storage.googleapis.com/chrome-for-testing-public/${CHROME_VERSION}/linux64/chrome-linux64.zip
unzip /tmp/chrome-linux64.zip -d /opt/
ln -s /opt/chrome-linux64/chrome /usr/bin/chrome
rm /tmp/chrome-linux64.zip
EOF

RUN <<EOF
curl -o /tmp/chromedriver-linux64.zip https://storage.googleapis.com/chrome-for-testing-public/${CHROME_VERSION}/linux64/chromedriver-linux64.zip
unzip /tmp/chromedriver-linux64.zip -d /tmp/
mv /tmp/chromedriver-linux64/chromedriver /usr/bin/chromedriver
chmod +x /usr/bin/chromedriver
rm -rf /tmp/chromedriver-linux64.zip /tmp/chromedriver-linux64
EOF

RUN pip install uv

WORKDIR /app

COPY pyproject.toml uv.lock .python-version /app

RUN <<EOF
uv export --format requirements-txt --output-file requirements.txt
pip install -r requirements.txt
EOF

COPY main.py /app

ENV CHROME_PATH=/usr/bin/chrome
ENV WEBDRIVER_PATH=/usr/bin/chromedriver
ENV TZ=${TZ}

CMD ["python", "main.py"]