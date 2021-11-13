import { Cursor, usePagenatedState } from 'lib/pagenated';
import { getPosts } from 'lib/posts';
import Head from 'next/head';

export default function Page3() {
    const dataFetcher = async (cursor: Cursor) => {
        const { posts, nextCursor } = await getPosts(cursor);
        return { data: posts, nextCursor };
    };
    const [posts, fetchNext, fetching] = usePagenatedState(dataFetcher);
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
                <button onClick={readMore} disabled={fetching}>Read more</button>
            </main>
        </div>
    )
}
