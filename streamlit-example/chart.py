import altair as alt
import pandas as pd
import streamlit as st

# データを作成
data = pd.DataFrame(
    {
        "x": range(1, 11),
        "y1": [3, 7, 8, 5, 10, 3, 4, 5, 6, 7],
        "y2": [1, 6, 4, 8, 3, 7, 4, 5, 6, 9],
    }
)

# 棒グラフを作成
bar = (
    alt.Chart(data)
    .mark_bar()
    .encode(x="x:O", y="y1:Q", color=alt.value("#00cc9966"), opacity=alt.value(0.6))
    .properties(width=600, height=400)
)

# 線グラフを作成
line = (
    alt.Chart(data)
    .mark_line(point=True)
    .encode(x="x:O", y="y2:Q", color=alt.value("#006633"))
)

# グラフをレイヤーとして重ねる
chart = alt.layer(bar, line).resolve_scale(y="independent")  # y軸を別々に設定する


st.altair_chart(chart, use_container_width=True)
