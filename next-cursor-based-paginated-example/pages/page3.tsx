import { Cursor, usePagenatedState } from 'lib/pagenated';
import { getPosts } from 'lib/posts';
import type { NextPage } from 'next';
import Head from 'next/head';

const Page3: NextPage = () => {
    const dataFetcher = async (cursor: Cursor) => {
        const { posts, nextCursor } = await getPosts(cursor);
        return { data: posts, nextCursor };
    };
    const [posts, fetchNext] = usePagenatedState(dataFetcher);
    const readMore = () => {
        fetchNext();
    };
    if (!posts) {
        return null;
    }
    return (
        <div>
            <Head>
                <title>useEffect version</title>
            </Head>

            <main>
                {posts.map(post => <p key={post}>{post}</p>)}
                <button onClick={readMore}>Read more</button>
            </main>
        </div>
    )
}

export default Page3
