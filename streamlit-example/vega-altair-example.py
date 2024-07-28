import altair as alt
import pandas as pd
import streamlit as st
from vega_datasets import data

url = data.cars.url

c = alt.Chart(url).mark_point().encode(x="Horsepower:Q", y="Miles_per_Gallon:Q")

alt.LayerChart().add_layers(c)

st.altair_chart(c)
