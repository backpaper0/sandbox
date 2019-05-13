import React from 'react';
import I18nContext from './i18n';

class Message extends React.Component {

  static contextType = I18nContext;

  render() {
    const { msg } = this.props;
    const message = this.context[msg];
    return (<p>{message}</p>);
  }
}

export default Message;
