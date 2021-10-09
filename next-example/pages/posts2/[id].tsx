import type { NextPage, GetStaticPaths, GetStaticProps } from 'next'
import Head from 'next/head'
import { ParsedUrlQuery } from 'querystring'
import { getAllPostsIds, findPost, Post } from '../../lib/posts'

const Posts2 = ({ post }: { post: Post }) => {
  return (
    <div>
      <Head>
        <title>{post.text}</title>
      </Head>
      <h1>Posts 2</h1>
      <p>{post.id}: {post.text}</p>
    </div>
  )
}

export const getStaticPaths: GetStaticPaths = async () => {
  const paths = await getAllPostsIds();
  return ({
    paths,
    fallback: false
  });
};

export const getStaticProps: GetStaticProps = async ({ params }) => {
  console.log('getStaticProps');
  const id = params!.id as string;
  const post = await findPost(id);
  if (post === undefined) {
    return ({ notFound: true })
  }
  return {
    props: { post }
  }
}

export default Posts2
