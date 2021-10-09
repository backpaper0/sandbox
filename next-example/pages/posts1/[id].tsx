import type { NextPage, GetStaticPaths, GetStaticProps } from 'next'
import Head from 'next/head'
import { ParsedUrlQuery } from 'querystring'
import { getAllPostsIds, findPost, Post } from '../../lib/posts'

interface Props {
  post: Post;
}

interface Param extends ParsedUrlQuery {
  id: string;
}

const Posts1: NextPage<Props> = ({ post }: Props) => {
  return (
    <div>
      <Head>
        <title>{post.text}</title>
      </Head>
      <h1>Posts 1</h1>
      <p>{post.id}: {post.text}</p>
    </div>
  )
}

export const getStaticPaths: GetStaticPaths<Param> = async () => {
  const paths = await getAllPostsIds();
  return ({
    paths,
    fallback: false
  });
};

export const getStaticProps: GetStaticProps<Props, Param> = async ({ params }) => {
  console.log('getStaticProps');
  const id = params!.id;
  const post = await findPost(id);
  if (post === undefined) {
    return ({ notFound: true })
  }
  return {
    props: { post }
  }
}

export default Posts1
