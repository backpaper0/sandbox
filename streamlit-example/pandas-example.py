import streamlit as st
import pandas as pd

st.write("""
# Streamlit + Pandas を試す

兵庫県のオープンデータを利用して、地域毎のフリーWi-Fiスポット数を棒グラフで可視化してみる。
次のウェブページにある「設置箇所リスト（CSV：8KB）」を利用。

- [公衆無線LAN（Hyogo Free Wi-Fi）の運用・普及推進](https://web.pref.hyogo.lg.jp/kk26/musenlan.html)
""")

df = pd.read_csv("https://web.pref.hyogo.lg.jp/kk26/documents/hyogo_free_wi_fi_list_zahyo.csv")
st.bar_chart(df["地域"].value_counts())