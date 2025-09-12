"""
コマンドライン引数を取得する例。

uv run streamlit run args.py -- --foo xxx --bar=yyy baz qux
"""
import streamlit as st
import sys
import os


body = ["# arguments"]
body.append("")
for arg in sys.argv:
    body.append(f"1. `{arg}`")
body.append("")
st.markdown("\n".join(body))
