
const tweets = [];

const addTweet = (() => {
  let idGenerator = 0;
  return function (content) {
    const id = (++idGenerator).toString();
    const createdAt = new Date().toISOString();
    const tweet = { id, content, createdAt };
    tweets.push(tweet);
    return tweet;
  };
})();

['foo', 'bar', 'baz', 'qux', 'quux', 'corge', 'grault', 'garply', 'waldo', 'fred', 'plugh', 'xyzzy', 'thud',
'hoge', 'fuga', 'piyo', 'hogera', 'hogehoge', 'helloworld'].forEach(content => addTweet(content));

function findTweets({ nextCursor }) {
  const index = nextCursor !== undefined ? tweets.findIndex(tweet => tweet.id === nextCursor) : tweets.length - 1;
  if (index < 0) {
    return ({ tweets: [] });
  }
  const pageSize = 5;
  const tweetsOfPage = tweets.slice(Math.max(0, index - pageSize), index + 1);
  const nc = tweetsOfPage.length > pageSize ? tweetsOfPage[0].id : undefined;
  const tw = nc !== undefined ? tweetsOfPage.slice(1, tweetsOfPage.length) : tweetsOfPage;
  return ({ tweets: tw, nextCursor: nc, });
}

module.exports = {
  tweets,
  addTweet,
  findTweets,
};
