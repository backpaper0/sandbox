import streamlit as st

conn = st.connection("mydb")

st.write(f"conn = {conn}")
