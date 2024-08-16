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
from pathlib import Path

dir = Path("data")

with open(dir / "embeddings_sample.json", "r") as f:
    data = json.load(f)["data"][0]["embedding"]

with open(dir / "data.json", "w") as f:
    json.dump(data, f)

with open(dir / "data.bin", "wb") as f:
    f.write(struct.pack(f"{len(data)}f", *data))

with open(dir / "data.pkl", "wb") as f:
    pickle.dump(data, f)

with open(dir / "data.json", "rb") as f:
    print(f"  Text file size: {len(f.read()):,}")

with open(dir / "data.bin", "rb") as f:
    print(f"Binary file size: {len(f.read()):,}")

with open(dir / "data.pkl", "rb") as f:
    print(f"Pickle file size: {len(f.read()):,}")

print()

size = 5
print(f"Original data: {data[:size]}")

with open(dir / "data.json", "r") as f:
    data1 = json.load(f)
    print(f"    Text file: {data1[:size]}")

with open(dir / "data.bin", "rb") as f:
    data2 = list(struct.unpack(f"{len(data)}f", f.read()))
    print(f"  Binary file: {data2[:size]}")

with open(dir / "data.pkl", "rb") as f:
    data3 = pickle.load(f)
    print(f"  Pickle file: {data3[:size]}")
