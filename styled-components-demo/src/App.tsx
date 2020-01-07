import React from 'react';
import styled from 'styled-components';

// Create a <Title> react component that renders an <h1> which is
// centered, palevioletred and sized at 1.5em
const Title = styled.h1`
  font-size: 1.5em;
  text-align: center;
  color: palevioletred;
`;

// Create a <Wrapper> react component that renders a <section> with
// some padding and a papayawhip background
const Wrapper = styled.section`
  padding: 4em;
  background: papayawhip;
`;

const App: React.FC = () => {
  return (
    <div>
      <Wrapper>
        <Title>Hello World, this is my first styled component!</Title>
      </Wrapper>
      <div>
        <Button primary={false}>Normal</Button>
        <Button primary={true}>Primary</Button>
      </div>
    </div>
  );
}



interface ButtonProps {
  primary: boolean;
}

const Button = styled.button<ButtonProps>`
  /* Adapt the colors based on primary prop */
  background: ${props => props.primary ? "palevioletred" : "white"};
  color: ${props => props.primary ? "white" : "palevioletred"};

  font-size: 1em;
  margin: 1em;
  padding: 0.25em 1em;
  border: 2px solid palevioletred;
  border-radius: 3px;
`;



export default App;
