import { connect } from 'react-redux';
import Counter from '../components/Counter';

const mapStateToProps = state => ({
    count: state.counter
});

const mapDispatchToProps = dispatch => ({
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Counter);
