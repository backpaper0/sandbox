# %%
import pandas as pd
from matplotlib import rcParams
import matplotlib.pyplot as plt

# グラフの日本語が豆腐化するので日本語フォントを指定する。
# なんかPlemolJP以外は指定してもフォントを見つけてくれなかった。なぜ？
rcParams["font.family"] = "PlemolJP"

# %%
df = pd.read_csv("https://web.pref.hyogo.lg.jp/kk26/documents/hyogo_free_wi_fi_list_zahyo.csv")

# %%
df["地域"].value_counts().plot.bar()
plt.title("地域別公衆無線LAN")

plt.savefig("地域別公衆無線LAN.svg")