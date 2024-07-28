import altair as alt
import pandas as pd
import streamlit as st

# データの読み込み
df = pd.read_json("data/log.jsonl", lines=True)

# データの形式に応じてカラム名を調整
df["timestamp"] = pd.to_datetime(df["timestamp"])

# 時間帯ごとのパーセンタイル値の計算
df["hour"] = df["timestamp"].dt.hour
df["weekday"] = df["timestamp"].dt.dayofweek

percentiles = [95, 90, 50]

hourly_percentiles = [
    df.groupby("hour")["response_time"].quantile(p / 100).reset_index()
    for p in percentiles
]
hourly_min_max = [
    df.groupby("hour")["response_time"].min().reset_index(),
    df.groupby("hour")["response_time"].max().reset_index(),
]


weekly_percentiles = [
    df.groupby("weekday")["response_time"].quantile(p / 100).reset_index()
    for p in percentiles
]
weekly_min_max = [
    df.groupby("weekday")["response_time"].min().reset_index(),
    df.groupby("weekday")["response_time"].max().reset_index(),
]


# 曜日番号から曜日名へのマッピング
weekday_map = {
    0: "Monday",
    1: "Tuesday",
    2: "Wednesday",
    3: "Thursday",
    4: "Friday",
    5: "Saturday",
    6: "Sunday",
}

# 曜日名の追加
for wp in weekly_percentiles + weekly_min_max:
    wp["weekday"] = wp["weekday"].map(weekday_map)


st.title("応答速度")

colors_percentile = [
    "navy",
    "blue",
    "aqua",
]
colors_min_max = [
    "orange",
    "red",
]

# 時間帯ごとのパーセンタイル値のグラフ
hour_charts = [
    alt.Chart(hp)
    .mark_bar(color=color)
    .encode(x="hour", y="response_time")
    .properties(title=f"時間帯ごとの応答速度{p}パーセンタイル値")
    for (p, hp, color) in zip(percentiles, hourly_percentiles, colors_percentile)
] + [
    alt.Chart(hp)
    .mark_line(color=color)
    .encode(x="hour", y="response_time")
    .properties(title=f"時間帯ごとの応答速度{p}値")
    for (p, hp, color) in zip(["最小", "最大"], hourly_min_max, colors_min_max)
]
hour_chart = alt.layer(*hour_charts)

# 曜日ごとのパーセンタイル値のグラフ
week_charts = [
    alt.Chart(wp)
    .mark_bar(color=color)
    .encode(x="weekday", y="response_time")
    .properties(title=f"曜日ごとの応答速度{p}パーセンタイル値")
    for (p, wp, color) in zip(percentiles, weekly_percentiles, colors_percentile)
] + [
    alt.Chart(wp)
    .mark_line(color=color)
    .encode(x="weekday", y="response_time")
    .properties(title=f"曜日ごとの応答速度{p}値")
    for (p, wp, color) in zip(["最小", "最大"], weekly_min_max, colors_min_max)
]
week_chart = alt.layer(*week_charts)

st.altair_chart(hour_chart, use_container_width=True)
st.altair_chart(week_chart, use_container_width=True)
