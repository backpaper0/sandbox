import React, { useState, useEffect, useRef } from 'react';
import styled, { createGlobalStyle } from 'styled-components';

const GlobalStyle = createGlobalStyle`
  body {
    background-color: #CBC5FF;
    color: #86A5D9;
    margin: 0;
    padding: 0;
  }
  
  header a {
    color: #9690FF;
  }
`;

const Section = styled.div`
  padding: 1rem;
`;

const ScrollHeader = styled.header`
  background-color: #5F4BB6;
  color: #9690FF;
  width: 100%;
  padding: 0.5rem 1rem;
`;

const FixedHeader = styled.header`
  background-color: #5F4BB6;
  color: #9690FF;
  width: 100%;
  padding: 0.5rem 1rem;
  position: fixed;
  top: 0;
  margin-top: 0;
`;

const App: React.FC = () => {
  const [isFixed, setFixed] = useState(false);
  const header = useRef<HTMLDivElement>(null);
  useEffect(() => {
    if (header && header.current) {
      const a = header.current.offsetTop;

      const update = () => {
        if (document.documentElement.scrollTop < a) {
          setFixed(false);
        } else {
          setFixed(true);
        }
      }
      
      update();
      
      document.addEventListener("scroll", update);
      return () => {
        document.removeEventListener("scroll", update);
      };
    }
  }, []);
  const Header = isFixed ? FixedHeader : ScrollHeader;
  return (
    <React.Fragment>
      <GlobalStyle/>
      <Section>
        <p>foo foo foo</p><p>foo foo foo</p><p>foo foo foo</p><p>foo foo foo</p><p>foo foo foo</p>
        <p>foo foo foo</p><p>foo foo foo</p><p>foo foo foo</p><p>foo foo foo</p><p>foo foo foo</p>
      </Section>
      <div ref={header}>
        <Header>
          <h1>Header Header Header</h1>
          <p><a href="https://github.com/backpaper0/sandbox/tree/master/html-css-js-example/fixed-header-on-scroll-react">Source code</a></p>
        </Header>
      </div>
      <Section>
        <p>bar bar bar</p><p>bar bar bar</p><p>bar bar bar</p><p>bar bar bar</p><p>bar bar bar</p>
        <p>bar bar bar</p><p>bar bar bar</p><p>bar bar bar</p><p>bar bar bar</p><p>bar bar bar</p>
      </Section>
      <Section>
        <p>baz baz baz</p><p>baz baz baz</p><p>baz baz baz</p><p>baz baz baz</p><p>baz baz baz</p>
        <p>baz baz baz</p><p>baz baz baz</p><p>baz baz baz</p><p>baz baz baz</p><p>baz baz baz</p>
      </Section>
      <Section>
        <p>qux qux qux</p><p>qux qux qux</p><p>qux qux qux</p><p>qux qux qux</p><p>qux qux qux</p>
        <p>qux qux qux</p><p>qux qux qux</p><p>qux qux qux</p><p>qux qux qux</p><p>qux qux qux</p>
      </Section>
    </React.Fragment>
  );
}

export default App;
