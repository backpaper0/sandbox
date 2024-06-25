import streamlit as st


def page1() -> None:
    st.write("# Page 1")


def page2() -> None:
    st.write("# Page 2")


pg = st.navigation(
    [
        st.Page(page1),
        st.Page(page2),
    ]
)
pg.run()
