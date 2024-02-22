# %%
import os
from dotenv import load_dotenv

# %%
load_dotenv()

# %%
v = os.environ.get("HELLO_DOTENV")
print(v)
