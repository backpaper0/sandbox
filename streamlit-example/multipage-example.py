import streamlit as st

def page1():
    st.write("# Page 1")

def page2():
    st.write("# Page 2")

def page3():
    st.write("# Page 3")

pages = {
    "page1": page1,
    "page2": page2,
    "page3": page3,
}

page_name = st.sidebar.selectbox("Choose page", pages.keys())

st.sidebar.link_button("page1", "aaa")

pages[page_name]()