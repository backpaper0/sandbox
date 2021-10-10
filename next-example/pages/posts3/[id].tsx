import type { NextPage } from 'next'
import Head from 'next/head'
import { findPost, Post } from '../../lib/posts'

interface Props {
  post: Post
}

const Posts3: NextPage<Props> = ({ post }) => {
  return (
    <div>
      <Head>
        <title>{post.text}</title>
      </Head>
      <h1>Posts 3</h1>
      <p>{post.id}: {post.text}</p>
    </div>
  )
}

Posts3.getInitialProps = async (ctx) => {
  const id = ctx.query.id as string;
  const post = await findPost(id);
  if (!post) {
    return ({
      post: { id, text: 'not found' }
    });
  }
  return ({ post });
}

export default Posts3
