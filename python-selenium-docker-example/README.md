# Python + Selenium + Docker example

## 非Docker

```bash
uv run main.py > out1.png
```

## Docker

```bash
docker build --platform linux/x86_64 -t demo .
```

```bash
docker run --rm -e URL=https://urgm.jp/private_screening_lycoris_recoil.html demo > out2.png
```
