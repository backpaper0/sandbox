import React from 'react';
import { connect } from 'react-redux';
import { increment } from '../actions';

const Increment = ({ dispatch }) => {
    return (
        <button onClick={e => dispatch(increment())}>
            Increment
        </button>
    );
};

export default connect()(Increment);
