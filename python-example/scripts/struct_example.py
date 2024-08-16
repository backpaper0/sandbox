"""
list[float]をなるべく小さなサイズでシリアライズする方法を検証するスクリプト。

```
python -m scripts.struct_example
```

テキスト(JSON)は最もサイズが大きくなる。
structモジュールを使ったpackはサイズが一番小さくなるが、unpackすると誤差が生じている。
pickleはstructよりもサイズが大きいが、unpackしても誤差が生じない。
"""

import json
import pickle
import struct
from io import BytesIO
from pathlib import Path

dir = Path("data")

with open(dir / "embeddings_sample.json", "r") as f:
    data = json.load(f)["data"][0]["embedding"]


b1 = BytesIO()
b2 = BytesIO()
b3 = BytesIO()

b1.write(json.dumps(data).encode())

b2.write(struct.pack(f"{len(data)}f", *data))

pickle.dump(data, b3)

print(f"  Text file size: {len(b1.getvalue()):,}")
print(f"Binary file size: {len(b2.getvalue()):,}")
print(f"Pickle file size: {len(b3.getvalue()):,}")

print()

size = 5
print(f"Original data: {data[:size]}")

data1 = json.loads(b1.getvalue())
print(f"    Text file: {data1[:size]}")

data2 = list(struct.unpack(f"{len(data)}f", b2.getbuffer()))
print(f"  Binary file: {data2[:size]}")

data3 = pickle.loads(b3.getbuffer())
print(f"  Pickle file: {data3[:size]}")
