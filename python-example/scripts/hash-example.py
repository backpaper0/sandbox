import base64
import hashlib

data = "hello".encode()
hash1 = hashlib.sha3_224(data).hexdigest()
hash2 = hashlib.sha3_256(data).hexdigest()
hash3 = hashlib.sha3_384(data).hexdigest()
hash4 = hashlib.sha3_512(data).hexdigest()

print(f"SHA-3(224): {hash1}")
print(f"SHA-3(256): {hash2}")
print(f"SHA-3(384): {hash3}")
print(f"SHA-3(512): {hash4}")

hash5 = base64.urlsafe_b64encode(hashlib.sha256(data).digest()).decode()
print(f"Base64(SHA-256) {hash5}")
